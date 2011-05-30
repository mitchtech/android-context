package edu.fsu.cs.contextprovider;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
	    
	    private long POPUP_FREQ = 300;
	    // 5 min = 300 sec
	    // 15 min = 900 sec

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
	        // timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
	        timer.schedule(new mainTask(), (POPUP_FREQ*1000));  // seconds * 1000
	    }

	    private class mainTask extends TimerTask
	    { 
	        public void run() 
	        {
	        	// Random myRandom = new Random();
	        	// long delay = 5000; // + myRandom.nextInt();
	            toastHandler.sendEmptyMessage(0);
	            // toastHandler.sendMessage((Message) String.valueOf(delay));
	            timer.schedule(new mainTask(), (POPUP_FREQ*1000));  // seconds * 1000
	        }
	    }    

	    public void onDestroy() 
	    {
	          super.onDestroy();
	          Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
	    }

	    private final Handler toastHandler = new Handler()
	    {
	        @Override
	        public void handleMessage(Message msg)
	        {
	            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
//	            startActivity(new Intent(ctx, edu.fsu.cs.contextprovider.DialogActivity.class));
//	            Intent intent = new Intent(ctx, edu.fsu.cs.contextprovider.ContextExpandableListActivity.class);
	            Intent intent = new Intent(ctx, edu.fsu.cs.contextprovider.DialogActivity.class);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
	            
	        }
	    };    
	}