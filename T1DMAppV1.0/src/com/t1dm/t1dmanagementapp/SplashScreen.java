package com.t1dm.t1dmanagementapp;



import java.io.File;



import android.os.AsyncTask;

import android.os.Bundle;

import android.widget.Toast;

import android.app.Activity;

import android.content.Intent;

public class SplashScreen extends Activity {

	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
		new AsyncDatabaseOperations().execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class AsyncDatabaseOperations extends AsyncTask<Void, Void, Void>{
		private Class<?> nextClass;
		@Override
	      protected void onPreExecute(){
			super.onPreExecute();
	      }

	      @Override
	      protected Void doInBackground(Void... voids){
	    	  try {
	  				File dbPath = new File(commonMethods.APP_PATH + File.separator + commonMethods.DB_FOLDER);
	  				boolean charts = true, recordings = true, captures = true, databases = true, reports = true;
	  				if (!dbPath.isDirectory())
	  					databases = commonMethods.createDatabaseFolder();
	  				
	  				File reportsPath = new File(commonMethods.APP_PATH + File.separator + commonMethods.REPORT_FOLDER);
	  				if (!reportsPath.isDirectory())
	  					reports = commonMethods.createReportsFolder();
		
	  				
	  				File chartsPath = new File(commonMethods.APP_PATH + File.separator + commonMethods.CHARTS_FOLDER);
	  				if (!chartsPath.isDirectory())
	  					charts = commonMethods.createChartsFolder();
		
	  				File recordingsPath = new File(commonMethods.APP_PATH + File.separator + commonMethods.RECORDING_FOLDER);

	  				if (!recordingsPath.isDirectory())

	  					recordings = commonMethods.createRecordingsFolder();

	  				File capturesPath = new File(commonMethods.APP_PATH + File.separator + commonMethods.CAPTURE_FOLDER);

	  				if (!capturesPath.isDirectory())

	  					captures = commonMethods.createCapturesFolder();

 				
	  				boolean createTableStatus = appContext.getDbHandler().insertOneRowInAllTables();

	  				if (createTableStatus && databases && charts && recordings && captures && reports) {

	  					nextClass = commonMethods.decideNextActivity();

	  				} else {

	  					Toast.makeText(

	  							appContext.getContext(),

	  							"T1DM says, oops database inconsistency found, I don't know what to do !!!!\n You can delete database file",

	  							Toast.LENGTH_LONG).show();

	  				}
	  		} catch (Exception e) {

	  			e.printStackTrace();

	  		} finally {

	  			finish(); 

	  		}

			return null;

	      }



	      @Override

	      protected void onPostExecute(Void params){
	    	  if (nextClass!=null)
	    		  startActivity(new Intent(appContext.getContext(),	nextClass));
	    	  else

					Toast.makeText(

							appContext.getContext(),

							"T1DM says, severe error occurred !!!!",

							Toast.LENGTH_LONG).show();
	            finish();

	      }



	}

}