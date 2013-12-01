/* 
* Created On: Nov 13, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

public class ScheduleAlarmReceiver extends BroadcastReceiver{

	private CommonMethods commonMethods = new CommonMethods();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(2000);
	    String sender = intent.getStringExtra("Sender");
	    int ID = intent.getIntExtra("ID", 0);
		Intent i = new Intent();
	    i.setClassName("com.t1dm.t1dmanagementapp", "com.t1dm.t1dmanagementapp.ScheduleAlarmActivity");
	    i.putExtra("Sender", sender);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.setAction(intent.getAction());
	    
	    Intent intentAlarm = new Intent(context, HomeScreen.class);
	     PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentAlarm, 0);

	    commonMethods.showNotification(context, pendingIntent, ID, true, "T1DM", intent.getAction());
	    context.startActivity(i);		
	}

}
