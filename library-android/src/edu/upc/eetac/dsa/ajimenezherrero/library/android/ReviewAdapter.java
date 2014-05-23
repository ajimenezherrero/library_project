package edu.upc.eetac.dsa.ajimenezherrero.library.android;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.upc.eetac.dsa.ajimenezherrero.library.android.api.Review;

public class ReviewAdapter  extends BaseAdapter {
	
	private ArrayList<Review> data;
	private LayoutInflater inflater;
	
	public ReviewAdapter(Context context, ArrayList<Review> data) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
	}
	private static class ViewHolder {
		TextView tvUsername;
		TextView tvText;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(((Review)getItem(position)).getIdreview());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_review, null);
			viewHolder = new ViewHolder();
			viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
			viewHolder.tvText = (TextView) convertView.findViewById(R.id.tvText);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String username = data.get(position).getUsername();
		String text = data.get(position).getText();
		viewHolder.tvUsername.setText(username);
		viewHolder.tvText.setText(text);
		return convertView;
	}
}
