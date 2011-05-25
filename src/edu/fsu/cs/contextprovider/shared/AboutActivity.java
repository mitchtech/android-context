package edu.fsu.cs.contextprovider.shared;

import java.util.LinkedList;

import edu.fsu.cs.contextprovider.R;
import edu.fsu.cs.contextprovider.shared.AboutActivity.AboutAdapter.AboutItem;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends ListActivity {
	private AboutAdapter aboutAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.about);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.abouttitle);

		TextView versionTextView = (TextView) findViewById(R.id.version);
		try {
			versionTextView.setText(getString(R.string.version) + " " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
		}

		aboutAdapter = new AboutAdapter(this);

		generateDevelopmentItems();
		generateMoreInfoItems();

		setListAdapter(aboutAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		AboutItem item = aboutAdapter.getAboutItem(position);
		final Intent actionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getAction()));
		try {
			startActivity(actionIntent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, R.string.error_email, Toast.LENGTH_LONG).show();
		}
	}

	private void generateDevelopmentItems() {
		aboutAdapter.addSeparator(R.string.development);
		aboutAdapter.addItem("Bills Books", "info@mitchtech.net", "mailto:info@mitchtech.net", R.drawable.context64);
	}

	private void generateMoreInfoItems() {
		aboutAdapter.addSeparator(R.string.more_info);
		aboutAdapter.addItem(getString(R.string.project_webpage), "http://mitchtech.net/", "http://mitchtech.net/", R.drawable.context64);
	}

	public class AboutAdapter extends BaseAdapter implements ListAdapter {
		private Context context;
		private LinkedList<AboutItem> items;

		public AboutAdapter(Context context) {
			this.context = context;
			items = new LinkedList<AboutItem>();
		}

		public int getCount() {
			return items.size();
		}

		public void addItem(String title, String description, String action, int icon) {
			items.add(new AboutItem(title, description, action, icon, false));
		}

		public void addSeparator(int title) {
			items.add(new AboutItem(getString(title), null, null, 0, true));
		}

		public Object getItem(int position) {
			return position;
		}

		public AboutItem getAboutItem(int position) {
			return items.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			AboutItem item = items.get(position);
			if (item.isSeparator()) {
				View aboutView = LayoutInflater.from(context).inflate(R.layout.aboutseparator, null);
				TextView titleTextView = (TextView) aboutView.findViewById(R.id.title);
				titleTextView.setText(item.getTitle());
				return aboutView;
			}

			View aboutView = LayoutInflater.from(context).inflate(R.layout.aboutitem, null);

			TextView titleTextView = (TextView) aboutView.findViewById(R.id.title);
			titleTextView.setText(item.getTitle());
			TextView detailsTextView = (TextView) aboutView.findViewById(R.id.details);
			detailsTextView.setText(item.getDescription());
			if (item.getIcon() > 0) {
				ImageView iconImageView = (ImageView) aboutView.findViewById(R.id.icon);
				iconImageView.setImageResource(item.getIcon());
			}

			return aboutView;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return !items.get(position).isSeparator() && items.get(position).getAction() != null;
		}

		public class AboutItem {
			private String title;
			private String description;
			private String action;
			private int icon;
			private boolean separator;

			public AboutItem(String title, String description, String action, int icon, boolean separator) {
				this.title = title;
				this.description = description;
				this.action = action;
				this.icon = icon;
				this.separator = separator;
			}

			public String getTitle() {
				return title;
			}

			public String getDescription() {
				return description;
			}

			public String getAction() {
				return action;
			}

			public int getIcon() {
				return icon;
			}

			public boolean isSeparator() {
				return separator;
			}
		}
	}
}