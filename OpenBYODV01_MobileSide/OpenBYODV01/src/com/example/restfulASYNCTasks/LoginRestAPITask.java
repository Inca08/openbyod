package com.example.restfulASYNCTasks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.example.openbyodv01.Launcher;
import com.example.openbyodv01.Login;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * This class is meant to do http get/post request to a rest API, returning the
 * values from the API
 * 
 */
public class LoginRestAPITask extends AsyncTask<String, String, String> {

	private String response = "";
	private Login loginActivity;
	public LoginRestAPITask(Login loginActivity) {
		this.loginActivity = loginActivity;
	}

	/**
	 * The params to this methods are passed by doInBackground(String param1,
	 * String param2, etc...)
	 * 
	 * @param URL
	 *            of the RESTFUL API, like "https:server:8080/resource/id"
	 * @param JSON
	 *            Request Body
	 */
	@Override
	protected String doInBackground(String... params) {

		String urlString = params[0];
		String bodyRequest = params[1];

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

			if (responseCode == 202) {

				// gets input stream
				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());

				// Process the input stream and writes it in the response
				response = this.processInputStream(in);

				// Closes the input stream
				in.close();
			}

			// Close the urlConnection
			urlConnection.disconnect();

		} catch (Exception ex) {
			Log.e("ERROR POST LOGIN", ex.getMessage());
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		// super.onPostExecute(result);

		if (result.equals("")) {
			Toast.makeText(this.loginActivity, "Credenciais incorretas!",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this.loginActivity, "Login feito com sucesso!",
					Toast.LENGTH_SHORT).show();
			
			//storeUserPreferences
			/*SharedPreferences.Editor editor = this.loginActivity
					.getPreferences(0).edit();
			editor.putString("employee_id", result);
			editor.commit();*/
			
			System.setProperty("employee_id", result);
			
			System.setProperty("employee_email", "employee" + result + "@localhost.mydomain");

			Intent intent = new Intent(this.loginActivity, Launcher.class);
			this.loginActivity.startActivity(intent);
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
