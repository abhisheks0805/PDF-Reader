package com.theandroiddev.pdfreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class Recycler_Adapter extends RecyclerView.Adapter<List_View_Holder>
{
    private ArrayList<File> data_file = new ArrayList<>();
    private onClickItemListner onClickItemListner;

    public Recycler_Adapter(ArrayList<File> data_file, onClickItemListner onClickItemListner)
    {
        this.data_file = data_file;
        this.onClickItemListner = onClickItemListner;
    }

    @NonNull
    @Override
    public List_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //setting layout for adapter
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      View view = inflater.inflate(R.layout.list_view,parent,false);
      List_View_Holder view_holder = new List_View_Holder(view,onClickItemListner);
      return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull List_View_Holder holder, int position)
    {
        String formattedFileName = data_file.get(position).toString();
        holder.getFileName().setText(formattedFileName.substring(formattedFileName.lastIndexOf('/')+1));;
    }

    @Override
    public int getItemCount()
    {
        return  data_file.size();
    }

    //onCLick item interface
    public interface onClickItemListner
    {
        void onClickitem(int position);
    }
}
