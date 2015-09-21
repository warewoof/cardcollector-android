package com.warewoof.psasearch;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.warewoof.psasearch.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ResultsActivity extends Activity {

	public static final String TAG = "ResultsActivity";
	public static final String ARG_SEARCH_TEXT = "search_text";
	public static final String ARG_LOG_SEARCH = "log_search";
	private LinearLayout resultsView;
	ProgressDialog progressDialog;
	private String optText;
	private Boolean logSearch;
	private Activity activity;
	
	
	 
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.main_results);
		

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		
		final ProgressBar Pbar;
		Pbar = (ProgressBar) findViewById(R.id.pB1);
		
		
		Intent intent = getIntent();

		resultsView = (LinearLayout) findViewById(R.id.results_view);
		resultsView.setVisibility(View.INVISIBLE);

		optText = intent.getStringExtra(ARG_SEARCH_TEXT);
		logSearch = intent.getBooleanExtra(ARG_LOG_SEARCH, true);

		Log.d(TAG, "Search text: " + optText);

		String searchArgs = "";

		try {
			searchArgs += URLEncoder.encode(optText.trim(), "utf-8");				            
		} catch (Exception e) {
			e.printStackTrace();
		}

		searchArgs = "http://www.psacard.com/Cert/GetByCertNo/" + searchArgs;
		//Log.d(TAG, "Loading: " + searchArgs);
		
		
		activity = this;

		new GetData().execute(searchArgs);

	}

	private class GetData extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = ProgressDialog.show(ResultsActivity.this, "", "Searching...");

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			String response;

			try {

				HttpClient httpclient = new DefaultHttpClient();

				//HttpPost httppost = new HttpPost(url);
				HttpPost httppost = new HttpPost(params[0]);//you can also pass it and get the Url here.

				HttpResponse responce = httpclient.execute(httppost);

				HttpEntity httpEntity = responce.getEntity();

				response = EntityUtils.toString(httpEntity);

				Log.d("response is", response);

				return new JSONObject(response);

			} catch (Exception ex) {

				ex.printStackTrace();

			}

			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result)
		{
			super.onPostExecute(result);

			progressDialog.dismiss();

			if(result != null)
			{
				try
				{
					JSONObject jobj = result;

					String status = jobj.getString("CertNotFound");


					Log.d(TAG, status);
					if(status.equals("false"))
					{
						resultsView.setVisibility(View.VISIBLE);
						String certNo = jobj.getString("CertNo");
						String year = jobj.getString("Year");
						String brand = jobj.getString("Brand");
						String cardNo = jobj.getString("CardNumber");
						String player = jobj.getString("Player");
						String variety = jobj.getString("Variety");
						String grade = jobj.getString("Grade");
						String pop = jobj.getString("Population");
						String qualpop = jobj.getString("QualifierPopulation");
						String higherpop = jobj.getString("PopHigher");
						activity.setTitle("Cert #"+certNo);
						((TextView) resultsView.findViewById(R.id.tv_yearbrand)).setText(year + " " + brand);
						((TextView) resultsView.findViewById(R.id.tv_cardno)).setText("#"+cardNo);
						((TextView) resultsView.findViewById(R.id.tv_player)).setText(player);
						if (variety.equalsIgnoreCase("")) {
							resultsView.findViewById(R.id.tv_variety).setVisibility(TextView.GONE);
						} else {
							((TextView) resultsView.findViewById(R.id.tv_variety)).setText(variety);
						}

						((TextView) resultsView.findViewById(R.id.tv_grade)).setText(grade);
						((TextView) resultsView.findViewById(R.id.tv_pop)).setText(pop);
						((TextView) resultsView.findViewById(R.id.tv_qualpop)).setText(qualpop);
						((TextView) resultsView.findViewById(R.id.tv_higherpop)).setText(higherpop);

						TextView priceLink = (TextView) findViewById(R.id.priceLink);
						String searchUrl = "<a href=\"http://search.ebay.com/ws/search/SaleSearch?satitle="
											+ Uri.encode(year + "+"
											+ brand + "+"
											+ cardNo + "+"
											+ player)
											+ "\">See eBay listings</a>";

						Log.d(TAG, "Search URL: " + searchUrl);
						priceLink.setText(Html.fromHtml(searchUrl));
						priceLink.setMovementMethod(LinkMovementMethod.getInstance());
						if (logSearch)
							Functions.UpdateHistoryList(new String[]{year +  " " + brand + " " + grade + "\n" + player, "Cert #" + certNo});
						//
					} else {
						//resultsView.setVisibility(View.INVISIBLE);
						activity.setTitle(R.string.default_search_notfound_title);
						findViewById(R.id.result_status).setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.result_status)).setText("Cert # "+ optText +" Not Found");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(ResultsActivity.this, "Network Problem", Toast.LENGTH_LONG).show();
			}
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		super.onBackPressed();
	    return true;
	}
	
	

}