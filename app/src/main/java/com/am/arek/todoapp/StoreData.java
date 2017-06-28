package com.am.arek.todoapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by arek on 25.04.17.
 */

public class StoreData {

    private Context mContext;
    private String mFilename;

    public StoreData(Context context, String filename) {
        mContext = context;
        mFilename = filename;
    }

    public void saveData(ArrayList<Item> items) throws IOException, JSONException{
        FileOutputStream fileOutputStream;
        OutputStreamWriter outputStreamWriter;
        //openFileOutput will create a new file if it doesn't already exist
        fileOutputStream = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(toJSONArray(items).toString());
        outputStreamWriter.close();
        fileOutputStream.close();
    }

    public ArrayList<Item> loadData() throws IOException, JSONException {
        ArrayList<Item> items = new ArrayList<>();
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = mContext.openFileInput(mFilename);
            StringBuilder builder = new StringBuilder();
            String line;
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(builder.toString()).nextValue();
            for(int i = 0; i < jsonArray.length(); i++) {
                Item item = new Item(jsonArray.getJSONObject(i));
                items.add(item);
            }
        }
        catch (FileNotFoundException ex) {
            //Might happen during first run, because the file doesn't exist, but saveData will create such
        }
        finally {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(fileInputStream != null) {
                fileInputStream.close();
            }

        }
        return items;
    }

    public JSONArray toJSONArray(ArrayList<Item> items) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(Item item : items) {
            JSONObject jsonObject = item.toJSON();
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }


}
