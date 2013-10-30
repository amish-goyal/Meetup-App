package com.google.cloud.backend.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConversationsActivity extends CloudBackendActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversations);

		// Subscribe to messages
		CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
		      @Override
		      public void onComplete(List<CloudEntity> messages) {
		        for (CloudEntity ce : messages) {
		          Toast.makeText(getApplicationContext(), (CharSequence) ce.get("contacts"), Toast.LENGTH_LONG).show();
		          
		          Intent activityChangeIntent = new Intent(ConversationsActivity.this, MessageActivity.class);
		          activityChangeIntent.putExtras(getIntent());//passes location, time, groupname to next intent
					activityChangeIntent.putExtra("contacts", (String)ce.get("contacts"));
					activityChangeIntent.putExtra("location", (String)ce.get("location"));
					activityChangeIntent.putExtra("time", (String)ce.get("time"));
					activityChangeIntent.putExtra("user", (String)ce.get("user"));
					activityChangeIntent.putExtra("eventID", (String)ce.get("eventID"));
					activityChangeIntent.putExtra("newEvent", true);
					startActivity(activityChangeIntent);
		          
		        }
		      }
		    };

		    

		    
		    // receive all posts that includes "#dog" or "#cat" hashtags
		    getCloudBackend().subscribeToCloudMessage(getUsername(), handler, 50);
		
		
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
	    return possibleEmails.get(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversations, menu);
		return true;
	}

}
