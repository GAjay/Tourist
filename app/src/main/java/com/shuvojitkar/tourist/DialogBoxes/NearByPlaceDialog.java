package com.shuvojitkar.tourist.DialogBoxes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.shuvojitkar.tourist.Activity.DetailActivity;
import com.shuvojitkar.tourist.Map_Helpers.PlaceDetails;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

import android.support.v7.app.*;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nuhel on 7/31/2017.
 */

public class NearByPlaceDialog {

    private RecyclerView recyclerView;
    private ArrayList<PlaceDetails> placeDetailsArrayList;
    private AlertDialog.Builder builder;
    private View view;
    private AlertDialog dialog;
    private LinearLayoutManager linearLayoutManager;
    private Activity activity;

    public void showDialog(Activity activity, String msg, ArrayList<PlaceDetails> placeDetailsArrayList) {
        this.placeDetailsArrayList = placeDetailsArrayList;
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
        view = activity.getLayoutInflater().inflate(R.layout.nearby_place_dialog, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recview);
        linearLayoutManager = new LinearLayoutManager(activity);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }


    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecHolder>{

        @Override
        public RecyclerAdapter.RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new RecHolder((View) LayoutInflater.from(activity).inflate(R.layout.nearby_place_list_base,null));
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.RecHolder holder, int position) {
            holder.bindData(placeDetailsArrayList.get(position));

        }

        @Override
        public int getItemCount() {
            return placeDetailsArrayList.size();
        }

        public class RecHolder extends RecyclerView.ViewHolder {
            TextView place_name,place_vicinity;
            public RecHolder(View itemView) {
                super(itemView);
                place_name = (TextView) itemView.findViewById(R.id.place_name);
                place_vicinity = (TextView) itemView.findViewById(R.id.place_vicinity);
            }

            void bindData(PlaceDetails data){
                place_name.setText(data.getPlaceName());
                place_vicinity.setText(data.getVicinity());
            }
        }
    }



}