/* 
* Created On: Nov 9, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.SmsManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class EmergencyDialog extends DialogFragment {
	private Context context;
	private GetUserLocation userCurrentLocation;
	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;
	private SharedPreferences sharedPrefs;
	
	public EmergencyDialog(Context context) {
		this.context = context;
		userCurrentLocation = new GetUserLocation(context);
		appContext = ((T1DMApplication) context);
		appContext.setContext(appContext);
		appContext.setDbHandler(new DatabaseHandler());		
	}
	  @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	 	        
	        getActivity().getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON | LayoutParams.FLAG_DISMISS_KEYGUARD);
	        
	        sharedPrefs = this.context.getSharedPreferences(commonMethods.PREFS_NAME, commonMethods.MODE_PRIVATE);
	         
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	 	        
	        builder.setTitle("T1DM");
	 	        
	        builder.setMessage("Send location to emergency contact ?");
	 	        
	        builder.setPositiveButton("Yes", new OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            
	               String message = "I am not feeling well. ";
	               String userLocation = userCurrentLocation.getCompleteAddress();
	               String emergencyNumber = appContext.getDbHandler().getEmergencyNumber();
	               if (emergencyNumber != ""){
	               SmsManager smsManager = SmsManager.getDefault();	       
	               if (userLocation.contains("Oops"))
	            	   smsManager.sendTextMessage( emergencyNumber, null, message, null, null);
	               else
	            	   smsManager.sendTextMessage( emergencyNumber, null, message+userLocation, null, null);
	               
	           	   Toast.makeText(appContext, "T1DM says, location sent to " + emergencyNumber, Toast.LENGTH_SHORT).show();
	               }else
	            	   Toast.makeText(appContext, "T1DM says, oops no emergency contact found", Toast.LENGTH_SHORT).show();
	               getActivity().finish();
	            }
	        });
	 
	        builder.setNegativeButton("No", new OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                getActivity().finish();
	            }
	        });
	        return builder.create();
	    }
	 
	    /** The application should be exit, if the user presses the back button */
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        getActivity().finish();
	    }
	    
		

}
