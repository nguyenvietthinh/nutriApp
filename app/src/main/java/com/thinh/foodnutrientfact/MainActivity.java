package com.thinh.foodnutrientfact;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;

import com.thinh.foodnutrientfact.di.FNServiceComponent;
import com.thinh.foodnutrientfact.di.FoodNutriApplication;
import com.thinh.foodnutrientfact.enums.ImageDetectEngine;
import com.thinh.foodnutrientfact.helper.InternetCheck;
import com.thinh.foodnutrientfact.helper.database.FoodNutriDAO;
import com.thinh.foodnutrientfact.model.FoodInfoDTO;
import com.thinh.foodnutrientfact.service.FoodNutriService;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CameraView cameraView;
    Button btnDetect;
    AlertDialog waitingDialog;
    View viewResult,viewDetailRessult;
    TextView  txtFoodName,calories,totalFat,cholesterol,protein;
    @Inject
    FoodNutriService foodNutriService;
    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpParam();
        ((FoodNutriApplication) getApplication()).getComponent().inject(this);
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
            cameraView.start();
            cameraView.captureImage();


        });
    }

    /**
     * Get the vision image detecter then analyze the image bitmap.
     * @param bitmap the picture taken from camera.
     */
    private void runDetect(Bitmap bitmap) {

        //Create a FirebaseVisionImage object from a Bitmap object
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        // If have internet, we will use COULD_ENGINE mode Else we will use DEVICE_ENGINE mode
        new InternetCheck(connectInternet -> {
            FirebaseVisionImageLabeler detectImageLabeler = getFirebaseVisionImageLabeler(connectInternet ? ImageDetectEngine.COULD_ENGINE : ImageDetectEngine.DEVICE_ENGINE);
            if(detectImageLabeler == null)
            {
                //TODO: log the error
                Log.d("Error","Detect Image Labeler cannot be null");
                return;
            }
            detectImageLabeler.processImage(image)
                    .addOnSuccessListener(labels -> {
                        if(labels != null && !labels.isEmpty())
                        {
                            // Image that is 'More correct' ia put at the top
                            labels.sort((lb1, lb2) -> (int) (lb2.getConfidence() - lb1.getConfidence()));
                            processDataResult(labels);
                        }else{
                            showFoodNutri("Error","Cannot be Detect Image");
                            waitingDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(e -> Log.d("EDMTERROR",e.getMessage()));
        });

    }

    /**
     * Get image labeler after validate
     * @param imageDetectEngine types of image labeler
     */
    private FirebaseVisionImageLabeler getFirebaseVisionImageLabeler(ImageDetectEngine imageDetectEngine) {
        if(imageDetectEngine == null)
            throw new IllegalArgumentException("Engine must be specified");
        switch (imageDetectEngine)
        {
            case COULD_ENGINE:
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
            if (itemName.equalsIgnoreCase("food") || itemName.equalsIgnoreCase("cup")){
                continue;
            }
            else {
                foodName = itemName;
                break;
            }
        }
        if (waitingDialog.isShowing()){
            waitingDialog.dismiss();
        }
        if(!foodName.equals("")){
            getNutrition(foodName);
        }
    }

    /**
     * Get Food Nutrition From food Name after detect image
     * @param foodName label after detect image
     */
    private void getNutrition(String foodName){

        //create  the instance of databases access class and open databases connection
//        FoodNutriDAO databaseAccess = FoodNutriDAO.getInstance(getApplicationContext());
//        FoodInfoDTO foodNutri = databaseAccess.getFoodNutri(foodName);
        String foodNutri = foodNutriService.getFoodNutri(foodName);

        if(foodNutri==null){
            showFoodNutri("Error","Not Found");
        }
        else{
            showFoodNutri("Nutrition Facts",foodNutri);
        }
    }

    /**
     * Show nutritional results have just been obtained
     * @param title
     * @param content detailed nutrition results have just been taken
     */
    private void showFoodNutri(String title, String content){
         AlertDialog builder = new AlertDialog.Builder(this,R.style.CustomDialog).create();
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.show();
    }

    /**
     * Show and Hide Result And Detail Result Layout
     * @param view
     */
    @Override
    public void onClick(View view) {

    }

    /**
     * Declare Params
     */
    public void setUpParam(){
        cameraView = findViewById(R.id.camemraView);
        btnDetect = findViewById(R.id.btnDetect);
        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage("Analyzing the object...").setCancelable(false).build();

    }
}
