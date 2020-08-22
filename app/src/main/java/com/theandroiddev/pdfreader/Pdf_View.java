package com.theandroiddev.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Pdf_View extends AppCompatActivity
{
    private PDFView pdf_view;
    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf__view);


        pdf_view = (PDFView) findViewById(R.id.pdfView);
        pos = getIntent().getIntExtra("position",-1); //getting position sent from onClick
        displayPdf();
    }
    private void displayPdf()
    {
        try
        {
            pdf_view.fromFile(MainActivity.data_file.get(pos))
                    .enableSwipe(true)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        }
        catch (Exception e)
        {
            //
        }


    }
}