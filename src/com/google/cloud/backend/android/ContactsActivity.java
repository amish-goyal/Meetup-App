package com.google.cloud.backend.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.google.cloud.backend.android.CloudQuery.Order;
import com.google.cloud.backend.android.CloudQuery.Scope;

public class ContactsActivity extends CloudBackendActivity {
	
	private ListView mListView;
	List<CloudEntity> posts = new LinkedList<CloudEntity>();
	ArrayList<String> contact_name = new ArrayList<String>();
	ArrayList<String> contact_email = new ArrayList<String>();
	ArrayList<String> items = new ArrayList<String>();
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		// Show the Up button in the action bar.
		setupActionBar();
		
		mListView = (ListView) findViewById(R.id.list);
		/*
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString( cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString( cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));		
				Cursor emailCur = cr.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null); 
				if(emailCur.moveToNext()) { 
						String email = emailCur.getString(
								emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						contact_name.add(name);
						contact_email.add(email);
				}
				emailCur.close();
			}

		} */
		
		/* Hardcoding the phone contacts since it takes a long time to fetch */
		contact_name.add("Vinay");
		contact_name.add("Swetha");
		contact_name.add("Amish");
		contact_name.add("Rochelle");
		
		contact_email.add("vinaykola@gmail.com");
		contact_email.add("swetha.shivakumar@gmail.com");
		contact_email.add("amish1804@gmail.com");
		contact_email.add("rochelle.l.lobo@gmail.com");
		
