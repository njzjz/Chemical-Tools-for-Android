package com.njzjz.chemicaltools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TitleActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.njzjz.chemicalTools.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
    }
    public void openElement(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openMass(View view) {
        Intent intent = new Intent(this, MassActivity.class);
        startActivity(intent);
    }
    }
