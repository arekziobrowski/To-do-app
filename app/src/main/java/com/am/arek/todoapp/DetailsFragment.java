package com.am.arek.todoapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by arek on 24.04.17.
 */

public class DetailsFragment extends Fragment {

    public static DetailsFragment newInstance(int index, Item item) {
        DetailsFragment detailsFragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putParcelable("item", item);
        detailsFragment.setArguments(args);

        return detailsFragment;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        TextView itemTitle = (TextView) view.findViewById(R.id.details_itemTitle);
        TextView itemDesc = (TextView) view.findViewById(R.id.details_itemDesc);
        TextView itemDate = (TextView) view.findViewById(R.id.details_itemDate);
        ImageView priorityImage = (ImageView) view.findViewById(R.id.details_priorityImage);

        Item item = getArguments().getParcelable("item");
        itemTitle.setText(item.getmItemTitle());
        if(item.getmDate() != null) {
            itemDesc.setText(item.getmItemDesc());
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd yyyy", Locale.US);
            itemDate.setText(sdf.format(item.getmDate()));
        }
        priorityImage.setImageResource(item.getmImageId());

        return view;
    }
}
