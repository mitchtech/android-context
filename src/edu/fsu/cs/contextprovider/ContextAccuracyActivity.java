/***
	Copyright (c) 2008-2011 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package edu.fsu.cs.contextprovider;

import android.app.Activity;

import android.os.Bundle;
import android.os.PowerManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.fsu.cs.contextprovider.data.ContextConstants;
import edu.fsu.cs.contextprovider.dialog.*;

public class ContextAccuracyActivity extends ListActivity {
	private static final String[] items = { "Location", "Movement", "Weather",
			"Social", "System", "Derived" };

	private PowerManager.WakeLock wakelock;
    private static Timer timer = new Timer(); 
    private long DISMISS_TIMEOUT = 30;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
//		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "ContextAccuracyActivity");

		ArrayList<ContextRowModel> list = new ArrayList<ContextRowModel>();

		for (String string : items) {
			list.add(new ContextRowModel(string));
		}

		setListAdapter(new ContextAdapter(list));
        timer.schedule(new ContextDismissTask(), (DISMISS_TIMEOUT*1000));
	}

	@Override
	protected void onPause() {
		super.onPause();
		wakelock.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		wakelock.acquire();
	}

	private ContextRowModel getModel(int position) {
		return (((ContextAdapter) getListAdapter()).getItem(position));
	}

	class ContextAdapter extends ArrayAdapter<ContextRowModel> {
		ContextAdapter(ArrayList<ContextRowModel> list) {
			super(ContextAccuracyActivity.this, R.layout.accuracyrow, R.id.label, list);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);
			ContextViewHolder holder = (ContextViewHolder) row.getTag();

			if (holder == null) {
				holder = new ContextViewHolder(row);
				row.setTag(holder);

				// RatingBar.OnRatingBarChangeListener l = new
				// RatingBar.OnRatingBarChangeListener() {
				// public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
				// Integer myPosition = (Integer) ratingBar.getTag();
				// RowModel model = getModel(myPosition);
				// model.rating = rating;
				// holder.rate.setOnRatingBarChangeListener(l);

				RadioButton.OnClickListener myOptionOnClickListener = new RadioButton.OnClickListener() {

					public void onClick(View v) {

						Integer myPosition = (Integer) v.getTag();
						ContextRowModel model = getModel(myPosition);
						Toast.makeText(ContextAccuracyActivity.this, "Option 1 : " + "\n", Toast.LENGTH_LONG).show();
					}
				};
			}

			ContextRowModel model = getModel(position);

			return (row);
		}

	}

	
	class ContextRowModel {
		String label;
		Boolean accurate = true;

		ContextRowModel(String label) {
			this.label = label;
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