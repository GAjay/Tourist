package com.shuvojitkar.tourist.Fragment;

import android.app.ProgressDialog;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.Model.Person_list;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

/**
 * Created by SHOBOJIT on 7/31/2017.
 */

public class Travler_list_fragment extends Fragment {
        private static  View v;
        private ProgressDialog mPd;
        private DatabaseReference mTourist;
    private RecyclerView mTouristList;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.traveler_list_fragment,container,false);
        init(v);
        mTouristList.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }

    private void init(View v) {
        mTouristList = (RecyclerView) v.findViewById(R.id.traveler_list_rec);
        mTouristList.setHasFixedSize(true);
        mTourist = GetFirebaseRef.GetDbIns().getReference().child("tourist");
        mPd = new ProgressDialog(v.getContext());
    }


    @Override
    public void onStart() {
        super.onStart();
        mPd.show();
        mPd.setCancelable(false);
        FirebaseRecyclerAdapter<Person_list,Travler_list_fragment.TouristListViewHolder> firrec =
                new FirebaseRecyclerAdapter<Person_list, TouristListViewHolder>(
                        Person_list.class,
                        R.layout.rec_single_layout,
                       TouristListViewHolder.class,
                        mTourist
                ) {
                    @Override
                    protected void populateViewHolder(TouristListViewHolder viewHolder, Person_list model, int position) {
                        viewHolder.setImage(model.getImage());
                        viewHolder.setName(model.getName());
                        viewHolder.setStatus(model.getStatus());
                    }

                    @Override
                    public void onDataChanged() {
                        if (mPd != null && mPd.isShowing()) {
                            mPd.dismiss();
                        }
                    }
                };

        mTouristList.setAdapter(firrec);




    }

    public static class TouristListViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TouristListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        void setImage(String s){
            ImageView img = (ImageView) mView.findViewById(R.id.rec_single_image);
            if (!s.equals("default")){
                Picasso.with(mView.getContext())
                        .load(s)
                        .into(img);
            }


        }

        void setName(String s){
            TextView Name= (TextView) itemView.findViewById(R.id.rec_single_name);
            Name.setText(s);
        }

        void setStatus (String s){
            TextView status = (TextView) itemView.findViewById(R.id.rec_single_status);
            status.setText(s);
        }
    }

}
