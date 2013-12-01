package com.t1dm.t1dmanagementapp;

import java.util.Calendar;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class RestoreAlarms extends BroadcastReceiver {

	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;
	private List<Model> listAlarms;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
    		Log.i("T1DM", "Broadcast Received: Boot Completed");
    	appContext = ((T1DMApplication) context.getApplicationContext());
		appContext.setContext(context.getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());		
		
        //Meal
		restoreAlarm(CommonMethods.ACTIVITY_MEAL, 1, "Meal", context);
    	//Insulin
		restoreAlarm(CommonMethods.ACTIVITY_INSULIN, 6, "Insulin", context);
		//Exercise
		restoreAlarm(CommonMethods.ACTIVITY_EXERCISE, 16, "Exercise", context);
    	//Sleep
		restoreAlarm(CommonMethods.ACTIVITY_SLEEP, 21, "Sleep", context);
    	//BG Checking
		restoreAlarm(CommonMethods.ACTIVITY_BGCHECKING, 11, "BGChecking", context);
		
		//emergency mode
		if(appContext.getDbHandler().isEmergencyEnabled())
			enableEmergency(context);
    	}
    }
    
    private void enableEmergency(Context context){
    	
    	boolean status = false;
    		Intent intent= new Intent(context, EmergencyService.class);
    		
    	    PendingIntent emergencySender = PendingIntent.getService(context, 0, intent, 0);    
    	    Calendar calendar = Calendar.getInstance();
    	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 15*60*1000, emergencySender);

    	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    	    
    	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
    	        if (EmergencyService.class.getName().equals(service.service.getClassName())) {
    	            status = true;
    	        }
    	    }
    	    
    	    if(status)
    	    	Toast.makeText(context, "T1DM says, emergency mode enabled", Toast.LENGTH_SHORT).show();
    	    else
    	    {
    	    	appContext.getDbHandler().updateEmergencyEnabled(false);
				Toast.makeText(context, "T1DM says, oops emergency mode failed !!!", Toast.LENGTH_SHORT).show();
    	    }
    }
    
    private void restoreAlarm(int type, int id, String typeName, Context context){
    	listAlarms = appContext.getDbHandler().getAlarms(type);
    	
		for(int i=0;i<listAlarms.size();i++){
			Log.i("T1DM", "Iteration i"+i);
			Calendar calendar = Calendar.getInstance();
			if (listAlarms.get(i).isSelected()){			
			
			calendar.set(Calendar.HOUR_OF_DAY, listAlarms.get(i).getHour());
			
			calendar.set(Calendar.MINUTE, listAlarms.get(i).getMinute());
			
			if (calendar.getTimeInMillis() < System.currentTimeMillis()){
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			Intent intentAlarm = new Intent("com.t1dm.t1dmanagementapp.ScheduleAlarmReceiver");
			intentAlarm.putExtra("Sender", type+":"+listAlarms.get(i).getName());
			intentAlarm.putExtra("ID", id+i+1);
			intentAlarm.setAction(typeName+"AlarmFor"+listAlarms.get(i).getName());
			
		    commonMethods.setAlarm(calendar.getTimeInMillis(), intentAlarm , context, commonMethods.REPEAT.get(0));
			}
    }
    }
   
}