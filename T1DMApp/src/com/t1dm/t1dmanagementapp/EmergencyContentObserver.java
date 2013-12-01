/* 
* Created On: Nov 9, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.PowerManager;

public class EmergencyContentObserver extends ContentObserver {

	 Context context;
	 int currentVolume, maxVolume;
	 private static int countChange = 0;
	 public static boolean displayed;
	 private static Long prevTime;
	
	 private static List<Long> changeTime = new ArrayList<Long>();
	 public EmergencyContentObserver(Context context, Handler handler) {
		super(handler);
		
       this.context=context;

       AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
       
       displayed = false;
	}


   @Override
   public boolean deliverSelfNotifications() {
       return super.deliverSelfNotifications();
   }

   @Override
   public void onChange(boolean selfChange) {
       super.onChange(selfChange);
       
       PowerManager power = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
       
       AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
       currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
       maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
       
       countChange ++;
       Calendar calendar = Calendar.getInstance();
       changeTime.add(calendar.getTimeInMillis());
       Long currentTime = calendar.getTimeInMillis();
       
		
			if ((countChange == maxVolume) && (currentVolume==0 || currentVolume==maxVolume)
					&& (changeTime.size() > 1 && (changeTime.get(changeTime
							.size() - 1) - changeTime.get(0)) <= 60 * 1100)) {
				countChange = 0;
				changeTime.clear();
				Intent i = new Intent(
						"com.t1dm.t1dmanagementapp.EmergencyActivity");
				PendingIntent operation = PendingIntent.getActivity(context, 0,
						i, Intent.FLAG_ACTIVITY_NEW_TASK);
				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService("alarm");
				calendar = Calendar.getInstance();
				long alarm_time = calendar.getTimeInMillis();
				alarmManager
						.set(AlarmManager.RTC_WAKEUP, alarm_time, operation);

			} else if ((countChange >= maxVolume) || ((changeTime.get(changeTime.size() - 1) - changeTime.get(0) )> 60 * 1100)){
				countChange = 0;
				changeTime.clear();
			}
   }
}
