package com.example.openbyodv01;

import com.example.restfulASYNCTasks.LoginRestAPITask;
import com.example.restfulASYNCTasks.SaveNewContactRestAPITask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NewContact extends Activity {

	// text that holds the name of the contact
	private EditText vEditTextNewContactName;
	// text that holds the number of the contact
	private EditText vEditTextNewContactNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_contact);
		
		setTitle("Cadastrar Contato");

		// Initialization of the Edit Texts
		vEditTextNewContactName = (EditText) findViewById(R.id.EditTextNewContactName);
		vEditTextNewContactNumber = (EditText) findViewById(R.id.EditTextNewContactNumber);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_contact, menu);
		return true;
	}
	
	/**
	 * Call a rest service to save the new contact and return to the "Agenda activity"
	 * 
	 * @param item MenuItem
	 */
	public void menuClickSaveNewContact(MenuItem item) {
		
		String contactName = this.vEditTextNewContactName.getText().toString();
		String contactnumber = this.vEditTextNewContactNumber.getText().toString();
		
		// Call restAPI, method to save a new contact in the database
		SaveNewContactRestAPITask restCall = new SaveNewContactRestAPITask(this);

		restCall.execute(getResources().getString(R.string.rest_server_url)
				+ "/contact/add", contactName, contactnumber);
		
	}

}
