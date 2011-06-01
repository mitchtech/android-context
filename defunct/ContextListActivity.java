//package edu.fsu.cs.contextprovider;
//
//import com.google.ads.*;
//import com.google.ads.AdRequest.Gender;
//
//import edu.fsu.cs.contextprovider.monitor.LocationMonitor;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.Vector;
//
//import edu.fsu.cs.contextprovider.rpc.ContextProviderService;
//import edu.fsu.cs.contextprovider.rpc.IContextProviderService;
//
//import android.app.ListActivity;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import java.util.Vector;
//
////import com.adwhirl.AdWhirlLayout;
////import com.adwhirl.AdWhirlManager;
////import com.adwhirl.AdWhirlTargeting;
////import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
////import com.adwhirl.adapters.AdWhirlAdapter;
////import com.adwhirl.util.AdWhirlUtil;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.RemoteException;
//import android.text.ClipboardManager;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnLongClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class ContextListActivity extends Activity {
//	public static boolean running = false;
//	public static IContextProviderService mService = null;
//	
//	private static final int REFRESH_ID = Menu.FIRST + 1;
//	
//	Vector<ContextListItem> Clist = new Vector<ContextListItem>();
//	ArrayAdapter<ContextListItem> adapter = null;
//	private static final String MY_AD_UNIT_ID = "a14dab536d4a388";
//	
//	private ServiceConnection conn = new ServiceConnection(){
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			mService = IContextProviderService.Stub.asInterface(service);
//		}
//
//		public void onServiceDisconnected(ComponentName name) {	
//		}
//	};
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		running = true;
//
//		/* Start ContextProviderService */
//		bindService(new Intent(this,ContextProviderService.class), conn, Context.BIND_AUTO_CREATE);
//
//		//		LinkedHashMap<String, String> cntx = ContextProvider.getAllOrdered();
//
//		setContentView(R.layout.context_list);
//		
//		Vector<ContextListItem> Clist = new Vector<ContextListItem>();
//		running = true;
//
//		LinkedHashMap<String, String> cntx = ContextProvider.getAll();
//		for (LinkedHashMap.Entry<String,String> entry: cntx.entrySet()) {
//			ContextListItem item = new ContextListItem();
//			item.setName(entry.getKey());
//			item.setValue(entry.getValue());
//			Clist.add(item);
//		}
//		
//		ListAdapter adapter = new ContextListAdapter(Clist, getBaseContext());
//		ListView lv = (ListView)findViewById(R.id.contextList);
//		lv.setAdapter(adapter);
//		
//	    AdView adView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);
//	    
//	    // Lookup your LinearLayout assuming it’s been given
//	    // the attribute android:id="@+id/mainLayout"
//	    LinearLayout layout = (LinearLayout)findViewById(R.id.rootLayout);
//	    // Add the adView to it
//	    layout.addView(adView);
//	    // Initiate a generic request to load it with an ad
//	    AdRequest req = new AdRequest();
////	    req.setTesting(false);
//	    req.setBirthday("19851022");
//	    req.setGender(AdRequest.Gender.MALE);
////	    req.addKeyword("jenn");
////	    req.addKeyword("sterger");
////	    req.addKeyword("fsu");
////	    Location loc = LocationMonitor.getLocation();
////	    if (loc != null){
////	    	req.setLocation(loc);
////	    }
//	  
//	    req.addKeyword("hamburger");
////	    req.addKeyword("frenchfries");
////	    req.addKeyword("McDonalds");
//	    adView.loadAd(req);
//	  
//	    
//
//	    
////		/* AdWhirl */
////        int width = 320;
////        int height = 52;
////        
////        String keywords[] = { "big", "tasty" };
////        AdWhirlTargeting.setKeywordSet(new HashSet<String>(Arrays.asList(keywords)));        
////        AdWhirlTargeting.setPostalCode("32301");
////        AdWhirlAdapter.setGoogleAdSenseCompanyName(COMPANY_NAME);
////        AdWhirlAdapter.setGoogleAdSenseAppName(APP_NAME);
////        
////		LinearLayout layout = (LinearLayout) findViewById(R.id.rootLayout);
////        AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "2bd8256fa48849cba30b7e31b2abc77b");
////        adWhirlLayout.setAdWhirlInterface(this);
////
////        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
////        float density = displayMetrics.density;
////
////        width = (int) (width * density);
////        height = (int) (height * density);
////        RelativeLayout.LayoutParams adWhirlLayoutParams = new RelativeLayout.LayoutParams(width, height);
////        layout.addView(adWhirlLayout, adWhirlLayoutParams);
//	}
//
//	@Override
//	public void onDestroy() {
//		running = false;
//		super.onDestroy();
//	}
//
//	private static class ContextListAdapter extends ArrayAdapter<ContextListItem> implements OnLongClickListener {
//		private LayoutInflater mInflater;
//		ClipboardManager clip = null;
//		Context mContext;	// keep the context to make toasts
//		int row = 0;
//		
//		public ContextListAdapter(Context context, int textViewResourceId) {
//			super(context, textViewResourceId);
//
//			mInflater = LayoutInflater.from(context);
//			clip = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
//			mContext = context;
//			row = textViewResourceId;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = mInflater.inflate(row, null);
//
//				holder = new ViewHolder();
//				holder.value = (TextView) convertView.findViewById(R.id.value);
//				holder.title = (TextView) convertView.findViewById(R.id.title);
//				convertView.setTag(holder);
//				convertView.setOnLongClickListener(this);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			ContextListItem item = this.getItem(position);
//			holder.title.setText(item.getName());
//			holder.value.setText(item.getValue());
//
//			holder.index = position;
//			return convertView;
//		}
//
//		public boolean onLongClick(View v) {
//			ViewHolder holder = (ViewHolder)v.getTag();
//			ContextListItem item = getItem(holder.index);
//			clip.setText(item.getValue());
//			Toast.makeText(mContext, item.getName() + "\n\nCopied: [" + item.getValue() + "]", Toast.LENGTH_SHORT).show();
//			return true;
//		}
//
//		static class ViewHolder {
//			TextView title;
//			TextView value;
//			int index;
//		}
//	}
//	
//	private void refresh() {
//		if (mService == null) {
//			return;
//		}
//		Map<String, String> cntx = null;
//		try {
//			cntx = (Map<String, String>)mService.getAll();
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for (Map.Entry<String,String> entry: cntx.entrySet()) {
//			ContextListItem item = new ContextListItem();
//			item.setName(entry.getKey());
//			item.setValue(entry.getValue());
//			adapter.add(item);
//		}
//	}
//
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(Menu.NONE, REFRESH_ID, Menu.NONE, "Refresh").setIcon(R.drawable.add64).setAlphabeticShortcut('a');
//
//		return (super.onCreateOptionsMenu(menu));
//	}
//
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case REFRESH_ID:
//			refresh();
//			adapter.notifyDataSetChanged();
//			return true;
//		}
//		return (super.onOptionsItemSelected(item));
//	}
//
//	public class ContextListItem {
//		private String name;
//		private String value;
//
//		public String getName() {
//			return name;
//		}
//		public void setName(String name) {
//			this.name = name;
//		}
//		public String getValue() {
//			return value;
//		}
//		public void setValue(String value) {
//			this.value = value;
//		}
//	}
//
////	@Override
////	public void adWhirlGeneric() {
////		// TODO Auto-generated method stub
////	    Log.e(AdWhirlUtil.ADWHIRL, "In adWhirlGeneric()");
////	}
//}
