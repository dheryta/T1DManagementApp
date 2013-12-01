package com.t1dm.t1dmanagementapp;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class GenerateReport extends Activity {

	private DatePickerDialog dpDialog;
	private Calendar calendar;
	private EditText fromDate;
	private EditText toDate;
	private T1DMApplication appContext;
	private CommonMethods commonMethods = new CommonMethods();
	private Button btnSearch;
	private long time_ms = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generate_report);
		calendar = Calendar.getInstance();
		fromDate = (EditText)findViewById(R.id.etFromDate);
		fromDate.setInputType(InputType.TYPE_NULL); 
		fromDate.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));	
		toDate = (EditText)findViewById(R.id.etToDate);
		toDate.setInputType(InputType.TYPE_NULL); 
		toDate.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
		
		fromDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDP(fromDate);
								
			}
		});
		
		toDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDP(toDate);
								
			}
		});
		
		appContext = ((T1DMApplication) getApplication());
		appContext.setDbHandler(new DatabaseHandler());
		
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {				
				
				
				AlertDialog.Builder builder = new Builder(GenerateReport.this);
				builder.setTitle("T1DM");
				builder.setMessage("Include trend?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					 
	                @Override
	                public void onClick(DialogInterface arg0, int arg1) {
	                	Intent intent = new Intent(appContext.getContext(), ReadingsChart.class);
	    				intent.putExtra("FromDate", fromDate.getText().toString());
	    				intent.putExtra("ToDate",  toDate.getText().toString());
	    				intent.putExtra("AskUser", true);
	    				Calendar calendar = Calendar.getInstance();
	    				time_ms = calendar.getTimeInMillis();
	    				intent.putExtra("Time_ms", time_ms);
	    				startActivity(intent);	    				
	    				finish();
	                }
	            });
	            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

	                @Override
	                public void onClick(DialogInterface arg0, int arg1) {
	                	new AsyncDatabaseOperations().execute();
	                		        			
	                }
	            });
	            builder.show();
	            
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generate_report, menu);
		return true;
	}

	private void showDP(final EditText tvDate){
		dpDialog = new DatePickerDialog(this, new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				String m = (monthOfYear<10)?("0"+Integer.toString(monthOfYear)):Integer.toString(monthOfYear+1);
				String d = (dayOfMonth<10)?("0"+Integer.toString(dayOfMonth)):Integer.toString(dayOfMonth);
				tvDate.setText(year+"-"+m+"-"+d);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dpDialog.show();
	}
	
	private class AsyncDatabaseOperations extends AsyncTask<Void, Void, Void>{

	private boolean  status;
	@Override
      protected void onPreExecute(){
		super.onPreExecute();
		
      }

  @Override
      protected Void doInBackground(Void... voids){

	  try {
		  if(commonMethods.isNetworkAvailable(GenerateReport.this)){
		  commonMethods.PrepareAndSendEmail(fromDate.getText().toString(), toDate.getText().toString(), -1,
      			null,
					GenerateReport.this);
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
    	  if(status)
    		  Toast.makeText(GenerateReport.this, "T1DM says, report sent to registered email.", Toast.LENGTH_SHORT).show();
    	  else
    		  Toast.makeText(GenerateReport.this, "T1DM says, oops some error occurred while sending email !!!!", Toast.LENGTH_SHORT).show();
        
            finish();

      }
}
}
