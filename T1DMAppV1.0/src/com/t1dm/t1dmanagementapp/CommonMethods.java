package com.t1dm.t1dmanagementapp;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Xml.Encoding;
import android.widget.Toast;



public class CommonMethods {	

	public static boolean USER_PROFILING_COMPLETE = false; 

	public static final int ACTIVITY_MEAL = 0;

	public static final int ACTIVITY_INSULIN = 1;

	public static final int ACTIVITY_BGCHECKING = 2;

	public static final int ACTIVITY_EXERCISE = 3;

	public static final int ACTIVITY_SLEEP = 4;

	public static final int ACTIVITY_SUGGESTION = 5;

	
	public final List<String> MAIN_MENU = Arrays.asList("Create/Edit Schedules","Manage Insulin Dosage","Enter Glucose Reading","Show Glucose Trend", "Generate GI Suggestions", "Enable Emergency Mode", "Edit Profile");
	
	public final List<String> MAIN_ACTIVITIES= Arrays.asList("MEAL", "INSULIN", "BGCHECKING", "EXERCISE", "SLEEP");//, "REPORT"); 

	public final List<String> DAYS_OF_WEEK = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

	public final List<String> REPEAT = Arrays.asList("Daily", "Weekly", "Fortnightly", "Monthly");
	
	public final List<String> SETTINGS = Arrays.asList("Use Audio Message", "Emergency Mode", "Insulin Dosage", "My Profile", "My Meal Plan", "Generate Report", "Feedback");
	
	
	public final String SUBTYPE_BREAKFAST = "BreakFast";

	public final String SUBTYPE_BRUNCH = "Brunch";

	public final String SUBTYPE_LUNCH = "Lunch";

	public final String SUBTYPE_SNACKS = "Snacks";

	public final String SUBTYPE_DINNER = "Dinner";

	public final String SUBTYPE_MORNING = "Morning";

	public final String SUBTYPE_NOON = "Noon";

	public final String SUBTYPE_EVENING = "Evening";

	public final String SUBTYPE_NIGHT = "Night";

	public final String SUBTYPE_DAILY = "Daily";

	public final String SUBTYPE_WEEKLY = "Weekly";

	public final String SUBTYPE_FORTNIGHTLY = "Fortnightly";

	public final String SUBTYPE_MONTHLY = "Monthly";

	public final String SUBTYPE_EXTRAS = "Other";

	public final String PREFS_NAME = "T1DM_Prefs";

	public final int MODE_PRIVATE = 0;
	
	public final int DB_VERSION = 1;

	public final String DB_NAME = "T1DM_DB";
	
	public final String RECORDING_NAME = "T1DM_RECORDING.3gp";
	
	public final String FOODS = "GL_GI_Database_PIPE.csv";
	
	public final String REPORT_NAME = "Report";

	public final File APP_PATH = Environment.getExternalStorageDirectory();

	public final String DB_FOLDER = "T1DMApp/Database";
	
	public final String REPORT_FOLDER = "T1DMApp/Reports";
	
	public final String CHARTS_FOLDER = "T1DMApp/Charts";

	public final String RECORDING_FOLDER = "T1DMApp/Recordings";
	
	public final String CAPTURE_FOLDER = "T1DMApp/Captures";

	public static final DatabaseHandler DATABASE_HANDLER = new DatabaseHandler();

