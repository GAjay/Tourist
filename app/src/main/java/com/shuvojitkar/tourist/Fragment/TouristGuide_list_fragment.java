package com.shuvojitkar.tourist.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.shuvojitkar.tourist.Activity.Guide_Profile_Activity;
import com.shuvojitkar.tourist.Activity.UserTouristProfileActivity;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.Model.Person_list;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

/**
 * Created by SHOBOJIT on 7/29/2017.
 */

public class TouristGuide_list_fragment extends Fragment {

    private RecyclerView mTouristGuideList;

    private ProgressDialog mPd;
    private DatabaseReference mTouristGuide;
   View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tourist_guide_fragment,container,false);
        init(v);
        mTouristGuideList.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return  v;
    }


    private void init(View v) {
        mTouristGuideList = (RecyclerView) v.findViewById(R.id.tourist_list);
        mTouristGuideList.setHasFixedSize(true);
        mTouristGuide = GetFirebaseRef.GetDbIns().getReference().child("touristGuide");
        mPd = new ProgressDialog(v.getContext());
    }



    @Override
    public void onStart() {
        super.onStart();
        mPd.show();
        mPd.setCancelable(false);
        FirebaseRecyclerAdapter<Person_list,TouristGuideViewHolder> firrec =
                new FirebaseRecyclerAdapter<Person_list, TouristGuideViewHolder>(
                        Person_list.class,
                        R.layout.rec_single_layout,
                        TouristGuideViewHolder.class,
                        mTouristGuide
                ) {
                    @Override
                    protected void populateViewHolder(TouristGuideViewHolder viewHolder, Person_list model, int position) {
                            viewHolder.setImage(model.getImage());
                            viewHolder.setName(model.getName());
                            viewHolder.setStatus(model.getStatus());

                        final String user_id = getRef(position).getKey();
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(v.getContext(),Guide_Profile_Activity.class).putExtra("user_id",user_id));
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

        mTouristGuideList.setAdapter(firrec);




    }



    public static class TouristGuideViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TouristGuideViewHolder(View itemView) {
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
