package com.am.arek.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by arek on 26.04.17.
 */

public class ArchiveActivity extends AppCompatActivity {

    StoreData mStoreData;
    ArrayList<Item> mItems;
    ListAdapter mListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_layout);

        mStoreData = new StoreData(this, ItemListFragment.ARCHIVE_FILENAME);

        mItems = getItemsFromInternalStorage();

        ListView listView = (ListView) findViewById(R.id.archive_list_view);
        mListAdapter = new ListAdapter(this, mItems);
        listView.setAdapter(mListAdapter);
    }

    private ArrayList<Item> getItemsFromInternalStorage() {
        ArrayList<Item> items = null;
        try {
            items = mStoreData.loadData();

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }

        if(items == null) {
            items = new ArrayList<>();
        }

        return items;
    }

    private void saveItemsToInternalStorage() {
        try {
            mStoreData.saveData(mItems);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void onClick(View view) {
        mItems.clear();
        mListAdapter.notifyDataSetChanged();
        saveItemsToInternalStorage();
    }


}
