package com.theandroiddev.pdfreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
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
    public void onBindViewHolder(@NonNull final List_View_Holder holder, final int position)
    {
        String formattedFileName = data_file.get(position).toString();
        holder.getFileName().setText(formattedFileName.substring(formattedFileName.lastIndexOf('/')+1));

        //onClickMoreOPtn

      holder.getMoreOptnBtn().setOnClickListener(new View.OnClickListener()
      {
          @Override
          public void onClick(View view)
          {
              holder.getMoreOptnBtn().setOnClickListener(new View.OnClickListener()
              {
                  @Override
                  public void onClick(View view)
                  {
                      PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(),view); //2nd parameter is anchor means where we want our popup so im saying at btn
                      popupMenu.inflate(R.menu.popup_menu);
                      popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                      {
                          @Override
                          public boolean onMenuItemClick(MenuItem menuItem) //handling popup menu buttons
                          {
                              switch(menuItem.getItemId())
                              {
                                  case R.id.delete:
                                      Toast.makeText(holder.itemView.getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                                      break;

                         case R.id.share:
                             Uri uri = FileProvider.getUriForFile(holder.itemView.getContext(),holder.itemView.getContext().getPackageName()+".provider",data_file.get(position));
                             Intent intent = new Intent(); //creating object of class Intent
                             intent.setAction(Intent.ACTION_SEND); //setting action for intent in this case sending data
                             intent.setType("application/pdf");
                             intent.putExtra(Intent.EXTRA_STREAM, uri);
                             intent = intent.createChooser(intent,"Share");
                             intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                             //starting intent activity
                             holder.itemView.getContext().startActivity(intent);

                             break;
                              }
                              return true;
                          }
                      });
                      popupMenu.show();
                  }
              });
                  }
              });


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
