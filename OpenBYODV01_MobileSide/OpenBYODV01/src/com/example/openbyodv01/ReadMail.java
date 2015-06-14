package com.example.openbyodv01;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ReadMail extends Activity {
	
	private Intent previousIntent;
	private Intent intent = new Intent();
	private EditText editTextReadEmailFrom;
	private EditText editTextReadEmailTo;
	private EditText editTextReadEmailSubject;
	private EditText editTextReadEmailBody;
	private String fromEmail;
	private String toEmail;
	private String subjectEmail;
	private String bodyEmail;
	
	public final static String FLAG_FROM_READ_MAIL = "com.example.openbyodv01.ReadMail.FLAG_FROM_READ_MAIL";
	public final static String FROM_EMAIL = "com.example.openbyodv01.ReadMail.FROM_EMAIL";
	public final static String SUBJECT_EMAIL = "com.example.openbyodv01.ReadMail.SUBJECT_EMAIL";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_mail);
		
		//Getting Messages from the intent that fired this activity
		previousIntent = getIntent();
		fromEmail = previousIntent.getStringExtra(Inbox.EMAIL_FROM);
		toEmail = previousIntent.getStringExtra(Inbox.EMAIL_TO);
		subjectEmail = previousIntent.getStringExtra(Inbox.EMAIL_SUBJECT);
		bodyEmail = previousIntent.getStringExtra(Inbox.EMAIL_BODY);
		//String emailId = previousIntent.getStringExtra(Inbox.EMAIL_ID);
		
		//Setting the activity title to be equal to the subject of the email
		this.setTitle("Detalhe do Email");
		
		//Initialization of the Edit Texts
		editTextReadEmailFrom = (EditText)findViewById(R.id.editTextReadEmailFrom);
		editTextReadEmailTo = (EditText)findViewById(R.id.editTextReadEmailTo);
		editTextReadEmailSubject = (EditText)findViewById(R.id.editTextReadEmailSubject);
		editTextReadEmailBody = (EditText)findViewById(R.id.editTextReadEmailBody);
		
		//putting the values of the email clicked
		editTextReadEmailFrom.setText(fromEmail);
		editTextReadEmailTo.setText(toEmail);
		editTextReadEmailSubject.setText(subjectEmail);
		editTextReadEmailBody.setText(bodyEmail);	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_mail, menu);
		return true;
	}
	
	/**
	 * Redirects the user to the Compose Activity sending as a parameter the destination of the email and the subject
	 * 
	 * @param item MenuItem
	 */
	public void menuClickReplyEmail(MenuItem item) {
		this.intent.setClass(ReadMail.this, Compose.class);
		this.intent.putExtra(FLAG_FROM_READ_MAIL, true);
		this.intent.putExtra(FROM_EMAIL, fromEmail);
		this.intent.putExtra(SUBJECT_EMAIL, "[RE] " + subjectEmail);		
		startActivity(intent);
	}
}
