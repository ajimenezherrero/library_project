package edu.upc.eetac.dsa.ajimenezherrero.library.android;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;


import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.Book;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.BookCollection;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.LibraryAPI;
import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.LibraryAndroidException;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class LibraryMainActivity extends ListActivity {
	private final static String TAG = LibraryMainActivity.class.toString();
	private ArrayList<Book> bookList;
	BookAdapter adapter;
	private BookCollection books = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_layout);
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("alex", "alex".toCharArray());
			}
		});
		bookList = new ArrayList<>();
		adapter = new BookAdapter(this, bookList);
		setListAdapter(adapter);
		(new FetchBooksTask()).execute();
	}

	public void postSearch(View v) {
		EditText etSearch = (EditText) findViewById(R.id.etSearch); 
		String search = etSearch.getText().toString();
		String url = books.getLinks().get("search")
				.getTarget();
		String urlSearch = url + search;
		(new SearchBooksTask()).execute(urlSearch);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Book book = bookList.get(position);
		Log.d(TAG, book.getLinks().get("self").getTarget());

		Intent intent = new Intent(this, BookDetailActivity.class);
		intent.putExtra("url", book.getLinks().get("self").getTarget());
		intent.putExtra("urlReviews", book.getLinks().get("bookreviews")
				.getTarget());
		startActivity(intent);
	}

	private void addBooks(BookCollection books) {
		bookList.addAll(books.getBooks());
		adapter.notifyDataSetChanged();
	}

	private class FetchBooksTask extends AsyncTask<Void, Void, BookCollection> {
		private ProgressDialog pd;

		@Override
		protected BookCollection doInBackground(Void... params) {
			try {
				books = LibraryAPI.getInstance(LibraryMainActivity.this)
						.getBooks();
			} catch (LibraryAndroidException e) {
				e.printStackTrace();
			}
			return books;
		}

		@Override
		protected void onPostExecute(BookCollection result) {
			bookList.clear();
			addBooks(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LibraryMainActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
	
	private class SearchBooksTask extends AsyncTask<String, Void, BookCollection> {
		private ProgressDialog pd;

		@Override
		protected BookCollection doInBackground(String... params) {
			BookCollection books = null;
			try {
				books = LibraryAPI.getInstance(LibraryMainActivity.this)
						.getBooksSearch(params[0]);
			} catch (LibraryAndroidException e) {
				e.printStackTrace();
			}
			return books;
		}

		@Override
		protected void onPostExecute(BookCollection result) {
			bookList.clear();
			addBooks(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LibraryMainActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
}
