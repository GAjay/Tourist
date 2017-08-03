package com.shuvojitkar.tourist.Fragment.Profile_Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.shuvojitkar.tourist.Activity.LoginActivity;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.MainActivity;
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
 * Created by SHOBOJIT on 7/28/2017.
 */

public class User_profile_fragment extends Fragment {
    private  RecyclerView mUserProRec;
    private static int GALLERY_PICK = 1;
    private DatabaseReference mRootRef;
    private StorageReference mImageStorageReference;
    private String UserId;
    private FirebaseUser firebaseUser;
    private static Uri resultUri;
    private CircleImageView img;
    private ProgressDialog img_up_pd;
    private FloatingActionButton mUserProfab;
    View v;
    View cn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_user_profile_fragment,container,false);
        init(v);
        cn=v;

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

                      }else {
                                if (resultUri==null){
                                    Toast.makeText(v.getContext(), "Nullaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();

                                }else {
                                    dialog.hide();
                                    img_up_pd.setTitle("Uploading Image....");
                                    img_up_pd.setMessage("Please wait while we upload and process the image");
                                    img_up_pd.setCanceledOnTouchOutside(false);
                                    img_up_pd.show();

                                   /* //Now Compress it for thumbline
                                    File image_post = new File(resultUri.getPath());
                                    Bitmap image_bitmap = null;
                                    try {
                                        image_bitmap = new Compressor(v.getContext())
                                                .setQuality(75)
                                                .setMaxWidth(300)
                                                .setMaxHeight(300)
                                                .compressToBitmap(image_post);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                *//*The putBytes() method is the simplest way to upload a file to Cloud Storage.
                putBytes() takes a byte[] and returns an UploadTask that you can use to
                manage and monitor the status of the upload.*//*
                //Process the bitmap for Firebase
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    final byte[] thumb_byte = baos.toByteArray();*/


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





                                                //sss
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


    private void SelcetImage(Context cn) {

        CropImage.activity().setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropWindowSize(500,500)
                .start(cn,this);
    }

    private void init(View v) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserId = firebaseUser.getUid();
        img_up_pd = new ProgressDialog(v.getContext());
        mRootRef = GetFirebaseRef.GetDbIns().getReference();
        mUserProfab = (FloatingActionButton) v.findViewById(R.id.profile_fab_btn);
        mImageStorageReference = FirebaseStorage.getInstance().getReference();
     /*   mUserProRec = (RecyclerView) v.findViewById(R.id.profile_post_rec);
*/
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
}
