package edu.fsu.cs.contextprovider;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.fsu.cs.contextprovider.data.ContextConstants;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class ContextService extends Service {
	private static final String TAG = "ContextService";

	    private static Timer timer = new Timer(); 
	    private Context ctx;
	    
	    // 5 min = 300 sec
	    // 15 min = 900 sec
	    private long POPUP_FREQ = 45;


	    public IBinder onBind(Intent arg0) 
	    {
	          return null;
	    }

	    public void onCreate() 
	    {
	          super.onCreate();
	          ctx = this; 
	          startService();
	    }

	    private void startService()
	    {           
	        IntentFilter eventFilter = new IntentFilter();
	        // eventFilter.addAction(android.content.Intent.ACTION_BATTERY_CHANGED);
	        eventFilter.addAction(ContextConstants.CONTEXT_STORE_INTENT);
	        registerReceiver(contextIntentReceiver, eventFilter);

	        timer.schedule(new ContextPopupTask(), (POPUP_FREQ*1000));  // seconds * 1000
	    }

	    private class ContextPopupTask extends TimerTask
	    { 
	        public void run() 
	        {
	        	// Random myRandom = new Random();
	        	// long delay = 5000; // + myRandom.nextInt();
	            toastHandler.sendEmptyMessage(0);
	            // toastHandler.sendMessage((Message) String.valueOf(delay));
	            timer.schedule(new ContextPopupTask(), (POPUP_FREQ*1000));  // seconds * 1000
	        }
	    }    
	    

	    public void onDestroy() 
	    {
	          super.onDestroy();
	          Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
	          unregisterReceiver(contextIntentReceiver);
	    }

	    private final Handler toastHandler = new Handler()
	    {
	        @Override
	        public void handleMessage(Message msg)
	        {
	            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
	            
	            Intent intent = new Intent(ctx, edu.fsu.cs.contextprovider.ContextAccuracyActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
	            
	        }
	    };    
	    
		BroadcastReceiver contextIntentReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				      Log.d(TAG, "Received Intent: " + intent.getAction());
				      Toast.makeText(getApplicationContext(), "ContextService:" + "Received Intent: " + intent.getAction(), Toast.LENGTH_SHORT).show();
			}
		};	
	}