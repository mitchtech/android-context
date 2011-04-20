package edu.fsu.cs.contextprovider.dialog;

import edu.fsu.cs.contextprovider.ContextConstants;
import edu.fsu.cs.contextprovider.R;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddressDialog {

	public static class add extends Dialog implements OnClickListener {
		SharedPreferences pref = null;
		Button saveButton;
		EditText addressEdit = null;
		
		private void addInit(Context context) {
			pref = context.getSharedPreferences(ContextConstants.PREFS_ADDRESS, 0);
			/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			/** Design the dialog in main.xml file */
			setContentView(R.layout.dialog_location_add);
			saveButton = (Button) findViewById(R.id.saveButton);
			addressEdit = (EditText) findViewById(R.id.addressEdit);
			saveButton.setOnClickListener(this);
			saveButton.setTag(context);
		}
	
		public add(Context context) {
			super(context);
			addInit(context);
		}
		
		public add(Context context, String address) {
			super(context);
			addInit(context);
			addressEdit.setText(address);
		}


		@Override
		public void onClick(View v) {
			EditText nicknameEdit = (EditText) findViewById(R.id.nicknameEdit);
			EditText addressEdit = (EditText) findViewById(R.id.addressEdit);
			String nickname = nicknameEdit.getText().toString();
			String address = addressEdit.getText().toString();

			if (nickname == null || nickname.equals("")) {
				nickname = address;
			}
			
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(nickname, address);
			editor.commit();

			dismiss();
		}


	}
}
