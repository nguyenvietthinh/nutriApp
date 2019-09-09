package com.thinh.foodnutrientfact;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.thinh.foodnutrientfact.enums.ImageDetectEngine;
import com.thinh.foodnutrientfact.helper.InternetCheck;
import com.thinh.foodnutrientfact.helper.database.DatabaseAccess;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CameraView cameraView;
    Button btnDetect;
    AlertDialog waitingDialog;
    View viewResult,viewDetailRessult;
    TextView  txtFoodName,calories,totalFat,cholesterol,protein;
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
            viewDetailRessult.setVisibility(LinearLayout.GONE);
            viewResult.setVisibility(LinearLayout.GONE);

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
            detectImageLabeler.processImage(image)
                    .addOnSuccessListener(labels -> processDataResult(labels))
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
        String foodName = null;
        for (FirebaseVisionImageLabel label : labels){
//            Toast.makeText(this,"Result: "+label.getText(),Toast.LENGTH_LONG).show();//Get and show the label's text description
//            showFoodNutri("Label",label.getText());
            if (label.getText().equalsIgnoreCase("food") || label.getText().equalsIgnoreCase("cup")){
                foodName = null;
            }
            else {
                foodName = label.getText();
                break;
            }
        }
        if (waitingDialog.isShowing()){
            waitingDialog.dismiss();
        }
        getNutrition(foodName);
    }

    /**
     * Get Food Nutrition From food Name after detect image
     * @param foodName label after detect image
     */
    private void getNutrition(String foodName){

        //create  the instance of databases access class and open databases connection
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        ArrayList<String> foodNutri = databaseAccess.getFoodNutri(foodName);

        if(foodNutri==null || foodNutri.isEmpty()){
            showFoodNutri("Error","Not Found");
        }
        else{
            txtFoodName.setText(foodName);
            calories.setText(foodNutri.get(0));
            totalFat.setText(foodNutri.get(1));
            cholesterol.setText(foodNutri.get(2));
            protein.setText(foodNutri.get(3));
//            showFoodNutri("Result", foodNutri+foodName);
            viewResult.setVisibility(LinearLayout.VISIBLE);
        }
        databaseAccess.close();
    }

    /**
     * Show nutritional results have just been obtained
     * @param title
     * @param content detailed nutrition results have just been taken
     */
    private void showFoodNutri(String title, String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.resultLayout){
            viewResult.setVisibility(LinearLayout.GONE);
            viewDetailRessult.setVisibility(LinearLayout.VISIBLE);
        }else if(view.getId()==R.id.detailresultLayout){
            viewDetailRessult.setVisibility(LinearLayout.GONE);
            viewResult.setVisibility(LinearLayout.VISIBLE);
        }
    }
    public void setUpParam(){
        cameraView = (CameraView) findViewById(R.id.camemraView);
        btnDetect = (Button)findViewById(R.id.btnDetect);
        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage("Please waiting...").setCancelable(false).build();
        viewResult = findViewById(R.id.resultLayout);
        viewDetailRessult = findViewById(R.id.detailresultLayout);
        viewDetailRessult.setVisibility(LinearLayout.GONE);
        viewResult.setVisibility(LinearLayout.GONE);
        viewDetailRessult.setOnClickListener(this);
        viewResult.setOnClickListener(this);
        txtFoodName = viewResult.findViewById(R.id.txtFoodName);
        calories = viewResult.findViewById(R.id.txtCal);
        totalFat = viewResult.findViewById(R.id.txtTotalFat);
        cholesterol = viewResult.findViewById(R.id.txtCholesterol);
        protein = viewResult.findViewById(R.id.txtProtein);
    }
}
