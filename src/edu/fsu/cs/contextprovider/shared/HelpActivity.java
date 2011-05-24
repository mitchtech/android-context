package edu.fsu.cs.contextprovider.shared;

import edu.fsu.cs.contextprovider.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		WebView wv = (WebView) findViewById(R.id.helpWebView);
		wv.loadUrl("file:///android_asset/help.html");
	}
}