package com.warewoof.psasearch;

import android.content.Context;
import java.util.ArrayList;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class Functions extends Application {
    /* Shared Preference settings */
    private static final String SP_FILE_NAME = "psasearch_sp_file";  //share preference file name
    private static final String SP_HISTORY_KEY = "psasearch_sp_historykey";
    private static final Integer MAX_HISTORY_SIZE = 10;
    public static Context context;
    private static String TAG = "Global";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static ArrayList<String[]> GetHistoryList() {
        SharedPreferences settings = context.getSharedPreferences(SP_FILE_NAME, 0);
        String historyAsString = new String(settings.getString(SP_HISTORY_KEY, ""));
        String splitted[] = historyAsString.split(";");
        ArrayList<String[]> historyList = new ArrayList<String[]>();
        for(int i=0 ; i<splitted.length ; i++){
            if (!splitted[i].equalsIgnoreCase(""))
                historyList.add(splitted[i].split(":"));
        }
        return historyList;
    }

    public static void UpdateHistoryList(String[] historyItem) {
        /* make sure the String[] is 2-item array */
        ArrayList<String[]> historyItems = GetHistoryList();
        historyItems.add(0, historyItem);
        if (historyItems.size() > MAX_HISTORY_SIZE) {
            historyItems.remove(historyItems.size()-1);
        }
        String historyAsString = "";
        for(int i=0 ; i< historyItems.size() ; i++){
            historyAsString += historyItems.get(i)[0]+":"+historyItems.get(i)[1] + ";";
        }
        SharedPreferences settings = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SP_HISTORY_KEY, historyAsString);
        editor.commit();
    }

    public static void ClearHistory() {
        SharedPreferences settings = context.getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SP_HISTORY_KEY, "");
        editor.commit();
    }

    public static int GetHistoryCount() {
        SharedPreferences settings = context.getSharedPreferences(SP_FILE_NAME, 0);
        String historyAsString = new String(settings.getString(SP_HISTORY_KEY, ""));
        String splitted[] = historyAsString.split(";");
        if (splitted[0].equalsIgnoreCase("")) {
            return 0;
        } else {
            return splitted.length;
        }
    }
}








