package com.google.cloud.backend.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.google.cloud.backend.android.CloudQuery.Order;
import com.google.cloud.backend.android.CloudQuery.Scope;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends CloudBackendActivity {

	TextView messageHistText;
	private boolean event = false;
	List<CloudEntity> posts = new LinkedList<CloudEntity>();
	// TextView messageHistText;
	Spinner spinner1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		messageHistText = (TextView) findViewById(R.id.messageHist);

		setTitle(getIntent().getStringExtra("groupName"));
		getLocations();
		
//-----------------------------------------------------------------------------------------------------
		//populate this try/catch with the meeting history
		/* try {
			AssetManager am = this.ggetAssets();
			InputStream is = am.open("meetingHistory.txt");
			Scanner scan = new Scanner(is);
			
			while(scan.hasNext()) {
				//only change the scan.nextLine() here! You may not need the while if you are
				//appending bunch of code with \n chars on the end.
				messageHistText.append(scan.nextLine()+"\n");
			}
			*/
			
			Intent prev = getIntent();
			if(prev.hasExtra("changedEvent")) {
				messageHistText.append(prev.getStringExtra("changeRequest"));
				event = true;
			}
			if(prev.hasExtra("newEvent")) {
				//Need to add group to conversation list
				String location = prev.getStringExtra("location");
				//String time = ChangeActivity.convertTime(prev);
				String time = prev.getStringExtra("time");
				String user = getUsername() + ": "; 
				//messageHistText.append(user+"<Meeting at "+location+" "+time+">\n");
				event = true;
			}
		//event = true;
		
		ImageButton addButton = (ImageButton) findViewById(R.id.btnAdd);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MessageActivity.this, LocationActivity.class);
				intent.putExtras(getIntent()); //This passes the groupName only to the next activity
				startActivity(intent);	
			}
		});
		if(event) { addButton.setEnabled(false); }
		if(event) {
			
			// Create the Spinner
			
			
			//addItemsOnSpinner1();
			addListenerOnButton();
			addListenerOnSpinnerItemSelection();
			
			
			Button yesButton = (Button) findViewById(R.id.btnYes);
			yesButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//--------------------------------------------------------------------------------------------------------------
					
					//need to record in messageHistory
					TextView messageHistText = (TextView) findViewById(R.id.messageHist);
					messageHistText.append(getUsername() + " Yes\n"); 
					
				}
			});
			Button noButton = (Button) findViewById(R.id.btnNo);
			noButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*
