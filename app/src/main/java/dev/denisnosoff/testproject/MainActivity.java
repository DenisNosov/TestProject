package dev.denisnosoff.testproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.denisnosoff.testproject.POJO.Post;
import dev.denisnosoff.testproject.POJO.PostList;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";

	private PostAdapter postAdapter;
	private SocketConnection socketConnection;
	private ListView lvPosts;
	private Button btnRefresh;

	private ArrayList<Post> currentPosts;

	private final static Gson gson = new GsonBuilder().create();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvPosts = findViewById(R.id.list_view);
		btnRefresh = findViewById(R.id.refresh_button);
		socketConnection = new SocketConnection(getApplicationContext());
		socketConnection.openConnection();
		EventBus.getDefault().register(this);
		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RealTimeEvent event = new RealTimeEvent("update", currentPosts.get(0).getFields().getTimestamp());
				String json = gson.toJson(event);
				socketConnection.clientWebSocket.ws.sendText(json);
			}
		});
	}

	@Override
	protected void onStop() {
		EventBus.getDefault().unregister(this);
		socketConnection.closeConnection();
		super.onStop();
	}

	@Subscribe
	public void handleRealTimeMessage(String message) {
		Log.d(TAG, "handleRealTimeMessage: " + message);

		try {
			JSONObject object = new JSONObject(message);
			if (!object.isNull("posts")) {
				PostList postList = gson.fromJson(message, PostList.class);
				ArrayList<Post> postArrayList = (ArrayList<Post>) postList.getPosts();
				Collections.reverse(postArrayList);
				currentPosts = postArrayList;
				if (postAdapter != null) {
					runOnUiThread(() -> {
						postAdapter.updateList(postArrayList);
						btnRefresh.setVisibility(View.GONE);
					});
				} else {
					postAdapter = new PostAdapter(this, R.layout.layout_post, postArrayList);
					runOnUiThread(() -> {
						lvPosts.setAdapter(postAdapter);
						btnRefresh.setVisibility(View.GONE);
					});
				}
			}

			if (!object.isNull("haveUpdates")) {
				Log.d(TAG, "handleRealTimeMessage: new update");
				runOnUiThread(() -> btnRefresh.setVisibility(View.VISIBLE));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
