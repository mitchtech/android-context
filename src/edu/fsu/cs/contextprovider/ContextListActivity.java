package edu.fsu.cs.contextprovider;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContextListActivity extends ListActivity {
	public static boolean running = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vector<ContextListItem> Clist = new Vector<ContextListItem>();
		running = true;

		LinkedHashMap<String, String> cntx = ContextProvider.getAll();
		for (LinkedHashMap.Entry<String,String> entry: cntx.entrySet()) {
			ContextListItem item = new ContextListItem();
			item.setName(entry.getKey());
			item.setValue(entry.getValue());
			Clist.add(item);
		}
		
		ListAdapter adapter = new ContextListAdapter(Clist, getBaseContext());
		setListAdapter(adapter);
	}
	
	@Override
	public void onDestroy() {
		running = false;
		super.onDestroy();
	}
	
	private static class ContextListAdapter extends BaseAdapter implements OnLongClickListener {
		private Vector<ContextListItem> Clist = new Vector<ContextListItem>();
		private LayoutInflater mInflater;
		ClipboardManager clip = null;
		Context mContext;	// keep the context to make toasts

		public ContextListAdapter(Vector<ContextListItem> Clist, Context context) {
			this.Clist = Clist;
			mInflater = LayoutInflater.from(context);
			clip = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
			mContext = context;
		}

		@Override
		public int getCount() {
			return Clist.size();
		}

		@Override
		public Object getItem(int position) {
			return Clist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row, null);

                holder = new ViewHolder();
                holder.value = (TextView) convertView.findViewById(R.id.value);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(holder);
                convertView.setOnLongClickListener(this);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            ContextListItem item = Clist.get(position);
            holder.title.setText(item.getName());
            holder.value.setText(item.getValue());
            
            holder.index = position;
            return convertView;
		}
		
	    public boolean onLongClick(View v) {
	    	ViewHolder holder = (ViewHolder)v.getTag();
	    	ContextListItem item = Clist.get(holder.index);
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
	}
}
