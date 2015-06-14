package com.example.openbyodv01;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.restfulASYNCTasks.SaveNewContactRestAPITask;
import com.example.restfulASYNCTasks.SendEmailrestfulASYNCTask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class Compose extends Activity {

	// text that holds the email of the employee
	private EditText vEditTextEmailFrom;
	// text that holds the email destination
	private EditText vEditTextEmailTo;
	// text that holds the email subject
	private EditText vEditTextEmailSubject;
	// text that holds the email body
	private EditText vEditTextEmailBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		// Setting the page title
		setTitle("Novo Email");

		// Initialization of the Edit Texts
		vEditTextEmailFrom = (EditText) findViewById(R.id.editTextEmailFrom);
		vEditTextEmailTo = (EditText) findViewById(R.id.editTextEmailTo);
		vEditTextEmailSubject = (EditText) findViewById(R.id.editTextEmailSubject);
		vEditTextEmailBody = (EditText) findViewById(R.id.editTextEmailBody);
		vEditTextEmailFrom.setText(System.getProperty("employee_email"));

		if (this.getIntent()
				.getBooleanExtra(ReadMail.FLAG_FROM_READ_MAIL, true)) {
			vEditTextEmailTo.setText(this.getIntent().getStringExtra(
					ReadMail.FROM_EMAIL));
			vEditTextEmailSubject.setText(this.getIntent().getStringExtra(
					ReadMail.SUBJECT_EMAIL));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	/**
	 * Call a rest service to send the email and return to the "Inbox activity"
	 * 
	 * @param item
	 *            MenuItem
	 */
	public void menuClickSendEmail(MenuItem item) {
		// Prepares the email to be sent

		String to = this.vEditTextEmailTo.getText().toString();
		String from = this.vEditTextEmailFrom.getText().toString();
		String subject = this.vEditTextEmailSubject.getText().toString();
		String body = this.vEditTextEmailBody.getText().toString();

		// prepare the json String to be sent as an email
		JSONObject requestBody = new JSONObject();
		try {
			requestBody.put("to", to);
			requestBody.put("from", from);
			requestBody.put("subject", subject);
			requestBody.put("body", body);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Call restAPI, method to save a new contact in the database
		SendEmailrestfulASYNCTask restCall = new SendEmailrestfulASYNCTask(this);

		restCall.execute(getResources().getString(R.string.rest_server_url)
				+ "/email/send", requestBody.toString());

		/*
		 * intent.setClass(Inbox.this, Compose.class); startActivity(intent);
		 */
	}

}
