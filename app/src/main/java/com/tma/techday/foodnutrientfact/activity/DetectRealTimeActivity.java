package com.tma.techday.foodnutrientfact.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andremion.counterfab.CounterFab;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
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
import com.tma.techday.foodnutrientfact.model.FoodBoxContain;
import com.tma.techday.foodnutrientfact.model.FoodInfoDTO;
import com.tma.techday.foodnutrientfact.service.FoodNutriService;
import com.tma.techday.foodnutrientfact.service.OrderService;
import com.tma.techday.foodnutrientfact.viewholder.GraphicOverlay;
import com.tma.techday.foodnutrientfact.viewholder.RectOverlay;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.log.LoggersKt;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.preview.FrameProcessor;
import io.fotoapparat.selector.FlashSelectorsKt;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;


/**
 * Detect image real time.
 */
public class DetectRealTimeActivity extends AppCompatActivity {
    public static final int DEGREES_90 = 90;
    public static final int BOUND_ABSOLUTE_ERROR = 17;
    private Fotoapparat fotoapparat;
    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    AlertDialog waitingDialog;
    CounterFab counterFab;
    RelativeLayout.LayoutParams layoutParams;
    FoodBoxContain foodBoxContain;

    @Inject
    FoodNutriService foodNutriService;

    @Inject
    OrderService orderService;

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
        IGNORE_LIST.add("Yellow");
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
     *     <li>Real time </li>
     *     <li>Detect image</li>
     *     <li>Detect object and track</li>
     *     <li>Show food nutrient result from detect result</li>
     * </ul>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_realtime_activity);
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
                .frameProcessor(new SampleFrameProcessor())
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

        graphicOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    Rect rect = foodBoxContain.getRect();
                    if (isPressedWithinBox(motionEvent, rect)) {
                        waitingDialog.show();
                        runDetect(foodBoxContain.getBitmapImage());
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
     * Create menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem switchOnOffItem = menu.findItem(R.id.switchOnOffItem);
        switchOnOffItem.setActionView(R.layout.switch_layout);
        Switch switchOnOff = switchOnOffItem.getActionView().findViewById(R.id.switchOnOff);
        switchOnOff.setChecked(true);
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(DetectRealTimeActivity.this,DetectActivity.class);
                startActivity(intent);
            }
        });
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
                            showAlertDialog(getString(R.string.error_title), getString(R.string.unable_detect_image));
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
        layoutParams = (RelativeLayout.LayoutParams) cameraView.getLayoutParams();
        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage(getString(R.string.analyzing_dialog)).setCancelable(false).build();
        counterFab = findViewById(R.id.fab);
        counterFab.setOnClickListener(view -> {
            Intent intent = new Intent(DetectRealTimeActivity.this,AddToCartActivity.class);
            startActivity(intent);
        });
        counterFab.setCount(orderService.getCountCart());
        fotoapparat = createFotoapparat();
    }

    /**
     * MLKit Object Detection Function
     */
    private void runObjectDetection(Frame frame) {

        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation(FirebaseVisionImageMetadata.ROTATION_90)
                .setHeight(frame.getSize().height)
                .setWidth(frame.getSize().width)
                .build();

        FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(frame.getImage(), metadata);
        FirebaseVisionObjectDetectorOptions detectorOptions =  new FirebaseVisionObjectDetectorOptions.Builder()
                .setDetectorMode(FirebaseVisionObjectDetectorOptions.STREAM_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build();

        FirebaseVisionObjectDetector objectDetector = FirebaseVision.getInstance().getOnDeviceObjectDetector(detectorOptions);

        objectDetector.processImage(image)
                .addOnSuccessListener(detectedObjects -> {
                            for (FirebaseVisionObject obj : detectedObjects) {
                                Rect bounds = buildBound(obj);
                                Drawable drawable = new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(frame.getImage(), 0, frame.getImage().length));
                                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
//                                Bitmap bmp =BitmapFactory.decodeByteArray(frame.getImage(), 0, frame.getImage().length);
                                cutBitmapImage(bmp, bounds);
                                Toast.makeText(DetectRealTimeActivity.this, getString(R.string.guide_detect), Toast.LENGTH_LONG).show();
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
        graphicOverlay.clear();
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
        foodBoxContain = FoodBoxContain.of(bounds,cutBitmap);
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
     * Perform frame processing
     */
    private class SampleFrameProcessor implements FrameProcessor {
        @Override
        public void process(@NotNull Frame frame) {
            runObjectDetection(frame);
        }
    }

}