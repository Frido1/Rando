package com.example.frido.rando.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.frido.rando.Database.RandoDatabaseContract;
import com.example.frido.rando.Database.RandoDbHelper;
import com.example.frido.rando.R;
import com.example.frido.rando.Utilities.CustomListAdapter;
import com.example.frido.rando.Utilities.VoronoAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fjmar on 1/26/2017.
 */

public class VoronoFragment extends Fragment {
    private ArrayList<String> listUrls;
    @BindView(R.id.historyListView) ListView listView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        listUrls = getURLSFromDatabase();
        unbinder = ButterKnife.bind(this,view );
        VoronoAdapter voronoAdapter = new VoronoAdapter(listUrls,getActivity().getApplicationContext());
        listView.setAdapter(voronoAdapter);
        return view;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.voronoiSwitch:
                switchFragments();
                break;
            default: break;
        }
        return true;
    }

    private void switchFragments() {
        Fragment historyListFragment = new HistoryListFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, historyListFragment);
        fragmentTransaction.commit();
    }

    private ArrayList<String> getURLSFromDatabase() {
        ArrayList<String> listUrls = new ArrayList<String>();
        RandoDbHelper dbHelper = new RandoDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String[] projection = {
                RandoDatabaseContract.RandoDatabase.COLUMN_THUMBNAIL_ID,
        };
        Cursor cursor = sqLiteDatabase.query(
                RandoDatabaseContract.RandoDatabase.TABLE_NAME,projection,null,null,null,null,null
        );
        while (cursor.moveToNext()){
            String temp =  cursor.getString(cursor.getColumnIndex(RandoDatabaseContract.RandoDatabase.COLUMN_THUMBNAIL_ID));
            listUrls.add(temp);

        }
        return listUrls;
    }
}