//---------------------------------------------------------------------------------------------------------------
					//values of location, time, groupName, and changeRequest
					Intent intent = new Intent(MessageActivity.this, ChangeActivity.class);
					Intent prev = getIntent();
					//Doing the following cause we don't want contacts to be passed, but you may want to 
					//	switch these three ifs to just intent.putExtras(prev) if you do want contacts passed.
					if(prev.hasExtra("location"))
						intent.putExtra("location", prev.getStringExtra("location")); //This adds the location
					if(prev.hasExtra("time"))
						intent.putExtra("time", prev.getStringExtra("time")); //This adds the time
					if(prev.hasExtra("groupName"))
						intent.putExtra("groupName", prev.getStringExtra("groupName")); //This adds the groupName
					//This adds a string that will be posted after changing the location and/or time
					intent.putExtra("changeRequest", "Rochelle: No");
					startActivity(intent); */
					LocationDisagree("vinaykola@gmail.com12:12 pm","vinaykola@gmail.com", "vinaykola@gmail.com;swetha.shivakumar@gmail.com","Van Leer","12:12 pm");
					
				}
			});
		}
		else {
			Button yesButton = (Button) findViewById(R.id.btnYes);
			yesButton.setEnabled(false);
			Button noButton = (Button) findViewById(R.id.btnNo);
			noButton.setEnabled(false);
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
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

	// add items into spinner dynamically
	  public void addItemsOnSpinner1() {
	 
		spinner1 = (Spinner) findViewById(R.id.spinner);
		List<String> list = new ArrayList<String>();
		//list.add("list 1");
		//list.add("list 2");
		//list.add("list 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
	  }
	 
	  public void addListenerOnSpinnerItemSelection() {
		spinner1 = (Spinner) findViewById(R.id.spinner);
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	  }
	  
	  public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		  
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			/* Toast.makeText(parent.getContext(), 
				"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show(); */
			  getLocationDetails(parent.getItemAtPosition(pos).toString());
			  
		  }
		 
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }
		 
		}
	 
	  // get the selected dropdown list value
	  public void addListenerOnButton() {
	 
		spinner1 = (Spinner) findViewById(R.id.spinner);
		  }
	  
	  public void getLocations() {

		    // create a response handler that will receive the query result or an error
		    CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
		      @Override
		      public void onComplete(List<CloudEntity> results) {
		        posts = results;
		        final StringBuilder sb = new StringBuilder();
		    for (CloudEntity post : posts) {
		        sb.append(";");
		        sb.append(post.get("location"));
		        
		        
		    }
		    spinner1 = (Spinner) findViewById(R.id.spinner);
			List<String> list = new ArrayList<String>();
			
			for (CloudEntity post : posts) {
		       list.add(post.get("location").toString());
		        
		    }
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner1.setAdapter(dataAdapter);
		      }

		      @Override
		      public void onError(IOException exception) {
		        handleEndpointException(exception);
		      }
		    };

		getCloudBackend().listByProperty("Event", "eventID",F.Op.EQ,"vinaykola@gmail.com12:12 pm",Order.DESC , 1, Scope.FUTURE_AND_PAST, handler);
		  }
	  
	  private void handleEndpointException(IOException e) {
		    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		    //next.setEnabled(true);
		  }
	  
	  public void LocationDisagree(String eventID, String UserEmailID, String group, String location, String time){      
	        String status="";
	        String eventID2="";
	        
	        String eventID1="amish1804230";
	        String UserEmailID1="swetha";
	        String group1="amish1804;vinaykola;swetha;rochelle.l";
	        String location1="CoC";
	        String time1="230";
	        
	        CloudEntity newPost = new CloudEntity("Event");
	        
	        eventID2=eventID2.concat(eventID);
	        eventID2=eventID2.concat(location);
	        
	        for (int i=0;i<group.split(";").length;i++){
	            if (group.split(";")[i].equals(UserEmailID)){
	                status=status.concat(";1");
	            }
	            else{
	                status=status.concat(";0"); 
	            }
	        }
	        status=status.substring(1,status.length());
	        
	        newPost.put("status",status);
	        newPost.put("group",group);
	        newPost.put("eventID",eventID);
	        newPost.put("eventID2",eventID2);
	        newPost.put("location",location);
	        newPost.put("time",time);
	        
	        
	         CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
	      @Override
	      public void onComplete(final CloudEntity result) {
	        getLocationDetails(String.valueOf(spinner1.getSelectedItem()));
	      }

	      @Override
	      public void onError(final IOException exception) {
	        handleEndpointException(exception);
	      }
	    };

	    // execute the insertion with the handler
	    getCloudBackend().insert(newPost, handler);
	    }

	  
	  public void getLocationDetails(String loc) {
		  // create a response handler that will receive the query result or an error
		    CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
		      @Override
		      public void onComplete(List<CloudEntity> results) {
		        posts = results;
		        messageHistText.setText("");
		    for (CloudEntity post : posts) {
		    	final StringBuilder sb = new StringBuilder();
		        /*
		    	sb.append(post.get("location"));
		                sb.append("#");
		        sb.append(post.get("status"));
		                sb.append("#");
		        sb.append(post.get("time"));
		                sb.append("#");
		        sb.append(post.get("eventID")); */
		    	sb.append(post.get("location") + "\n");
		    	sb.append(post.get("time") + "\n");
		    	String[] users = post.get("group").toString().split(";");
		    	String[] statuses = post.get("status").toString().split(";");
		    	for( int i = 0 ; i < users.length; i++){
		    	sb.append(users[i] + "\t");
		    	if(statuses[i].equals("1")){
		    		sb.append("yes\n");
		    	}
		    	else{
		    		sb.append("no\n");
		    	}
		    	}
		        messageHistText.append(sb.toString());
		    }
		    //return sb.toString();            
		      }

		      @Override
		      public void onError(IOException exception) {
		        handleEndpointException(exception);
		      }
		    };

		getCloudBackend().listByProperty("Event", "eventID2",F.Op.EQ,"vinaykola@gmail.com12:12 pm" +loc,Order.DESC , 1, Scope.FUTURE_AND_PAST, handler);  
		  }
}

		
