package com.tma.techday.foodnutrientfact.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tma.techday.foodnutrientfact.R;

import java.util.Locale;

public class SettingLanguageActivity extends AppCompatActivity {

    private RadioGroup radioLangGroup;
    private RadioButton radioLangButton;
    private Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_setting_language);
        setUpParam();
    }

    /**
     * Set Locale when choose language
     * @param lang
     */
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = getBaseContext().getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        getBaseContext().createConfigurationContext(config);
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    private void loadLocale(){
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("My_Lang","");
        setLocale(language);
    }

    private void setUpParam(){
        radioLangGroup = (RadioGroup) findViewById(R.id.radioLang);
        btnChange = (Button) findViewById(R.id.btnChange);

        btnChange.setOnClickListener(view -> {

            int selectedId = radioLangGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            radioLangButton = (RadioButton) findViewById(selectedId);
            if (radioLangButton.getText().toString().equals("Tiếng Việt")){
                setLocale("vn");
            }else if (radioLangButton.getText().toString().equals("English")){
                setLocale("en");
            }

        });
    }
}
