package com.shuvojitkar.tourist.Activity;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Guide_Profile_Activity extends AppCompatActivity {
    private TextView mProfilename,mProfilestatus,mProfiletotalfriends,mArea;
    private Button mProfileSendReqBtn,mProfileDeclinereqBtn,mHireButton;
    private ImageView mProfileImage;
    private ProgressDialog pd;
    private String mCurrentState;


    private DatabaseReference mHireDatabase;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mrootUserDatabase;
    private DatabaseReference mRootRef;
    private DatabaseReference mFriendsReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase,mHireReqDatabase;
    private FirebaseUser mCurrentUser;

    private String CurrentUserString;
    private String mHireState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_profile);
        final String id = getIntent().getStringExtra("user_id");
        init();


        CurrentUserString = FirebaseAuth.getInstance().getCurrentUser().toString();
        if (CurrentUserString.equals("") || CurrentUserString.equals(null)) {

        } else {

            mCurrentState = "not_friends";
            mHireState = "not_hire";

            //First time set Decline button to invisible
            mProfileDeclinereqBtn.setVisibility(View.INVISIBLE);
            mProfileDeclinereqBtn.setEnabled(false);


            pd.setTitle("Loading User Data");
            pd.setMessage("Please wait while er load the user data");
            pd.setCanceledOnTouchOutside(false);
            if (haveNetworkConnection() == true) {
                pd.show();
            }

            //Firebase Initialization
            mHireDatabase = GetFirebaseRef.GetDbIns().getReference().child("Hire_Guide");
            mHireReqDatabase = GetFirebaseRef.GetDbIns().getReference().child("hire_req");

            mFriendsReqDatabase = GetFirebaseRef.GetDbIns().getReference().child("Frnds_req");
            mFriendDatabase = GetFirebaseRef.GetDbIns().getReference().child("Friends");
            mUserDatabase = GetFirebaseRef.GetDbIns().getReference().child("touristGuide").child(id);
            mNotificationDatabase = GetFirebaseRef.GetDbIns().getReference().child("Notifications");
            mRootRef = GetFirebaseRef.GetDbIns().getReference();


            //get the id of current user
            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();



            //Read Data From Firebase
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String Name = dataSnapshot.child("name").getValue().toString();
                    String Status = dataSnapshot.child("status").getValue().toString();
                    String Image = dataSnapshot.child("image").getValue().toString();
                    String Area  = dataSnapshot.child("area").getValue().toString();
                    mProfilename.setText(Name);
                    mProfilestatus.setText(Status);
                    mArea.setText("Area : "+Area);

                    if (!Image.equals("default")){
                        Picasso.with(getApplicationContext()).load(Image).placeholder(R.drawable.person2).into(mProfileImage);}

                    mFriendsReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //------Friends List /Request Features
                            mFriendsReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(id)){
                                        String req_type = dataSnapshot.child(id).child("req_type").getValue().toString();
                                        if (req_type.equals("received")){
                                            mCurrentState = "req_received";
                                            mProfileSendReqBtn.setText("Accept Friend Request");

                                            mProfileDeclinereqBtn.setVisibility(View.VISIBLE);
                                            mProfileDeclinereqBtn.setEnabled(true);

                                        }else if(req_type.equals("sent")){
                                            mCurrentState = "req_sent";
                                            mProfileSendReqBtn.setText("Cancel Friend Request");

                                            mProfileDeclinereqBtn.setVisibility(View.INVISIBLE);
                                            mProfileDeclinereqBtn.setEnabled(false);

                                        }
                                    }else {
                                        mFriendDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(id)){
                                                    mCurrentState = "friends";
                                                    mProfileSendReqBtn.setText("UnFriend this Person");

                                                    mProfileDeclinereqBtn.setVisibility(View.INVISIBLE);
                                                    mProfileDeclinereqBtn.setEnabled(false);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                pd.dismiss();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    pd.dismiss();
                                }
                            });

                            pd.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mHireReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //------Friends List /Request Features
                            mHireReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(id)){
                                        String req_type = dataSnapshot.child(id).child("req_type").getValue().toString();
                                        if (req_type.equals("received")){
                                            mHireState = "hire_received";
                                            mHireButton.setText("Accept Hire Request");

                                        }else if(req_type.equals("sent")){
                                            mHireState = "hire_sent";
                                            mHireButton.setText("Cancel Hire Request");


                                        }
                                    }else {
                                        mHireReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(id)){
                                                    mHireState = "hired";
                                                    mHireButton.setText("Cancel the Deal");

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                pd.dismiss();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    pd.dismiss();
                                }
                            });

                            pd.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProfileSendReqBtn.setEnabled(false);


                    //  ------Not Friend State-----
                    if (mCurrentState.equals("not_friends")){
                        DatabaseReference mNotificationRef= mRootRef.child("Notifications").child(id).push();
                        String  newNotificaitonID = mNotificationRef.getKey();

                        HashMap<String,String> ntfmap = new HashMap<>();
                        ntfmap.put("from",mCurrentUser.getUid());
                        ntfmap.put("type","request");

                        Map requestMap = new HashMap();
                        requestMap.put("Frnds_req/"+mCurrentUser.getUid()+"/"+id+"/"+"req_type","sent");
                        requestMap.put("Frnds_req/"+id+"/"+mCurrentUser.getUid()+"/"+"req_type","received");
                        requestMap.put("Notifications/"+id+"/"+newNotificaitonID,ntfmap);

                        mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!=null){
                                    // Toast.makeText(ProfileActivity.this, "There was some error", Toast.LENGTH_SHORT).show();
                                }
                                mProfileSendReqBtn.setEnabled(true);
                                mCurrentState = "req_sent";
                                mProfileSendReqBtn.setText("Cancel Friend Request");
                            }
                        });
                    }



                    //  ------Cancel Request State-----
                    if (mCurrentState.equals("req_sent")){
                        mFriendsReqDatabase.child(mCurrentUser.getUid()).child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mFriendsReqDatabase.child(id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mProfileSendReqBtn.setEnabled(true);
                                        mCurrentState = "not_friends";
                                        mProfileSendReqBtn.setText("Sent Friend Request");

                                        mProfileDeclinereqBtn.setVisibility(View.INVISIBLE);
                                        mProfileDeclinereqBtn.setEnabled(false);
                                    }
                                });
                            }
                        });
                    }



                    // ------REQ RECEIVED STATE
                    if (mCurrentState.equals("req_received")){
                        final String CurrentDate  = DateFormat.getDateTimeInstance().format(new Date());
                        Map friendsmap = new HashMap();
                        friendsmap.put("Friends/"+mCurrentUser.getUid()+"/"+id+"/date",CurrentDate);
                        friendsmap.put("Friends/"+id+"/"+mCurrentUser.getUid()+"/date",CurrentDate);

                        friendsmap.put("Frnds_req/"+mCurrentUser.getUid()+"/"+id,null);
                        friendsmap.put("Frnds_req/"+id+"/"+mCurrentUser.getUid(),null);

                        mRootRef.updateChildren(friendsmap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError ==null){
                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrentState = "friends";
                                    mProfileSendReqBtn.setText("UnFriend this Person");


                                    mProfileDeclinereqBtn.setVisibility(View.INVISIBLE);
                                    mProfileDeclinereqBtn.setEnabled(false);
                                }else {
                                    String error = databaseError.getMessage().toString();
                                    //    Toast.makeText(ProfileActivity.this,error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }


                    // ------FRIENDS STATE
                    if (mCurrentState.equals("friends")){

                        Map unFriendMap = new HashMap();
                        unFriendMap.put("Friends/"+mCurrentUser.getUid()+"/"+id,null);
                        unFriendMap.put("Friends/"+id+"/"+mCurrentUser.getUid(),null);

                        mRootRef.updateChildren(unFriendMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError ==null){
                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrentState = "not_friends";
                                    mProfileSendReqBtn.setText("Sent Friend Request");


                                    mProfileDeclinereqBtn.setVisibility(View.INVISIBLE);
                                    mProfileDeclinereqBtn.setEnabled(false);
                                }else {
                                    String error = databaseError.getMessage().toString();
                                    //   Toast.makeText(ProfileActivity.this,error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            });





            mHireButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHireButton.setEnabled(false);

                    //  ------Not Friend State-----
                    if (mHireState.equals("not_hire")){

                        Map requestMap = new HashMap();
                        requestMap.put("hire_req/"+mCurrentUser.getUid()+"/"+id+"/"+"req_type","sent");
                        requestMap.put("hire_req/"+id+"/"+mCurrentUser.getUid()+"/"+"req_type","received");

                        mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!=null){
                                    // Toast.makeText(ProfileActivity.this, "There was some error", Toast.LENGTH_SHORT).show();
                                }
                                mHireButton.setEnabled(true);
                                mHireState= "hire_sent";
                                mHireButton.setText("Cancel hire Request");
                            }
                        });
                    }



                    //  ------Cancel Request State-----
                    if (mHireState.equals("hire_sent")){
                        mHireReqDatabase.child(mCurrentUser.getUid()).child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mFriendsReqDatabase.child(id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mHireButton.setEnabled(true);
                                        mHireState = "not_hire";
                                        mHireButton.setText("Sent Hire Request");

                                    }
                                });
                            }
                        });
                    }



                    // ------REQ RECEIVED STATE
                    if (mHireState.equals("hire_received")){
                        final String CurrentDate  = DateFormat.getDateTimeInstance().format(new Date());
                        Map friendsmap = new HashMap();
                        friendsmap.put("Hire_Guide/"+mCurrentUser.getUid()+"/"+id+"/date",CurrentDate);
                        friendsmap.put("Hire_Guide/"+id+"/"+mCurrentUser.getUid()+"/date",CurrentDate);

                        friendsmap.put("hire_req/"+mCurrentUser.getUid()+"/"+id,null);
                        friendsmap.put("hire_req/"+id+"/"+mCurrentUser.getUid(),null);

                        mRootRef.updateChildren(friendsmap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError ==null){
                                    mHireButton.setEnabled(true);
                                    mHireState = "hired";
                                    mHireButton.setText("Cancel this Deal");


                                }else {
                                    String error = databaseError.getMessage().toString();
                                    //    Toast.makeText(ProfileActivity.this,error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }


                    // ------FRIENDS STATE
                    if (mHireState.equals("hired")){

                        Map unFriendMap = new HashMap();
                        unFriendMap.put("Hire_Guide/"+mCurrentUser.getUid()+"/"+id,null);
                        unFriendMap.put("Hire_Guide/"+id+"/"+mCurrentUser.getUid(),null);

                        mRootRef.updateChildren(unFriendMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError ==null){
                                    mHireButton.setEnabled(true);
                                    mHireState = "not_hire";
                                    mHireButton.setText("Cancel the Hire Requet");

                                }else {
                                    String error = databaseError.getMessage().toString();
                                    //   Toast.makeText(ProfileActivity.this,error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }



                }
            });


        }
    }
    private void init() {

        pd =new ProgressDialog(this);
        mProfilename = (TextView) findViewById(R.id.guide_profile_displayName);
        mProfilestatus = (TextView) findViewById(R.id.guide_profile_status);
        mArea = (TextView) findViewById(R.id.guide_profile_area);
        mProfileSendReqBtn = (Button) findViewById(R.id.guide_profile_sendRequest);
        mProfileDeclinereqBtn = (Button) findViewById(R.id.guide_profile_declinereq);
        mProfileImage = (ImageView) findViewById(R.id.guide_profile_image);
        mHireButton = (Button) findViewById(R.id.guide_profile_hire);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
