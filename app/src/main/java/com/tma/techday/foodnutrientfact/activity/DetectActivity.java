package com.tma.techday.foodnutrientfact.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andremion.counterfab.CounterFab;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.enums.ImageDetectEngine;
import com.tma.techday.foodnutrientfact.gui.dialog.FoodNutriResultDialog;
import com.tma.techday.foodnutrientfact.helper.InternetCheck;
import com.tma.techday.foodnutrientfact.model.FoodBoxContain;
import com.tma.techday.foodnutrientfact.model.FoodInfoDTO;
import com.tma.techday.foodnutrientfact.model.PermissionsDelegate;
import com.tma.techday.foodnutrientfact.service.FoodNutriService;
import com.tma.techday.foodnutrientfact.service.OrderService;
import com.tma.techday.foodnutrientfact.viewholder.GraphicOverlay;
import com.tma.techday.foodnutrientfact.viewholder.RectOverlay;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.log.LoggersKt;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.selector.FlashSelectorsKt;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;


/**
 * Capture, detect image, add to cart
 */
public class DetectActivity extends AppCompatActivity {
    private final PermissionsDelegate permissionsDelegate = new PermissionsDelegate(this);
    private boolean hasCameraPermission;
    public static final int DEGREES_90 = 90;
    public static final int BOUND_ABSOLUTE_ERROR = 45;
    private Fotoapparat fotoapparat;
    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    Button btnDetect, btnDetectAgain;
    AlertDialog waitingDialog;
    CounterFab counterFab;
    RelativeLayout.LayoutParams layoutParams;
    List<FoodBoxContain> foodBoxContainList = new ArrayList<>();


    @Inject
    FoodNutriService foodNutriService;

    @Inject
    OrderService orderService;

    static Set<String> IGNORE_LIST_SINGLE_MODE = new HashSet<>();
    static {
        IGNORE_LIST_SINGLE_MODE.add("Food");
        IGNORE_LIST_SINGLE_MODE.add("Cup");
        IGNORE_LIST_SINGLE_MODE.add("Orange");
        IGNORE_LIST_SINGLE_MODE.add("Red");
        IGNORE_LIST_SINGLE_MODE.add("Blue");
        IGNORE_LIST_SINGLE_MODE.add("Green");
        IGNORE_LIST_SINGLE_MODE.add("White");
        IGNORE_LIST_SINGLE_MODE.add("Black");
        IGNORE_LIST_SINGLE_MODE.add("Yellow");
        IGNORE_LIST_SINGLE_MODE.add("Natural foods");
        IGNORE_LIST_SINGLE_MODE.add("Still life photography");
        IGNORE_LIST_SINGLE_MODE.add("Fruit");
        IGNORE_LIST_SINGLE_MODE.add("Pink");
        IGNORE_LIST_SINGLE_MODE.add("Dish");
    }

    static Set<String> IGNORE_LIST_MULT_MODE = new HashSet<>();
    static {

        IGNORE_LIST_MULT_MODE.add("Cup");
        IGNORE_LIST_MULT_MODE.add("Orange");
        IGNORE_LIST_MULT_MODE.add("Red");
        IGNORE_LIST_MULT_MODE.add("Blue");
        IGNORE_LIST_MULT_MODE.add("Green");
        IGNORE_LIST_MULT_MODE.add("White");
        IGNORE_LIST_MULT_MODE.add("Black");
        IGNORE_LIST_MULT_MODE.add("Yellow");
        IGNORE_LIST_MULT_MODE.add("Still life photography");
        IGNORE_LIST_MULT_MODE.add("Pink");
        IGNORE_LIST_MULT_MODE.add("Dish");
    }

    @Override
    protected void onResume() {
        super.onResume();
        fotoapparat.start();
        counterFab.setCount(orderService.getCountCart());
        graphicOverlay.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fotoapparat.stop();
        graphicOverlay.clear();
    }