		//System.out.println(contact_name.toArray().toString());
		//System.out.println(contact_email.toArray().toString());
		filterUsers();
		
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.contactlayout);
		RelativeLayout.LayoutParams rlpB = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		//Button next = new Button(this);
		rlpB.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		//next.setLayoutParams(rlpB);
		/* next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent activityChangeIntent = new Intent(ContactsActivity.this, MessageActivity.class);
				activityChangeIntent.putExtras(getIntent());
				activityChangeIntent.putExtra("newEvent", true);
				startActivity(activityChangeIntent);				
			}
		});*/
		
		//rl.addView(next);
	}

	public void NewEvent(String friends, String location, String time, String UserEmailID) {

        String status="";
        String group="";
        String eventID="";
        String eventID2="";

        System.out.println(friends+location+time+UserEmailID);
        
        //variables used for testing        
        String UserEmailID1="amish1804";
        String friends1="vinaykola;swetha;rochelle.l";
        String location1="Klauss";
        String time1="1230";
        
        CloudEntity newPost = new CloudEntity("Event");
        
        eventID=eventID.concat(UserEmailID);
        eventID=eventID.concat(time);
        
        eventID2=eventID2.concat(eventID);
        eventID2=eventID2.concat(location);
        
        group=group.concat(UserEmailID);
        group=group.concat(";");
        group=group.concat(friends);
        
        status=status.concat("1");
        for (int i=0;i<friends.split(";").length;i++){
            status=status.concat(";0");     
        }
        
        newPost.put("status",status);
        newPost.put("group",group);
        newPost.put("eventID",eventID);
        newPost.put("eventID2",eventID2);
        newPost.put("location",location);
        newPost.put("time",time);
        
        CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
            @Override
            public void onComplete(final CloudEntity result) {
              posts.add(0, result);
            }

            @Override
            public void onError(final IOException exception) {
              handleEndpointException(exception);
            }
          };

          // execute the insertion with the handler
          getCloudBackend().insert(newPost, handler);
      }

	
	public void filterUsers()
    {
		Log.d("d", "0");
    CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
      @Override
      public void onComplete(List<CloudEntity> results) {
        posts = results;
        updateContactsUI(contact_name.toArray(new String[contact_name.size()]), contact_email.toArray(new String[contact_email.size()]));
      }

      @Override
      public void onError(IOException exception) {
        handleEndpointException(exception);
      }
    };
    Log.d("d", "1");
   getCloudBackend().listByKind("Users", CloudEntity.PROP_CREATED_AT, Order.DESC,100,Scope.FUTURE_AND_PAST, handler);
   Log.d("d", "2");
    }
	
	public void updateContactsUI(String[] contact_name, String[] contact_email){
		final StringBuilder sb = new StringBuilder();
	    for (CloudEntity post : posts) {
	    sb.append(",");
	        sb.append(post.get("email ID"));

	    }
	    Log.d("d", "3");
	    String entries=sb.toString();
	    Log.d("d", entries);
	    entries=entries.substring(1,entries.length());
	    Log.d("d", entries);
	    String[] email_entries=entries.split(",");
	    //String entries=sb.toString().substring(1,sb.toString().length());
	    Log.d("d", "4");
	    //String[] email_entries=entries.split(",");
	    
	    
	        //String[] c_name = {"Swetha", "Abc"};
	        //createList(contact_name, contact_email);
	        
	        Set<String> aSet = new HashSet<String>();
	        Set<String> intersection = new HashSet<String>();
	        for (int i=0; i<contact_email.length; i++) {
	            aSet.add(contact_email[i]);
	        }
	        
	        for (int i=0; i<email_entries.length; i++) {
	            if (aSet.contains(email_entries[i])) {
	                intersection.add(email_entries[i]);
	            }
	        }
	        //createList(c_name, contact_email);
	        createList(contact_email,contact_email);

	}
	
	private void handleEndpointException(IOException e) {
	    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	    //next.setEnabled(true);
	  }
    
	
	public void createList(String[] contact_name, String[] contact_email)
	{
		final ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_multiple_choice, contact_name);
		mListView.setAdapter(adapter);
		
		Button button = (Button) findViewById(R.id.nextBtn);
		//button.setBackgroundResource(R.drawable.back);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//ListView thelistview = (ListView) findViewById(android.R.id.list);
				//SparseBooleanArray checkedItems = thelistview.getCheckedItemPositions();

				SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();

				
				if (checkedItems != null) {
					for (int i=0; i<checkedItems.size(); i++) {
						if (checkedItems.valueAt(i)) {
							String item = adapter.getItem(checkedItems.keyAt(i)).toString();
							items.add(item);
						}
					}
					Toast.makeText(getBaseContext(), items.toString(), Toast.LENGTH_LONG).show();

				}
				else
				{
					Toast.makeText(getBaseContext(), "checkedItems == null", Toast.LENGTH_LONG).show();
				}
				String[] selectedContacts = items.toArray(new String[items.size()]);
				System.out.println("contacts"+Arrays.toString(selectedContacts));

				String sel_contacts = "";
				for(Object i: items){
					sel_contacts = sel_contacts.concat(";");
					sel_contacts = sel_contacts.concat((String)i);
				}
				sel_contacts = sel_contacts.substring(1,sel_contacts.length());
				
				//pass selectedContacts to back end here!!
//---------------------------------------------------------------------------------------------------------------------------------------------------------------
				
				Intent activityChangeIntent = new Intent(ContactsActivity.this, MessageActivity.class);
				activityChangeIntent.putExtras(getIntent());//passes location, time, groupname to next intent
				activityChangeIntent.putExtra("contacts", Arrays.toString(selectedContacts)); //adds selected contacts as an extra to the intent
				activityChangeIntent.putExtra("newEvent", true);
				String location = activityChangeIntent.getStringExtra("location");
				String time = activityChangeIntent.getStringExtra("time");
				String user = getUsername();
				NewEvent(sel_contacts,location,time,user);
				for(String contact: selectedContacts){
					CloudEntity cm = getCloudBackend().createCloudMessage(contact);
					cm.put("contacts", sel_contacts);
					cm.put("location", location);
					cm.put("time", time);
					cm.put("user", user);
					cm.put("eventID", user+time);
					getCloudBackend().sendCloudMessage(cm);
				}
				startActivity(activityChangeIntent);
			}
		});

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


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
