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


public class Schedules extends Activity {

	private CommonMethods commonMethods = new CommonMethods();
	private SchedulesArrayAdapter adapter;
	private ListView activityMenuListView;
	private T1DMApplication appContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedules);
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());

		adapter = new SchedulesArrayAdapter(this, android.R.layout.simple_list_item_1 , commonMethods.MAIN_ACTIVITIES);
		activityMenuListView = (ListView) findViewById(R.id.listviewActivities_Menu);
		activityMenuListView.setAdapter(adapter);
		activityMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		      @Override
		      public void onItemClick(AdapterView<?> parent, View view,
		          int position, long id) {
		        
/*("MEAL", "INSULIN", "BGCHECKING", "EXERCISE", "SLEEP", "REPORT"); */
		        switch(position)
		        {
		        case 0:
		        	startActivity(new Intent(appContext.getContext(),	MealSchedule.class));
		        	break;
		        case 1:
		        	startActivity(new Intent(appContext.getContext(),	InsulinSchedule.class));
		        	break;
		        case 2:
		        	startActivity(new Intent(appContext.getContext(),	BGCheckingSchedule.class));
		        	break;
		        case 3:
		        	startActivity(new Intent(appContext.getContext(),	ExerciseSchedule.class));
		        	break;
		        case 4:
		        	startActivity(new Intent(appContext.getContext(),	SleepSchedule.class));
		        	break;
		        case 5:
		        	startActivity(new Intent(appContext.getContext(),	SuggestionSchedule.class));
		        	break;
		        }
		      }
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.schedules, menu);
		return true;
	}
	
	private class SchedulesArrayAdapter extends ArrayAdapter<String> {

	    public SchedulesArrayAdapter(Context context, int textViewResourceId, List<String> schedulesEntries) {
	      super(context, textViewResourceId, schedulesEntries);	     
	    }
	    	    
	  }

}
