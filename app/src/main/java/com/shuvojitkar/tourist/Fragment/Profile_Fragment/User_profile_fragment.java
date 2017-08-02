package com.shuvojitkar.tourist.Fragment.Profile_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shuvojitkar.tourist.R;

/**
 * Created by SHOBOJIT on 7/28/2017.
 */

public class User_profile_fragment extends Fragment {
    private  RecyclerView mUserProRec;
    private FloatingActionButton mUserProfab;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_user_profile_fragment,container,false);
        init(v);

      mUserProfab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show();


                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.user_post_dailog,null);
                        builder.setView(dialogView);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
          }

      });
    

        return v;
    }

    private void init(View v) {
        mUserProfab = (FloatingActionButton) v.findViewById(R.id.profile_fab_btn);
     /*   mUserProRec = (RecyclerView) v.findViewById(R.id.profile_post_rec);
*/
    }
}
