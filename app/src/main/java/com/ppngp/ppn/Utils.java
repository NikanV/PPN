package com.ppngp.ppn;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Utils {
    public static HashMap<String, String> readJsonFromAssets(Context context, String fileName) {
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));

            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            reader.close();

            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() {}.getType();
            hashMap = gson.fromJson(jsonStringBuilder.toString(), type);
        } catch (Exception e) {
            Log.e("JsonToHashMap", "Error reading JSON file from assets", e);
        }

        return hashMap;
    }

    public static void writeJsonToAssets(Context context, String fileName, HashMap<String, String> data) {
        try {
            String jsonString = new Gson().toJson(data);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(jsonString);
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("HashMapToJson", "Error writing JSON file to assets", e);
        }
    }
}
