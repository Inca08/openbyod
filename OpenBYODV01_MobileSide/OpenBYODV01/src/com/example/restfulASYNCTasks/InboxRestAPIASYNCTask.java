package com.example.restfulASYNCTasks;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.entity.Contact;
import com.example.entity.EmailEntity;
import com.example.openbyodv01.Inbox;
import com.example.openbyodv01.R;
import com.example.openbyodv01.ReadMail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class InboxRestAPIASYNCTask extends AsyncTask<String, String, String> {

	private String response = "";
	private Inbox inboxActivity;
	private Intent intent = new Intent();
	private ProgressBar progressBar;
	private ArrayList<EmailEntity> emailsFULLlist = new ArrayList<EmailEntity>();
	private ArrayList<String> emailsStringList = new ArrayList<String>();

	public InboxRestAPIASYNCTask(Inbox inboxActivity) {
		this.inboxActivity = inboxActivity;
	}
	
	@Override
	protected void onPreExecute() {
		this.progressBar = (ProgressBar) this.inboxActivity.findViewById(R.id.vProgressBarInbox);
		this.progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected String doInBackground(String... params) {
		String urlString = params[0];
		StringBuilder stringBuilder = new StringBuilder();

		HttpsURLConnection urlConnection;

		String headerParam_employee_id = this.getEmployee_id();

		if (!headerParam_employee_id.equals("")) {

			try {

				URL url = new URL(urlString);
				urlConnection = (HttpsURLConnection) url.openConnection();
				urlConnection.setChunkedStreamingMode(0);
				urlConnection.setConnectTimeout(100000);
				urlConnection.setReadTimeout(100000);
				urlConnection.setConnectTimeout(100000);
				urlConnection.setReadTimeout(100000);

				// set a "http header" employee_id
				urlConnection.setRequestProperty("employee_id",
						headerParam_employee_id);

				// trust all certificates
				if (urlConnection instanceof HttpsURLConnection) {
					urlConnection.setSSLSocketFactory(this.getSSLContext()
							.getSocketFactory());
				}

				// trust all hosts
				// http://www.nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				urlConnection.setHostnameVerifier(allHostsValid);

				// gets input stream
				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());

				// reads response code
				int responseCode = urlConnection.getResponseCode();

				if (responseCode == 200) {

					// Process the input stream and writes it in the response
					response = this.processInputStream(in);

					// Closes the stream, the Scanner and the urlConnection
					in.close();
					urlConnection.disconnect();
				}

			} catch (Exception ex) {
				Log.e("ERROR GET EMAIL INBOX", ex.getMessage());
			}
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {

		// jsonlist to hold the contacts list that came on the
		JSONArray jsonEmailsList = null;

		if (result.equals("")) {
			Toast.makeText(this.inboxActivity,
					"Ocorreram problemas para os emails! : ( ",
					Toast.LENGTH_SHORT).show();
		} else {

			try {
				jsonEmailsList = new JSONArray(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// extract values from the JSON
			this.extractEmailsValuesFromJSON(jsonEmailsList);
			
			this.progressBar.setVisibility(View.GONE);

			// declare list
			ListView inboxList;
			ArrayAdapter<String> emailsList;

			// populates the list
			emailsList = new ArrayAdapter<String>(this.inboxActivity,
					R.layout.list_email, this.emailsStringList);
			inboxList = (ListView) this.inboxActivity
					.findViewById(R.id.listInbox);

			// sets the list adapter, which is needed for the list show
			// predefined
			// items
			inboxList.setAdapter(emailsList);

			// sets onItemClickListener, to go to the different application
			// activities
			inboxList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View v,
								int position, long arg3) {
							
							EmailEntity emailDTO = emailsFULLlist.get(position);
							
							Log.w("VISUALIZOU EMAIL", "email - " + emailDTO.getFrom() + " " + emailDTO.getSentDate());
							intent.setClass(inboxActivity, ReadMail.class);
							intent.putExtra(Inbox.EMAIL_FROM, emailDTO.getFrom());
							intent.putExtra(Inbox.EMAIL_TO, emailDTO.getTo());
							intent.putExtra(Inbox.EMAIL_SUBJECT, emailDTO.getSubject());
							intent.putExtra(Inbox.EMAIL_BODY,emailDTO.getBody());
							inboxActivity.startActivity(intent);
						}
					});
		}

	}

	private String getEmployee_id() {
		String employee_id = "";

		employee_id = System.getProperty("employee_id");

		return employee_id;
	}

	private SSLContext getSSLContext() throws NoSuchAlgorithmException,
			KeyManagementException {
		// https://gist.github.com/aembleton/889392
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// Not implemented
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// Not implemented
			}
		} };

		SSLContext sc = SSLContext.getInstance("TLS");

		sc.init(null, trustAllCerts, new java.security.SecureRandom());

		return sc;

	}

	private String processInputStream(InputStream inputStream) {

		StringBuilder stringBuilder = new StringBuilder();

		// start listening to the stream
		Scanner inStream = new Scanner(inputStream);

		// process the stream and store it in StringBuilder
		while (inStream.hasNextLine()) {
			stringBuilder.append(inStream.nextLine());
		}

		// Closes the stream
		inStream.close();

		// return String
		return stringBuilder.toString();
	}

	private void extractEmailsValuesFromJSON(JSONArray jsonEmailsList) {
		
		for (int i = 0; i < jsonEmailsList.length(); i++) {
			JSONObject row = null;
			String from = "";
			String subject = "";
			String body = "";
			String to = "";
			String sentDate = "";
			

			try {
				row = jsonEmailsList.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				from = row.getString("from");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				subject = row.getString("subject");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				body = row.getString("body");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				to = row.getString("to");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				sentDate = row.getString("sentDate");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			EmailEntity email = new EmailEntity(to,from,sentDate,subject,body);

			emailsFULLlist.add(email);
			emailsStringList.add(sentDate + "-" + from + " - " + body);
		}
	}
}
