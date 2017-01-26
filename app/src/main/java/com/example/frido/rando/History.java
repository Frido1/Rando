package com.example.frido.rando;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.example.frido.rando.Database.RandoDatabaseContract;
import com.example.frido.rando.Database.RandoDbHelper;
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
        listUrls = getURLSFromDatabase();
        ButterKnife.bind(this);
        /*CustomListAdapter customListAdapter = new CustomListAdapter(getApplicationContext(),listUrls);
        listView.setAdapter(customListAdapter);
*/

        VoronoAdapter voronoAdapter = new VoronoAdapter(listUrls,getApplicationContext());
        listView.setAdapter(voronoAdapter);
    }

    private ArrayList<String> getURLSFromDatabase() {
        ArrayList<String> listUrls = new ArrayList<String>();
        RandoDbHelper dbHelper = new RandoDbHelper(getApplicationContext());
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
