/* 
* Created On: Nov 13, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class ScheduleAlarmDialog extends DialogFragment {
	private Context context;
	private T1DMApplication appContext;
	private String title;
	private CommonMethods commonMethods = new CommonMethods();
	private MediaPlayer   mediaPlayer = null;
	private String recordingName = commonMethods.APP_PATH + File.separator + commonMethods.RECORDING_FOLDER 
			+ File.separator + commonMethods.RECORDING_NAME;
	
	public ScheduleAlarmDialog(Context context, String title) {
		this.context = context;
		this.title = title;
		appContext = ((T1DMApplication) context);
		appContext.setContext(appContext);
		appContext.setDbHandler(new DatabaseHandler());		
	}
	  @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	 	        
	        getActivity().getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON | LayoutParams.FLAG_DISMISS_KEYGUARD);
	        
	        if(appContext.getDbHandler().isUseAudioEnabled())
	         playRecording();
	        
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	 	        
	        builder.setTitle(title);
	 	        
	        builder.setMessage("Schedule Alarm Received ?");
	
	        
	        builder.setPositiveButton("OK", new OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	 getActivity().finish();
	               
	            }
	        });
	 
	        /*builder.setNegativeButton("No", new OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                getActivity().finish();
	            }
	        });*/
	        return builder.create();
	    }
	 
	    /** The application should be exit, if the user presses the back button */
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        getActivity().finish();
	    }
	    
	    private void playRecording()
		{
	    	File file = new File(recordingName);
			
			if(file.exists())
			{	
			 mediaPlayer = new MediaPlayer();
			 
		        try {
		        	
		        	mediaPlayer.setDataSource(recordingName);
		            mediaPlayer.prepare();
		            mediaPlayer.start();
		            	            
		        } catch (Exception e) {
		            
		        }
			}
		}

}
