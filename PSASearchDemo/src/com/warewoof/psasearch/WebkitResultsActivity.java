package com.warewoof.psasearch;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.URLEncoder;

public class WebkitResultsActivity extends Activity {

	public static final String TAG = "ResultsActivity";
	public static final String ARG_SEARCH_TEXT = "search_text";
	public static final String ARG_SEARCH_LOCATION = "search_loc";
	public static final String ARG_SEARCH_RADIUS = "search_rad";
	private WebView resultsView;
	
	
	 
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//setContentView(R.layout.main_results);
		setContentView(R.layout.main_results_webkit);
		

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		
		final ProgressBar Pbar;
		Pbar = (ProgressBar) findViewById(R.id.pB1);
		
		
		Intent intent = getIntent();

		resultsView = (WebView) findViewById(R.id.results_view);
		resultsView.setVisibility(View.INVISIBLE);
		resultsView.getSettings().setJavaScriptEnabled(true);
		resultsView.getSettings().setBuiltInZoomControls(true);

		String optText = intent.getStringExtra(ARG_SEARCH_TEXT);

		Log.d(TAG, "Search text: " + optText);

		String searchArgs = "";

		try {
			searchArgs += URLEncoder.encode(optText.trim(), "utf-8");				            
		} catch (Exception e) {
			e.printStackTrace();
		}

		searchArgs = "http://www.psacard.com/Cert/" + searchArgs;
		Log.d(TAG, "Loading: " + searchArgs);
		
		
		final Activity activity = this;
		
		resultsView.setWebChromeClient(new WebChromeClient() {

			public void onProgressChanged(WebView view, int progress) {
				try {

					if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
						Pbar.setVisibility(ProgressBar.VISIBLE);
					}
					activity.setTitle("Loading... " + String.valueOf(progress) + "%");
					Pbar.setProgress(progress);
					if (progress == 100) {
						Pbar.setVisibility(ProgressBar.GONE);
						activity.setTitle(view.getTitle().replace(" - Mobile", ""));

					}
				} catch (Exception e) {
					e.printStackTrace();
					activity.setTitle("");
					Pbar.setVisibility(ProgressBar.GONE);
				}
			}
		});

		resultsView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try { 
					view.loadUrl(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d(TAG, "onPageFinished");
				
				resultsView.loadUrl("javascript:(function() { document.getElementById('cert-mobile-container').getElementsByTagName('div')[0].style.display = 'none';})()");
				resultsView.loadUrl("javascript:(function() { " +"document.getElementById('cert-mobile-loading').style.display = 'none';" +"})()");
				resultsView.loadUrl("javascript:(function() { " +"document.getElementById('cert-mobile-results-description-top').style.display = 'none';" +"})()");
				resultsView.loadUrl("javascript:(function() { " +"document.getElementById('cert-mobile-title').style.display = 'none';" +"})()");
				resultsView.loadUrl("javascript:(function() { " +"document.getElementById('cert-mobile-description').style.display = 'none';" +"})()");
				resultsView.loadUrl("javascript:(function() { " +"document.getElementsByClassName('cert-mobile-top')[0].style.display = 'none';" +"})()");
				resultsView.loadUrl("javascript:(function() { " +"document.getElementById('cert-not-found').getElementsByTagName('p')[1].style.display = 'none';" +"})()");
				/*resultsView.loadUrl("javascript:(function() { " +
						"var element = document.getElementById('cert-not-found').getElementsByTagName('p')[1];" +
						"if (typeof(element) != 'undefined' && element != null)" +
						"{" +
						"document.getElementById('cert-not-found').getElementsByTagName('p')[1].style.display = 'none';" +
						"} else {" +
						"document.getElementById('cert-not-found').getElementsByTagName('p')[0].style.display = 'none';" +
						"}})()");
				*/
				resultsView.loadUrl("javascript:(function() { " +"document.getElementById('SubmitCert').style.display = 'none';" +"})()");
				resultsView.loadUrl("javascript:(function() { " +"document.getElementById('cert-mobile-fullsite').style.display = 'none';" +"})()");
				//resultsView.loadUrl("javascript:(function() { " +"document.getElementById('CertNoInstruction').style.display = 'none';" +"})()");
				//resultsView.loadUrl("javascript:(function() { " +"document.getElementById('CertNoInstruction').style.display = 'none';" +"})()");
				/* further customization can be made to the returned HTML here	*/
				resultsView.setVisibility(View.VISIBLE);
			}  			
			
		});
		
		resultsView.loadUrl(searchArgs);
	}

	@Override
	public void onBackPressed() {
		if (resultsView.canGoBack()) {
			resultsView.goBack();			
		} else {
			super.onBackPressed();
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		super.onBackPressed();
	    return true;

	}
	
	

}