/* 
* Created On: Nov 13, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ScheduleAlarmActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String sender = intent.getAction();
        
        ScheduleAlarmDialog alert = new ScheduleAlarmDialog(getApplicationContext(), sender);
        
        alert.show(getSupportFragmentManager(), "T1DM Alarm Sent By:"+ sender);
    }
}