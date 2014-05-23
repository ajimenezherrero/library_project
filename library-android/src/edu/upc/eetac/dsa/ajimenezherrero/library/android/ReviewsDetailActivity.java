package edu.upc.eetac.dsa.ajimenezherrero.library.android;

import java.util.ArrayList;

import android.os.Bundle;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.LibraryAPI;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.LibraryAndroidException;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.Review;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.ReviewCollection;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ReviewsDetailActivity extends ListActivity{
	private final static String TAG = LibraryMainActivity.class.toString();
	private ArrayList<Review> reviewList;
	ReviewAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviews_layout);
		reviewList = new ArrayList<>();
		adapter = new ReviewAdapter(this, reviewList);
		setListAdapter(adapter);
		String urlBook = (String) getIntent().getExtras().get("url");
		(new FetchReviewsTask()).execute(urlBook);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.reviews_actions, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miWrite:
			Intent intent = new Intent(this, WriteDetailActivity.class);
			String urlBook = (String) getIntent().getExtras().get("url");
			intent.putExtra("url", urlBook);
			startActivity(intent);
			return true;
	 
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Review review = reviewList.get(position);
		Log.d(TAG, review.getLinks().get("self-review").getTarget());
		Intent intent = new Intent(this, ReviewDetailActivity.class);
		intent.putExtra("url", review.getLinks().get("self-review").getTarget());
		startActivity(intent);
	}
	
	private void addReviews(ReviewCollection reviews){
		reviewList.addAll(reviews.getReviews());
		adapter.notifyDataSetChanged();
	}
	
	
	private class FetchReviewsTask extends AsyncTask<String, Void, ReviewCollection> {
		private ProgressDialog pd;

		@Override
		protected ReviewCollection doInBackground(String... params) {
			ReviewCollection reviews = null;
			try {
				reviews = LibraryAPI.getInstance(ReviewsDetailActivity.this).getReviews(params[0]);
			} catch (LibraryAndroidException e) {
				e.printStackTrace();
			}
			return reviews;
		}

		@Override
		protected void onPostExecute(ReviewCollection result) {
			addReviews(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ReviewsDetailActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
	
	
}
