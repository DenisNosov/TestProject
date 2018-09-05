package dev.denisnosoff.testproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.denisnosoff.testproject.POJO.Post;


public class PostAdapter extends ArrayAdapter<Post> {

	private Context mContext;
	private int mResource;
	private ArrayList<Post> mObjects;

	public PostAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Post> objects) {
		super(context, resource, objects);
		mObjects = objects;
		mContext = context;
		mResource = resource;
	}

	public void updateList(ArrayList<Post> list) {
		mObjects.clear();
		mObjects.addAll(list);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Nullable
	@Override
	public Post getItem(int position) {
		return mObjects.get(position);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		String text = getItem(position).getFields().getText();
		String time = getItem(position).getFields().getTimestamp();

		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(mResource, parent, false);

		TextView tvText = convertView.findViewById(R.id.text_text_view);
		TextView tvTime = convertView.findViewById(R.id.time_text_view);

		tvText.setText(text);
		tvTime.setText(time);

		return convertView;
	}
}
