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
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.entity.Contact;
import com.example.openbyodv01.Agenda;
import com.example.openbyodv01.Launcher;
import com.example.openbyodv01.Login;
import com.example.openbyodv01.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AgendaRestAPITask extends AsyncTask<String, String, String> {

	private String response = "";
	private Agenda agendaActivity;
	private Intent intent = new Intent();
	private ProgressBar progressBar;
	private ArrayList<Contact> contactsFULLlist = new ArrayList<Contact>();
	private ArrayList<String> contactsStringList = new ArrayList<String>();

	public AgendaRestAPITask(Agenda agendaActivity) {
		this.agendaActivity = agendaActivity;
	}
	
	@Override
	protected void onPreExecute() {
		this.progressBar = (ProgressBar) this.agendaActivity.findViewById(R.id.vProgressBarAgenda);
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
				Log.e("ERROR GET AGENDA", ex.getMessage());
			}
		}
		return response;
	}

	private String getEmployee_id() {
		String employee_id = "";

		employee_id = System.getProperty("employee_id");

		return employee_id;
	}

	@Override
	protected void onPostExecute(String result) {

		// jsonlist to hold the contacts list that came on the
		JSONArray jsonContactsList = null;

		if (result.equals("")) {
			Toast.makeText(this.agendaActivity,
					"Ocorreram problemas para carregar a agenda! : ( ",
					Toast.LENGTH_SHORT).show();
		} else {

			try {
				jsonContactsList = new JSONArray(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

		// extract values from the JSON		
		this.extractValuesFromJSON(jsonContactsList);
		
		this.progressBar.setVisibility(View.GONE);

		// declare list
		ListView listAgenda;
		ArrayAdapter<String> contactsList;

		// populates the list
		contactsList = new ArrayAdapter<String>(this.agendaActivity,
				R.layout.list_launcher, contactsStringList);
		listAgenda = (ListView) this.agendaActivity
				.findViewById(R.id.listAgenda);

		// sets the list adapter, which is needed for the list show predefined
		// items
		listAgenda.setAdapter(contactsList);

		// sets onItemClickListener, to go to the different application activities
		listAgenda.setOnItemClickListener(new AdapterView.OnItemClickListener() {			
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				try {
					
							Contact contactDTO = contactsFULLlist.get(position);
					
							Log.w("MENSAGEM REST","Discou para contato " + contactDTO.getName() + " numero " + contactDTO.getContactNumber());
							// Call restAPI, method to save the call in the database for further report
							LogCallAPITask logCallrestAPI = new LogCallAPITask(agendaActivity);
							logCallrestAPI.execute(agendaActivity.getResources().getString(R.string.rest_server_url) + "/call/add",
							String.valueOf(contactDTO.getId()),System.getProperty("employee_id"));
					
							//Call to the number using the default dial application
							intent.setAction(Intent.ACTION_CALL);
							// here it will be getting the previously specified number based on the index
							intent.setData(Uri.parse("tel:"+contactDTO.getContactNumber())); 
							agendaActivity.startActivity(intent);
						} catch (Exception e) {
							Log.e("ERROR AO DISCAR", Log.getStackTraceString(e));
						}
					}
				});
			
		}
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
	
	private void extractValuesFromJSON(JSONArray jsonContactsList){
		
		for (int i = 0; i < jsonContactsList.length(); i++) {
			JSONObject row = null;
			int contact_number = 0;
			String name = "";
			int idcontact = 0;

			try {
				row = jsonContactsList.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				contact_number = row.getInt("contact_number");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				name = row.getString("name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				idcontact = row.getInt("idcontact");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Contact contact = new Contact(name, contact_number, idcontact);

			contactsFULLlist.add(contact);
			contactsStringList.add(name);
		}
	}

}
