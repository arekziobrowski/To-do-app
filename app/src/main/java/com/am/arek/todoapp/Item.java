package com.am.arek.todoapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by arek on 24.04.17.
 */

public class Item implements Parcelable {

    private int mImageId;
    private String mItemTitle;
    private String mItemDesc;
    private Date mDate;

    private static final String ITEM_IMAGE_ID = "imageid";
    private static final String ITEM_TITLE = "title";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_DATE = "date";


    public Item(int imageId, String itemTitle, String itemDesc, Date date) {
        mImageId = imageId;
        mItemTitle = itemTitle;
        mItemDesc = itemDesc;
        mDate = date;
    }

    public Item(JSONObject jsonObject) throws JSONException {
        mImageId = jsonObject.getInt(ITEM_IMAGE_ID);
        mItemTitle = jsonObject.getString(ITEM_TITLE);
        mItemDesc = jsonObject.getString(ITEM_DESCRIPTION);
        mDate = new Date(jsonObject.getLong(ITEM_DATE));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ITEM_IMAGE_ID, mImageId);
        jsonObject.put(ITEM_TITLE, mItemTitle);
        jsonObject.put(ITEM_DESCRIPTION, mItemDesc);
        jsonObject.put(ITEM_DATE, mDate.getTime());

        return jsonObject;
    }

    public int getmImageId() {
        return mImageId;
    }

    public void setmImageId(int mImageId) {
        this.mImageId = mImageId;
    }

    public String getmItemTitle() {
        return mItemTitle;
    }

    public void setmItemTitle(String mItemTitle) {
        this.mItemTitle = mItemTitle;
    }

    public String getmItemDesc() {
        return mItemDesc;
    }

    public void setmItemDesc(String mItemDesc) {
        this.mItemDesc = mItemDesc;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    protected Item(Parcel in) {
        mImageId = in.readInt();
        mItemTitle = in.readString();
        mItemDesc = in.readString();
        long tmpMDate = in.readLong();
        mDate = tmpMDate != -1 ? new Date(tmpMDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImageId);
        dest.writeString(mItemTitle);
        dest.writeString(mItemDesc);
        dest.writeLong(mDate != null ? mDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}