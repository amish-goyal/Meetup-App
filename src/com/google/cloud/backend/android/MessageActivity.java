package com.google.cloud.backend.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.backend.android.CloudQuery.Order;
import com.google.cloud.backend.android.CloudQuery.Scope;

public class MessageActivity extends CloudBackendActivity {

	private LocationTimeList locTimeList = new LocationTimeList();
	ListView messageList;
	List<CloudEntity> posts = new LinkedList<CloudEntity>();
	boolean event = false;
	// TextView messageHistText;
	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		messageList = (ListView) findViewById(R.id.LV_List);

		setTitle(getIntent().getStringExtra("groupName"));
		String s = getIntent().getStringExtra("location")
				+ "-"
				+ ChangeActivity
						.convertTime(getIntent().getStringExtra("time"));

		if (!locTimeList.contains(s)) {
			locTimeList.add(s);
		}

		final EditText et = (EditText) findViewById(R.id.reason);
		// /et.setEnabled(false);
		// //-----------------------------------------------------------------------------------------------------
		// //populate this try/catch with the meeting history
		// /* try {
		// AssetManager am = this.ggetAssets();
		// InputStream is = am.open("meetingHistory.txt");
		// Scanner scan = new Scanner(is);
		//
		// while(scan.hasNext()) {
		// //only change the scan.nextLine() here! You may not need the while if
		// you are
		// //appending bunch of code with \n chars on the end.
		// messageHistText.append(scan.nextLine()+"\n");
		// }
		// */
		//

		// if(prev.hasExtra("changedEvent")) {
		// messageHistText.append(prev.getStringExtra("changeRequest"));
		// }
		// String location, time, user;
		// if(prev.hasExtra("newEvent")) {
		// //Need to add group to conversation list
		// location = prev.getStringExtra("location");
		// //String time = ChangeActivity.convertTime(prev);
		// time = prev.getStringExtra("time");
		// user = getUsername() + ": ";
		// //messageHistText.append(user+"<Meeting at "+location+" "+time+">\n");
		// }

