/***
	Copyright (c) 2008-2011 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package edu.fsu.cs.contextprovider.dialog;

import edu.fsu.cs.contextprovider.R;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class ContextViewHolder {
	RadioButton rb1;
	RadioButton rb2;
	TextView contextText;
	
	Boolean accurate;
	
	public ContextViewHolder(View base) {
		this.rb1 = (RadioButton) base.findViewById(R.id.radioYes);
		this.rb2 = (RadioButton) base.findViewById(R.id.radioNo);
		this.contextText = (TextView) base.findViewById(R.id.currentContext);
		
		if(rb1.isChecked() == true) {
			accurate = true;
		} else if(rb2.isChecked() == true) {
			accurate = false;
		}
	}
}
