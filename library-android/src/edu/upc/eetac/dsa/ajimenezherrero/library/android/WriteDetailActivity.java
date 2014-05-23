package edu.upc.eetac.dsa.ajimenezherrero.library.android;

import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.LibraryAPI;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.LibraryAndroidException;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.Review;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class WriteDetailActivity extends Activity {
	private final static String TAG = WriteDetailActivity.class.getName();
 
	private class PostReviewTask extends AsyncTask<String, Void, Review> {
		private ProgressDialog pd;
 
		@Override
		protected Review doInBackground(String... params) {
			Review review = null;
			try {
				review = LibraryAPI.getInstance(WriteDetailActivity.this).createReview(params[0], params[1]);
			} catch (LibraryAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return review;
		}
 
		@Override
		protected void onPostExecute(Review result) {
			showReviews(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(WriteDetailActivity.this);
 
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_review_layout);
 
	}
 
	public void cancel(View v) {
		finish();
	}
 
	public void postReview(View v) {
		EditText etText = (EditText) findViewById(R.id.etText);
 
		String content = etText.getText().toString();
		String url = (String) getIntent().getExtras().get("url");
		(new PostReviewTask()).execute(content, url);
	}
 
	private void showReviews(Review review) {
		Log.d(TAG, review.getLinks().get("reviews").getTarget());
		Intent intent = new Intent(this, ReviewsDetailActivity.class);
		intent.putExtra("url", review.getLinks().get("reviews").getTarget() );
		startActivity(intent);
	}
}
