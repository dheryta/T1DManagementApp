/* 
* Created On: Nov 9, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;



public class EmergencyActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        
        EmergencyDialog alert = new EmergencyDialog(getApplicationContext());
           
        alert.show(getSupportFragmentManager(), "T1DM Emergency");
    }

}