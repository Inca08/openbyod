package com.example.restfulASYNCTasks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.openbyodv01.Agenda;
import com.example.openbyodv01.Login;
import com.example.openbyodv01.R;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class LogCallAPITask extends AsyncTask<String, String, String>{
	
	private String response = "";
	private Agenda agendaActivity;
	public LogCallAPITask(Agenda agendaActivity) {
		this.agendaActivity = agendaActivity;
	}

	@Override
	protected String doInBackground(String... params) {
		
		String urlString = params[0];
		String contact_id = params[1];
		String employee_id = params[2];

		// Prepare request body
		JSONObject JSONrequestBody = new JSONObject();
		try {
			JSONrequestBody.put("employee_idemployee", employee_id);
			JSONrequestBody.put("contact_idcontact", contact_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String bodyRequest = JSONrequestBody.toString();

		HttpsURLConnection urlConnection;

		try {
			URL url = new URL(urlString);
			urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setChunkedStreamingMode(0);
			urlConnection.setConnectTimeout(100000);
			urlConnection.setReadTimeout(100000);
			urlConnection.setConnectTimeout(100000);
			urlConnection.setReadTimeout(100000);
			urlConnection.setRequestProperty("Content-Type",
					"application/json;charset=utf8");

			// trust all certificates and hosts
			// http://www.nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
			if (urlConnection instanceof HttpsURLConnection) {
				urlConnection.setSSLSocketFactory(this.getSSLContext()
						.getSocketFactory());
			}
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			urlConnection.setHostnameVerifier(allHostsValid);

			// Open connection
			urlConnection.connect();

			// posts stream
			OutputStream out = new BufferedOutputStream(
					urlConnection.getOutputStream());
			out.write(bodyRequest.getBytes("UTF-8"));
			out.flush();

			// reads response code
			int responseCode = urlConnection.getResponseCode();
			
			response = String.valueOf(responseCode);
			
			Log.w("POST TO SAVE CALL RESPONSE CODE -> ", response);
			
			// Close the urlConnection
			urlConnection.disconnect();

		} catch (Exception ex) {
			Log.e("ERROR POST SAVE CALL", ex.getMessage());
		}

		return response;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (result.equals("201")) {
			Toast.makeText(this.agendaActivity, "Chamada foi logada com sucesso no servidor! : )",
					Toast.LENGTH_SHORT).show();
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

}
