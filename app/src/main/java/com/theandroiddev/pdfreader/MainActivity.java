package com.theandroiddev.pdfreader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.SearchManager;
import android.view.MenuInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView.OnQueryTextListener;

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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.Inflater;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements Recycler_Adapter.onClickItemListner
{
    private final int REQUEST_CODE = 123;

    private RecyclerView recyclerView;
    public static ArrayList<File> data_file = new ArrayList<>(); //data file
    private File dir; //location of file
    private Recycler_Adapter recycler_adapter;
    private FrameLayout recylerframelayout;
    private FrameLayout progressframe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recylerframelayout = findViewById(R.id.recycler_frame);
        progressframe = findViewById(R.id.progress_Frame);

        recylerframelayout.setVisibility(View.GONE);
        progressframe.setVisibility(View.VISIBLE);

        AppRate.with(this)
        .setInstallDays(2)
        .setLaunchTimes(2)
        .setRemindInterval(2)
        .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);


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
            recycler_adapter = new Recycler_Adapter(data_file,this);
            recyclerView.setAdapter(recycler_adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            progressframe.setVisibility(View.GONE);
            recylerframelayout.setVisibility(View.VISIBLE);


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
                recycler_adapter = new Recycler_Adapter(data_file,this);
                recyclerView.setAdapter(recycler_adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                progressframe.setVisibility(View.GONE);
                recylerframelayout.setVisibility(View.VISIBLE);




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

    //search creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                recycler_adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
}