		ImageButton addButton = (ImageButton) findViewById(R.id.btnAdd);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MessageActivity.this,
						ChangeActivity.class);
				intent.putExtra("changeRequest", true);
				Intent prev = getIntent();
				if (prev.hasExtra("newEvent")) {
					intent.putExtras(prev);
				}
				startActivity(intent);
			}
		});

		createSpinner();

		ImageButton yesButton = (ImageButton) findViewById(R.id.btnYes);
		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent prev = getIntent();
				if (prev.hasExtra("eventID")) {
					String location = prev.getStringExtra("location");
					String time = prev.getStringExtra("time");
					String user = getUsername();
					String eventID = prev.getStringExtra("eventID");
					String group = prev.getStringExtra("contacts");
					// locationDecision(eventID, user, group, location, time,
					// "yes");
					LocationAgree(eventID + location, group, user);
				}
			}
		});
		ImageButton noButton = (ImageButton) findViewById(R.id.btnNo);
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent prev = getIntent();
				if (prev.hasExtra("eventID")) {
					String location = prev.getStringExtra("location");
					String time = prev.getStringExtra("time");
					String user = getUsername();
					String eventID = prev.getStringExtra("eventID");
					String group = prev.getStringExtra("contacts");
					// locationDecision(eventID, user, group, location, time,
					// "no");
					// et.setClickable(true); Not sure why this is here
					LocationDisagree(eventID + location, group, user, et
							.getText().toString());
				}
			}
		});
		getLocationDetails(spinner.getItemAtPosition(0).toString());
	}

	private String getUsername() {
		AccountManager manager = AccountManager.get(this);
		Account[] accounts = manager.getAccountsByType("com.google");
		List<String> possibleEmails = new LinkedList<String>();

		for (Account account : accounts) {
			possibleEmails.add(account.name);
		}
		if (possibleEmails.size() != 0) {
			return possibleEmails.get(0);
		} else {
			return "avd@gmail.com"; // Fallback if there's no account associated
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

	// public void getLocations() {
	// // create a response handler that will receive the query result or an
	// error
	// CloudCallbackHandler<List<CloudEntity>> handler = new
	// CloudCallbackHandler<List<CloudEntity>>() {
	// @Override
	// public void onComplete(List<CloudEntity> results) {
	// posts = results;
	// final StringBuilder sb = new StringBuilder();
	// for (CloudEntity post : posts) {
	// sb.append(";");
	// sb.append(post.get("location"));
	// }
	// spinner = (Spinner) findViewById(R.id.spinner);
	// List<String> list = new ArrayList<String>();
	//
	// for (CloudEntity post : posts) {
	// list.add(post.get("location").toString());
	// }
	// ArrayAdapter<String> dataAdapter = new
	// ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,
	// list);
	// dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// spinner.setAdapter(dataAdapter);
	// }
	//
	// @Override
	// public void onError(IOException exception) {
	// handleEndpointException(exception);
	// }
	// };
	//
	// // getCloudBackend().listByProperty("Event",
	// "eventID",F.Op.EQ,"vinaykola@gmail.com12:12 pm",Order.DESC , 1,
	// Scope.FUTURE_AND_PAST, handler);
	// }

	private void handleEndpointException(IOException e) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		// next.setEnabled(true);
	}

	public void getLocationDetails(String loc) {
		System.out
				.println("-----------------------------------------\nEntering getLocationDetails()");
		loc = loc.split("-")[0];
		System.out.println(loc);
		// create a response handler that will receive the query result or an
		// error
		CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
			@Override
			public void onComplete(List<CloudEntity> results) {

				posts = results;
				if (posts.size() == 1) {

					// for (CloudEntity post : posts) {
					System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
					String location = posts.get(0).get("location").toString();
					String time = posts.get(0).get("time").toString();
					String group = posts.get(0).get("group").toString();
					String status = posts.get(0).get("status").toString();
					createTextFields(location, time, status, group);
					// }
				}
			}

			@Override
			public void onError(IOException exception) {
				handleEndpointException(exception);
			}
		};
		System.out.println(getIntent().getStringExtra("eventID"));
		System.out.println(loc);

		getCloudBackend().listByProperty("Event", "eventID2", F.Op.EQ,
				getIntent().getStringExtra("eventID") + loc, Order.DESC, 1,
				Scope.FUTURE_AND_PAST, handler);
	}

	public void createTextFields(String location, String time, String status, String group) {
		//		RelativeLayout.LayoutParams rlpLocTi = new RelativeLayout.LayoutParams(
		//				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		//		rlpLocTi.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		//		TextView locTi = new TextView(this);
		//		locTi.setId(5000);
		//		locTi.setTextSize(30);
		//		locTi.setLayoutParams(rlpLocTi);
		//		String t = ChangeActivity.convertTime(time);
		//		locTi.setText(location+"-"+t);
		//		messageHeader.addView(locTi);

		String[] statuses = status.split(";");
		String[] names = group.split(";");
		
		int index =0;
		List<HashMap<String, String>> AL = new ArrayList<HashMap<String,String>>();
		for(String name : names){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("Name", name);
			map.put("Status", statuses[index++]);
			AL.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), AL, R.layout.list_template, new String[]{"Name","Status"}, new int[]{R.id.TV_Name, R.id.IV_Status});
		adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if(view.getId() == R.id.IV_Status){
					String status = (String)data;
					if(status.equalsIgnoreCase("1")){
						view.setBackgroundResource(R.drawable.green_check_icon);
					}else if (status.equalsIgnoreCase("0")){
						view.setBackgroundResource(R.drawable.yellow_question_mark);
					}else{
						view.setBackgroundResource(R.drawable.red_x_icon);
						TextView reason = (TextView) view.findViewById(R.id.TV_Reason);
						reason.setVisibility(View.VISIBLE);
						reason.setText(status);
					}
				}
				return false;
			}
		});
		messageList.setAdapter(adapter);
		//stat.setId(5000+j);
		//stat.setLayoutParams(rlpStat);
		//stat.setText(group.split(";")[j]);
		
