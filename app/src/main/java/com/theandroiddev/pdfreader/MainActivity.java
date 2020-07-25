package com.theandroiddev.pdfreader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.lang.UCharacter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;

import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Recycler_Adapter.onClickItemListner
{
    private final int REQUEST_CODE = 123;

    private RecyclerView recyclerView;
    public static ArrayList<File> data_file = new ArrayList<>(); //data file
    File dir; //location of file

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing adapter
        recyclerView = findViewById(R.id.recycler_view);


        dir = new File(Environment.getExternalStorageDirectory().toString());
        askPermission();

    }

    public void askPermission()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }
        else
        {
            //if permission is already granted go do the things
           getFile(dir);

            recyclerView.setAdapter(new Recycler_Adapter(data_file,this));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    //if permission granted for first time then check if permission is granted then execute prog
    //checking permission granted or not


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                getFile(dir);
                recyclerView.setAdapter(new Recycler_Adapter(data_file,this));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }
    }

    private  ArrayList<File> getFile(File dir)
    {
        File[]  listFile = dir.listFiles();  //getting all files from dir and storing it in an array
        if (listFile != null && listFile.length > 0)
        {
            for(int i = 0; i < listFile.length; i++) //iterating through all elements of listfiles
            {
                if (listFile[i].isDirectory()) //checkingg if its folder
                {
                    getFile(listFile[i]); //if its directory then go back
                }
                else
                {
                    if (listFile[i].getName().endsWith(".pdf"))
                    {
                        data_file.add(listFile[i]);
                    }
                }
            }
        }


        return data_file; //returning new modified data file
    }

    //setting onClick listner for items of recycler view
    @Override
    public void onClickitem(int position)
    {
        data_file.get(position); //getting file at position of index
        Intent intent = new Intent(this,Pdf_View.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }


}