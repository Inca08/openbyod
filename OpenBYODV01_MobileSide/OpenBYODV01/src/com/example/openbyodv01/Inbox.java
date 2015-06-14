package com.example.openbyodv01;

import java.util.ArrayList;
import java.util.List;

import com.example.restfulASYNCTasks.AgendaRestAPITask;
import com.example.restfulASYNCTasks.InboxRestAPIASYNCTask;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Inbox extends Activity {
	
	private ListView inboxList;
	private ArrayAdapter<String> emailsList;
	private Intent intent = new Intent();
	
	//constants to pass the values to the another activity
	public final static String EMAIL_TO = "com.example.openbyodv01.TO";
	public final static String EMAIL_FROM = "com.example.openbyodv01.FROM";
	public final static String EMAIL_SUBJECT = "com.example.openbyodv01.SUBJECT";
	public final static String EMAIL_BODY = "com.example.openbyodv01.BODY";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		
		// Call restAPI, method to authenticate in the application
				InboxRestAPIASYNCTask inboxRestCall = new InboxRestAPIASYNCTask(this);

				inboxRestCall.execute(getResources().getString(R.string.rest_server_url)
						+ "/email/inbox");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inbox, menu);
		return true;
	}
	
	/**
	 * Redirects the user to the compose email screen
	 * 
	 * @param item MenuItem
	 */
	public void menuClickGoToCompose(MenuItem item) {
		intent.setClass(Inbox.this, Compose.class);
		startActivity(intent);
	}
}