//		for(int i=0; i<group.split(";").length; i++) {
//		//for(int i=0; i<2; i++) {
//			rlpStat.addRule(RelativeLayout.BELOW,5000+i);
//			
//			stat.setId(5000+i);
//			stat.setLayoutParams(rlpStat);
//			stat.setText(group.split(";")[i]);
//					
//				if(status.split(";")[i] == "1") {
//					Drawable img = this.getResources().getDrawable( R.drawable.green_check_icon );
//					img.setBounds(new Rect(0,0,36,36));
//					stat.setCompoundDrawables(null, null, img, null);
//				}
//				else if(status.split(";")[i] == "0") {
//					Drawable img = this.getResources().getDrawable( R.drawable.yellow_question_mark );
//					img.setBounds(new Rect(0,0,28,36));
//					stat.setCompoundDrawables(null, null, img, null);
//				}
//				else {
//					Drawable img = this.getResources().getDrawable( R.drawable.red_x_icon );
//					img.setBounds(new Rect(0,0,36,36));
//					stat.setCompoundDrawables(null, null, img, null);
//				}
//				
//			}messageHist.addView(stat);	
}

	private void createSpinner() {
		spinner = (Spinner) findViewById(R.id.spinner);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, locTimeList.toArray());
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				getLocationDetails(parent.getItemAtPosition(pos).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				getLocationDetails(parent.getItemAtPosition(0).toString());
			};
		});
	}

	// Updates status of user with emailID = UserEmailID
	public void LocationAgree(String eventID2, final String group,
			final String UserEmailID) {
		CloudCallbackHandler<List<CloudEntity>> handler3 = new CloudCallbackHandler<List<CloudEntity>>() {
			@Override
			public void onComplete(final List<CloudEntity> result) {
				posts = result;
				System.out.println(group);
				System.out.println(UserEmailID);

				if (posts.size() == 1) {
					String status = (String) posts.get(0).get("status");
					String newStatus = "";
					String[] group_split = group.split(";");
					for (int i = 0; i < group_split.length; i++) {
						if (group_split[i].equals(UserEmailID)) {
							newStatus = newStatus.concat(";1");
						} else {
							newStatus = newStatus.concat(";"
									+ status.split(";")[i]);
						}

					}
					newStatus = newStatus.substring(1, newStatus.length());
					System.out.println(status + "-------->" + newStatus);
					posts.get(0).put("status", newStatus);
					getCloudBackend().update(posts.get(0),
							Handlers.getHandler2());
				}
			}

			@Override
			public void onError(final IOException exception) {
				handleEndpointException(exception);
			}
		};

		getCloudBackend().listByProperty("Event", "eventID2", F.Op.EQ,
				eventID2, Order.DESC, 1, Scope.FUTURE_AND_PAST, handler3);
	}

	// Updates status of user with emailID = UserEmailID, adds reason for
	// disagreeing
	public void LocationDisagree(String eventID2, final String group,
			final String UserEmailID, final String reason) {

		CloudCallbackHandler<List<CloudEntity>> handler3 = new CloudCallbackHandler<List<CloudEntity>>() {
			@Override
			public void onComplete(final List<CloudEntity> result) {
				posts = result;
				if (posts.size() == 1) {
					String status = (String) posts.get(0).get("status");
					String newStatus = "";
					for (int i = 0; i < group.split(";").length; i++) {
						if (group.split(";")[i].equals(UserEmailID)) {
							newStatus = newStatus.concat(";" + reason);
						} else {
							newStatus = newStatus.concat(";"
									+ status.split(";")[i]);
						}

					}
					newStatus = newStatus.substring(1, newStatus.length());
					posts.get(0).put("status", newStatus);
					getCloudBackend().update(posts.get(0),
							Handlers.getHandler2());
				}
			}

			@Override
			public void onError(final IOException exception) {
				handleEndpointException(exception);
			}
		};

		getCloudBackend().listByProperty("Event", "eventID2", F.Op.EQ,
				eventID2, Order.DESC, 1, Scope.FUTURE_AND_PAST, handler3);

		// createTextFields(location, time, String status, String group);
	}

	public CloudCallbackHandler<CloudEntity> getHandler2() {
		CloudCallbackHandler<CloudEntity> handler2 = new CloudCallbackHandler<CloudEntity>() {

			@Override
			public void onComplete(final CloudEntity result) {
				getLocationDetails(String.valueOf(spinner.getSelectedItem()));
			}

			@Override
			public void onError(final IOException exception) {
				// handleEndpointException(exception);
			}

		};

		return handler2;
	}

}