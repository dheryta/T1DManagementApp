package com.t1dm.t1dmanagementapp;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class T1DM_Menu extends Activity {

	private CommonMethods commonMethods = new CommonMethods();
	private MainMenuArrayAdapter adapter;
	private ListView mainMenuListView;
	private T1DMApplication appContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_t1_dm__menu);
		
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		
		adapter = new MainMenuArrayAdapter(this, android.R.layout.simple_list_item_1 , commonMethods.MAIN_MENU);
		mainMenuListView = (ListView) findViewById(R.id.listviewT1DM_Menu);
		mainMenuListView.setAdapter(adapter);
		mainMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		      @Override
		      public void onItemClick(AdapterView<?> parent, View view,
		          int position, long id) {
		        final String item = (String) parent.getItemAtPosition(position);
/*"Create/Edit Schedules",
 * "Manage Insulin Dosage",
 * "Enter Glucose Reading",
 * "Show Glucose Trend", 
 * "Generate GI Suggestions", 
 * "Enable Emergency Mode"
 * Edit user profile
 * */
		        switch(position)
		        {
		        case 0:
		        	startActivity(new Intent(appContext.getContext(),	Schedules.class));
		        	break;
		        case 1:
		        	startActivity(new Intent(appContext.getContext(),	ManageInsulinDosage.class));
		        	break;
		        case 2:
		        	startActivity(new Intent(appContext.getContext(),	BeginMonitoring.class));
		        	break;
		        case 3:
		        	startActivity(new Intent(appContext.getContext(),	PlotReadings.class));
		        	break;
		        case 4:
		        	Toast.makeText(T1DM_Menu.this, "Item Choosen position:"+item, Toast.LENGTH_LONG).show();
		        	break;
		        case 5:
		        	startActivity(new Intent(appContext.getContext(),	ManageEmergencyModule.class));
		        	break;
		        case 6:
		        	startActivity(new Intent(appContext.getContext(),	UserDetailsActivity.class));
		        	break;
		        }
		      }
		});
		      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.t1_dm__menu, menu);
		return true;
	}

	private class MainMenuArrayAdapter extends ArrayAdapter<String> {

	    public MainMenuArrayAdapter(Context context, int textViewResourceId, List<String> mainMenuEntries) {
	      super(context, textViewResourceId, mainMenuEntries);	     
	    }
	    	    
	  }
}
