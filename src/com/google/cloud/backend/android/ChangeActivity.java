package com.google.cloud.backend.android;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChangeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change);
		
		Button locBtn = (Button) findViewById(R.id.locationBtn);
		Intent intent = getIntent();
		locBtn.setText(intent.getStringExtra("location"));//change this to name of location for event
		locBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChangeActivity.this, LocationActivity.class);
				intent.putExtras(getIntent());
				startActivity(intent);
			}
		});
		
		Button timeBtn = (Button) findViewById(R.id.timeBtn);
		final String sTime = convertTime(intent);
		timeBtn.setText(sTime);//change this to the time of the event
		timeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChangeActivity.this, TimeActivity.class);
				intent.putExtras(getIntent());
				startActivity(intent);
			}
		});
		
		Button done = (Button) findViewById(R.id.doneBtn);
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChangeActivity.this, MessageActivity.class);
				Intent prev = getIntent();
				if(prev.hasExtra("changedEvent")) {
					String s = "";
					s += intent.getStringExtra("changeRequest")+" <Meeting at ";
					s += prev.getStringExtra("location")+" ";
					s += sTime+">";
					prev.putExtra("changeRequest", s);
					prev.putExtra("changedEvent", true);
				}
				prev.putExtra("location", prev.getStringExtra("location"));
				prev.putExtra("time", prev.getStringExtra("time"));
				prev.putExtra("groupName", prev.getStringExtra("groupName"));
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change, menu);
		return true;
	}

	public static String convertTime(Intent intent) {
		String[] time = intent.getStringExtra("time").split(":");
		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1]);
		String standardTime = "";
		String min = "";
		if(minute < 10) {
			min = "0"+minute;
		}
		else
			min = ""+minute;
		if(hour == 0) {
			standardTime += "12:"+min+" am";
		}
		else if(hour == 12) {
			standardTime += "12:"+min+" pm";
		}
		else if(hour > 12) {
			standardTime += ""+hour%12+":"+min+" pm";
		}
		else {
			standardTime += ""+hour+":"+min+" am";
		}
		return standardTime;
	}
}
