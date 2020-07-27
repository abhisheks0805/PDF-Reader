package com.theandroiddev.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Details extends AppCompatActivity
{
    private TextView details;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        details = findViewById(R.id.details);
        details.setText(getIntent().getStringExtra("filename"));
    }
}