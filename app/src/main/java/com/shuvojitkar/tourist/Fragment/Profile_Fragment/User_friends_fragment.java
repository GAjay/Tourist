package com.shuvojitkar.tourist.Fragment.Profile_Fragment;

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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.Model.Person_list;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

/**
 * Created by SHOBOJIT on 8/4/2017.
 */

public class User_friends_fragment extends Fragment {
    private ProgressDialog mPd;
    private RecyclerView mFrndrec;
    private static View v;
    private String userId;
    DatabaseReference mUserFrndRef;
    DatabaseReference mUser01,mUser02;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_user_friend_fragment,container,false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        init(v);
        mFrndrec.setHasFixedSize(true);
        mFrndrec.setLayoutManager(new LinearLayoutManager(v.getContext()));

        return  v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Person_list,FrndListViewHolder> fr =
                new FirebaseRecyclerAdapter<Person_list, FrndListViewHolder>(
                        Person_list.class,
                        R.layout.rec_single_layout,
                        FrndListViewHolder.class,
                        mUserFrndRef
                ) {
                    @Override
                    protected void populateViewHolder(final FrndListViewHolder viewHolder, final Person_list model, int position) {
                        final String key =getRef(position).getKey();
                        mUser01.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(key).exists()){
                                    final DatabaseReference db = GetFirebaseRef.GetDbIns().getReference().child("tourist").child(key);
                                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String Name = dataSnapshot.child("name").getValue().toString();
                                            String Image = dataSnapshot.child("image").getValue().toString();
                                            String  Status = dataSnapshot.child("status").getValue().toString();

                                            viewHolder.setImage(Image);
                                            viewHolder.setName(Name);
                                            viewHolder.setStatus(Status);
                                       //     Toast.makeText(v.getContext(), Name, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }else {
                                    mUser02.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child(key).exists()){
                                                DatabaseReference db = GetFirebaseRef.GetDbIns().getReference().child("touristGuide").child(key);
                                                db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String Name = dataSnapshot.child("name").getValue().toString();
                                                        String Image = dataSnapshot.child("image").getValue().toString();
                                                        String  Status = dataSnapshot.child("status").getValue().toString();
                                                       // Toast.makeText(v.getContext(), Name, Toast.LENGTH_SHORT).show();
                                                        viewHolder.setImage(Image);
                                                        viewHolder.setName(Name);
                                                        viewHolder.setStatus(Status);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                };
                mFrndrec.setAdapter(fr);


    }

    private void init(View v) {

        mFrndrec = (RecyclerView) v.findViewById(R.id.User_Frnd_Rec);
        mFrndrec.setHasFixedSize(true);
        mUserFrndRef = GetFirebaseRef.GetDbIns().getReference().child("Friends").child(userId);
        mPd = new ProgressDialog(v.getContext());
        mUser01 =GetFirebaseRef.GetDbIns().getReference().child("tourist");
        mUser02 =GetFirebaseRef.GetDbIns().getReference().child("touristGuide");
    }


    public static class FrndListViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FrndListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        void setImage(String s){
            ImageView img = (ImageView) mView.findViewById(R.id.rec_single_image);
            if (!s.equals("default")||!s.equals(null)){
                Picasso.with(mView.getContext())
                        .load(s)
                        .placeholder(R.drawable.person2)
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
