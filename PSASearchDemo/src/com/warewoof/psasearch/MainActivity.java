package com.warewoof.psasearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.warewoof.psasearch.R;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";
	public EditText searchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_search);

		setTitle(R.string.app_name);

		((TextView) findViewById(R.id.header_text))
				.setText(R.string.search_prompt);

		searchText = (EditText) findViewById(R.id.edit_box);
	}

	public void keyClicked(View view) {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(15);

		String currentSearch = searchText.getText().toString();
		switch (view.getId()) {

		case R.id.key_back:
			if (currentSearch.length() > 0)
				searchText.setText(currentSearch.substring(0,
						currentSearch.length() - 1));
			break;
		case R.id.key_clear:
			searchText.setText("");
			break;
		case R.id.key_0:
			searchText.setText(currentSearch + "0");
			break;
		case R.id.key_1:
			searchText.setText(currentSearch + "1");
			break;
		case R.id.key_2:
			searchText.setText(currentSearch + "2");
			break;
		case R.id.key_3:
			searchText.setText(currentSearch + "3");
			break;
		case R.id.key_4:
			searchText.setText(currentSearch + "4");
			break;
		case R.id.key_5:
			searchText.setText(currentSearch + "5");
			break;
		case R.id.key_6:
			searchText.setText(currentSearch + "6");
			break;
		case R.id.key_7:
			searchText.setText(currentSearch + "7");
			break;
		case R.id.key_8:
			searchText.setText(currentSearch + "8");
			break;
		case R.id.key_9:
			searchText.setText(currentSearch + "9");
			break;

		case R.id.search_button:
			if (searchText.getText().toString().length() < 8) {
				Toast.makeText(getApplicationContext(), R.string.input_error,
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent results = new Intent(MainActivity.this,
					ResultsActivity.class);

			results.putExtra(ResultsActivity.ARG_SEARCH_TEXT, searchText
					.getText().toString());
			MainActivity.this.startActivity(results);
			break;

		case R.id.history_button:
			Intent history = new Intent(MainActivity.this,
					HistoryActivity.class);
			MainActivity.this.startActivity(history);
			break;
		case R.id.cancel_button:
			searchText.setText("");
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent about = new Intent(MainActivity.this, AboutActivity.class);
		MainActivity.this.startActivity(about);
		return true;
	}
}
