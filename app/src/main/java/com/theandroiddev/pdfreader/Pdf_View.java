package com.theandroiddev.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

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
        pdf_view.fromFile(MainActivity.data_file.get(pos))
        .enableSwipe(true)
        .enableAnnotationRendering(true)
        .scrollHandle(new DefaultScrollHandle(this))
        .load();
    }
}