    /**
     * <ul>
     *     <li>Capture image</li>
     *     <li>Detect image</li>
     *     <li>Detect object and track</li>
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
        setOnClickAndTouchListener();
    }

    /**
     * Create Fotoapparat
     * @return
     */
    private Fotoapparat createFotoapparat() {
        return Fotoapparat
                .with(this)
                .into(cameraView)           // view which will draw the camera preview
                .previewScaleType(ScaleType.CenterCrop)  // we want the preview to fill the view
                .photoResolution(ResolutionSelectorsKt.highestResolution())   // we want to have the biggest photo possible
                .lensPosition(LensPositionSelectorsKt.back())       // we want back camera
                .focusMode(SelectorsKt.firstAvailable(  // (optional) use the first focus mode which is supported by device
                        FocusModeSelectorsKt. continuousFocusPicture(),
                        FocusModeSelectorsKt.autoFocus(),        // in case if continuous focus is not available on device, auto focus will be used
                        FocusModeSelectorsKt.fixed()             // if even auto focus is not available - fixed focus mode will be used
                ))
                .flash(SelectorsKt.firstAvailable(      // (optional) similar to how it is done for focus mode, this time for flash
                        FlashSelectorsKt.autoRedEye(),
                        FlashSelectorsKt.autoFlash(),
                        FlashSelectorsKt.torch()
                ))
                .logger(LoggersKt.loggers(            // (optional) we want to log camera events in 2 places at once
                        LoggersKt.logcat(),           // ... in logcat
                        LoggersKt.fileLogger(this)    // ... and to file
                ))
                .build();
    }

