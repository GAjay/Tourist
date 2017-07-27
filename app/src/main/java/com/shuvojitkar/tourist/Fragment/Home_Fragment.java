package com.shuvojitkar.tourist.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.shuvojitkar.tourist.Activity.DetailActivity;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.Model.Home_frag_Model;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by SHOBOJIT on 7/22/2017.
 */

public class Home_Fragment extends Fragment {
    View v;
    private RecyclerView mRecyclerView;

    private ProgressDialog mPd;
    private DatabaseReference mUserDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.home_fragment,container,false);
        init(v);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        //Toast.makeText(v.getContext(), "model.getName()", Toast.LENGTH_SHORT).show();
        return v;
    }

    private void init(final View v) {
        mUserDatabase  = GetFirebaseRef.GetDbIns().getReference().child("home_page_post");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.home_recView);
        mRecyclerView.setHasFixedSize(true);
        mPd = new ProgressDialog(v.getContext());


    }

    @Override
    public void onStart() {
        super.onStart();
        mPd.show();
        mPd.setCancelable(false);
        FirebaseRecyclerAdapter<Home_frag_Model,HomePageViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Home_frag_Model, HomePageViewHolder>(
                        Home_frag_Model.class,
                        R.layout.home_rec_layout,
                        HomePageViewHolder.class,
                        mUserDatabase
                ) {
                    @Override
                    protected void populateViewHolder(HomePageViewHolder viewHolder, final Home_frag_Model model, int position) {
                        viewHolder.setImage(model.getImage());
                        viewHolder.setName(model.getName());
                        //   Toast.makeText(v.getContext(), model.getName(), Toast.LENGTH_SHORT).show();

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in = new Intent(v.getContext(),DetailActivity.class);
                                Bundle b = new Bundle();

                                Bundle extras = new Bundle();
                                extras.putString("Lang",String.valueOf(model.getLang()));
                                extras.putString("Lat",String.valueOf(model.getLat()));
                                extras.putString("Image",model.getImage());
                                extras.putString("Name",model.getName());
                                extras.putString("Description",model.getDescription());
                                in.putExtras(extras);
                                startActivity(in);


                                // Toast.makeText(v.getContext(), "Lang"+model.getLang(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onDataChanged() {
                        if (mPd != null && mPd.isShowing()) {
                           mPd.dismiss();
                        }
                    }

                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class HomePageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public HomePageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        void setImage(String s){
            ImageView img = (ImageView) mView.findViewById(R.id.home_rec_imageView);
            Picasso.with(mView.getContext())
                    .load(s)

                    .into(img);

        }

        void setName(String s){
            TextView placeName = (TextView) itemView.findViewById(R.id.home_rec_place_name);
            placeName.setText(s);
        }
    }
}
