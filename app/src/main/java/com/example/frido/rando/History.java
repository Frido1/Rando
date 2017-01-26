package com.example.frido.rando;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.frido.rando.Database.RandoDatabaseContract;
import com.example.frido.rando.Database.RandoDbHelper;
import com.example.frido.rando.Fragments.VoronoFragment;
import com.example.frido.rando.Utilities.CustomListAdapter;
import com.example.frido.rando.Utilities.VoronoAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class History extends AppCompatActivity {
    private LayoutInflater layoutInflater;
    private ArrayList<String> listUrls;
    @BindView(R.id.historyListView) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }


}
