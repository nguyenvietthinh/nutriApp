package com.thinh.foodnutrientfact.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.di.FoodNutriApplication;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    Button btnDetect, btnSetting;


    /**
     * Switch from home screen to calorie setting and detect components
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupParam();
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
        if ( isExternalStorageWritable() ) {
            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/MyPersonalAppFolder" );
            File logDirectory = new File( appDirectory + "/log" );
            File logFile = new File( logDirectory, "logcat" + System.currentTimeMillis() + ".txt" );

            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }

            // create log folder
            if ( !logDirectory.exists() ) {
                logDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -f " + logFile);
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        } else if ( isExternalStorageReadable() ) {
            // only readable
        } else {
            // not accessible
        }
        btnDetect.setOnClickListener(view -> {
            doOpenOtherActivity(DetectActivity.class);
        });

        btnSetting.setOnClickListener(view -> {
            doOpenOtherActivity(SettingActivity.class);
        });


    }

    /**
     * Switch to other activity
     * @param aClass destination activity
     */
    public void doOpenOtherActivity(Class aClass)
    {
        Intent intent =new Intent(this, aClass);
        startActivity(intent);
    }

    /**
     * Declare Params
     */
    public void setupParam(){
        btnDetect = findViewById(R.id.btnDetect);
        btnSetting = findViewById(R.id.btnSetting);
    }

    /**
     * Checks if external storage is available for read and write
     * @return
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read
     * @return
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
            return true;
        }
        return false;
    }

}
