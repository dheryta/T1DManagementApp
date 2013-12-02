package com.t1dm.t1dmanagementapp;

import java.util.Calendar;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ManageEmergencyModule extends Activity {

	private Button btnChangeEmergency ;
	private RadioGroup rgManageEmergencyModule ;
	private RadioButton rbSelected ;
	private T1DMApplication appContext;
	private CommonMethods commonMethods = new CommonMethods();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_emergency_module);
		btnChangeEmergency = (Button)findViewById(R.id.btnChangeEmergency);
		rgManageEmergencyModule = (RadioGroup)findViewById(R.id.rgManageEmergencyModule);
		
		appContext = ((T1DMApplication) getApplication());
		appContext.setDbHandler(new DatabaseHandler());
		
		
			
		btnChangeEmergency.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// get selected radio button from radioGroup
				int selectedId = rgManageEmergencyModule.getCheckedRadioButtonId();
	 
				// find the radiobutton by returned id
				rbSelected = (RadioButton) findViewById(selectedId);
	 
				if (rbSelected.getText().equals("Enable Emergency")){
					
					setServiceAlarm();
					if (isEmergencyServiceRunning()){
						appContext.getDbHandler().updateEmergencyEnabled(true);
						Toast.makeText(ManageEmergencyModule.this, "T1DM says, emergency mode enabled", Toast.LENGTH_SHORT).show();
			      	}else{
			      		appContext.getDbHandler().updateEmergencyEnabled(false);
						Toast.makeText(ManageEmergencyModule.this, "T1DM says, oops emergency mode failed !!!", Toast.LENGTH_SHORT).show();
			      	}
					finish();
				}
				
				if (rbSelected.getText().equals("Disable Emergency")){
					Intent i= new Intent(ManageEmergencyModule.this, EmergencyService.class);
					ManageEmergencyModule.this.stopService(i);
			        String notificationService = Context.NOTIFICATION_SERVICE;
			        NotificationManager nMgr = (NotificationManager) ManageEmergencyModule.this.getSystemService(notificationService);
			        nMgr.cancel(0);
			        clearServiceAlarm();
			        appContext.getDbHandler().updateEmergencyEnabled(false);
			        Toast.makeText(ManageEmergencyModule.this, "T1DM says, emergency mode disabled", Toast.LENGTH_SHORT).show();
			        finish();
				}
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.manage_emergency_module, menu);
		return true;
	}
	
	private boolean isEmergencyServiceRunning() {
	    ActivityManager manager = (ActivityManager) ManageEmergencyModule.this.getSystemService(Context.ACTIVITY_SERVICE);
	    
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (EmergencyService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	private void setServiceAlarm() {
		Intent intent= new Intent(ManageEmergencyModule.this, EmergencyService.class);
		
	    PendingIntent emergencySender = PendingIntent.getService(ManageEmergencyModule.this, 0, intent, 0);    
	    Calendar calendar = Calendar.getInstance();
	    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 15*60*1000, emergencySender);

	}
	
	private void clearServiceAlarm() {
		Intent intent= new Intent(ManageEmergencyModule.this, EmergencyService.class);
		
	    PendingIntent emergencySender = PendingIntent.getService(ManageEmergencyModule.this, 0, intent, 0);    
	    Calendar calendar = Calendar.getInstance();
	    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    alarmManager.cancel(emergencySender);

	}
}
