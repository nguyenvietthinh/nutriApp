package com.tma.techday.foodnutrientfact.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.tma.techday.foodnutrientfact.R;

public class SettingResultModeActivity extends AppCompatActivity {

    private Button btnChange;
    Switch switchOnOffResultMode,switchOnOffScanMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_result_mode);
        setUpParam();
    }

    /**
     * Declare Params and set onclick listener for language radio button
     */
    private void setUpParam(){
        switchOnOffResultMode = findViewById(R.id.switchOnOffResultMode);
        switchOnOffResultMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchOnOffResultMode.setText(getString(R.string.result_mode_title_mult));
                }else {
                    switchOnOffResultMode.setText(getString(R.string.result_mode_title_single));
                }
            }
        });
        switchOnOffScanMode = findViewById(R.id.switchOnOffScanMode);
        switchOnOffScanMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchOnOffScanMode.setText(getString(R.string.scan_mode_realtime));
                }else {
                    switchOnOffScanMode.setText(getString(R.string.scan_mode_photo));
                }
            }
        });


        btnChange = (Button) findViewById(R.id.btnChange);
        SharedPreferences.Editor editor = getSharedPreferences("Mode",MODE_PRIVATE).edit();
        btnChange.setOnClickListener(view -> {

            if (!switchOnOffResultMode.isChecked()){
                Intent intent =new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.resultmode), getString(R.string.single));
                editor.putString(getString(R.string.My_Mode), getString(R.string.single));
                startActivity(intent);
            }else if (switchOnOffResultMode.isChecked()){
                Intent intent =new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.resultmode), getString(R.string.multi));
                editor.putString(getString(R.string.My_Mode), getString(R.string.multi));
                startActivity(intent);
            }

            if (switchOnOffScanMode.isChecked()){
                Intent intent =new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.scanmode), getString(R.string.reatime));
                editor.putString(getString(R.string.My_Scan_Mode), getString(R.string.reatime));
                startActivity(intent);
            }else if(!switchOnOffScanMode.isChecked()){
                Intent intent =new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.scanmode), getString(R.string.photo));
                editor.putString(getString(R.string.My_Scan_Mode), getString(R.string.photo));
                startActivity(intent);
            }
            editor.apply();
        });
    }


}