	public boolean copyFoodsCSV(Context context){
		boolean status = false;

		File fileFoods = new File(APP_PATH + File.separator + DB_FOLDER
				+ File.separator + FOODS);
		
		File fileDB = new File(APP_PATH + File.separator + DB_FOLDER
				+ File.separator + DB_NAME);
		
		if (fileDB.exists())
			status = true;
		else {
			AssetManager assetManager = context.getAssets();
			InputStream in = null;
			OutputStream out = null;
			// new File(APP_PATH + File.separator + DB_FOLDER);
			String[] files = null;
			try {
				files = assetManager.list("");
			} catch (Exception e) {

			}

			for (String filename : files) {

				if (filename.contains("T1DM_DB")){
						//|| filename.contains("GL_GI_Database_PIPE")) {
					try {
						// filename = GL_GI_Database_CSV.csv
						in = assetManager.open(filename);
						out = new FileOutputStream(APP_PATH + File.separator
								+ DB_FOLDER + File.separator + filename);

						byte[] buffer = new byte[1024];
						int read;
						while ((read = in.read(buffer)) != -1) {
							out.write(buffer, 0, read);
						}
						status = true;
					} catch (Exception e) {
						status = false;
					} finally {
						try {
							if (in != null)
								in.close();
							if (out != null)
								out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return status;
	}
	
	public Class<?> decideNextActivity()

	{		

		DatabaseHandler dbHandler = new DatabaseHandler();

		Class<?> nextClass = null;

		if (dbHandler!=null)

			nextClass = (dbHandler.isUserCreated() == 0)? HomeScreen.class : UserDetailsActivity.class; 

	    return nextClass;

	}

	

	public boolean dropDatabase()

	{		

		DatabaseHandler dbHandler = new DatabaseHandler();

		boolean status = false;

		if (dbHandler!=null)

			status = dbHandler.dropDatabase();

	    return status;

	}

	

	public boolean createDatabaseFolder()
	{
		File dBFolderPath = new File(APP_PATH + File.separator + DB_FOLDER);
		return dBFolderPath.mkdirs();
	}

	public boolean createChartsFolder()
	{
		File folderPath = new File(APP_PATH + File.separator + CHARTS_FOLDER);
		return folderPath.mkdirs();
	}
	
	public boolean createReportsFolder()
	{
		File folderPath = new File(APP_PATH + File.separator + REPORT_FOLDER);
		return folderPath.mkdirs();
	}
	
	public boolean createRecordingsFolder()

	{

		File folderPath = new File(APP_PATH + File.separator + RECORDING_FOLDER);

		return folderPath.mkdirs();

	}

	public boolean createCapturesFolder()

	{

		File folderPath = new File(APP_PATH + File.separator + CAPTURE_FOLDER);

		return folderPath.mkdirs();

	}

	public long convertDateTimeToEpoch(String dateTime)

	{

		long epoch = 0;

		SimpleDateFormat input_df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa");

		SimpleDateFormat output_df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

		
		try {

			output_df.setTimeZone(GregorianCalendar.getInstance().getTimeZone());

			input_df.setTimeZone(GregorianCalendar.getInstance().getTimeZone());

			Date date12hr = input_df.parse(dateTime);;

		    String date24hr_str = output_df.format(date12hr);

		    Date date24hr = output_df.parse(date24hr_str);

			epoch = date24hr.getTime();

		} catch (ParseException e) {

			

			return epoch;			

		}

	    

		return epoch;

	}


	public long convertDateTimeToEpoch24Hr(String dateTime)

	{

		long epoch = 0;

		//SimpleDateFormat input_df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa");

		SimpleDateFormat output_df = new SimpleDateFormat("MMM dd, yyyy HH:mm");

		

	    

		try {

			output_df.setTimeZone(GregorianCalendar.getInstance().getTimeZone());

			//input_df.setTimeZone(GregorianCalendar.getInstance().getTimeZone());

			//Date date12hr = input_df.parse(dateTime);;

		    //String date24hr_str = output_df.format(dateTime);

		    Date date24hr = output_df.parse(dateTime);

			epoch = date24hr.getTime();

		} catch (ParseException e) {

			

			return epoch;			

		}

	    

		return epoch;

	}


	public String convertEpochToDateTime(long epoch)

	{

		String dateTime = null;

		try{

		Date date = new Date(epoch);

		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa");

        df.setTimeZone(TimeZone.getDefault());

        dateTime = df.format(date);

		}catch(Exception e){

			dateTime = null;

			return dateTime;

		}

		return dateTime;

	}
	

	public void setAlarm(Long alarmTime, Intent intentSchedule, Context context, String repeat)
	{
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		int days = 1;
		if (repeat.equals(this.REPEAT.get(0)))
			days = 1;
		if (repeat.equals(this.REPEAT.get(1)))
			days = 7;
		if (repeat.equals(this.REPEAT.get(2)))
			days = 14;
		if (repeat.equals(this.REPEAT.get(3)))
			days = 28;
		
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(context,1,  intentSchedule, PendingIntent.FLAG_UPDATE_CURRENT));
	}
	
	public void setScheduleAlarm(int hour, int minute, Context context)
	{
		Calendar alarmTime = Calendar.getInstance();

		alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);

        Intent alarmIntent = new Intent(context, ScheduleAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(pendingIntent);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        
	}

	public void showNotification(Context context, PendingIntent pendingIntent, int notificationID, boolean autoCancel, String title, String text){
		NotificationCompat.Builder notificationBuilder =
    	        new NotificationCompat.Builder(context)
    	        .setSmallIcon(R.drawable.ic_launcher)
    	        .setContentTitle(title)
    	        .setContentText(text)
    	        .setAutoCancel(autoCancel);
     notificationBuilder.setContentIntent(pendingIntent);
     
    NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
   
    notificationManager.notify(notificationID, notificationBuilder.build());
	}
	
	private String prepareContent(String from, String to, long time){
		StringBuilder content = new StringBuilder();
		List<MonitoringReadings> readings = DATABASE_HANDLER.getMonitoringReadings(from, to);
		
		UserDetails userDetails = DATABASE_HANDLER.getUserDetail();
		String height = DATABASE_HANDLER.updateAndGetHeight("", "");
		String weight = DATABASE_HANDLER.updateAndGetWeight("");
		String gender = DATABASE_HANDLER.updateAndGetGender("");
		
		
		content.append( "<!DOCTYPE html><html><body><h1>T1DM Report</h1><h2>From:" + from + 
				"</h2><h2>To:" + to + "</h2><h3>Patient's Name: "+ userDetails.get_NAME() +
				", Dr. Name: " + userDetails.get_DrNAME() +"</h3> "
						+ "<h3>Height: "+ ((height==null || height.equals(""))?"EMPTY":height) 
								+ " Weight: " + ((weight==null || weight.equals(""))?"EMPTY":weight) + "Kgs"
								+ " Gender: " + ((gender==null || gender.equals(""))?"EMPTY":gender) 
								+ " Age: " + userDetails.get_AGE() + "Years"
										+ "</h3>");
		
		List<ModelInsulin> insulinDosage = DATABASE_HANDLER.getInsulinDosage();
		if (insulinDosage.size()>0){
		content.append("<p>Current Insulin Dosage</p>");
		content.append("<table border=1><tr><td>Dose Time</td><td>Units</td></tr>");
		
		for(int i=0;i< insulinDosage.size();i++){
			content.append("<tr>");
			content.append("<td>");
			content.append(insulinDosage.get(i).getText());
			content.append("</td>");
			content.append("<td>");
			content.append(insulinDosage.get(i).getUnits());
			content.append("</td>");
			content.append("</tr>");
		}
		content.append("</table>");
		}
		
		List<FoodModel> mealPlan = DATABASE_HANDLER.getMealPlan();
		if (mealPlan.size()>0){
		content.append("<p>Meal Plan</p>");
		content.append("<table border=1><tr><td>Time</td><td>Food Details</td></tr>");
		
		for (int  i=0;i<mealPlan.size();i++){
			content.append("<tr>");
			content.append("<td>");
			content.append(mealPlan.get(i).get_FoodType());
			content.append("</td>");
			content.append("<td>");
			content.append(mealPlan.get(i).get_FoodName());
			content.append("</td>");
			content.append("</tr>");
		}
		content.append("</table>");
		}
		
		if (time>0){
			content.append(  "<p>Blood Sugar Monitoring</p><img src=\"cid:image_chart\" alt=\"T1DM Chart\"><p>Daily Recordings</p>");			
		}
		else
			content.append( "<p>Blood Sugar Monitoring</p><p>Daily Recordings</p>");
					
		content.append("<table border=1><tr><td>Reading Date</td><td>Time</td><td>Reading</td><td>Meal</td><td>Insulin</td>"
				+ "<td>Miscellaneous</td></tr>");
		for (int i=0; i< readings.size(); i++){
			content.append("<tr>");
			content.append("<td>");
			content.append(readings.get(i).get_Date());
			content.append("</td>");
			content.append("<td>");
			content.append(readings.get(i).get_Timestamp());
			content.append("</td>");
			content.append("<td>");
			content.append( Integer.toString(readings.get(i).get_Reading()));
			content.append("</td>");
			content.append("<td>");
			content.append((readings.get(i).get_Meal()!=null)?readings.get(i).get_Meal():"");
			content.append("</td>");
			content.append("<td>");
			content.append(Integer.toString(readings.get(i).get_Insulin()));
			content.append("</td>");
			content.append("<td>");
			content.append((readings.get(i).get_Misc()!=null)?readings.get(i).get_Misc():"");
			content.append("</td>");
			content.append("</tr>");
		}
		content.append("</table></body></html>");
		return content.toString();
	}

	public void PrepareAndSendEmail(String fromDate, String toDate, long time_ms, String imagePath, Context context){
		String emailContent = prepareContent(fromDate, toDate, time_ms);
        
		UserDetails user = DATABASE_HANDLER.getUserDetail();
		SendEmail sendEmail = new SendEmail(user.get_EMAIL(), emailContent);
		try {
			sendEmail.sendEmail(imagePath);
			
			
		} catch (Exception e) {
			
		}
	}
	
	public boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}