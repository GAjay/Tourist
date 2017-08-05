package com.shuvojitkar.tourist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.MainActivity;
import com.shuvojitkar.tourist.R;

import java.util.HashMap;

public class Guide_Create_Account extends AppCompatActivity {
    private TextInputLayout mDisplayName,mPassowrd,mEmail;
    private String  AccountType ="touristGuide" ;
    private ArrayAdapter ar ;
    String Place ;
    private  static Spinner mSpinner;
    private static DatabaseReference mDatabaseReference;
    private static Button mCreateAccountBtn;
    private static ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;
    private Context cn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide__create__account);
        cn = this;
        init();
        mFirebaseAuth=  FirebaseAuth.getInstance();
        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email =mEmail.getEditText().getText().toString();
                String password = mPassowrd.getEditText().getText().toString();
                //  Toast.makeText(v.getContext(),email, Toast.LENGTH_SHORT).show();
                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position>0){
                            Toast.makeText(v.getContext(), "id :"+position, Toast.LENGTH_SHORT).show();
                            String s = mSpinner.getItemAtPosition(position).toString();
                            Place = s;
                        }else {
                            Place = "Dhaka";
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                if(display_name.equals("") ||email.equals("") ||password.equals("")){


                }else {

                    mProgressDialog.setTitle("Registering User");
                    mProgressDialog.setMessage("Please wait while we create your account !");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    register_user(display_name,email,password);


                }
            }
        });
    }


    private void register_user(final String display_name, final String email, final String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                if(task.isSuccessful()){



                    AlertDialog alertDialog = new AlertDialog.Builder(cn).create();
                    alertDialog.setTitle("Account Type");
                    alertDialog.setMessage("You Select :"+Place);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                                    String Uid = currentuser.getUid();
                                    mDatabaseReference = GetFirebaseRef.GetDbIns().getReference().child("touristGuide").child(Uid);
                                    HashMap<String,String> map= new HashMap<String, String>();
                                    map.put("name",display_name);
                                    map.put("email",email);
                                    map.put("password",password);
                                    map.put("area",Place);
                                    map.put("type","touristGuide");
                                    map.put("image","default");
                                    map.put("status","I love Bangladesh");
                                    mDatabaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                mProgressDialog.dismiss();
                                                Toast.makeText(cn, "Account Create Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Guide_Create_Account.this,MainActivity.class));
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


                }
                else {
                    mProgressDialog.hide();
                    String error = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Weak Password!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Invalid Email";
                    } catch (FirebaseAuthUserCollisionException e) {
                        error = "Existing account!";
                    } catch (Exception e) {
                        error = "Unknow error!";
                        e.printStackTrace();
                    }
                    Toast.makeText(cn, error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }




    private void init() {
        mDisplayName = (TextInputLayout) findViewById(R.id.guide_reg_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.guide_reg_email);
        mPassowrd = (TextInputLayout) findViewById(R.id.guide_reg_password);
        mCreateAccountBtn = (Button) findViewById(R.id.guide_reg_create_btn);
        mProgressDialog = new ProgressDialog(cn);
        mSpinner = (Spinner) findViewById(R.id.guide_place_spinner);
    }
}
