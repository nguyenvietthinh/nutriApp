package com.tma.techday.foodnutrientfact.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.tma.techday.foodnutrientfact.R;

/**
 * The screen is used to select the language for the app
 */
public class SettingLanguageActivity extends AppCompatActivity {

    private RadioGroup radioLangGroup;
    private RadioButton radioLangButton;
    private Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_language);
        setUpParam();
    }

    /**
     * Declare Params and set onclick listener for language radio button
     */
    private void setUpParam(){
        radioLangGroup = (RadioGroup) findViewById(R.id.radioLang);
        btnChange = (Button) findViewById(R.id.btnChange);
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        btnChange.setOnClickListener(view -> {
            int selectedId = radioLangGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            radioLangButton =  findViewById(selectedId);
            if (radioLangButton.getText().toString().equals(getString(R.string.Vietnamese))){
                Intent intent =new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.language), getString(R.string.vi));
                editor.putString(getString(R.string.My_Lang), getString(R.string.vi));
                startActivity(intent);
            }else if (radioLangButton.getText().toString().equals(getString(R.string.English))){
                Intent intent =new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.language), getString(R.string.en));
                editor.putString(getString(R.string.My_Lang), getString(R.string.en));
                startActivity(intent);
            }
            editor.apply();
        });
    }


}
