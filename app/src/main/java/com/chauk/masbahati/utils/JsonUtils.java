package com.chauk.masbahati.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static String loadJSONFromAsset(Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open("azkar.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONArray getZekerByCategory(Context context, String category) {
        JSONArray newJsonArray = new JSONArray();
        String myJson = loadJSONFromAsset(context);
        try {
            JSONArray jsonArray = new JSONArray(myJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String cat = jsonObject.getString("category");
                if (category.equals(cat)) {
                    newJsonArray.put(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newJsonArray;
    }

    public static List<String> getCateories(Context context) {
        String myJson = loadJSONFromAsset(context);
        List<String> listOfCateories = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(myJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String category = jsonObject.getString("category");
                if (!listOfCateories.contains(category))
                    listOfCateories.add(category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfCateories;
    }
}
