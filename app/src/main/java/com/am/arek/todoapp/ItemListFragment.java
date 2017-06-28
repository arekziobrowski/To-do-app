package com.am.arek.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by arek on 24.04.17.
 */

public class ItemListFragment extends ListFragment {

    boolean mDualPane;
    int mCurrentPos = 0;
    ListAdapter mListAdapter;
    ArrayList<Item> mItems;
    ArrayList<Item> mArchiveItems;
    StoreData mStoreData;
    StoreData mStoreDataArchive;
    public static String FILENAME = "items.json";
    public static String ARCHIVE_FILENAME = "archive.json";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStoreData = new StoreData(getContext(), FILENAME);
        mStoreDataArchive = new StoreData(getContext(), ARCHIVE_FILENAME);
        mArchiveItems = getArchiveItemsFromInternalStorage();
        Thread thread = null;
        if(savedInstanceState != null) {
            mCurrentPos = savedInstanceState.getInt("currentPosition");
            mItems = savedInstanceState.getParcelableArrayList("items");
        }
        else {
            thread = new Thread() {
                @Override
                public void run() {
                    mItems = getItemsFromInternalStorage();
                }
            };
            thread.start();
        }
        if(thread != null) {
            try {
                thread.join();
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        mListAdapter = new ListAdapter(getActivity(), mItems);
        setListAdapter(mListAdapter);

        View detailsFrame = getActivity().findViewById(R.id.details_frame);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Item dummy = mItems.get(position);
                mItems.remove(position);
                mArchiveItems.add(dummy);
                mListAdapter.notifyDataSetChanged();
                if(mDualPane) {
                    if(mItems.isEmpty()) {
                        showDetails(mCurrentPos, true);
                    }
                    else {
                        if(position != 0) {
                            showDetails(mListAdapter.getPosition(mItems.get(position - 1)),true);
                        }
                        else {
                            showDetails(0,true);
                        }
                    }
                }
                Snackbar snackbar = Snackbar
                        .make(view, "Item deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mItems.add(dummy);
                                mArchiveItems.remove(dummy);
                                mListAdapter.notifyDataSetChanged();
                                if(mDualPane) {
                                   showDetails(mListAdapter.getPosition(dummy), true);
                                }
                            }
                        });

                snackbar.show();
                return true;
            }
        });


        if(mDualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurrentPos, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Thread thread = new Thread() {
            @Override
            public void run() {
                saveArchiveItemsToInternalStorage();
            }
        };
        thread.start();
    }

    @Override
    public void onStop() {
        super.onStop();

        Thread thread = new Thread() {
            @Override
            public void run() {
                saveItemsToInternalStorage();
            }
        };
        thread.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putInt("currentPosition", mCurrentPos);
            outState.putParcelableArrayList("items", mItems);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mArchiveItems = getArchiveItemsFromInternalStorage();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position, false);
    }



    //Deletion is a special kind of parameter that indicates whether a deletion has been made and the view needs to be refreshed
    void showDetails(int index, boolean deletion) {
        mCurrentPos = index;

        if(mDualPane) {
            DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details_frame);

            if(mItems.size() == 0) {
                Item dummy = new Item(R.drawable.ic_speaker_notes_off_black_24dp, "No items", "No items",null);
                //It is invoked with parameter -1, so the condition if(detailsFragment == null || detailsFragment.getShownIndex() != index)
                //is always false true when we add an item to fill the list
                detailsFragment = DetailsFragment.newInstance(-1, dummy);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.details_frame, detailsFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
            else {
                if (detailsFragment == null || detailsFragment.getShownIndex() != index || deletion) {
                    detailsFragment = DetailsFragment.newInstance(index, mItems.get(index));

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.details_frame, detailsFragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.commit();
               }
            }
        }
        else {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("item", mItems.get(index));
            startActivity(intent);
        }
    }

    public void changeData(Item item) {
        mItems.add(item);
        mListAdapter.notifyDataSetChanged();

        if(mDualPane) {
            showDetails(mListAdapter.getPosition(item), true);
        }
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

    private ArrayList<Item> getArchiveItemsFromInternalStorage() {
        ArrayList<Item> items = null;
        try {
            items = mStoreDataArchive.loadData();
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
            mStoreDataArchive.saveData(mArchiveItems);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void saveArchiveItemsToInternalStorage() {
        try {
            mStoreDataArchive.saveData(mArchiveItems);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
