package com.google.cloud.backend.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConversationsActivity extends CloudBackendActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversations);

		// Handler for broadcast messages
		CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
			@Override
			public void onComplete(List<CloudEntity> messages) {
				CloudEntity ce = messages.get(0);
				if((String)ce.get("user") != getUsername()){

					NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(getApplicationContext())
					.setSmallIcon(R.drawable.icon1) // notification icon
					.setContentTitle((String)ce.get("groupName"))
					.setContentText((String)ce.get("location") + " @ " + ce.get("time")) // message for notification
					.setAutoCancel(true); // clear notification after click
					// TODO: Check if all items are being passed in the Intent
					Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
					intent.putExtra("contacts", (String)ce.get("contacts"));
					intent.putExtra("location", (String)ce.get("location"));
					intent.putExtra("time", (String)ce.get("time"));
					intent.putExtra("user", (String)ce.get("user"));
					intent.putExtra("eventID", (String)ce.get("eventID"));
					PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
					mBuilder.setContentIntent(pi);
					NotificationManager mNotificationManager =
							(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					mNotificationManager.notify(((String)ce.get("eventID")).hashCode(), mBuilder.build());
				}
			}
		};

		// Subscribe the messages with my username
		getCloudBackend().subscribeToCloudMessage(getUsername(), handler, 50);

		/*
		// Test cloud message to set off a notification
		CloudEntity cm = getCloudBackend().createCloudMessage(getUsername());
		// TODO: Check what all stuff should be passed
		cm.put("groupName", "Social Computing Group");
		cm.put("location", "Klaus");
		cm.put("time", "18:06");
		cm.put("user", "vinaykola@gmail.com");
		cm.put("contacts", "vinaykola@gmail.com;amish1804@gmail.com");
		cm.put("eventID", "vinaykola@gmail.com18:06");
		getCloudBackend().sendCloudMessage(cm);
		 */
		try {
			AssetManager as = this.getAssets();
			InputStream is = as.open("conversations.txt");
			Scanner scan = new Scanner(is);
			//accessing relative layout defined in xml
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.conversationLayout);

			ImageButton button = (ImageButton) findViewById(R.id.btnAdd);
			button.setOnClickListener( new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ConversationsActivity.this, LocationActivity.class);
					//intent.putExtra("groupname", button.)
					//There is no groupName here and therefore nothing is sent!
					startActivity(intent);
				}
			});

			int i=0;
			while(scan.hasNext()) {
				//creating buttons dynamically
				TextView t = new TextView(this);
				final String groupName = scan.nextLine();
				t.setText(groupName);
				t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				t.setTextColor(Color.WHITE);
				t.setBackgroundResource(R.drawable.back);
				t.setBackgroundColor(R.drawable.button1);
				t.setClickable(true);
				t.setId(4000 + i);
				t.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						//calling time choosing activity and passing location
						Intent intent = new Intent(ConversationsActivity.this, MessageActivity.class);
						intent.putExtra("groupName", groupName); //adds groupName to be passed through
						startActivity(intent);
					}
				});

				if (i == 0) {
					RelativeLayout.LayoutParams rlpB = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					//setting first button below text view
					rlpB.addRule(RelativeLayout.BELOW, R.id.rlpCAdd);
					t.setLayoutParams(rlpB);
					rl.addView(t);
				} else {
					RelativeLayout.LayoutParams rlpB = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					//setting buttons to come one after another
					rlpB.addRule(RelativeLayout.BELOW, t.getId() - 1);
					t.setLayoutParams(rlpB);
					rl.addView(t);
				}
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getUsername(){
		AccountManager manager = AccountManager.get(this); 
		Account[] accounts = manager.getAccountsByType("com.google"); 
		List<String> possibleEmails = new LinkedList<String>();

		for (Account account : accounts) {
			possibleEmails.add(account.name);
		}
		if(possibleEmails.size() != 0){
			return possibleEmails.get(0);
		}
		else{
			return "avd@gmail.com"; // Fallback if there's no account associated
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversations, menu);
		return true;
	}

}
