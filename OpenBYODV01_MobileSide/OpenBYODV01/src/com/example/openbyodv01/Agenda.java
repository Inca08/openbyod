package com.example.openbyodv01;

import java.util.ArrayList;
import java.util.List;

import com.example.restfulASYNCTasks.AgendaRestAPITask;
import com.example.restfulASYNCTasks.LoginRestAPITask;

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

public class Agenda extends Activity {

	private ListView listAgenda;
	private ArrayAdapter<String> contactsList;
	private Intent intent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agenda);

		// Call restAPI, method to authenticate in the application
		AgendaRestAPITask agendaRestCall = new AgendaRestAPITask(this);

		agendaRestCall.execute(getResources().getString(R.string.rest_server_url)
				+ "/contact/list");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agenda, menu);
		return true;
	}
	
	/**
	 * Redirects the user to the compose email screen
	 * 
	 * @param item MenuItem
	 */
	public void menuClickGoToSaveNewContact(MenuItem item) {
		intent.setClass(Agenda.this, NewContact.class);
		startActivity(intent);
	}

}
