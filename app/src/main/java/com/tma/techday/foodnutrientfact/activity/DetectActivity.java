package com.tma.techday.foodnutrientfact.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.andremion.counterfab.CounterFab;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.enums.ImageDetectEngine;
import com.tma.techday.foodnutrientfact.gui.dialog.FoodNutriResultDialog;
import com.tma.techday.foodnutrientfact.helper.InternetCheck;
import com.tma.techday.foodnutrientfact.model.FoodInfoDTO;
import com.tma.techday.foodnutrientfact.service.FoodNutriService;
import com.tma.techday.foodnutrientfact.service.OrderService;
import com.tma.techday.foodnutrientfact.viewholder.GraphicOverlay;
import com.tma.techday.foodnutrientfact.viewholder.RectOverlay;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import dmax.dialog.SpotsDialog;

/**
 * Capture ,detect image, add to cart
 */
public class DetectActivity extends AppCompatActivity {

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    Button btnDetect, btnDetectAgain;
    AlertDialog waitingDialog;
    CounterFab counterFab;
    RelativeLayout.LayoutParams layoutParams;

    @Inject
    FoodNutriService foodNutriService;

    @Inject
    OrderService orderService;

    //TODO: refactor this
    static Set<String> IGNORE_LIST = new HashSet<>();
    static {
        IGNORE_LIST.add("Food");
        IGNORE_LIST.add("Cup");
        IGNORE_LIST.add("Orange");
        IGNORE_LIST.add("Red");
        IGNORE_LIST.add("Blue");
        IGNORE_LIST.add("Green");
        IGNORE_LIST.add("White");
        IGNORE_LIST.add("Black");
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
        counterFab.setCount(orderService.getCountCart());
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    /**
     * <ul>
     *     <li>Capture image</li>
     *     <li>Detect image</li>
     *     <li>Show food nutrient result from detect result</li>
     * </ul>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_activity);
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
        setUpParam();

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }
            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                waitingDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap =  Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),false);
                cameraView.stop();

                runDetect(bitmap);

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        btnDetect.setOnClickListener((view)->{
            showAlertDialogNoticeImageCapture();
            cameraView.captureImage(cameraKitImage -> {
                runObjectDetection(cameraKitImage.getBitmap());

            });
            layoutParams.removeRule(RelativeLayout.ABOVE);
            layoutParams.addRule(RelativeLayout.ABOVE,R.id.btnDetectAgain);

        });

        btnDetectAgain.setOnClickListener(view -> {
            btnDetectAgain.setVisibility(View.GONE);
            btnDetect.setVisibility(View.VISIBLE);
            layoutParams.removeRule(RelativeLayout.ABOVE);
            layoutParams.addRule(RelativeLayout.ABOVE,R.id.btnDetect);
            cameraView.start();

        });

    }


    /**
     * Create menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    /**
     * Get the vision image detecter then analyze the image bitmap.
     * @param bitmap the picture taken from camera.
     */
    private void runDetect(Bitmap bitmap) {

        //Create a FirebaseVisionImage object from a Bitmap object
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        // If have internet, we will use CLOUD_ENGINE mode Else we will use DEVICE_ENGINE mode
        new InternetCheck(connectInternet -> {
            FirebaseVisionImageLabeler detectImageLabeler = getFirebaseVisionImageLabeler(connectInternet ? ImageDetectEngine.CLOUD_ENGINE : ImageDetectEngine.DEVICE_ENGINE);
            if (detectImageLabeler == null)
            {
                Log.e("Error",getString(R.string.detect_labeler_cannot_null));
                return;
            }
            detectImageLabeler.processImage(image)
                    .addOnSuccessListener(labels -> {
                        if (labels != null && !labels.isEmpty())
                        {
                            // Image that is 'More correct' ia put at the top
                            labels.sort((lb1, lb2) -> (int) (lb2.getConfidence() - lb1.getConfidence()));
                            processDataResult(labels);
                        } else {
                            waitingDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(getString(R.string.error_title));
                            builder.setMessage(getString(R.string.unable_detect_image));
                            builder.show();
                            btnDetect.setVisibility(View.GONE);
                            btnDetectAgain.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(e -> Log.e("DETECTERROR",e.getMessage()));
        });

    }

    /**
     * Get image labeler after validate
     * @param imageDetectEngine types of image labeler
     */
    private FirebaseVisionImageLabeler getFirebaseVisionImageLabeler(ImageDetectEngine imageDetectEngine) {
        if (imageDetectEngine == null)
            throw new IllegalArgumentException(getString(R.string.engine_must_be_specified));
        switch (imageDetectEngine)
        {
            case CLOUD_ENGINE:
                FirebaseVisionCloudImageLabelerOptions cloudLabeler =          //Finding Labels in a supplied image runs inference on cloud
                        new FirebaseVisionCloudImageLabelerOptions.Builder()
                                .setConfidenceThreshold(0.9f) // Get highest Confidence Threshold
                                .build();
                return FirebaseVision.getInstance()
                        .getCloudImageLabeler(cloudLabeler);
            case DEVICE_ENGINE:
                FirebaseVisionOnDeviceImageLabelerOptions deviceLabeler =       //Finding Labels in a supplied image runs inference on device
                        new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                                .setConfidenceThreshold(0.82f) // Get highest Confidence Threshold
                                .build();
                return FirebaseVision.getInstance()
                        .getOnDeviceImageLabeler(deviceLabeler);
            default:
                return null;
        }
    }

    /**
     * Get the label's text description then the image labeling operation succeeds
     * @param labels a list of FirebaseVisionImageLabel objects after detect image
     */
    private void processDataResult(List<FirebaseVisionImageLabel> labels) {
        String foodName = "";
        List<String> itemNames = labels.stream().map(lbl -> lbl.getText()).collect(Collectors.toList());
        for (String itemName : itemNames){
            if ( IGNORE_LIST.contains(itemName) ) {
                continue;
            }
            else {
                foodName = itemName;
                break;
            }
        }
        Log.i("foodName", foodName);
        if (waitingDialog.isShowing()) {
            waitingDialog.dismiss();
            btnDetect.setVisibility(View.GONE);
            btnDetectAgain.setVisibility(View.VISIBLE);
        }
        if (!foodName.equals("")) {
            getNutrition(foodName);
        }
    }

    /**
     * Get Food Nutrition From food Name after detect image
     * @param foodName label after detect image
     */
    private void getNutrition(String foodName){
        FoodInfoDTO foodNutri = foodNutriService.getFoodNutri(foodName);
        if (foodNutri != null)
        {
            Log.i(getString(R.string.food_nutri), foodNutri.toDebugString());
            showFoodNutri(foodNutri);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.error_title));
            builder.setMessage(getString(R.string.not_found_food));
            builder.show();
        }
    }

    /**
     * Show nutritional results have just been obtained
     * @param foodNutri detailed nutrition results have just been taken
     */
    private void showFoodNutri( FoodInfoDTO foodNutri){
        FoodNutriResultDialog foodNutriResultDialog = new FoodNutriResultDialog();
        Bundle args = new Bundle();   //Use bundle to pass data for food nutrient result dialog
        args.putSerializable(getString(R.string.food_nutri), foodNutri);
        foodNutriResultDialog.setArguments(args);
        foodNutriResultDialog.show(getSupportFragmentManager(),"dialog");
    }

    /**
     * Declare Params
     */
    public void setUpParam(){
        cameraView = findViewById(R.id.camemraView);
        graphicOverlay = findViewById(R.id.graphicOverlay);
        btnDetect = findViewById(R.id.btnDetect);
        layoutParams = (RelativeLayout.LayoutParams) cameraView.getLayoutParams();
        btnDetectAgain = findViewById(R.id.btnDetectAgain);
        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage(getString(R.string.analyzing_dialog)).setCancelable(false).build();
        counterFab = findViewById(R.id.fab);
        counterFab.setOnClickListener(view -> {
            Intent intent = new Intent(DetectActivity.this,AddToCartActivity.class);
            startActivity(intent);
        });
        counterFab.setCount(orderService.getCountCart());
    }

    /**
     * Show dialog notice waiting captured image
     */
    public void showAlertDialogNoticeImageCapture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetectActivity.this);
        builder.setTitle("Notice Dialog")
                .setMessage(getString(R.string.hold_phone))
                .setCancelable(false).setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
            }
        }, 1500);
    }

    /**
     * MLKit Object Detection Function
     */
    private void runObjectDetection(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionObjectDetectorOptions options =  new FirebaseVisionObjectDetectorOptions.Builder()
                .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableClassification()
                .build();
        FirebaseVisionObjectDetector objectDetector =
                FirebaseVision.getInstance().getOnDeviceObjectDetector(options);
        objectDetector.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<FirebaseVisionObject>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionObject> detectedObjects) {
                                for (FirebaseVisionObject obj : detectedObjects) {
                                    Integer id = obj.getTrackingId();
                                    Rect bounds = obj.getBoundingBox();

                                    // If classification was enabled:
                                    int category = obj.getClassificationCategory();
                                    Float confidence = obj.getClassificationConfidence();
//                                    Toast.makeText(DetectActivity.this, category, Toast.LENGTH_LONG).show();
                                    RectOverlay rectOverLay = RectOverlay.of(graphicOverlay, bounds);
                                    graphicOverlay.add(rectOverLay);

                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
    }
}
