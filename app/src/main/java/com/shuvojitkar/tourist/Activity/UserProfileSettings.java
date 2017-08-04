package com.shuvojitkar.tourist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class UserProfileSettings extends AppCompatActivity {
    String UserId ;
    String UserType;

    private StorageReference mImageStorage;
    private static ProgressDialog  img_up_pd;
    Context cn;
    private static CircleImageView mUserImage;
    private  static TextView mUserName,mUserStatus;
    private static Button mUserImageBtn,mUserStatusBtn;
    private DatabaseReference mRootRef,mFindUser01,mFindUser02;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);
        cn = this;
       // Toast.makeText(cn, getIntent().getStringExtra("id"), Toast.LENGTH_SHORT).show();
        init();


        //Set up Storage Ref Instance
        mImageStorage = FirebaseStorage.getInstance().getReference();
        UserId =FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        //For Name and Status
        mFindUser01 =mRootRef.child("tourist");
        mFindUser02=mRootRef.child("touristGuide");
        mFindUser01.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(UserId).exists()){

                    String name = dataSnapshot.child(UserId).child("name").getValue().toString();
                    String status = dataSnapshot.child(UserId).child("status").getValue().toString();
                    String image = dataSnapshot.child(UserId).child("image").getValue().toString();
                    UserType = dataSnapshot.child(UserId).child("type").getValue().toString();
                    Toast.makeText(cn, "Tourist", Toast.LENGTH_SHORT).show();
                    mUserName.setText(name);
                    mUserStatus.setText(status);
                    if (!image.equals("default")||!image.equals("")){
                        Picasso.with(cn).load(image).into(mUserImage);
                    }


                }else {
                    mFindUser02.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(UserId).exists()){

                                String name = dataSnapshot.child(UserId).child("name").getValue().toString();
                                String status = dataSnapshot.child(UserId).child("status").getValue().toString();
                                String image = dataSnapshot.child(UserId).child("image").getValue().toString();
                                UserType = dataSnapshot.child(UserId).child("type").getValue().toString();
                                mUserName.setText(name);
                                mUserStatus.setText(status);
                                if (!image.equals("default")||!image.equals("")){
                                    Picasso.with(cn).load(image).placeholder(R.drawable.person2).into(mUserImage);
                                }
                                Toast.makeText(UserProfileSettings.this, "Guide", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(cn, "Error02", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(cn, "Error 01", Toast.LENGTH_SHORT).show();

            }
        });



        mUserImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             CropImage.activity().setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropWindowSize(500,500)
                        .start(UserProfileSettings.this);
                Toast.makeText(cn, "Click", Toast.LENGTH_SHORT).show();
            }
        });

        mUserStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatus();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                img_up_pd = new ProgressDialog(cn);
                img_up_pd.setTitle("Uploading Image....");
                img_up_pd.setMessage("Please wait while we upload and process the image");
                img_up_pd.setCanceledOnTouchOutside(false);
                img_up_pd.show();

                Uri resultUri = result.getUri();

                //Now Compress it for thumbline
                File thumb_image = new File(resultUri.getPath());
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setQuality(75)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .compressToBitmap(thumb_image);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                /*The putBytes() method is the simplest way to upload a file to Cloud Storage.
                putBytes() takes a byte[] and returns an UploadTask that you can use to
                manage and monitor the status of the upload.*/
                //Process the bitmap for Firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child("profile_images").child(UserId+".jpg");
                //Storage Reference for thumb image
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(UserId+".jpg");

                //================


                //Now first upload the main image
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete( Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            final String download_url = task.getResult().getDownloadUrl().toString();


                            //after that now upload the thumb image
                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete( Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_donwload_url = thumb_task.getResult().getDownloadUrl().toString();
                                    if (thumb_task.isSuccessful()){

                                        //After the Upload of both file now we
                                        //update the database
                                        //Error found --> Hashmap not update the value its set the value
                                        //thats why now use map for just update the value
                                        Map map = new HashMap();
                                        map.put("image",download_url);
                                        map.put("thumb_image",thumb_donwload_url);
                                        //for map we use updatechildren
                                        mRootRef.child(UserType).child(UserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete( Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    img_up_pd.dismiss();
                                                    Toast.makeText(cn, "thumbline Upload Succesfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        img_up_pd.dismiss();
                                        Toast.makeText(cn, "Error in Uploading thumbline", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        /*    //update the database for Main Image
                            mDatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete( Task<Void> task) {
                                if (task.isSuccessful()){
                                    img_up_pd.dismiss();
                                    Toast.makeText(cn, "Image Upload Succesfully", Toast.LENGTH_SHORT).show();
                                 }
                              }
                            });*/
                        }else {
                            img_up_pd.dismiss();
                            Toast.makeText(cn, "Error in Image Uploading", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }

        }
    }

    private void init() {
        mUserName  = (TextView) findViewById(R.id.settings_name);
        mUserStatus = (TextView) findViewById(R.id.settings_status);
        mUserImageBtn = (Button) findViewById(R.id.settings_image_btn);
        mUserStatusBtn = (Button) findViewById(R.id.settings_status_btn);
        mRootRef = GetFirebaseRef.GetDbIns().getReference();
        mUserImage = (CircleImageView) findViewById(R.id.setting_image);
    }

    private void ChangeStatus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileSettings.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.status_dialog,null);
        final Button savebtn = (Button) dialogView.findViewById(R.id.dialog_status_btn);
        final TextInputLayout statusEdittext = (TextInputLayout) dialogView.findViewById(R.id.dialog_status);
        statusEdittext.getEditText().setText(mUserStatus.getText().toString());
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = statusEdittext.getEditText().getText().toString();
                savebtn.setVisibility(View.GONE);
                statusEdittext.setVisibility(View.GONE);
                final ProgressBar pd = (ProgressBar) dialog.findViewById(R.id.pd);
                pd.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(status)){
                    DatabaseReference mUserStatus= mRootRef.child(UserType).child(UserId);
                    mUserStatus.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if(task.isSuccessful()){
                                pd.setVisibility(View.GONE);
                                dialog.dismiss();
                            }else {
                                Toast.makeText(cn, "Please Try Again Letter", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

                }else {
                    Toast.makeText(cn, "Please Try Again Letter", Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }
            }
        });
    }




}
