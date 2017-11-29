package com.shuvojitkar.tourist.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.shuvojitkar.tourist.Activity.DetailActivity;
import com.shuvojitkar.tourist.Activity.NearByPlaceDetailsMapActivity;
import com.shuvojitkar.tourist.DialogBoxes.NearByPlaceDialog;
import com.shuvojitkar.tourist.GetFirebaseRef;
import com.shuvojitkar.tourist.Map_Helpers.PlaceDetails;
import com.shuvojitkar.tourist.Model.Home_frag_Model;
import com.shuvojitkar.tourist.Model.Person_list;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHOBOJIT on 7/22/2017.
 */

public class Home_Fragment extends Fragment {
    View v;
    private RecyclerView mRecyclerView;
    private ArrayList<Home_frag_Model> ar;
    private ProgressDialog mPd;
    private DatabaseReference mUserDatabase;
    private RecyclerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_fragment, container, false);
        init(v);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        ar = new ArrayList();
        adapter = new RecyclerAdapter(ar);
        mRecyclerView.setAdapter(adapter);

        DatabaseReference homeDb = GetFirebaseRef.GetDbIns().getReference().child("home_page_post");
        homeDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    String name = i.child("name").getValue().toString();
                    String image = i.child("image").getValue().toString();
                    String lang = i.child("lang").getValue().toString();
                    String lat = i.child("lat").getValue().toString();
                    String decription = i.child("description").getValue().toString();
                    ar.add(new Home_frag_Model(image, name, Double.parseDouble(lang), Double.parseDouble(lat), decription));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setHasOptionsMenu(true);
        return v;
    }






    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu2, menu);
        try {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // do your search on change or save the last string or...
                    final List<Home_frag_Model> filteredModelList = filter(ar, s);

                    adapter.setFilter(filteredModelList);
                    return false;
                }



                private List<Home_frag_Model> filter(List<Home_frag_Model> models, String query) {
                    query = query.toLowerCase();
                    final List<Home_frag_Model> filteredModelList = new ArrayList<>();
                    for (Home_frag_Model model : models) {
                        final String text = model.getName().toLowerCase();
                        if (text.contains(query)) {
                            filteredModelList.add(model);
                        }
                    }
                    return filteredModelList;
                }
            });


        }catch(Exception e){e.printStackTrace();}
    }






    private void init(final View v) {

        mUserDatabase = GetFirebaseRef.GetDbIns().getReference().child("home_page_post");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.home_recView);
        mRecyclerView.setHasFixedSize(true);
        mPd = new ProgressDialog(v.getContext());

    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecHolder> {
        List<Home_frag_Model> result ;
        public RecyclerAdapter(ArrayList<Home_frag_Model> ar) {
            this.result=ar;
        }

        @Override
        public RecyclerAdapter.RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.home_rec_layout, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecHolder(v);
        }

        public void setFilter(List<Home_frag_Model> Models) {
            result = new ArrayList<>();
            result.addAll(Models);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.RecHolder holder, int position) {
            View v = holder.itemView;
            v.setTag(position);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent in = new Intent(v.getContext(), DetailActivity.class);
                        Bundle b = new Bundle();
                        Bundle extras = new Bundle();
                        extras.putString("Lang", String.valueOf(result.get(position).getLang()));
                        extras.putString("Lat", String.valueOf(result.get(position).getLat()));
                        extras.putString("Image", result.get(position).getImage());
                        extras.putString("Name", result.get(position).getName());
                        extras.putString("Description", result.get(position).getDescription());
                        in.putExtras(extras);
                        startActivity(in);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
            });

            holder.bindData(result.get(position));
        }

        @Override
        public int getItemCount() {
            return result.size();
        }

        public class RecHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textView;

            public RecHolder(View itemView) {

                super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.home_rec_imageView);
                textView = (TextView) itemView.findViewById(R.id.home_rec_place_name);
                //initialize views here
            }

            void bindData(Home_frag_Model data) {
                Picasso.with(getContext()).load(data.getImage()).into(imageView);
                textView.setText(data.getName());
            }

        }

    }

}