    /**
     * Set On click and touch listener
     */
    private void setOnClickAndTouchListener() {

        btnDetect.setOnClickListener((view)->{

            cameraView.getHeight();
            cameraView.getWidth();
            takePicture();
            layoutParams.removeRule(RelativeLayout.ABOVE);
            layoutParams.addRule(RelativeLayout.ABOVE,R.id.btnDetectAgain);
            btnDetect.setVisibility(View.GONE);
            btnDetectAgain.setVisibility(View.VISIBLE);

        });

        btnDetectAgain.setOnClickListener(view -> {
            btnDetectAgain.setVisibility(View.GONE);
            btnDetect.setVisibility(View.VISIBLE);
            layoutParams.removeRule(RelativeLayout.ABOVE);
            layoutParams.addRule(RelativeLayout.ABOVE,R.id.btnDetect);
            graphicOverlay.clear();
            fotoapparat.start();

        });

        graphicOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FoodBoxContain found = null;
                for(FoodBoxContain boxContain: foodBoxContainList){
                    Rect rect = boxContain.getRect();
                    if (isPressedWithinBox(motionEvent, rect)) {
                        found = boxContain;
                    }
                }
                if (found != null){
                    waitingDialog.show();

                    detectOwnerData(found.getBitmapImage());
                }
                return false;
            }
        });
    }

    /**
     * Check if the touch position is in the box yet
     * @param motionEvent
     * @param rect
     * @return
     */
    private boolean isPressedWithinBox(MotionEvent motionEvent, Rect rect) {
        return rect.contains((int)motionEvent.getX(), (int)motionEvent.getY());
    }

    /**
     * Take a picture and send bitmap for detect object
     */
    private void takePicture() {
        PhotoResult photoResult = fotoapparat.takePicture();
        photoResult.toBitmap().whenDone(bitmapPhoto -> {
            if (bitmapPhoto == null) {
                Toast.makeText(DetectActivity.this, getString(R.string.CAPTURE_ERROR), Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap bitmap = bitmapPhoto.bitmap;
            bitmap =  Bitmap.createScaledBitmap(bitmap,cameraView.getHeight(),cameraView.getWidth(),true);
            Matrix matrix = new Matrix();
            matrix.postRotate(DEGREES_90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            fotoapparat.stop();
            runObjectDetection(rotatedBitmap);
        });

    }

    /**
     * Create menu and set listener (switch button)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    /**
     * Get the vision image detected then analyze the image bitmap.
     * @param bitmap the picture taken from camera.
     * @param connectInternet check whether or not the internet.
     */
    private void detectFirebaseData(Bitmap bitmap, boolean connectInternet) {

        //Create a FirebaseVisionImage object from a Bitmap object
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        // If have internet, we will use CLOUD_ENGINE mode Else we will use DEVICE_ENGINE mode
        FirebaseVisionImageLabeler detectImageLabeler = getFirebaseVisionImageLabeler(connectInternet ? ImageDetectEngine.CLOUD_ENGINE : ImageDetectEngine.DEVICE_ENGINE);
        if (detectImageLabeler == null)
        {
            showAlertDialog(getString(R.string.error_title), getString(R.string.unable_detect_image));
            return;
        }
        detectImageLabeler.processImage(image)
                .addOnSuccessListener(labels -> {
                    if (labels != null && !labels.isEmpty())
                    {
                        // Image that is 'More correct' ia put at the top
                        labels.sort((lb1, lb2) -> (int) (lb2.getConfidence() - lb1.getConfidence()));
                        SharedPreferences sharedPreferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                        String mode = sharedPreferences.getString("My_Mode","");
                        if(mode.equalsIgnoreCase("multiple")){
                            resultOptionDialog(processMultiDataResult(labels));
                        }else {
                            processDataResult(labels);
                        }
                    } else {
                        waitingDialog.dismiss();
                        showAlertDialog(getString(R.string.error_title), getString(R.string.unable_detect_image));

                    }
                })
                .addOnFailureListener(e -> {
                    waitingDialog.dismiss();
                    showAlertDialog(getString(R.string.error_title), getString(R.string.unable_detect_image));
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
                                .setConfidenceThreshold(0.8f) // Get highest Confidence Threshold
                                .build();
                return FirebaseVision.getInstance()
                        .getOnDeviceImageLabeler(deviceLabeler);
            default:
                return null;
        }
    }

    /**
     * Detect image and use owner data to get result
     * @param bitmap the picture taken from camera
     */
    private void detectOwnerData(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        // If have internet, we will use Owner data mode Else we will use Firebase data mode
        new InternetCheck(internet -> {
            if (!internet){
                detectFirebaseData(bitmap, internet);
            }else {
                FirebaseAutoMLRemoteModel remoteModel = new FirebaseAutoMLRemoteModel.Builder("food_train_data").build();
                FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().requireWifi().build();
                FirebaseModelManager.getInstance().download(remoteModel, conditions)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder optionsBuilder = new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(remoteModel);
                                FirebaseVisionOnDeviceAutoMLImageLabelerOptions options = optionsBuilder.setConfidenceThreshold(0.78f).build();
                                try {
                                    FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
                                    labeler.processImage(image)
                                            .addOnSuccessListener(labels -> {
                                                if (labels != null && !labels.isEmpty())
                                                {
                                                    // Image that is 'More correct' ia put at the top
                                                    labels.sort((lb1, lb2) -> (int) (lb2.getConfidence() - lb1.getConfidence()));
                                                    SharedPreferences sharedPreferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                                                    String mode = sharedPreferences.getString("My_Mode","");
                                                    if(mode.equalsIgnoreCase("multiple")){
                                                        resultOptionDialog(processMultiDataResult(labels));
                                                    }else {
                                                        processDataResult(labels);
                                                    }

                                                } else {
                                                    detectFirebaseData(bitmap, internet);
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                waitingDialog.dismiss();
                                                detectFirebaseData(bitmap, internet);
                                            });
                                } catch (FirebaseMLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

        });

    }

    /**
     * Get the label's text description then the image labeling operation succeeds in single result mode.
     * @param labels a list of FirebaseVisionImageLabel objects after detect image
     */
    private void processDataResult(List<FirebaseVisionImageLabel> labels) {
        String foodName = "";
        List<String> itemNames = labels.stream().map(lbl -> lbl.getText()).collect(Collectors.toList());
        for (String itemName : itemNames){
            if ( IGNORE_LIST_SINGLE_MODE.contains(itemName) ) {
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
        }else {
            showAlertDialog(getString(R.string.error_title), getString(R.string.not_found_food));
        }
    }

    /**
     * Get the array label's text description then the image labeling operation succeeds in multiple result mode.
     * @param labels a list of FirebaseVisionImageLabel objects after detect image
     */
    private String[] processMultiDataResult(List<FirebaseVisionImageLabel> labels) {
        String foodName = "";
        List<String> foodNameResult = new ArrayList<>();
        List<String> itemNames = labels.stream().map(lbl -> lbl.getText()).collect(Collectors.toList());
        List<String> multItemNames = itemNames.stream().limit(3).collect(Collectors.toList());
        for (String itemName : multItemNames){
            if ( IGNORE_LIST_MULT_MODE.contains(itemName) ) {
                continue;
            }
            else {
               foodNameResult.add(itemName);
            }
        }
        String[] itemsArray = new String[foodNameResult.size()];
        itemsArray = foodNameResult.toArray(itemsArray);
        return itemsArray;
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
            showAlertDialog(getString(R.string.error_title), getString(R.string.not_found_food));
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

        cameraView = findViewById(R.id.cameraView);
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
        hasCameraPermission = permissionsDelegate.hasCameraPermission();

        if (hasCameraPermission) {
            cameraView.setVisibility(View.VISIBLE);

        } else {
            permissionsDelegate.requestCameraPermission();
        }
        fotoapparat = createFotoapparat();
    }

    /**
     * MLKit Object Detection Function and draw red box for object just detected
     * @param bitmap the picture taken from camera
     */
    private void runObjectDetection(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionObjectDetectorOptions options =  new FirebaseVisionObjectDetectorOptions.Builder()
                .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build();
        FirebaseVisionObjectDetector objectDetector = FirebaseVision.getInstance().getOnDeviceObjectDetector(options);
        objectDetector.processImage(image)
                .addOnSuccessListener(detectedObjects -> {
                            for (FirebaseVisionObject obj : detectedObjects) {
                                Rect bounds = buildBound(obj);
                                cutBitmapImage(bitmap, bounds);
                                Toast.makeText(DetectActivity.this, getString(R.string.guide_detect), Toast.LENGTH_LONG).show();
                            }
                        }
                )
                .addOnFailureListener(e -> e.printStackTrace());
    }


    /**
     * Build bound depend on object detected
     * @param obj
     * @return
     */
    @NotNull
    private Rect buildBound(FirebaseVisionObject obj) {
        Rect bounds = obj.getBoundingBox();
        Rect rect = new Rect(bounds.left,bounds.top+BOUND_ABSOLUTE_ERROR,bounds.right,bounds.bottom+BOUND_ABSOLUTE_ERROR);
        RectOverlay rectOverLay = RectOverlay.of(graphicOverlay, rect);
        graphicOverlay.add(rectOverLay);
        return bounds;
    }

    /**
     * Cut bitmap depend on bound detected
     * @param bitmap
     * @param bounds
     */
    private void cutBitmapImage(Bitmap bitmap, Rect bounds) {
        Bitmap cutBitmap = Bitmap.createBitmap(bounds.right,
                bounds.bottom, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cutBitmap);
        canvas.drawBitmap(bitmap, bounds, bounds, null);
        FoodBoxContain foodBoxContain = FoodBoxContain.of(bounds,cutBitmap);
        foodBoxContainList.add(foodBoxContain);
    }

    /**
     * Show Alert Dialog
     */
    private void showAlertDialog(String title, String message){
        AlertDialog.Builder builder = buildAlertDialog(title, message);
        builder.show();
    }

    /**
     * Build Alert Dialog
     * @param title
     * @param message
     * @return
     */
    @NotNull
    private AlertDialog.Builder buildAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        return builder;
    }

    /**
     * Request Permissions Result and open camera when allowed
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsDelegate.resultGranted(requestCode, permissions, grantResults)) {
            hasCameraPermission = true;
            fotoapparat = createFotoapparat();
            fotoapparat.start();
            cameraView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Dialog display result when user choose multiple result mode
     * @param labels list label detected
     */
    private void  resultOptionDialog(String[] labels){
        if (waitingDialog.isShowing()) {
            waitingDialog.dismiss();
            btnDetect.setVisibility(View.GONE);
            btnDetectAgain.setVisibility(View.VISIBLE);
        }
        final String[] foodName = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose item");
        int checkedItem = -1; //this will checked the item when user open the dialog
        if (labels.length == 0){
            builder.setMessage(getString(R.string.not_found_food));
        }else {
            builder.setSingleChoiceItems(labels, checkedItem,(dialog, which) ->  {
                foodName[0] = labels[which];
            });
        }
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!foodName[0].equals("")) {
                    getNutrition(foodName[0]);
                }else {
                    showAlertDialog(getString(R.string.error_title), getString(R.string.not_found_food));
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

