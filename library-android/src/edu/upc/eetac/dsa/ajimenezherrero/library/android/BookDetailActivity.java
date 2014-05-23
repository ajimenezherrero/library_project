package edu.upc.eetac.dsa.ajimenezherrero.library.android;

import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.Book;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.LibraryAPI;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.LibraryAndroidException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class BookDetailActivity extends Activity {
	private final static String TAG = BookDetailActivity.class.getName();
	private String urlReviews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail_layout);
		String urlBook = (String) getIntent().getExtras().get("url");
		urlReviews = (String) getIntent().getExtras().get("urlReviews");
		(new FetchStingTask()).execute(urlBook);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.library_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miReviews:
			Intent intent = new Intent(this, ReviewsDetailActivity.class);
			intent.putExtra("url", urlReviews);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void loadBook(Book book) {
		TextView tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
		TextView tvDetailAuthor = (TextView) findViewById(R.id.tvDetailAuthor);
		tvDetailTitle.setText(book.getTitle());
		tvDetailAuthor.setText(book.getAuthor());
	}

	private class FetchStingTask extends AsyncTask<String, Void, Book> {
		private ProgressDialog pd;

		@Override
		protected Book doInBackground(String... params) {
			Book book = null;
			try {
				book = LibraryAPI.getInstance(BookDetailActivity.this).getBook(
						params[0]);
			} catch (LibraryAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return book;
		}

		@Override
		protected void onPostExecute(Book result) {
			loadBook(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(BookDetailActivity.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
}
