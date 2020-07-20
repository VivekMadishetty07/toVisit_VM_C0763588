package com.favplaces.maps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.favplaces.R;
import com.favplaces.roomDb.DatabaseClient;
import com.favplaces.roomDb.FavouritePlacesBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FavoritePlacesAdapter extends RecyclerView.Adapter<FavoritePlacesAdapter.ContactsViewHolder> {

    private Context mCtx;
    private ArrayList<FavouritePlacesBean> favList;

    public FavoritePlacesAdapter(Context mCtx, ArrayList<FavouritePlacesBean> favList) {
        this.mCtx = mCtx;
        this.favList = favList;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_favorite_places, parent, false);
        return new ContactsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ContactsViewHolder holder, final int position) {
        final FavouritePlacesBean t = favList.get(position);
        int currentPosition = position + 1;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());

        holder.tvname.setText(currentPosition + ".");
        holder.tvaddress.setText(t.getAddress());
        holder.tvdate.setText(date);
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCtx,UpdateFavoritePlace.class);
                intent.putExtra("id",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .favouritePlacesDao()
                        .delete(favList.get(position));
               Intent intent=new Intent(  new Intent(mCtx, MapsActivity.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               mCtx.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return favList.size();
    }


    static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView tvdate, tvaddress, tvname;
        ImageButton delete, update;

        public ContactsViewHolder(final View view) {
            super(view);

            tvname = view.findViewById(R.id.txt_name);
            delete = view.findViewById(R.id.btn_delete);
            update = view.findViewById(R.id.btn_edit);
            tvaddress = view.findViewById(R.id.txt_address);
            tvdate = view.findViewById(R.id.txt_date);


        }


    }
}