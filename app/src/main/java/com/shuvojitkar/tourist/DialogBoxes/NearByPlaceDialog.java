package com.shuvojitkar.tourist.DialogBoxes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.shuvojitkar.tourist.Activity.DetailActivity;
import com.shuvojitkar.tourist.Activity.NearByPlaceDetailsMapActivity;
import com.shuvojitkar.tourist.Map_Helpers.DownloadPlaceDetails;
import com.shuvojitkar.tourist.Map_Helpers.GetNplaceDetails;
import com.shuvojitkar.tourist.Map_Helpers.PlaceDetails;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

import android.support.v7.app.*;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nuhel on 8/3/2017.
 */

public class NearByPlaceDialog {

    private RecyclerView recyclerView;
    private ArrayList<PlaceDetails> placeDetailsArrayList;
    private AlertDialog.Builder builder;
    private View view;
    private AlertDialog dialog = null;
    private LinearLayoutManager linearLayoutManager;
    private Activity activity;
    private int viewTracker = 1;
    private double mainLat;
    private double mainLan;
    private String main_place_name;

    public NearByPlaceDialog(final Activity activity, ArrayList<PlaceDetails> placeDetailsArrayList1, Double lat, Double lan, String main_place_name) {
        this.placeDetailsArrayList = placeDetailsArrayList1;
        this.activity = activity;
        this.mainLat = lat;
        this.mainLan = lan;
        this.main_place_name = main_place_name;
        builder = new AlertDialog.Builder(activity);
        view = activity.getLayoutInflater().inflate(R.layout.nearby_place_dialog, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recview);
        linearLayoutManager = new LinearLayoutManager(activity);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (viewTracker == 2) {
                    CircleImageView circleImageView;
                    for (int a = 0; a < recyclerView.getChildCount(); a++) {
                        Object[] DataTransfer = new Object[4];
                        String url = "https://maps.googleapis.com/maps/api/place/details/json?reference=";
                        url += placeDetailsArrayList.get(a).getReference();
                        url += "&sensor=true&key=";
                        url += "AIzaSyD-2lbqP9aonaHTxg3r_8L4PgzRx0SEdZ8";
                        DataTransfer[0] = url;
                        DataTransfer[1] = recyclerView.getChildAt(a);
                        DataTransfer[2] = placeDetailsArrayList.get(a);
                        DataTransfer[3] = activity;
                        new GetNplaceDetails().execute(DataTransfer);
                    }
                }
                viewTracker++;

            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        builder.setView(view);
        dialog = builder.create();
        // dialog.show();

    }


    public AlertDialog getDialog() {
        return dialog;
    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecHolder> {

        @Override
        public RecyclerAdapter.RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(activity).inflate(R.layout.nearby_place_list_base, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.RecHolder holder, int position) {
            View v = holder.itemView;
            v.setTag(position);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    if (!placeDetailsArrayList.get(position).getPhoneNumber().equals("-NA-")) {
                        Intent intent = new Intent(activity, NearByPlaceDetailsMapActivity.class);
                        intent.putExtra("NLat", placeDetailsArrayList.get(position).getLat());
                        intent.putExtra("NLan", placeDetailsArrayList.get(position).getLan());
                        intent.putExtra("Number", placeDetailsArrayList.get(position).getPhoneNumber());
                        intent.putExtra("MLat", mainLat);
                        intent.putExtra("MLan", mainLan);
                        intent.putExtra("NName", placeDetailsArrayList.get(position).getPlaceName());
                        intent.putExtra("MName", main_place_name);
                        activity.startActivity(intent);
                    } else
                        Toast.makeText(activity, "Please Wait a moment", Toast.LENGTH_SHORT).show();
                }
            });
            holder.bindData(placeDetailsArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return placeDetailsArrayList.size();
        }

        public class RecHolder extends RecyclerView.ViewHolder {
            private LinearLayout linearLayout;
            private TextView place_name, place_vicinity;
            public RecHolder(View itemView) {
                super(itemView);

                place_name = (TextView) itemView.findViewById(R.id.place_name);
                place_vicinity = (TextView) itemView.findViewById(R.id.place_vicinity);
            }

            void bindData(PlaceDetails data) {
                place_name.setText(data.getPlaceName());
                place_vicinity.setText(data.getVicinity());
            }
        }
    }
}