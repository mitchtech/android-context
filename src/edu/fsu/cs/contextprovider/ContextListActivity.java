package edu.fsu.cs.contextprovider;

import com.google.ads.*;
import com.google.ads.AdRequest.Gender;

import edu.fsu.cs.contextprovider.monitor.LocationMonitor;

import java.util.LinkedHashMap;
import java.util.Vector;

//import com.adwhirl.AdWhirlLayout;
//import com.adwhirl.AdWhirlManager;
//import com.adwhirl.AdWhirlTargeting;
//import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
//import com.adwhirl.adapters.AdWhirlAdapter;
//import com.adwhirl.util.AdWhirlUtil;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContextListActivity extends Activity {
	public static boolean running = false;
	private static final String MY_AD_UNIT_ID = "a14dab536d4a388";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.context_list);
		
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
		ListView lv = (ListView)findViewById(R.id.contextList);
		lv.setAdapter(adapter);
		
	    AdView adView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);
	    
	    // Lookup your LinearLayout assuming it’s been given
	    // the attribute android:id="@+id/mainLayout"
	    LinearLayout layout = (LinearLayout)findViewById(R.id.rootLayout);
	    // Add the adView to it
	    layout.addView(adView);
	    // Initiate a generic request to load it with an ad
	    AdRequest req = new AdRequest();
//	    req.setTesting(false);
	    req.setBirthday("19851022");
	    req.setGender(AdRequest.Gender.MALE);
//	    req.addKeyword("jenn");
//	    req.addKeyword("sterger");
//	    req.addKeyword("fsu");
//	    Location loc = LocationMonitor.getLocation();
//	    if (loc != null){
//	    	req.setLocation(loc);
//	    }
	  
	    req.addKeyword("hamburger");
//	    req.addKeyword("frenchfries");
//	    req.addKeyword("McDonalds");
	    adView.loadAd(req);
	  
	    

	    
//		/* AdWhirl */
//        int width = 320;
//        int height = 52;
//        
//        String keywords[] = { "big", "tasty" };
//        AdWhirlTargeting.setKeywordSet(new HashSet<String>(Arrays.asList(keywords)));        
//        AdWhirlTargeting.setPostalCode("32301");
//        AdWhirlAdapter.setGoogleAdSenseCompanyName(COMPANY_NAME);
//        AdWhirlAdapter.setGoogleAdSenseAppName(APP_NAME);
//        
//		LinearLayout layout = (LinearLayout) findViewById(R.id.rootLayout);
//        AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "2bd8256fa48849cba30b7e31b2abc77b");
//        adWhirlLayout.setAdWhirlInterface(this);
//
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        float density = displayMetrics.density;
//
//        width = (int) (width * density);
//        height = (int) (height * density);
//        RelativeLayout.LayoutParams adWhirlLayoutParams = new RelativeLayout.LayoutParams(width, height);
//        layout.addView(adWhirlLayout, adWhirlLayoutParams);
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

//	@Override
//	public void adWhirlGeneric() {
//		// TODO Auto-generated method stub
//	    Log.e(AdWhirlUtil.ADWHIRL, "In adWhirlGeneric()");
//	}
}
