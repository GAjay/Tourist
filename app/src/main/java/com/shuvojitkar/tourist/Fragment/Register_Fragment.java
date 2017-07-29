package com.shuvojitkar.tourist.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shuvojitkar.tourist.R;

import java.util.HashMap;

/**
 * Created by SHOBOJIT on 7/22/2017.
 */

public class Register_Fragment extends Fragment {
    private TextInputLayout mDisplayName,mPassowrd,mEmail;
    private String  AccountType ="Tourist Guide" ;
    private ArrayAdapter ar ;
    private  static Spinner mSpinner;
    private static DatabaseReference mDatabaseReference;
    private static Button mCreateAccountBtn;
    private static ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.register_fragment,container,false);
        init(v);
        mFirebaseAuth = FirebaseAuth.getInstance();


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
                            AccountType = s;
                        }else {
                            AccountType ="Tourist Guide" ;
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

                        register_user(display_name,email,password,AccountType);


                }
            }
        });
        return v;

    }


    private void register_user(final String display_name, final String email, final String password, String accountType) {
        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final String s ;
                    if(AccountType.equals("Tourist Guide")){
                        s = "touristGuide";
                    }else {
                        s="tourist";
                    }

                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                                alertDialog.setTitle("Account Type");
                                alertDialog.setMessage("You Select :"+s);
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                                                String Uid = currentuser.getUid();
                                                mDatabaseReference = GetFirebaseRef.GetDbIns().getReference().child(s).child(Uid);
                                                HashMap<String,String> map= new HashMap<String, String>();
                                                map.put("name",display_name);
                                                map.put("email",email);
                                                map.put("password",password);
                                                map.put("type",s);
                                                map.put("image","default");
                                                map.put("status","I love Bangladesh");
                                                mDatabaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            mProgressDialog.dismiss();
                                                            Toast.makeText(v.getContext(), "Account Create Successfully", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(v.getContext(), error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void init(View v) {
        mDisplayName = (TextInputLayout) v.findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout) v.findViewById(R.id.reg_email);
        mPassowrd = (TextInputLayout) v.findViewById(R.id.reg_password);
        mCreateAccountBtn = (Button) v.findViewById(R.id.reg_create_btn);
        mProgressDialog = new ProgressDialog(v.getContext());
        mSpinner = (Spinner) v.findViewById(R.id.reg_spinner);




    }
}
