package com.example.openbyodv01;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Launcher extends Activity {
	
	private ListView listLauncher;
	private ArrayAdapter<String> featuresList;
	private Intent intent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		//Setting the page title
		setTitle("Escolha uma aplicação");
		
		//populates the list
		featuresList = new ArrayAdapter<String>(this, R.layout.list_launcher, this.getLauncherList());
		listLauncher = (ListView) this.findViewById(R.id.listLauncher);
		
		//sets the list adapter, which is needed for the list show predefined items
		listLauncher.setAdapter(featuresList);
		
		//sets onItemClickListener, to go to the different application activities
		listLauncher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				  switch (position) {
				case 0:
					//Log.w("MENSAGEM REST", "Usuario entrou no aplicativo de Email");
					intent.setClass(Launcher.this,Inbox.class);
					startActivity(intent);
					break;
				case 1:
					//Log.w("MENSAGEM REST", "Usuario entrou no aplicativo de Agenda");
					intent.setClass(Launcher.this,Agenda.class);
					startActivity(intent);
					break;
				case 2:
					//Log.w("MENSAGEM REST", "Usuario entrou no aplicativo de Arquivos");
					Toast.makeText(Launcher.this, "O módulo de Arquivos não está disponível.", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}

			  }
			});
	}
	
	
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}
	*/
	
	/**
	 * Returns the list that will be shown in the screen "Launcher"
	 * 
	 * @return List<String>
	 */
	public List<String> getLauncherList(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("Email");
		list.add("Agenda");
		list.add("Arquivos");
		
		return list;
	}

}
