/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.fsu.cs.contextprovider;

import java.util.Timer;
import java.util.TimerTask;

import edu.fsu.cs.contextprovider.data.ContextConstants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class DialogActivity extends Activity {
	
    private static Timer timer = new Timer(); 
    private long DISMISS_TIMEOUT = 30;
    	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.dialog_activity);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
        timer.schedule(new ContextDismissTask(), (DISMISS_TIMEOUT*1000));
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
