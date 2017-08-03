package com.shuvojitkar.tourist.Fragment.Profile_Fragment;

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

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.Model.User_Post;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SHOBOJIT on 7/28/2017.
 */

public class User_Post_fragment extends Fragment {
    private static RecyclerView mRecyclerView;
    private DatabaseReference mRootRef;
    private DatabaseReference mDatabaseRef;
    private static View v;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.profile_user_post_fragment,container,false);
        init(v);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(lin);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
       Query query =mRootRef.orderByValue();
        FirebaseRecyclerAdapter <User_Post,User_post_fragment_VH> fr = new FirebaseRecyclerAdapter<User_Post, User_post_fragment_VH>(
                User_Post.class,
                R.layout.user_post_rec_layout,
                User_post_fragment_VH.class,
                query
        ) {
            @Override
            protected void populateViewHolder(final User_post_fragment_VH viewHolder, User_Post model, int position) {
                viewHolder.setImage(model.getImage());
                viewHolder.setDate(model.getDate());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());


                mDatabaseRef.child(model.getUser_type()).child(model.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userProimg = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();

                        viewHolder.setUserproImage(userProimg);
                        viewHolder.setUserName(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






            }
        };
        mRecyclerView.setAdapter(fr);

    }

    private void init(View v) {
        mRootRef = GetFirebaseRef.GetDbIns().getReference().child("User_Post");
        mDatabaseRef = GetFirebaseRef.GetDbIns().getReference();
        mRecyclerView  = (RecyclerView) v.findViewById(R.id.m_user_post_frag_rec);

    }

    public static class User_post_fragment_VH extends RecyclerView.ViewHolder{
        View mView;
        public User_post_fragment_VH(View itemView) {
            super(itemView);
            mView=itemView;
        }

            void setImage(String s) {
             ImageView img = (ImageView) mView.findViewById(R.id.user_post_rec_image);
             Picasso.with(mView.getContext()).load(s).into(img);
        }
            void setDate(String s) {
            TextView date = (TextView) mView.findViewById(R.id.user_post_rec_datetime);
             date.setText(s);
        }
            void setTitle(String s) {
            TextView title = (TextView) mView.findViewById(R.id.user_post_rec_title);
            title.setText(s);
        }
            void setDescription(String s) {
             TextView des= (TextView) mView.findViewById(R.id.user_post_rec_des);
             des.setText(s);
        }
            void setUserName(String s){
                TextView username = (TextView) mView.findViewById(R.id.user_post_rec_name);
                username.setText(s);

            }
            void setUserproImage(String userProimg){
                ImageView userName = (ImageView) mView.findViewById(R.id.user_rec_post_pro_img);
                Picasso.with(mView.getContext()).load(userProimg).into(userName);

        }


    }
}
