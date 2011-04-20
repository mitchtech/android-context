/**
 * Mike: Look at the refresh function and the onOptionsItemSelected() ADD_ID case.
 * Those are the only substantial differences between this code and ListActivity.
 * Please integrate ADD_ID into a context click  of "Proximity"
 * 
 */

package edu.fsu.cs.contextprovider;

import java.util.Locale;
import java.util.Map;

import edu.fsu.cs.contextprovider.dialog.AddressDialog;
import edu.fsu.cs.contextprovider.monitor.LocationMonitor;
import edu.fsu.cs.contextprovider.monitor.MovementMonitor;
import edu.fsu.cs.contextprovider.rpc.ContextProviderService;
import edu.fsu.cs.contextprovider.rpc.IContextProviderService;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContextProximityActivity extends ListActivity {
	private static final int REFRESH_ID = Menu.FIRST + 1;
	private static final int ADD_ID = Menu.FIRST + 2;
	public static boolean running = false;
	private static IContextProviderService mService = null;

	private ServiceConnection conn = new ServiceConnection(){
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IContextProviderService.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName name) {	
		}
	};
	ArrayAdapter<ContextListItem> addressAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		running = true;
		
		Intent intent = null;
		/* Start GPS Service */
		intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.GPSService.class);
		startService(intent);

		/* Start Network Service */
		intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.NetworkService.class);
		startService(intent);

		/* Start Accelerometer Service */
		intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.AccelerometerService.class);
		startService(intent);

		/* Start movement context */
		MovementMonitor.StartThread(5);

		/* Start LocationMonitor */
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		LocationMonitor.StartThread(5, geocoder);

		/* Start System/Phone/SMS State Monitor Services */
		intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.TelephonyService.class);
		startService(intent);

		/* Start ContextProviderService */
		bindService(new Intent(this, ContextProviderService.class), conn, Context.BIND_AUTO_CREATE);

		
		addressAdapter = new ContextListAdapter(getBaseContext(), R.layout.row);
		setListAdapter(addressAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, REFRESH_ID, Menu.NONE, "Refresh").setIcon(R.drawable.add).setAlphabeticShortcut('a');
		menu.add(Menu.NONE, ADD_ID, Menu.NONE, "Add").setIcon(R.drawable.add).setAlphabeticShortcut('a');

		return (super.onCreateOptionsMenu(menu));
	}


	private void refreshAddress() {
		addressAdapter.clear();
		SharedPreferences pref = getSharedPreferences(ContextConstants.PREFS_ADDRESS, 0);
		Map<String, String> list = (Map<String, String>)pref.getAll();
		for (Map.Entry<String,String> entry: list.entrySet()) {
			ContextListItem item = new ContextListItem();
			item.setName(entry.getKey());
			double proximity = 0;
			try {
				proximity = mService.proximityToAddress(entry.getValue());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			item.setValue(String.valueOf(proximity));
			addressAdapter.add(item);
		}
		addressAdapter.notifyDataSetChanged();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case REFRESH_ID:
			refreshAddress();
			//adapter.notifyDataSetChanged();
			return true;
		case ADD_ID:
			Context context = getApplicationContext();
			String address = null;
			try {
				address = mService.getCurrentAddress();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AddressDialog.add addAddressDialog = new AddressDialog.add(this, address);
			addAddressDialog.show();
			this.refreshAddress();
		}
		return (super.onOptionsItemSelected(item));
	}
	
	
	
	private static class ContextListAdapter extends ArrayAdapter<ContextListItem> implements OnLongClickListener {
		private LayoutInflater mInflater;
		ClipboardManager clip = null;
		Context mContext;	// keep the context to make toasts
		int row = 0;
		
		public ContextListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);

			mInflater = LayoutInflater.from(context);
			clip = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
			mContext = context;
			row = textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(row, null);

				holder = new ViewHolder();
				holder.value = (TextView) convertView.findViewById(R.id.value);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
				convertView.setOnLongClickListener(this);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ContextListItem item = this.getItem(position);
			holder.title.setText(item.getName());
			holder.value.setText(item.getValue());

			holder.index = position;
			return convertView;
		}

		public boolean onLongClick(View v) {
			ViewHolder holder = (ViewHolder)v.getTag();
			ContextListItem item = getItem(holder.index);
			clip.setText(item.getValue());
			Toast.makeText(mContext, item.getName() + "\n\nCopied: [" + item.getValue() + "]", Toast.LENGTH_SHORT).show();
			return true;
		}

		static class ViewHolder {
			TextView title;
			TextView value;
			int index;
		}
	}
	
	private class ContextListItem {
		private String name;
		private String value;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		public String toString() {
			return name + ": " + value;
		}
	}
	
}
