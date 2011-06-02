
package edu.fsu.cs.contextprovider;

import java.util.Timer;
import java.util.TimerTask;

import edu.fsu.cs.contextprovider.data.ContextConstants;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class ContextAccuracyActivity extends Activity implements View.OnClickListener {
	SeekBar locationBar = null;
	SeekBar movementBar = null;
	SeekBar weatherBar = null;
	SeekBar socialBar = null;
	SeekBar systemBar = null;
	SeekBar derivedBar = null;
	
	Button submitBtn = null;
	Button resetBtn = null;
	
	final int INDEX_LOCATION = 1;
	final int INDEX_MOVEMENT = 2;
	final int INDEX_WEATHER = 3;
	final int INDEX_SOCIAL = 4;
	final int INDEX_SYSTEM = 5;
	final int INDEX_DERIVED = 6;
	
//	private PowerManager.WakeLock wakelock;
    private static Timer timer = new Timer(); 
    private long DISMISS_TIMEOUT = 30;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "ContextAccuracyActivity");
		
		setContentView(R.layout.accuracy);

		locationBar = (SeekBar) findViewById(R.id.location);
		movementBar = (SeekBar) findViewById(R.id.movement);
		weatherBar = (SeekBar) findViewById(R.id.weather);
		socialBar = (SeekBar) findViewById(R.id.social);
		systemBar = (SeekBar) findViewById(R.id.system);
		derivedBar = (SeekBar) findViewById(R.id.derived);
		
		submitBtn = (Button) findViewById(R.id.SubmitButton);
		resetBtn = (Button) findViewById(R.id.ResetButton);
		
		submitBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
		
		resetBars();
		
        timer.schedule(new ContextDismissTask(), (DISMISS_TIMEOUT*1000));

	}
	
//	@Override
//	protected void onPause() {
//		super.onPause();
////		wakelock.release();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
////		wakelock.acquire();
//	}
	
		

	private void initBar(SeekBar bar, final int stream) {
		bar.setMax(10);
		bar.setProgress(10);

		bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
			}

			public void onStartTrackingTouch(SeekBar bar) {
			}

			public void onStopTrackingTouch(SeekBar bar) {
			}
		});
	}
	
	
	private void resetBars() {
		initBar(locationBar, INDEX_LOCATION);
		initBar(movementBar, INDEX_LOCATION);
		initBar(weatherBar, INDEX_LOCATION);
		initBar(socialBar, INDEX_LOCATION);
		initBar(systemBar, INDEX_LOCATION);
		initBar(derivedBar, INDEX_LOCATION);
	}


	@Override
	public void onClick(View v) {
			if (v == resetBtn) {
				Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();		
				resetBars();
			} else if (v == submitBtn) {
				Toast.makeText(this, "Submit", Toast.LENGTH_SHORT).show();
				finish();
			}
	}
	
	
    private class ContextDismissTask extends TimerTask
    { 
        public void run() 
        {
        	Intent intent = new Intent(ContextConstants.CONTEXT_STORE_INTENT);
        	intent.putExtra(ContextConstants.LOCATION_ACCURATE, true);
        	intent.putExtra(ContextConstants.MOVEMENT_ACCURATE, true);
      		intent.putExtra(ContextConstants.WEATHER_ACCURATE, true);
     		intent.putExtra(ContextConstants.SOCIAL_ACCURATE, true);
        	intent.putExtra(ContextConstants.SYSTEM_ACCURATE, true);
        	intent.putExtra(ContextConstants.DERIVED_ACCURATE, true);
        	sendBroadcast(intent);
        	
        	finish();
        }
    }   




	
	
}
