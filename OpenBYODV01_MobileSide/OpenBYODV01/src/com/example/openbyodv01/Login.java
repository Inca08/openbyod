package com.example.openbyodv01;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.restfulASYNCTasks.LoginRestAPITask;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private EditText vUser;
	private EditText vEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		this.checkForemployee_id();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		this.checkForemployee_id();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.checkForemployee_id();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void login(View view) {
		// Gets data from the screen
		vUser = (EditText) this.findViewById(R.id.user);
		vEmail = (EditText) this.findViewById(R.id.password);

		// Prepare request body
		JSONObject requestBody = new JSONObject();
		try {
			requestBody.put("userid", vUser.getText().toString());
			requestBody.put("password", vEmail.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Call restAPI, method to authenticate in the application
		LoginRestAPITask restCall = new LoginRestAPITask(this);

		restCall.execute(
				getResources().getString(R.string.rest_server_url) + "/login/authenticate",
				requestBody.toString());

	}
	
	private void checkForemployee_id(){
		/*SharedPreferences prefs = getPreferences(MODE_PRIVATE); 
		String employee_id = prefs.getString("employee_id", null);*/
		
		String employee_id = System.getProperty("employee_id");
		if (employee_id != null){
			Intent intent = new Intent(this, Launcher.class);
			this.startActivity(intent);
		}
	}

}
