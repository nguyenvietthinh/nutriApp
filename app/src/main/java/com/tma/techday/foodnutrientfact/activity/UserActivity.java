package com.tma.techday.foodnutrientfact.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.tma.techday.foodnutrientfact.R;

public class UserActivity extends AppCompatActivity {

    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setUpParam();
    }

    private void setUpParam(){
        btnEdit = findViewById(R.id.btnEditUser);
        btnEdit.setOnClickListener(view -> {
            Intent intent =new Intent(this, EditUserActivity.class);
            startActivity(intent);
        });
    }
}
