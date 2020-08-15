package com.theandroiddev.pdfreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Recycler_Adapter extends RecyclerView.Adapter<List_View_Holder> implements Filterable
{
    private ArrayList<File> data_file = new ArrayList<>();
    private List<File>data_file_all; //becoz data_file will we modified by search so this will keep track of every element
    private onClickItemListner onClickItemListner;
    private File file;


    public Recycler_Adapter(ArrayList<File> data_file, onClickItemListner onClickItemListner)
    {
        this.data_file = data_file;
        this.data_file_all = new ArrayList<>(data_file); //always use list to array list to prevent bugs
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
        final String formattedFileName = data_file.get(position).toString();
        holder.getFileName().setText(formattedFileName.substring(formattedFileName.lastIndexOf('/')+1));



        file = new File(data_file.get(position).toString());
        final long size = Long.parseLong(String.valueOf(file.length()/1024)); //TODO
        holder.getFileSize().setText(size+"KB");
        //date
        Date lastModified = new Date(file.lastModified());
        final String formattedLastModified = lastModified.toString();
        try
        {
            String formattedDate = formattedLastModified.substring(4,10)+formattedLastModified.substring(29,34);;
            holder.getFileDate().setText(formattedDate);
        }
        catch(Exception e)
        {
           //error
        }

        //onClickMoreOPtn
        holder.getMoreOptnBtn().setOnClickListener(new View.OnClickListener()
      {
          @Override
          public void onClick(View view)
          {
              holder.getMoreOptnBtn().setOnClickListener(new View.OnClickListener() //clicking 3 dots
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
                                      Toast.makeText(holder.itemView.getContext(),"Not Allowed",Toast.LENGTH_SHORT).show();
                                      return true;

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

                                  case R.id.detailspopup:
                                     Intent detailsIntent = new Intent(holder.itemView.getContext(),Details.class);
                                     detailsIntent.putExtra("filename","location: "+formattedFileName+ "\n" + "\n" + "Date Created "+ formattedLastModified + "\n" + "\n" +"file size "+ size+"KB");
                                     holder.itemView.getContext().startActivity(detailsIntent);
                                     return true;
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

    //search filter
    @Override
    public Filter getFilter()
    {
        return filter;
    }

    Filter filter = new Filter()
    {
        @Override
        //background thread
        protected FilterResults performFiltering(CharSequence charSequence)
        {
            //creaating a list too store filtered content
            List <File> filteredList = new ArrayList<>();
            //checking if searcch bar is empty
            if(charSequence.toString().isEmpty())
            {
                if(filteredList.addAll(data_file_all))
                {
                    data_file_all.toString();
                }
            }
            else
            {
                for(File results : data_file_all)
                {
                    if(results.toString().toLowerCase().contains(charSequence.toString().toLowerCase()))
                    {
                        filteredList.add(results);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values=filteredList;
            return filterResults;
        }

        @Override
        //ui thread
        protected void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            //updatating ui
                data_file.clear();
                data_file.addAll((Collection<? extends File>) filterResults.values);
                notifyDataSetChanged();

        }
    };


    //onCLick item interface
    public interface onClickItemListner
    {
        void onClickitem(int position);
    }
}
