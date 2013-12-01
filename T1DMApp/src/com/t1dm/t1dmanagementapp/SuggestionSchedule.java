package com.t1dm.t1dmanagementapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class SuggestionSchedule extends Activity {
	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;

	private List<String> values = commonMethods.REPEAT;
	private Spinner repeat;
	private TimePicker timePicker;
	private List<Model> alarms;
	private CheckBox cbEnableReporting;
	private boolean canSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion_schedule);
		
		repeat = (Spinner) findViewById(R.id.spinnerRepeat);
		
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());

		timePicker = (TimePicker) findViewById(R.id.timePickerRepeat);
		cbEnableReporting = (CheckBox)findViewById(R.id.cbEnableAutoReporting);
		alarms = getModel();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, values);
	
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		repeat.setAdapter(adapter);
		repeat.setSelection((alarms.size()>0)?values.indexOf(alarms.get(0).getName()):1);
		Calendar calendar = Calendar.getInstance();
		timePicker.setCurrentHour((alarms.size()>0)?alarms.get(0).getHour():calendar.get(Calendar.HOUR));
		timePicker.setCurrentMinute((alarms.size()>0)?alarms.get(0).getMinute():calendar.get(Calendar.MINUTE));
		
		cbEnableReporting.setChecked((alarms.size()>0)?true:false);
		timePicker.setEnabled(cbEnableReporting.isChecked());
		repeat.setEnabled(cbEnableReporting.isChecked());				
		canSave = cbEnableReporting.isChecked()                                                                                                ;
		cbEnableReporting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {				
					timePicker.setEnabled(isChecked);
					repeat.setEnabled(isChecked);				
				canSave = isChecked;
			}
		});
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
		case R.id.menuDone:

			List<ActivityScheduleDetail> schedule = new ArrayList<ActivityScheduleDetail>();

			ActivityScheduleDetail subtypeDetail = new ActivityScheduleDetail();
			subtypeDetail.set_Type(5);
			if(canSave){
			subtypeDetail.set_Time(timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute());
			subtypeDetail.set_SubType(repeat.getSelectedItem().toString());
			}else{
				subtypeDetail.set_Time("");
				subtypeDetail.set_SubType("Dummy");				
			}
			schedule.add(subtypeDetail);

			if (appContext.getDbHandler().insertActivitySchedule(schedule) == 1) {
				int i=0;
					Calendar calendar = Calendar.getInstance();
					if (schedule.get(i).get_Time() != ""){
					String[] timeSplit = schedule.get(i).get_Time().split(":"); 
					calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
					calendar.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
					Intent intent = new Intent("com.t1dm.t1dmanagementapp.ScheduleAlarmReceiver");
					intent.putExtra("Sender", schedule.get(i).get_Type()+":"+schedule.get(i).get_SubType());
					intent.putExtra("ID", 24+i+1);
					intent.setAction("SuggestionAlarm");
					
				    commonMethods.setAlarm(calendar.getTimeInMillis(), intent , SuggestionSchedule.this, schedule.get(i).get_SubType());
					
					}
			
				
			} else
				Toast.makeText(SuggestionSchedule.this,
						"T1DM says, oops error occurred", Toast.LENGTH_LONG)
						.show();

			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private List<Model> getModel() {
		return appContext.getDbHandler().getAlarms(CommonMethods.ACTIVITY_SUGGESTION);
	}

}
