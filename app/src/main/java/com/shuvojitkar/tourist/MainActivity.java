package com.shuvojitkar.tourist;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shuvojitkar.tourist.Fragment.Admin_Fragment;
import com.shuvojitkar.tourist.Fragment.Home_Fragment;
import com.shuvojitkar.tourist.Fragment.Register_Fragment;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mFrameLayout;
    private FloatingActionButton mFav;
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int menuitem =item.getItemId();

        if(menuitem==R.id.menu_home){
            Toast.makeText(this, "Index : "+FragIndex, Toast.LENGTH_SHORT).show();
            if(FragIndex!=1)

                LoadHomeFragment();
        }
        else if(menuitem==R.id.menu_create_actcount){
            Toast.makeText(this, "Index : "+FragIndex, Toast.LENGTH_SHORT).show();
            if(FragIndex!=2)
                Load_Register();
        }
        else if(menuitem==R.id.menu_admin){
            Toast.makeText(this, "Index : "+FragIndex, Toast.LENGTH_SHORT).show();
            if(FragIndex!=3)
                Load_Admin();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



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

    public void Load_Admin(){
        FragIndex=3;
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        getSupportActionBar().setTitle("Admin");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeframe,new Admin_Fragment(),"Admin")
                        .commit();
            }
        };

        if(runnable!=null){
            mHandler.post(runnable);
        }

    }
}
