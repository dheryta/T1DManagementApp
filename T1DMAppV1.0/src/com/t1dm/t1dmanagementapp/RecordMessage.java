package com.t1dm.t1dmanagementapp;

import java.io.File;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class RecordMessage extends Activity {

	    
	    private Button btnStartStop;
	    private T1DMApplication appContext;
	    private CheckBox cbEnableAudio;
		private CommonMethods commonMethods = new CommonMethods();
		private MediaPlayer   mediaPlayer = null;
		private MediaRecorder  mediaRecorder = null;
		
		private String recordingName = commonMethods.APP_PATH + File.separator + commonMethods.RECORDING_FOLDER 
				+ File.separator + commonMethods.RECORDING_NAME;

		private boolean recording, playing;
	    private int duration;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record_message);
		appContext = ((T1DMApplication) getApplication());
		appContext.setDbHandler(new DatabaseHandler());
			
		
		cbEnableAudio = (CheckBox)findViewById(R.id.cbEnableAudio);
		btnStartStop = (Button)findViewById(R.id.btnStartStop);	
		cbEnableAudio.setChecked(appContext.getDbHandler().isUseAudioEnabled());
		btnStartStop.setText("Start");
		btnStartStop.setEnabled(cbEnableAudio.isChecked());
		
		cbEnableAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnStartStop.setEnabled(isChecked);
				appContext.getDbHandler().updateUseAudio(isChecked);
			}
		});
		
		btnStartStop.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				
				if (btnStartStop.getText().equals("Start")){
					File file = new File(recordingName);
					if(file.exists()){
						AlertDialog dialog = new AlertDialog.Builder(RecordMessage.this).create();
					    dialog.setTitle("T1DM Recording");
					    dialog.setMessage("Overwrite previous recording?");
					    dialog.setCancelable(true);
					    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int buttonId) {
					        	removeRecording();
					        	btnStartStop.setText("Stop");
						         startRecording();   
						         appContext.getDbHandler().updateUseAudio(true);
					        }
					    });
					    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int buttonId) {
					            finish();
					        }
					    });
					    dialog.setIcon(android.R.drawable.ic_dialog_alert);
					    dialog.show();
					}else{
						btnStartStop.setText("Stop");
				         startRecording();   
					}
				        
				}else{
					btnStartStop.setText("Start");
			        stopRecording();
				}
			}
		});
	}
	
	@Override
    public void onPause() {
        super.onPause();
        if (mediaRecorder != null) {
        	mediaRecorder.release();
        	mediaRecorder = null;
        }

        if (mediaPlayer != null) {
        	mediaPlayer.release();
        	mediaPlayer = null;
        }
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.record_message, menu);
		return true;
	}

	private void startRecording()
	{
		 mediaRecorder = new MediaRecorder();
		 mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		 mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		 mediaRecorder.setOutputFile(recordingName);
		 mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

	        try {
	        	
	        	mediaRecorder.prepare();
	        	mediaRecorder.start();
	        	recording = true;
	        	btnStartStop.setText("Stop");
	        } catch (Exception e) {
	        	Toast.makeText(this, "T1DM says, oops error occurred while recording", Toast.LENGTH_SHORT).show();   
	        }

	        

	}
	
	private void stopRecording(){
		if (mediaPlayer!=null){
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
		recording = false;
		btnStartStop.setText("Record");
		}
	}
	
	private void removeRecording()
	{
		File file = new File(recordingName);
		try{
		if(file.exists())
			file.delete();
		}catch(Exception e){
			Toast.makeText(this, "T1DM says, oops error occurred while removing", Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(this, "T1DM says, recording removed", Toast.LENGTH_SHORT).show();
	}
	
	private void playRecording()
	{
		 mediaPlayer = new MediaPlayer();
		 
	        try {
	        	
	        	mediaPlayer.setDataSource(recordingName);
	            mediaPlayer.prepare();
	            mediaPlayer.start();
	            	            
	        } catch (Exception e) {
	         Toast.makeText(this, "T1DM says, oops error occurred while playing", Toast.LENGTH_SHORT).show();   
	        }

	}
	
	private void pauseRecording()
	{
		if (mediaPlayer!=null){
		mediaPlayer.release();
		mediaPlayer = null;
		recording = false;
		
		}
	}
}
