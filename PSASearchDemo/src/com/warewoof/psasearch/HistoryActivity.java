package com.warewoof.psasearch;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Collections;

public class HistoryActivity extends Activity {
    public static final String TAG = "HistoryActivity";
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_history);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Functions.GetHistoryCount() == 0) {
            setTitle(R.string.history_none_text);
        } else {
            listview = (ListView) findViewById(R.id.history_list);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3) {
                    String[] value = (String[]) adapter.getItemAtPosition(position);
                    String[] searchText = value[1].split("#");
                    Intent results = new Intent(HistoryActivity.this, ResultsActivity.class);
                    results.putExtra(ResultsActivity.ARG_SEARCH_TEXT, searchText[1]);
                    results.putExtra(ResultsActivity.ARG_LOG_SEARCH, false);
                    HistoryActivity.this.startActivity(results);
                }
            });
            listview.setAdapter(new CustomListAdapter(this, Functions.GetHistoryList()));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        super.onMenuItemSelected(featureId, item);
        if (item.getItemId() == R.id.action_clearhistory) {
            Log.d(TAG, "Clear history");
            Functions.ClearHistory();
            finish();
            startActivity(getIntent());
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        super.onBackPressed();
        return true;
    }

/*
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
*/
}

