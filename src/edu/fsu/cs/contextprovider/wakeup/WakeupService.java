/***
	Copyright (c) 2008-2011 CommonsWare, LLC
	Licensed under the Apache License, Version 2.0 (the "License"); you may not
	use this file except in compliance with the License. You may obtain	a copy
	of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
	by applicable law or agreed to in writing, software distributed under the
	License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
	OF ANY KIND, either express or implied. See the License for the specific
	language governing permissions and limitations under the License.
	
	From _The Busy Coder's Guide to Advanced Android Development_
		http://commonsware.com/AdvAndroid
 */

package edu.fsu.cs.contextprovider.wakeup;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class WakeupService extends WakefulIntentService {
	private static final String TAG = "WakeupService";

	public WakeupService() {
		super("AppService");
	}

	@Override
	protected void doWakefulWork(Intent intent) {
		Intent accuracyIntent = new Intent(getApplicationContext(), edu.fsu.cs.contextprovider.ContextAccuracyActivity.class);
		accuracyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(accuracyIntent);
	}
}
