package com.example.restfulASYNCTasks;

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
import com.example.openbyodv01.Inbox;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SendEmailrestfulASYNCTask extends
		AsyncTask<String, String, String> {

	private Context activityContext;
	private String response;

	public SendEmailrestfulASYNCTask(Context activityContext) {
		this.activityContext = activityContext;
	}

	@Override
	protected String doInBackground(String... params) {

		String urlString = params[0];

		// Request body
		String bodyRequest = params[1];

		HttpsURLConnection urlConnection;

		String headerParam_employee_id = this.getEmployee_id();

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

			// set a "http header" employee_id
			urlConnection.setRequestProperty("employee_id",
					headerParam_employee_id);

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

			Log.w("POST TO SEND EMAIL RESPONSE CODE -> ", response);

			// Close the urlConnection
			urlConnection.disconnect();

		} catch (Exception ex) {
			Log.e("ERROR POST SEND EMAIL", ex.getMessage());
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		
		if (result.equals("201")) {

			Toast.makeText(this.activityContext,
					"Email enviado com sucesso! : )", Toast.LENGTH_SHORT)
					.show();

			Intent intent = new Intent();
			intent.setClass(this.activityContext, Inbox.class);
			this.activityContext.startActivity(intent);
		} else {
			Toast.makeText(this.activityContext,
					"Problemas para enviar email! : (",
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

	private String getEmployee_id() {
		String employee_id = "";

		employee_id = System.getProperty("employee_id");

		return employee_id;
	}
}
