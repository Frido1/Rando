package com.example.frido.rando.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.frido.rando.Objects.RandoPicture;
import com.example.frido.rando.R;
import com.example.frido.rando.Utilities.CustomListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fjmar on 1/26/2017.
 */

public class HistoryListFragment extends Fragment  {
    private ArrayList<String> listUrls;
    @BindView(R.id.historyListView)
    ListView listView;
    private Unbinder unbinder;
    private CustomListAdapter customListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_history,container,false);
        listUrls = getURLSFromFirebase();
        unbinder = ButterKnife.bind(this,view );
        customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(),listUrls);
        listView.setAdapter(customListAdapter);
        return view;

    }

    private ArrayList<String> getURLSFromFirebase() {
        final ArrayList<String> urls = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();
        DatabaseReference reference = database.getReference("users").child(userID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("firebase",dataSnapshot.toString());
                Iterable<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = dataSnapshotIterator.iterator();
               while (iterator.hasNext()){
                   RandoPicture randoPicture =iterator.next().getValue(RandoPicture.class);
                   urls.add(randoPicture.getThumbnail_ID());
               }
                customListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return urls;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history_list_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.voronoiSwitch:
                switchVoronoFragment();
                break;
            case R.id.gridLayout:
                switchGridoFragments();
                break;
            default: break;
        }
        return true;
    }

    private void switchGridoFragments() {
        Fragment gridFragment   = new HistoryStaggeredGridFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, gridFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private void switchVoronoFragment() {
        Fragment VoronoFragment = new VoronoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, VoronoFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

}
