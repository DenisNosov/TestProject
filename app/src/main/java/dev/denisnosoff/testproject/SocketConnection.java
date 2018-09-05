package dev.denisnosoff.testproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

public class SocketConnection implements ClientWebSocket.MessageListener {

	private static final String TAG = "SocketConnection";

	public static final String SOCKET_URL = "ws://31b43bdd.ngrok.io/feed/";

	public ClientWebSocket clientWebSocket;
	private Context context;
	public Gson gson = new Gson();
	private Handler socketConnectionHandler;

	private Runnable checkConnectionRunnable = () -> {
		if (!clientWebSocket.getConnection().isOpen()) {
			Log.d(TAG, ": connection not opened");
			openConnection();
		}
		startCheckConnection();
	};

	private void startCheckConnection() {
		socketConnectionHandler.postDelayed(checkConnectionRunnable, 5000);
	}

	private void stopCheckConnection() {
		socketConnectionHandler.removeCallbacks(checkConnectionRunnable);
	}

	public SocketConnection(Context context) {
		this.context = context;
		socketConnectionHandler = new Handler();
		Log.d(TAG, "SocketConnection: socketconnection created");
	}

	public void openConnection() {
		Log.d(TAG, "openConnection: opening connection");
		if (clientWebSocket != null) clientWebSocket.close();
		try {
			clientWebSocket = new ClientWebSocket(this,
					SOCKET_URL);
			clientWebSocket.connect();
			Log.d(TAG, "openConnection: connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		initScreenStateListener();
		startCheckConnection();
	}

	public void closeConnection() {
		if (clientWebSocket != null) {
			clientWebSocket.close();
			clientWebSocket = null;
		}
		releaseScreenStateListener();
		stopCheckConnection();
	}


	@Override
	public void onSocketMessage(String message) {
		EventBus.getDefault().post(message);
	}

	/**
	 * Screen state listener for socket live cycle
	 */
	private void initScreenStateListener() {
		context.registerReceiver(screenStateReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
		context.registerReceiver(screenStateReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}

	private void releaseScreenStateListener() {
		try {
			context.unregisterReceiver(screenStateReceiver);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private BroadcastReceiver screenStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				Log.i("Websocket", "Screen ON");
				openConnection();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				Log.i("Websocket", "Screen OFF");
				closeConnection();
			}
		}
	};

	public boolean isConnected() {
		return clientWebSocket != null &&
				clientWebSocket.getConnection() != null &&
				clientWebSocket.getConnection().isOpen();
	}
}
