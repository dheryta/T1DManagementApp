/* 
* Created On: Nov 9, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class EmergencyService extends Service {

	IntentFilter filter;
	
	EmergencyContentObserver mSettingsContentObserver;

	public static int prevStartID;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent i, int flags, int startId) {
		mSettingsContentObserver = new EmergencyContentObserver(this,new Handler());
		getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );
		 Intent intent = new Intent(this, ManageEmergencyModule.class);
	     PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

	     NotificationCompat.Builder notificationBuilder =
	    	        new NotificationCompat.Builder(this)
	    	        .setSmallIcon(R.drawable.ic_launcher)
	    	        .setContentTitle("T1DM Emergency Mode:")
	    	        .setContentText("Enabled")
	    	        .setAutoCancel(false);
	     notificationBuilder.setContentIntent(pendingIntent);
	     
	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	   
	    notificationManager.notify(0, notificationBuilder.build());

		return Service.START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId) {

	}
	
	 @Override
	    public void onDestroy() {
		 Log.i("T1DM Service","Service Destroyed");
		 getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
	    }

}
