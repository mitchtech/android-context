package edu.fsu.cs.contextprovider;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.fsu.cs.contextprovider.rpc.ContextProviderService;
import edu.fsu.cs.contextprovider.rpc.IContextProviderService;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContextListActivity extends ListActivity {
	public static boolean running = false;
	public static IContextProviderService mService = null;
	
	private static final int REFRESH_ID = Menu.FIRST + 1;
	
	Vector<ContextListItem> Clist = new Vector<ContextListItem>();
	ArrayAdapter<ContextListItem> adapter = null;
	
	private ServiceConnection conn = new ServiceConnection(){
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IContextProviderService.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName name) {	
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		running = true;

		/* Start ContextProviderService */
		bindService(new Intent(this,ContextProviderService.class), conn, Context.BIND_AUTO_CREATE);

		//		LinkedHashMap<String, String> cntx = ContextProvider.getAllOrdered();


		adapter = new ContextListAdapter(getBaseContext(), R.layout.row);
		setListAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		running = false;
		super.onDestroy();
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
	
	private void refresh() {
		if (mService == null) {
			return;
		}
		Map<String, String> cntx = null;
		try {
			cntx = (Map<String, String>)mService.getAll();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Map.Entry<String,String> entry: cntx.entrySet()) {
			ContextListItem item = new ContextListItem();
			item.setName(entry.getKey());
			item.setValue(entry.getValue());
			adapter.add(item);
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, REFRESH_ID, Menu.NONE, "Refresh").setIcon(R.drawable.add).setAlphabeticShortcut('a');

		return (super.onCreateOptionsMenu(menu));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case REFRESH_ID:
			refresh();
			adapter.notifyDataSetChanged();
			return true;
		}
		return (super.onOptionsItemSelected(item));
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
	}
}
