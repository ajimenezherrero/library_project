package edu.upc.eetac.dsa.ajimenezherrero.library.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class LibraryAPI {

	private final static String TAG = LibraryAPI.class.getName();
	private static LibraryAPI instance = null;
	private URL url;

	private LibraryRootAPI rootAPI = null;

	private LibraryAPI(Context context) throws IOException,
			LibraryAndroidException {
		super();

		AssetManager assetManager = context.getAssets();
		Properties config = new Properties();
		config.load(assetManager.open("config.properties"));
		String serverAddress = config.getProperty("server.address");
		String serverPort = config.getProperty("server.port");
		url = new URL("http://" + serverAddress + ":" + serverPort
				+ "/library-api");

		Log.d("LINKS", url.toString());
		getRootAPI();
	}

	public final static LibraryAPI getInstance(Context context)
			throws LibraryAndroidException {
		if (instance == null)
			try {
				instance = new LibraryAPI(context);
			} catch (IOException e) {
				throw new LibraryAndroidException(
						"Can't load configuration file");
			}
		return instance;
	}

	private void getRootAPI() throws LibraryAndroidException {
		Log.d(TAG, "getRootAPI()");
		rootAPI = new LibraryRootAPI();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't connect to Library API Web Service");
		}

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, rootAPI.getLinks());
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't get response from Library API Web Service");
		} catch (JSONException e) {
			throw new LibraryAndroidException("Error parsing Library Root API");
		}

	}

	private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
			throws LibraryAndroidException, JSONException {
		for (int i = 0; i < jsonLinks.length(); i++) {
			Link link = SimpleLinkHeaderParser
					.parseLink(jsonLinks.getString(i));
			String rel = link.getParameters().get("rel");
			String rels[] = rel.split("\\s");
			for (String s : rels)
				map.put(s, link);
		}
	}

	public BookCollection getBooks() throws LibraryAndroidException {

		Log.d(TAG, "getBooks()");
		BookCollection books = new BookCollection();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
					.get("books").getTarget()).openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't connect to Beeter API Web Service");
		}
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, books.getLinks());

			books.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
			books.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
			JSONArray jsonBooks = jsonObject.getJSONArray("books");
			for (int i = 0; i < jsonBooks.length(); i++) {
				Book book = new Book();
				JSONObject jsonBook = jsonBooks.getJSONObject(i);
				book.setIdbook(jsonBook.getString("idbook"));
				book.setAuthor(jsonBook.getString("author"));
				book.setTitle(jsonBook.getString("title"));
				book.setLenguage(jsonBook.getString("lenguage"));
				book.setEditorial(jsonBook.getString("editorial"));
				book.setEdition_date(jsonBook.getLong("edition_date"));
				book.setPrint_date(jsonBook.getLong("print_date"));
				jsonLinks = jsonBook.getJSONArray("links");
				parseLinks(jsonLinks, book.getLinks());
				books.getBooks().add(book);
			}
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new LibraryAndroidException("Error parsing Beeter Root API");
		}

		return books;

	}

	public BookCollection getBooksSearch(String urlBook) throws LibraryAndroidException {
		BookCollection books = new BookCollection();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlBook);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, books.getLinks());

			books.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
			books.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
			JSONArray jsonBooks = jsonObject.getJSONArray("books");
			for (int i = 0; i < jsonBooks.length(); i++) {
				Book book = new Book();
				JSONObject jsonBook = jsonBooks.getJSONObject(i);
				book.setIdbook(jsonBook.getString("idbook"));
				book.setAuthor(jsonBook.getString("author"));
				book.setTitle(jsonBook.getString("title"));
				book.setLenguage(jsonBook.getString("lenguage"));
				book.setEditorial(jsonBook.getString("editorial"));
				book.setEdition_date(jsonBook.getLong("edition_date"));
				book.setPrint_date(jsonBook.getLong("print_date"));
				jsonLinks = jsonBook.getJSONArray("links");
				parseLinks(jsonLinks, book.getLinks());
				books.getBooks().add(book);
			}
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException(
					"Exception when getting the sting");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Exception parsing response");
		}

		return books;
	}
	
	public Book getBook(String urlBook) throws LibraryAndroidException {
		Book book = new Book();

		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlBook);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonBook = new JSONObject(sb.toString());
			book.setIdbook(jsonBook.getString("idbook"));
			book.setTitle(jsonBook.getString("title"));
			book.setAuthor(jsonBook.getString("author"));
			book.setLenguage(jsonBook.getString("lenguage"));
			book.setEdition_date(jsonBook.getLong("edition_date"));
			book.setPrint_date(jsonBook.getLong("print_date"));
			book.setEditorial(jsonBook.getString("editorial"));
			JSONArray jsonLinks = jsonBook.getJSONArray("links");
			parseLinks(jsonLinks, book.getLinks());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException(
					"Exception when getting the sting");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Exception parsing response");
		}

		return book;
	}

	public ReviewCollection getReviews(String urlReviews) throws LibraryAndroidException {

		Log.d(TAG, "getReviews()");
		ReviewCollection reviews = new ReviewCollection();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlReviews);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, reviews.getLinks());

			reviews.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
			reviews.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
			JSONArray jsonReviews = jsonObject.getJSONArray("reviews");
			for (int i = 0; i < jsonReviews.length(); i++) {
				Review review = new Review();
				JSONObject jsonReview = jsonReviews.getJSONObject(i);
				review.setIdreview(jsonReview.getString("idreview"));
				review.setIdbook(jsonReview.getString("idbook"));
				review.setUsername(jsonReview.getString("username"));
				review.setText(jsonReview.getString("text"));
				review.setDate_review(jsonReview.getLong("date_review"));
				jsonLinks = jsonReview.getJSONArray("links");
				parseLinks(jsonLinks, review.getLinks());
				reviews.getReviews().add(review);
			}
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't get response from Library API Web Service");
		} catch (JSONException e) {
			throw new LibraryAndroidException("Error parsing Library Root API");
		}

		return reviews;
	}
	
	public Review getReview(String urlSting) throws LibraryAndroidException {
		Review review = new Review();

		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlSting);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonReview = new JSONObject(sb.toString());
			review.setIdreview(jsonReview.getString("idreview"));
			review.setIdbook(jsonReview.getString("idbook"));
			review.setUsername(jsonReview.getString("username"));
			review.setText(jsonReview.getString("text"));
			review.setDate_review(jsonReview.getLong("date_review"));
			JSONArray jsonLinks = jsonReview.getJSONArray("links");
			parseLinks(jsonLinks, review.getLinks());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException(
					"Exception when getting the sting");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Exception parsing response");
		}

		return review;
	}
	
	public Review createReview(String text, String url) throws LibraryAndroidException {
		Review review = new Review();
		review.setText(text);
		HttpURLConnection urlConnection = null;
		try {
			JSONObject jsonReview = createJsonReview(review);
			URL urlPostStings = new URL(url);
			urlConnection = (HttpURLConnection) urlPostStings.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.LIBRARY_API_REVIEW);
			urlConnection.setRequestProperty("Content-Type",
					MediaType.LIBRARY_API_REVIEW);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			PrintWriter writer = new PrintWriter(
					urlConnection.getOutputStream());
			writer.println(jsonReview.toString());
			writer.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			jsonReview = new JSONObject(sb.toString());
			review.setIdreview(jsonReview.getString("idreview"));
			review.setIdbook(jsonReview.getString("idbook"));
			review.setUsername(jsonReview.getString("username"));
			review.setText(jsonReview.getString("text"));
			review.setDate_review(jsonReview.getLong("date_review"));
			JSONArray jsonLinks = jsonReview.getJSONArray("links");
			parseLinks(jsonLinks, review.getLinks());
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Error parsing response");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Error getting response");
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return review;
	}
	 
	private JSONObject createJsonReview(Review review) throws JSONException {
		JSONObject jsonSting = new JSONObject();
		jsonSting.put("text", review.getText());
	 
		return jsonSting;
	}
}

