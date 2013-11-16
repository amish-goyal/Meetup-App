package com.google.cloud.backend.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TimePicker;

public class TimeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);

		TimePicker timePicker = (TimePicker) findViewById(R.id.pickTime);
		Intent intent = getIntent();
		if(intent.hasExtra("groupName")) {
			EditText group = (EditText) findViewById(R.id.groupName);
			group.setText(intent.getStringExtra("groupName"));
			group.setEnabled(false);
//			TextView groupT = new TextView(this);
//			groupT.setText(intent.getStringExtra("groupName"));
//			groupT.setLayoutParams(group.getLayoutParams());
		}
		if(intent.hasExtra("time")) {
			String[] time = intent.getStringExtra("time").split(":");
			int hour = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);
			timePicker.setCurrentHour(hour);
			timePicker.setCurrentMinute(minute);
		}
		
		RelativeLayout rlpT = (RelativeLayout) findViewById(R.id.timelayout);
		if(intent.hasExtra("time")) {
			RelativeLayout.LayoutParams rlpBtn = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			Button doneBtn = new Button(this);
			doneBtn.setTextColor(Color.parseColor("#FFFFFF"));
			doneBtn.setBackgroundColor(R.drawable.button1);
			doneBtn.setBackgroundResource(R.drawable.back);
			rlpBtn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			doneBtn.setText(R.string.done);
			doneBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(TimeActivity.this, ChangeActivity.class);
					String t = getTime(v);
					if(!getIntent().getStringExtra("time").equals(t)) {
						intent.putExtra("time",getTime(v));
						intent.putExtra("changedEvent", true);
						intent.putExtra("location", getIntent().getStringExtra("location"));
						intent.putExtra("groupName", intent.getStringExtra("groupName"));
					}
					else {
						intent.putExtras(getIntent());
					}
					startActivity(intent);
				}
			});
			doneBtn.setLayoutParams(rlpBtn);
			rlpT.addView(doneBtn);
		}
		else {
			RelativeLayout.LayoutParams rlpBtn = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			Button nextBtn = new Button(this);
			rlpBtn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			//nextBtn.setBackgroundResource(R.drawable.back);
			nextBtn.setTextColor(Color.WHITE);
			nextBtn.setText(R.string.next);
			nextBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(TimeActivity.this, ContactsActivity.class);
					EditText et = (EditText) findViewById(R.id.groupName);
					String group = et.getText().toString();
					intent.putExtra("groupName", group);
					intent.putExtra("location", getIntent().getStringExtra("location"));
					intent.putExtra("time", getTime(v));
					startActivity(intent);
				}
			});
			nextBtn.setLayoutParams(rlpBtn);
			rlpT.addView(nextBtn);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time, menu);
		return true;
	}
	
	//This method is called when the user selects the time 
		public String getTime(View view) {
			TimePicker timePicker = (TimePicker) findViewById(R.id.pickTime);
			int hour = timePicker.getCurrentHour();
			int minute = timePicker.getCurrentMinute();
			Log.d("time",hour+":"+minute);
			return hour+":"+minute;
		}
}
