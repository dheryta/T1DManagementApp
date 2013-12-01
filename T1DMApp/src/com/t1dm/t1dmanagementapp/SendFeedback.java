package com.t1dm.t1dmanagementapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;






import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SendFeedback extends Activity {

	private Button btnSendFeedback;
	private ProgressBar uploadingStatus;
	private EditText comments;
	private CommonMethods commonMethods=new CommonMethods();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_feedback);
		uploadingStatus = (ProgressBar) findViewById(R.id.uploadingStatus);
		uploadingStatus.setVisibility(View.INVISIBLE);
		btnSendFeedback = (Button)findViewById(R.id.btnSend);
		comments = (EditText)findViewById(R.id.etComments);
		
		btnSendFeedback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				if (comments.getText().toString().length() > 0)
					new AsyncDatabaseOperations().execute();
				else
					Toast.makeText(getApplicationContext(), "T1DM says, oops you forgot to fill feedback !!!!", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.send_feedback, menu);
		return true;
	}

		private class AsyncDatabaseOperations extends AsyncTask<Void, Void, Void>{

		private Class<?> nextClass;
		private HttpClient client;
		private HttpPost post;
		private List<BasicNameValuePair> results;
		private boolean status;
		
		@Override
	      protected void onPreExecute(){
			super.onPreExecute();
			client = new DefaultHttpClient();
		    post = new HttpPost("https://docs.google.com/a/iiitd.ac.in/forms/d/1Fa-uZt6xtFhkL90dkYXM2XTooXdRkyPsAcWwNYxJ7O8/formResponse");
		    
		    GetUserLocation userCurrentLocation = new GetUserLocation(getApplicationContext());
		    
		    	    
		    results = new ArrayList<BasicNameValuePair>();
		    results.add(new BasicNameValuePair("entry_119428427", comments.getText().toString()));
		    results.add(new BasicNameValuePair("entry_1000603571", userCurrentLocation.getCompleteAddress()));
		    uploadingStatus.setVisibility(View.VISIBLE);
		    comments.setEnabled(false);
		    btnSendFeedback.setEnabled(false);
	      }

      @Override
	      protected Void doInBackground(Void... voids){

    	  try {
    		  if(commonMethods.isNetworkAvailable(SendFeedback.this)){
		        post.setEntity(new UrlEncodedFormEntity(results));
		        client.execute(post);
		        status = true;
    		  }else
    			  status = false;
    		  
		    } catch (Exception e) {
		     status = false;
		    } finally {
	  			finish(); 
	  		}
			return null;
	      }

	      @Override
	      protected void onPostExecute(Void params){
	    	  uploadingStatus.setVisibility(View.INVISIBLE);
	    	  comments.setEnabled(true);
			   btnSendFeedback.setEnabled(true);
	    	  if (status)
	    		  Toast.makeText(getApplicationContext(), "T1DM says, thanks for valuable feedback !!!!", Toast.LENGTH_SHORT).show();
	    	  else
	    		  Toast.makeText(getApplicationContext(), "T1DM says, oops could not send feedback !!!!", Toast.LENGTH_SHORT).show();
	            finish();

	      }
	}
}
