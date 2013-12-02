package com.t1dm.t1dmanagementapp;



import java.io.File;




import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class SplashScreen extends Activity {

	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;
	final String PREFS_NAME = "T1DM_Prefs";

	private SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
		
		sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

		if (sharedPrefs.getBoolean("t1dm_first_time", true)) {
			sharedPrefs.edit().putBoolean("t1dm_first_time", false).commit(); 
		
		
		AlertDialog.Builder builder = new Builder(SplashScreen.this);
		builder.setTitle("T1DM - Important");
		builder.setMessage("This app gives dietary suggestions based on GI and GL values mainly for Type 1 diabetics."
				+ "It also helps to manage daily blood sugar levels."
				+ "Food database is taken from Atkinson FS, Foster-Powell K, Brand-Miller JC. International Tables of Glycemic Index and Glycemic Load Values: 2008. "
				+ "DiabCare 2008; 31(12). All suggestions are approximations and may differ from patient to patient.  "
				+ "You are advised to consult your doctor before selecting any food in your diet."
				+ "Under no circumstances developer(s) shall be held responsible for any mishappenings.");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			 
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            	sharedPrefs.edit().putBoolean("t1dm_user_ok", true).commit();
            	new AsyncDatabaseOperations().execute();
            	
               
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {            	
            	sharedPrefs.edit().putBoolean("t1dm_user_ok", false).commit();
            	finish();
            }
        });
        builder.show();
        
		}else if (sharedPrefs.getBoolean("t1dm_user_ok", true)){
			new AsyncDatabaseOperations().execute();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class AsyncDatabaseOperations extends AsyncTask<Void, Void, Void>{
		private Class<?> nextClass;
		private NotificationCompat.Builder notificationBuilder;
		private NotificationManager notificationManager;
		
		@Override
	      protected void onPreExecute(){
			super.onPreExecute();
			
		     PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);

		     notificationBuilder =
		    	        new NotificationCompat.Builder(getApplicationContext())
		    	        .setSmallIcon(R.drawable.ic_launcher)
		    	        .setContentTitle("T1DM Installation")
		    	        .setContentText("Loading database")
		    	        .setAutoCancel(false);
		     notificationBuilder.setContentIntent(pendingIntent);
		     
		    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		   
		    notificationManager.notify(100, notificationBuilder.build());

	      }

	      @Override
	      protected Void doInBackground(Void... voids){
	    	  try {
	  				File dbPath = new File(commonMethods.APP_PATH + File.separator + commonMethods.DB_FOLDER);
	  				boolean charts = true, recordings = true, captures = true, databases = true, reports = true, foods = true;
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

	  				foods = commonMethods.copyFoodsCSV(appContext.getContext());
 				
	  				boolean createTableStatus = appContext.getDbHandler().insertOneRowInAllTables();

	  				if (createTableStatus && databases && charts && recordings && captures && reports && foods) {

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
	    	  notificationManager.cancel(100);
	            finish();

	      }



	}

	
}