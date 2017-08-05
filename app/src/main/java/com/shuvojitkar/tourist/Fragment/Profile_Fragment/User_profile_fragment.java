package com.shuvojitkar.tourist.Fragment.Profile_Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;
import com.shuvojitkar.tourist.Activity.LoginActivity;
import com.shuvojitkar.tourist.Activity.UserProfileSettings;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.MainActivity;
import com.shuvojitkar.tourist.Model.User_Post;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * Created by SHOBOJIT on 8/5/2017.
 */

public class User_profile_fragment extends Fragment {
    private  RecyclerView mUserProRec;
    private static int GALLERY_PICK = 1;
    private DatabaseReference mRootRef;
    private DatabaseReference mUserProfileDb;
    private DatabaseReference mUserProfileDb2;
    private StorageReference mImageStorageReference;
    private String UserId;
    private FirebaseUser firebaseUser;
    private static Uri resultUri;
    private CircleImageView img;
    private ProgressDialog img_up_pd;
    private FloatingActionButton mUserProfab;

    TextView UserNameTxt,UserStatusTxt;
    CircleImageView UserProFileImage;
    Button UserEditSettingbtn;

    String UserType;

    View v;
    View cn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_user_profile_fragment,container,false);
        init(v);
        mUserProRec.setHasFixedSize(true);
        mUserProRec.setLayoutManager(new LinearLayoutManager(getContext()));
        cn=v;




        UserEditSettingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  String id =FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
               // Toast.makeText(v.getContext(), id, Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getContext(),UserProfileSettings.class);
                startActivity(in);
            }
        });


      mUserProfileDb.child("touristGuide").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(UserId).exists()){

                   // Toast.makeText(v.getContext(), "hgghho", Toast.LENGTH_SHORT).show();
                    String name = dataSnapshot.child(UserId).child("name").getValue().toString();
                    String status = dataSnapshot.child(UserId).child("status").getValue().toString();
                    String image = dataSnapshot.child(UserId).child("image").getValue().toString();
                    UserNameTxt.setText(name);
                    UserStatusTxt.setText(status);

                    UserType = "touristGuide";

                    if (!image.equals("")||!image.equals("default")){
                        Picasso.with(v.getContext()).load(image).placeholder(R.drawable.person2).into(UserProFileImage);
                    }
                  //  Toast.makeText(v.getContext(), "TT", Toast.LENGTH_SHORT).show();
                   // UserPostList("touristGuide",UserId);



                }else {
                    mUserProfileDb2.child("tourist").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(UserId).exists()){

                               // Toast.makeText(v.getContext(), "hgghho", Toast.LENGTH_SHORT).show();
                                String name = dataSnapshot.child(UserId).child("name").getValue().toString();
                                String status = dataSnapshot.child(UserId).child("status").getValue().toString();
                                String image = dataSnapshot.child(UserId).child("image").getValue().toString();
                                UserNameTxt.setText(name);
                                UserStatusTxt.setText(status);

                                if (!image.equals("")||!image.equals("default")){
                                    Picasso.with(v.getContext()).load(image).placeholder(R.drawable.person2).into(UserProFileImage);
                                }

                                UserType = "tourist";
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



      mUserProfab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //  UserPost(getContext());

              AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
              final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.user_post_dailog,null);
              builder.setView(dialogView);
              final AlertDialog dialog = builder.create();

              final TextInputLayout titleEdit = (TextInputLayout) dialogView.findViewById(R.id.user_post_title);
              final TextInputLayout descriptionEdit = (TextInputLayout) dialogView.findViewById(R.id.user_post_description);
              final Button selcetImage = (Button) dialogView.findViewById(R.id.user_add_image);
              Button post = (Button) dialogView.findViewById(R.id.user_post_go_btn);
              img= (CircleImageView) dialogView.findViewById(R.id.user_post_image);

              selcetImage.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                      //   Intent gallaryIntent = new Intent().setType("image*//**//*").setAction(Intent.ACTION_GET_CONTENT);
                    //  startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
                      startActivityForResult(galleryIntent,GALLERY_PICK);
                  }
              });

              post.setOnClickListener(new View.OnClickListener() {
                  @Override
                        public void onClick(View v) {
                            final String title =titleEdit.getEditText().getText().toString();
                            final String Description = descriptionEdit.getEditText().getText().toString();
                            if (title.equals("")|| Description.equals("")){
                             Toast.makeText(v.getContext(), "Please fill the form", Toast.LENGTH_SHORT).show();

                        }   else {
                                if (resultUri==null){
                                    Toast.makeText(v.getContext(), "Nullaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();

                                }else {
                                    dialog.hide();
                                    img_up_pd.setTitle("Uploading Image....");
                                    img_up_pd.setMessage("Please wait while we upload and process the image");
                                    img_up_pd.setCanceledOnTouchOutside(false);
                                    img_up_pd.show();


                                    String image_id = random()+".jpg";
                                    //Storage Reference for main image
                                    StorageReference filepath = mImageStorageReference.child("UserPost").child(image_id);

                                    filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                final String download_url = task.getResult().getDownloadUrl().toString();
                                                final String CurrentDate  = DateFormat.getDateTimeInstance().format(new Date());
                                                DatabaseReference pushdb = mRootRef.child("User_Post").push();
                                                final DatabaseReference  mUserDatabase = GetFirebaseRef.GetDbIns().getReference().child("touristGuide");
                                                final DatabaseReference  mUserDatabase02 = GetFirebaseRef.GetDbIns().getReference().child("tourist");
                                                final String pushkey = pushdb.getKey();
                                                final Map  imageUp = new HashMap();

                                                mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.child(UserId).exists()){

                                                            //For User_Post
                                                            imageUp.put("User_Post/"+pushkey+"/image",download_url);
                                                            imageUp.put("User_Post/"+pushkey+"/title",title);
                                                            imageUp.put("User_Post/"+pushkey+"/description",Description);
                                                            imageUp.put("User_Post/"+pushkey+"/userid",UserId);
                                                            imageUp.put("User_Post/"+pushkey+"/user_type","touristGuide");
                                                            imageUp.put("User_Post/"+pushkey+"/date",CurrentDate);

                                                            //For Store In User Id
                                                            imageUp.put("touristGuide/"+UserId+"/post"+"/"+pushkey+"/image",download_url);
                                                            imageUp.put("touristGuide/"+UserId+"/post"+"/"+pushkey+"/title",title);
                                                            imageUp.put("touristGuide/"+UserId+"/post"+"/"+pushkey+"/description",Description);
                                                            imageUp.put("touristGuide/"+UserId+"/post"+"/"+pushkey+"/date",CurrentDate);
                                                            imageUp.put("touristGuide/"+UserId+"/post"+"/"+pushkey+"/user_type","touristGuide");
                                                            imageUp.put("touristGuide/"+UserId+"/post"+"/"+pushkey+"/userid",UserId);

                                                            mRootRef.updateChildren(imageUp, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                    if (databaseError!=null){
                                                                        img_up_pd.dismiss();
                                                                        Toast.makeText(cn.getContext(), "Something Went Wrong, Please Try Again Later", Toast.LENGTH_SHORT).show();

                                                                    }else {
                                                                        img_up_pd.dismiss();
                                                                    }
                                                                }
                                                            });



                                                        } else {
                                                            mUserDatabase02.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.child(UserId).exists()){
                                                                        //For User_Post
                                                                        imageUp.put("User_Post/"+pushkey+"/image",download_url);
                                                                        imageUp.put("User_Post/"+pushkey+"/title",title);
                                                                        imageUp.put("User_Post/"+pushkey+"/description",Description);
                                                                        imageUp.put("User_Post/"+pushkey+"/userid",UserId);
                                                                        imageUp.put("User_Post/"+pushkey+"/user_type","tourist");
                                                                        imageUp.put("User_Post/"+pushkey+"/date",CurrentDate);

                                                                        //For Store In User Id
                                                                        imageUp.put("tourist/"+UserId+"/post"+"/"+pushkey+"/image",download_url);
                                                                        imageUp.put("tourist/"+UserId+"/post"+"/"+pushkey+"/title",title);
                                                                        imageUp.put("tourist/"+UserId+"/post"+"/"+pushkey+"/description",Description);
                                                                        imageUp.put("tourist/"+UserId+"/post"+"/"+pushkey+"/date",CurrentDate);
                                                                        imageUp.put("tourist/"+UserId+"/post"+"/"+pushkey+"/user_type","tourist");
                                                                        imageUp.put("tourist/"+UserId+"/post"+"/"+pushkey+"/userid",UserId);


                                                                        mRootRef.updateChildren(imageUp, new DatabaseReference.CompletionListener() {
                                                                            @Override
                                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                                if (databaseError!=null){
                                                                                    img_up_pd.dismiss();
                                                                                    Toast.makeText(cn.getContext(), "Something Went Wrong, Please Try Again Later", Toast.LENGTH_SHORT).show();

                                                                                }else {
                                                                                    img_up_pd.dismiss();
                                                                                }
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
                                        }
                                    });

                                }
                      }
                  }
              });

              dialog.show();
          }
      });

        return v;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(v.getContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //=============

        DatabaseReference mUserListDB = mRootRef;
        mUserListDB.keepSynced(true);
        mUserListDB.child("tourist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(UserId).exists()){
                    UserPostList("tourist",UserId);
                    Toast.makeText(v.getContext(), "Called", Toast.LENGTH_SHORT).show();


                }else {
                    DatabaseReference mUserListDB2 = mRootRef;
                    mUserListDB2.keepSynced(true);
                    mUserListDB2.child("touristGuide").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserPostList("touristGuide",UserId);
                            Toast.makeText(v.getContext(), "Called", Toast.LENGTH_SHORT).show();

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

        //=========
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK){
            resultUri = data.getData();
            img.setVisibility(View.VISIBLE);
            Picasso.with(v.getContext()).load(resultUri).noPlaceholder().centerCrop().fit()
                    .into(img);
        }
    }

    private void init(View v) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserId = firebaseUser.getUid();
        img_up_pd = new ProgressDialog(v.getContext());
        mRootRef = GetFirebaseRef.GetDbIns().getReference();
        mUserProfab = (FloatingActionButton) v.findViewById(R.id.profile_fab_btn);
        mImageStorageReference = FirebaseStorage.getInstance().getReference();
        mUserProRec = (RecyclerView) v.findViewById(R.id.profile_post_rec);

        mUserProfileDb = GetFirebaseRef.GetDbIns().getReference();
        mUserProfileDb.keepSynced(true);
        mUserProfileDb2 = GetFirebaseRef.GetDbIns().getReference();
        mUserProfileDb2.keepSynced(true);



        UserNameTxt = (TextView) v.findViewById(R.id.UserName);
        UserStatusTxt = (TextView) v.findViewById(R.id.UserStatus);
        UserProFileImage = (CircleImageView) v.findViewById(R.id.UserProImage);
        UserEditSettingbtn = (Button) v.findViewById(R.id.UserEditbtn);

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void UserPostList(final String UserType, final String Userid){


        DatabaseReference listdb = GetFirebaseRef.GetDbIns().getReference().child(UserType).child(Userid).child("post");
        listdb.keepSynced(true);
        final DatabaseReference listdb2 =GetFirebaseRef.GetDbIns().getReference().child(UserType).child(Userid);
        listdb2.keepSynced(true);


        FirebaseRecyclerAdapter <User_Post,User_post_fragment_VH> fr = new FirebaseRecyclerAdapter<User_Post, User_post_fragment_VH>(
                User_Post.class,
                R.layout.user_post_rec_layout,
                User_post_fragment_VH.class,
                listdb
        ) {
            @Override
            protected void populateViewHolder(final User_post_fragment_VH viewHolder, User_Post model, int position) {
                viewHolder.setImage(model.getImage());
                viewHolder.setDate(model.getDate());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());

                listdb2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userProimg = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        Toast.makeText(v.getContext(), name, Toast.LENGTH_SHORT).show();

                        viewHolder.setUserproImage(userProimg);
                        viewHolder.setUserName(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };
        mUserProRec.setAdapter(fr);
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
