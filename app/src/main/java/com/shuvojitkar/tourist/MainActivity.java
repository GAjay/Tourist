package com.shuvojitkar.tourist;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shuvojitkar.tourist.Activity.LoginActivity;
import com.shuvojitkar.tourist.Activity.ProfileActivity;
import com.shuvojitkar.tourist.Fragment.Home_Fragment;
import com.shuvojitkar.tourist.Fragment.Register_Fragment;
import com.shuvojitkar.tourist.Fragment.TouristGuide_list_fragment;
import com.shuvojitkar.tourist.Fragment.Travler_list_fragment;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mFrameLayout;
    private FloatingActionButton mFav;
    private FirebaseAuth mFirebaseAuth;
    private static boolean LoginState;
    private static Toolbar mToolbar;
    private static Handler mHandler;
    private static int FragIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        LoadHomeFragment();

        if(haveNetworkConnection()==false){
            //AlertMessage.showMessage(con,"Alert","No Internet Connection","OK",R.drawable.alertpic);
            Snackbar.make(findViewById(R.id.home_nav),"No Internet Connection",Snackbar.LENGTH_LONG)
                    .show();
        }
    }



    private void init() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_dl);
        mNavigationView  = (NavigationView) findViewById(R.id.home_nav);
        mNavigationView.setNavigationItemSelectedListener(this);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mFrameLayout = (FrameLayout) findViewById(R.id.homeframe);
        mHandler = new Handler();

        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }



    //==================Check the networkState=================
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




    //=================Navigation Drawer Click Listener===============
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int menuitem =item.getItemId();

        if(menuitem==R.id.menu_home){
            Toast.makeText(this, "Index : "+FragIndex, Toast.LENGTH_SHORT).show();
            if(FragIndex!=1)
                LoadHomeFragment();
        }
        else if(menuitem==R.id.menu_create_account){
            Toast.makeText(this, "Index : "+FragIndex, Toast.LENGTH_SHORT).show();
            if(FragIndex!=2)
                if (ChecktheLoginState()==true){
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                                alertDialog.setTitle("Attention");
                                alertDialog.setMessage("You are currently logd in.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sign Out",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseAuth.getInstance().signOut();
                                                Load_Register();
                                                dialog.dismiss();
                                            }
                                        });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                }else {
                    Load_Register();
                }

        }
        else if(menuitem==R.id.menu_profile){

            if(FragIndex!=3){
                if (ChecktheLoginState()==true){
                    //LoadProfileFragment();
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }else {
                    Toast.makeText(this, "You are not Sign in", Toast.LENGTH_SHORT).show();
                }

            }

        }

        else if(menuitem==R.id.menu_guide){
            Toast.makeText(this, "Index : "+FragIndex, Toast.LENGTH_SHORT).show();
            if(FragIndex!=4)
                Load_Guide_list();
        }
        else if(menuitem==R.id.menu_tourist){
            Toast.makeText(this, "Index : "+FragIndex, Toast.LENGTH_SHORT).show();
            if(FragIndex!=5)
                Load_Tourist_list();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




        //================Fragment List===============
    public void LoadHomeFragment(){
        FragIndex=1;
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        getSupportActionBar().setTitle("Home");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeframe,new Home_Fragment(),"Home Fragment")
                        .commit();
            }
        };

        if(runnable!=null){
            mHandler.post(runnable);
        }

    }
    public void Load_Register(){
        FragIndex=2;
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        getSupportActionBar().setTitle("Register Account");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeframe,new Register_Fragment(),"Register Fragment")
                        .commit();
            }
        };

        if(runnable!=null){
            mHandler.post(runnable);
        }

    }
/*    public void LoadProfileFragment(){
        FragIndex=3;
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        Toast.makeText(this, "Index : "+FragIndex, Toast.LENGTH_SHORT).show();
        getSupportActionBar().setTitle("Profile");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeframe,new Profile_fragment(),"Profile Fragment")
                        .commit();
            }
        };

        if(runnable!=null){
            mHandler.post(runnable);
        }

    }*/
    public void Load_Guide_list(){
        FragIndex=4;
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        getSupportActionBar().setTitle("Tourist Guide List");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeframe,new TouristGuide_list_fragment(),"Admin")
                        .commit();
            }
        };

        if(runnable!=null){
            mHandler.post(runnable);
        }

    }
    public void Load_Tourist_list(){
        FragIndex=5;
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        getSupportActionBar().setTitle("Traveler List");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeframe,new Travler_list_fragment(),"Admin")
                        .commit();
            }
        };

        if(runnable!=null){
            mHandler.post(runnable);
        }

    }




    //------------Toolbar menu------------
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    //==============Toolbar menu item Click Listener===============
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.main_logout_btn){

                if (ChecktheLoginState() ==true){
                    FirebaseAuth.getInstance().signOut();
                    if (FragIndex==3){
                        LoadHomeFragment();
                    }
                }else {
                    Toast.makeText(this, "You are not logd in", Toast.LENGTH_SHORT).show();

                }
        }
        else if(item.getItemId()==R.id.main_login_btn){
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //================Check the Login state=======================
    public boolean ChecktheLoginState(){
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser == null) {
            return false;
        }else {
            return true;
        }
    }

    //Check the user is sign in or not
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //otherwise it return null method
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser == null) {
            LoginState =false;
        }else {
            LoginState = true;
        }
    }
}
