package com.t1dm.t1dmanagementapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class InsulinSchedule extends ListActivity {
	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;
	
	private String[] values = new String[] { commonMethods.SUBTYPE_BREAKFAST,
			commonMethods.SUBTYPE_BRUNCH, commonMethods.SUBTYPE_LUNCH,
			commonMethods.SUBTYPE_SNACKS, commonMethods.SUBTYPE_DINNER };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
	
		ActivityArrayAdapter adapter = new ActivityArrayAdapter(this,
				getModel(), R.layout.activity_insulin_schedule);
		setListAdapter(adapter);

		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_done, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuAddNewSchedule:

			return true;
		case R.id.menuDone:
			ListView listView = getListView();
			List<ActivityScheduleDetail> schedule = new ArrayList<ActivityScheduleDetail>();
			for (int i = 0; i < listView.getAdapter().getCount(); i++) {
				Model value = (Model) listView.getAdapter().getItem(i);
				if (value.isSelected()){
					ActivityScheduleDetail subtypeDetail = new ActivityScheduleDetail();
					subtypeDetail.set_Type(1);
					subtypeDetail.set_Time(value.getTime());
					subtypeDetail.set_SubType(value.getName());
					schedule.add(subtypeDetail);
				}else{
					ActivityScheduleDetail subtypeDetail = new ActivityScheduleDetail();
					subtypeDetail.set_Type(1);
					subtypeDetail.set_Time("");
					subtypeDetail.set_SubType(value.getName());
					schedule.add(subtypeDetail);
				}
			}
			
			if (appContext.getDbHandler().insertActivitySchedule(schedule) == 1){
				for(int i=0;i<schedule.size();i++){
					Calendar calendar = Calendar.getInstance();
					if (schedule.get(i).get_Time() != ""){
					String[] timeSplit = schedule.get(i).get_Time().split(":"); 
					calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
					calendar.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
					if (calendar.getTimeInMillis() < System.currentTimeMillis()){
						calendar.add(Calendar.DAY_OF_MONTH, 1);
					}
					Intent intent = new Intent("com.t1dm.t1dmanagementapp.ScheduleAlarmReceiver");
					intent.putExtra("Sender", schedule.get(i).get_Type()+":"+schedule.get(i).get_SubType());
					intent.putExtra("ID", 6+i+1);
					intent.setAction("InsulinAlarmFor"+schedule.get(i).get_SubType());
					
				    commonMethods.setAlarm(calendar.getTimeInMillis(), intent , InsulinSchedule.this, commonMethods.REPEAT.get(0));
					}
					}
				
			}
			else
				Toast.makeText(InsulinSchedule.this, "T1DM says, oops error occurred", Toast.LENGTH_LONG).show();
			
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private List<Model> getModel() {
		List<Model> list = appContext.getDbHandler().getAlarms(CommonMethods.ACTIVITY_INSULIN);
		if (list.size() == 0)
			for (int i = 0; i < values.length; i++)
				list.add(new Model(values[i]));
		return list;
	}
}
