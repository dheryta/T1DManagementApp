package com.t1dm.t1dmanagementapp;

/**

 * 

 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author dheryta
 * 
 * 
 */

public class DatabaseHandler {

	private CommonMethods commonMethods = new CommonMethods();

	// Table Names

	private static final String TBL_USER = "user";

	private static final String TBL_FOODS = "foods";

	private static final String TBL_ACTIVITIES = "activities";

	private static final String TBL_ACTIVITY_MEAL = "meals";

	private static final String TBL_ACTIVITY_INSULIN = "insulin";

	private static final String TBL_ACTIVITY_BG = "bg_checking";

	private static final String TBL_ACTIVITY_EXERCISE = "exercise";

	private static final String TBL_ACTIVITY_SLEEP = "sleep";

	private static final String TBL_SUGGESTIONS = "suggestions";

	private static final String TBL_INSULIN_DOSAGE = "insulin_dosage";

	private static final String TBL_SCHEDULE = "schedule";

	private static final String TBL_MONITORING = "monitoring";

	private static final String TBL_GI_INDEX = "giIndex";

	private static final String TBL_USER_PROFILING = "userProfiling";

	private long uid;

	private long aid;

	private long sid;

	private long upid;

	private ArrayList<Long> amid = new ArrayList<Long>();

	private ArrayList<Long> aiid = new ArrayList<Long>();

	private ArrayList<Long> abid = new ArrayList<Long>();

	private ArrayList<Long> aeid = new ArrayList<Long>();

	private ArrayList<Long> asid = new ArrayList<Long>();

	private ArrayList<Long> cid = new ArrayList<Long>();

	private static final String CREATE_TBL_USER = "CREATE TABLE  IF NOT EXISTS "
			+ TBL_USER

			+ "(UID INTEGER PRIMARY KEY, NAME TEXT, AGE INTEGER, EMAIL TEXT, DrNAME TEXT, "

			+ "DrPHONE TEXT, ADDRESS TEXT, EMERGENCY TEXT, DUMMY INTEGER, USEAUDIO BOOLEAN, EMERGENCY_ENABLED BOOLEAN"
			+ "BMI_BMR TEXT, CALORIES TEXT, INSULIN TEXT, CARBS TEXT"
			+ "HEIGHT_FT TEXT, HEIGHT_IN TEXT, WEIGHT TEXT, GENDER TEXT, ACTIVITY_LEVEL TEXT)";

	private static final String CREATE_TBL_FOODS = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_FOODS

			+ "(FID INTEGER PRIMARY KEY, FOOD_NAME TEXT, GI TEXT, GL TEXT, SERVING_SIZE_GRAMS TEXT,"
			+ "SERVING_SIZE_OZ TEXT, CARBS TEXT, " + "CALORIES TEXT)";

	private static final String CREATE_TBL_ACTIVITIES = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_ACTIVITIES + "(AID INTEGER PRIMARY KEY, ACTIVITY_NAME TEXT)";

	private static final String CREATE_TBL_ACTIVITY_MEAL = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_ACTIVITY_MEAL

			+ "(AID INTEGER PRIMARY KEY, ACTIVITY_SUBTYPE TEXT, ACTIVITY_TIME TEXT, FOOD_DETAILS TEXT)";

	private static final String CREATE_TBL_ACTIVITY_INSULIN = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_ACTIVITY_INSULIN

			+ "(AID INTEGER PRIMARY KEY, ACTIVITY_SUBTYPE TEXT, ACTIVITY_TIME TEXT)";

	private static final String CREATE_TBL_ACTIVITY_BG = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_ACTIVITY_BG

			+ "(AID INTEGER PRIMARY KEY, ACTIVITY_SUBTYPE TEXT, ACTIVITY_TIME TEXT)";

	private static final String CREATE_TBL_ACTIVITY_EXERCISE = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_ACTIVITY_EXERCISE

			+ "(AID INTEGER PRIMARY KEY, ACTIVITY_SUBTYPE TEXT, ACTIVITY_TIME TEXT)";

	private static final String CREATE_TBL_ACTIVITY_SLEEP = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_ACTIVITY_SLEEP

			+ "(AID INTEGER PRIMARY KEY, ACTIVITY_SUBTYPE TEXT, ACTIVITY_TIME TEXT)";

	private static final String CREATE_TBL_SUGGESTIONS = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_SUGGESTIONS

			+ "(AID INTEGER PRIMARY KEY, ACTIVITY_SUBTYPE TEXT, ACTIVITY_TIME TEXT)";

	private static final String CREATE_TBL_SCHEDULE = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_SCHEDULE

			+ "(SID INTEGER PRIMARY KEY, S_AID INTEGER,  ACTIVITY_TIME DATETIME, "

			+ "FOREIGN KEY(S_AID) REFERENCES " + TBL_ACTIVITIES + "(AID))";

	private static final String CREATE_TBL_MONITORING = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_MONITORING

			+ "(MID INTEGER PRIMARY KEY, DATESTAMP DATETIME, TIMESTAMP DATETIME, INSULIN INTEGER, "

			+ "BGLUCOSE INTEGER, FOOD TEXT, MISCELLANEOUS TEXT)";

	private static final String CREATE_TBL_GI_INDEX = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_GI_INDEX

			+ "(GID INTEGER PRIMARY KEY, FOOD_ITEM TEXT, GI_VALUE INTEGER, EMERGENCY BOOLEAN)";

	private static final String CREATE_TBL_USER_PROFILING = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_USER_PROFILING

			+ "(UPID INTEGER PRIMARY KEY, PROFILE_LOADED BOOLEAN)";

	private static final String CREATE_TBL_INSULIN_DOSAGE = "CREATE TABLE  IF NOT EXISTS "

			+ TBL_INSULIN_DOSAGE

			+ "(IID INTEGER PRIMARY KEY, TIME_INSTANCE TEXT, UNITS INTEGER, ENABLED BOOLEAN)";

	private static final int MAX_ACTIVITIES = 5;
	private static final int MAX_INSULIN_DOSAGE = 6;
	private static final int MAX_ROWS = 1;

	public DatabaseHandler() {

		loadAllIDs();

	}

	private void loadAllIDs()

	{

		this.uid = 1;// getPrimaryKeyID(TBL_USER, "uid");

		// this.amid = 1;//getPrimaryKeyID(TBL_ACTIVITY_MEAL, "aid");

		for (int i = 1; i <= MAX_ACTIVITIES; i++)
			this.amid.add(Long.valueOf(i));

		// this.aiid = 1;//getPrimaryKeyID(TBL_ACTIVITY_INSULIN, "aid");

		for (int i = 1; i <= MAX_ACTIVITIES; i++)
			this.aiid.add(Long.valueOf(i));

		// this.abid = 1;//getPrimaryKeyID(TBL_ACTIVITY_BG, "aid");

		for (int i = 1; i <= MAX_ACTIVITIES; i++)
			this.abid.add(Long.valueOf(i));

		// this.aeid = 1;//getPrimaryKeyID(TBL_ACTIVITY_EXERCISE, "aid");

		for (int i = 1; i <= MAX_ACTIVITIES; i++)
			this.aeid.add(Long.valueOf(i));

		// this.asid = 1;//getPrimaryKeyID(TBL_ACTIVITY_SLEEP, "aid");

		for (int i = 1; i <= MAX_ACTIVITIES; i++)
			this.asid.add(Long.valueOf(i));

		this.sid = MAX_ROWS;// getPrimaryKeyID(TBL_SUGGESTIONS, "aid");

		this.upid = MAX_ROWS;// getPrimaryKeyID(TBL_USER_PROFILING, "upid");

	}

	public void onCreate(SQLiteDatabase T1DM_Database) {

		T1DM_Database.execSQL(CREATE_TBL_USER);

		T1DM_Database.execSQL(CREATE_TBL_ACTIVITIES);

		T1DM_Database.execSQL(CREATE_TBL_ACTIVITY_MEAL);

		T1DM_Database.execSQL(CREATE_TBL_ACTIVITY_INSULIN);

		T1DM_Database.execSQL(CREATE_TBL_ACTIVITY_BG);

		T1DM_Database.execSQL(CREATE_TBL_ACTIVITY_EXERCISE);

		T1DM_Database.execSQL(CREATE_TBL_ACTIVITY_SLEEP);

		T1DM_Database.execSQL(CREATE_TBL_SUGGESTIONS);

		T1DM_Database.execSQL(CREATE_TBL_SCHEDULE);

		T1DM_Database.execSQL(CREATE_TBL_MONITORING);

		T1DM_Database.execSQL(CREATE_TBL_GI_INDEX);

		T1DM_Database.execSQL(CREATE_TBL_USER_PROFILING);

		T1DM_Database.execSQL(CREATE_TBL_INSULIN_DOSAGE);

		T1DM_Database.execSQL(CREATE_TBL_FOODS);

	}

	public boolean dropDatabase() {

		boolean status = false;

		File dbFile = new File(commonMethods.APP_PATH + File.separator
				+ commonMethods.DB_FOLDER + File.separator
				+ commonMethods.DB_NAME);

		if (dbFile.exists())

			status = dbFile.delete();

		else

			status = true;

		return status;

	}

	private boolean loadAllFoods() {
		boolean retVal = true;
		// Read csv and load all values in table foods
		// Food Glycemic Index Glycemic Load Serving Size grams serving oz
		// Avail. Carb per serving
		// + "(FID INTEGER PRIMARY KEY, FOOD_NAME TEXT, GI TEXT, GL TEXT,
		// SERVING_SIZE_GRAMS TEXT,
		// SERVING_SIZE_OZ TEXT, CARBS TEXT, "
		// + "CALORIES TEXT)"
		String foodsCSV = commonMethods.APP_PATH + File.separator
				+ commonMethods.DB_FOLDER + File.separator
				+ commonMethods.FOODS;
		BufferedReader br = null;
		String line = "";
		String splitter = "|";
		int row = 1;

		int count = getRecordCount(TBL_FOODS);
		if (count < 1255) {
			SQLiteDatabase db = this.getWritableDatabase();

			if (db != null) {
				try {

					br = new BufferedReader(new FileReader(foodsCSV));
					while ((line = br.readLine()) != null) {
						if (row > 1) {
							String[] rowEntry = line.split("\\|");

							ContentValues values = new ContentValues();
							for (int i=0; i< rowEntry.length; i++){
							if (i == 0)	
							values.put("FOOD_NAME", rowEntry[0]);					
							if (i == 1)
							values.put("GI", rowEntry[1]);
							if (i == 2)
							values.put("GL", rowEntry[2]);
							if (i == 3)
							values.put("SERVING_SIZE_GRAMS", rowEntry[3]);
							if (i == 4)
							values.put("SERVING_SIZE_OZ", rowEntry[4]);
							if (i == 5)
							values.put("CARBS", rowEntry[5]);
							// TODO: values.put("CALORIES", value);
							
							}
							if (rowEntry.length >= 1 ){
							db.insert(TBL_FOODS, null, values);
							Log.i("T1DM", "Inserted row:"+retVal);
							Thread.sleep(300);}
						}
						row = row + 1;
					}

				} catch (Exception e) {
					retVal = false;
					return retVal;
				} finally {
					db.close();
					if (br != null) {
						try {
							br.close();
						} catch (Exception e) {

						}
					}
				}

			}
		} else
			retVal = true;

		return retVal;
	}

	public List<FoodModel> getMatchingFoodItems(String foodName) {
		List<FoodModel> matchingFoods = new ArrayList<FoodModel>();
		UserDetails user = null;

		String selectQuery = "SELECT  * FROM " + TBL_FOODS
				+ " WHERE FOOD_NAME LIKE \"%" + foodName + "%\"";
		Cursor cursor = null;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			if (db != null) {

				cursor = db.rawQuery(selectQuery, null);

				if (cursor.moveToFirst()) {

					while (!cursor.isAfterLast()) {
						FoodModel food = new FoodModel();
						/*
						 * +
						 * "(FID INTEGER PRIMARY KEY, FOOD_NAME TEXT, GI TEXT, GL TEXT, SERVING_SIZE_GRAMS TEXT,"
						 * + "SERVING_SIZE_OZ TEXT, CARBS TEXT, " +
						 * "CALORIES TEXT)";
						 */
						food.set_FoodName(cursor.getString(cursor
								.getColumnIndex("FOOD_NAME")));
						food.set_GI(cursor.getString(cursor
								.getColumnIndex("GI")));
						food.set_GL(cursor.getString(cursor
								.getColumnIndex("GL")));
						food.set_Quantity(cursor.getString(cursor
								.getColumnIndex("SERVING_SIZE_GRAMS"))
								+ "/"
								+ cursor.getString(cursor
										.getColumnIndex("SERVING_SIZE_OZ")));
						food.set_Carbs(cursor.getString(cursor
								.getColumnIndex("CARBS")));
						// TODO:
						// food.set_Calories(cursor.getString(cursor.getColumnIndex("CALORIES")));
						matchingFoods.add(food);
						cursor.moveToNext();
					}
				}
			}
		} catch (Exception ex) {
			matchingFoods = new ArrayList<FoodModel>();
			return matchingFoods;
		} finally {

			if (cursor != null)

				cursor.close();

			db.close();

		}

		return matchingFoods;
	}

	// Returns 1 on success else 0

	public long insertUser(UserDetails userDetails) {

		SQLiteDatabase db = this.getWritableDatabase();

		long user_id = -1;

		if (db != null) {

			ContentValues values = new ContentValues();

			values.put("NAME", userDetails.get_NAME());

			values.put("AGE", userDetails.get_AGE());

			values.put("EMAIL", userDetails.get_EMAIL());

			values.put("DrNAME", userDetails.get_DrNAME());

			values.put("DrPHONE", userDetails.get_DrPHONE());

			values.put("ADDRESS", userDetails.get_ADDRESS());

			values.put("EMERGENCY", userDetails.get_EMERGENCY());

			values.put("DUMMY", 0);

			values.put("USEAUDIO", userDetails.is_useAUDIO());

			try {

				user_id = db.update(TBL_USER, values, " uid=" + this.uid, null);

			} catch (Exception e) {

				user_id = -1;

				return user_id;

			} finally {

				db.close();

			}

		}

		return user_id;

	}

	public long updateEmergencyEnabled(boolean enableEmergency) {

		SQLiteDatabase db = this.getWritableDatabase();

		long user_id = -1;

		if (db != null) {

			ContentValues values = new ContentValues();

			values.put("EMERGENCY_ENABLED", (enableEmergency) ? "true"
					: "false");

			try {

				user_id = db.update(TBL_USER, values, " uid=" + this.uid, null);

			} catch (Exception e) {

				user_id = -1;

				return user_id;

			} finally {

				db.close();

			}

		}
		return user_id;

	}

	public String updateAndGetBMIBMR(String BMI_BMR) {

		if (!BMI_BMR.equals("")) {
			SQLiteDatabase db = this.getWritableDatabase();

			String retVal = null;

			if (db != null) {

				ContentValues values = new ContentValues();

				values.put("BMI_BMR", BMI_BMR);

				try {

					db.update(TBL_USER, values, " uid=" + this.uid, null);
					retVal = BMI_BMR;

				} catch (Exception e) {

					retVal = null;

					return retVal;

				} finally {

					db.close();

				}
			}
			return retVal;
		} else {
			String selectQuery = "SELECT  BMI_BMR FROM " + TBL_USER;
			Cursor cursor = null;
			String retVal = null;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				if (db != null) {

					cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						retVal = cursor.getString(cursor
								.getColumnIndex("BMI_BMR"));
					}
				}
			} catch (Exception ex) {
				retVal = null;
				return retVal;
			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

			return retVal;

		}

	}

	public String updateAndGetInsulin(String insulin) {

		if (!insulin.equals("")) {
			SQLiteDatabase db = this.getWritableDatabase();

			String retVal = null;

			if (db != null) {

				ContentValues values = new ContentValues();

				values.put("INSULIN", insulin);

				try {

					db.update(TBL_USER, values, " uid=" + this.uid, null);
					retVal = insulin;

				} catch (Exception e) {

					retVal = null;

					return retVal;

				} finally {

					db.close();

				}
			}
			return retVal;
		} else {
			String selectQuery = "SELECT  INSULIN FROM " + TBL_USER;
			Cursor cursor = null;
			String retVal = null;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				if (db != null) {

					cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						retVal = cursor.getString(cursor
								.getColumnIndex("INSULIN"));
					}
				}
			} catch (Exception ex) {
				retVal = null;
				return retVal;
			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

			return retVal;

		}

	}

	public String updateAndGetCarbs(String carbs) {

		if (!carbs.equals("")) {
			SQLiteDatabase db = this.getWritableDatabase();

			String retVal = null;

			if (db != null) {

				ContentValues values = new ContentValues();

				values.put("CARBS", carbs);

				try {

					db.update(TBL_USER, values, " uid=" + this.uid, null);
					retVal = carbs;

				} catch (Exception e) {

					retVal = null;

					return retVal;

				} finally {

					db.close();

				}
			}
			return retVal;
		} else {
			String selectQuery = "SELECT  CARBS FROM " + TBL_USER;
			Cursor cursor = null;
			String retVal = null;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				if (db != null) {

					cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						retVal = cursor.getString(cursor
								.getColumnIndex("CARBS"));
					}
				}
			} catch (Exception ex) {
				retVal = null;
				return retVal;
			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

			return retVal;

		}

	}

	public String updateAndGetCalories(String calories) {

		if (!calories.equals("")) {
			SQLiteDatabase db = this.getWritableDatabase();

			String retVal = null;

			if (db != null) {

				ContentValues values = new ContentValues();

				values.put("CALORIES", calories);

				try {

					db.update(TBL_USER, values, " uid=" + this.uid, null);
					retVal = calories;

				} catch (Exception e) {

					retVal = null;

					return retVal;

				} finally {

					db.close();

				}
			}
			return retVal;
		} else {
			String selectQuery = "SELECT  CALORIES FROM " + TBL_USER;
			Cursor cursor = null;
			String retVal = null;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				if (db != null) {

					cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						retVal = cursor.getString(cursor
								.getColumnIndex("CALORIES"));
					}
				}
			} catch (Exception ex) {
				retVal = null;
				return retVal;
			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

			return retVal;

		}
	}

	public String updateAndGetHeight(String HEIGHT_FT, String HEIGHT_IN) {

		if (!HEIGHT_FT.equals("") && !HEIGHT_IN.equals("")) {
			SQLiteDatabase db = this.getWritableDatabase();

			String retVal = null;

			if (db != null) {

				ContentValues values = new ContentValues();

				values.put("HEIGHT_FT", HEIGHT_FT);
				values.put("HEIGHT_IN", HEIGHT_IN);

				try {

					db.update(TBL_USER, values, " uid=" + this.uid, null);
					retVal = HEIGHT_FT + "." + HEIGHT_IN;

				} catch (Exception e) {

					retVal = null;

					return retVal;

				} finally {

					db.close();

				}
			}
			return retVal;
		} else {
			String selectQuery = "SELECT  HEIGHT_FT,HEIGHT_IN FROM " + TBL_USER;
			Cursor cursor = null;
			String retVal = null;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				if (db != null) {

					cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						String ft = cursor.getString(cursor
								.getColumnIndex("HEIGHT_FT"));
						String in = cursor.getString(cursor
								.getColumnIndex("HEIGHT_IN"));
						retVal = ft + ":" + in;
					}
				}
			} catch (Exception ex) {
				retVal = null;
				return retVal;
			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

			return retVal;

		}

	}

	public String updateAndGetWeight(String WEIGHT) {

		if (!WEIGHT.equals("")) {
			SQLiteDatabase db = this.getWritableDatabase();

			String retVal = null;

			if (db != null) {

				ContentValues values = new ContentValues();
				try {

					values.put("WEIGHT", WEIGHT);
					db.update(TBL_USER, values, " uid=" + this.uid, null);
					retVal = WEIGHT;

				} catch (Exception e) {

					retVal = null;

					return retVal;

				} finally {

					db.close();

				}
			}
			return retVal;
		} else {
			String selectQuery = "SELECT  WEIGHT FROM " + TBL_USER;
			Cursor cursor = null;
			String retVal = null;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				if (db != null) {

					cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						retVal = Integer.toString(cursor.getInt(cursor
								.getColumnIndex("WEIGHT")));
					}
				}
			} catch (Exception ex) {
				retVal = null;
				return retVal;
			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

			return retVal;

		}
	}

	public String updateAndGetGender(String GENDER) {

		if (!GENDER.equals("")) {
			SQLiteDatabase db = this.getWritableDatabase();

			String retVal = null;

			if (db != null) {

				ContentValues values = new ContentValues();

				values.put("GENDER", GENDER);

				try {

					db.update(TBL_USER, values, " uid=" + this.uid, null);
					retVal = GENDER;

				} catch (Exception e) {

					retVal = null;

					return retVal;

				} finally {

					db.close();

				}
			}
			return retVal;
		} else {
			String selectQuery = "SELECT  GENDER FROM " + TBL_USER;
			Cursor cursor = null;
			String retVal = null;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				if (db != null) {

					cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						retVal = cursor.getString(cursor
								.getColumnIndex("GENDER"));
					}
				}
			} catch (Exception ex) {
				retVal = null;
				return retVal;
			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

			return retVal;

		}
	}

	public String updateAndGetActivityLevel(String ACTIVITY_LEVEL) {

		if (!ACTIVITY_LEVEL.equals("")) {
			SQLiteDatabase db = this.getWritableDatabase();

			String retVal = null;

			if (db != null) {

				ContentValues values = new ContentValues();

				values.put("ACTIVITY_LEVEL", ACTIVITY_LEVEL);

				try {

					db.update(TBL_USER, values, " uid=" + this.uid, null);
					retVal = ACTIVITY_LEVEL;

				} catch (Exception e) {

					retVal = null;

					return retVal;

				} finally {

					db.close();

				}
			}
			return retVal;
		} else {
			String selectQuery = "SELECT  ACTIVITY_LEVEL FROM " + TBL_USER;
			Cursor cursor = null;
			String retVal = null;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				if (db != null) {

					cursor = db.rawQuery(selectQuery, null);

					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						retVal = cursor.getString(cursor
								.getColumnIndex("ACTIVITY_LEVEL"));
					}
				}
			} catch (Exception ex) {
				retVal = null;
				return retVal;
			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

			return retVal;

		}
	}

	public boolean isEmergencyEnabled() {
		boolean EMERGENCY_ENABLED = false;

		String selectQuery = "SELECT  EMERGENCY_ENABLED FROM " + TBL_USER;
		Cursor cursor = null;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			if (db != null) {

				cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list

				if (cursor.moveToFirst()) {
					EMERGENCY_ENABLED = cursor.getString(
							cursor.getColumnIndex("EMERGENCY_ENABLED")).equals(
							"true") ? true : false;
				}
			}
		} catch (Exception ex) {
			EMERGENCY_ENABLED = false;
			return EMERGENCY_ENABLED;
		} finally {

			if (cursor != null)

				cursor.close();

			db.close();

		}

		return EMERGENCY_ENABLED;
	}

	public long updateUseAudio(boolean useAudio) {

		SQLiteDatabase db = this.getWritableDatabase();

		long user_id = -1;

		if (db != null) {

			ContentValues values = new ContentValues();

			values.put("USEAUDIO", (useAudio) ? "true" : "false");

			try {

				user_id = db.update(TBL_USER, values, " uid=" + this.uid, null);

			} catch (Exception e) {

				user_id = -1;

				return user_id;

			} finally {

				db.close();

			}

		}
		return user_id;

	}

	public boolean isUseAudioEnabled() {
		boolean useAudio = false;

		String selectQuery = "SELECT  USEAUDIO FROM " + TBL_USER;
		Cursor cursor = null;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			if (db != null) {

				cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list

				if (cursor.moveToFirst()) {
					useAudio = cursor.getString(
							cursor.getColumnIndex("USEAUDIO")).equals("true") ? true
							: false;
				}
			}
		} catch (Exception ex) {
			useAudio = false;
			return useAudio;
		} finally {

			if (cursor != null)

				cursor.close();

			db.close();

		}

		return useAudio;
	}

	public UserDetails getUserDetail() {
		UserDetails user = null;

		String selectQuery = "SELECT  * FROM " + TBL_USER;
		Cursor cursor = null;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			if (db != null) {

				cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list

				if (cursor.moveToFirst()) {
					user = new UserDetails();

					user.set_NAME(cursor.getString(cursor.getColumnIndex("NAME")));

					user.set_AGE(cursor.getInt(cursor.getColumnIndex("AGE")));

					user.set_EMAIL(cursor.getString(cursor.getColumnIndex("EMAIL")));

					user.set_DrNAME(cursor.getString(cursor.getColumnIndex("DrNAME")));

					user.set_DrPHONE(cursor.getString(cursor.getColumnIndex("DrPHONE")));

					user.set_ADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));

					user.set_EMERGENCY(cursor.getString(cursor.getColumnIndex("EMERGENCY")));

					user.set_useAUDIO((cursor.getString(
							cursor.getColumnIndex("USEAUDIO"))!=null && cursor.getString(
							cursor.getColumnIndex("USEAUDIO")).equals("true")) ? true
							: false);
				}
			}
		} catch (Exception ex) {
			user = null;
			return user;
		} finally {

			if (cursor != null)

				cursor.close();

			db.close();

		}

		return user;
	}

	public List<UserDetails> getUsers() {

		List<UserDetails> users = new ArrayList<UserDetails>();

		String selectQuery = "SELECT  * FROM " + TBL_USER;

		SQLiteDatabase db = this.getReadableDatabase();

		if (db != null) {

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list

			if (cursor.moveToFirst()) {

				do {

					UserDetails user = new UserDetails();

					user.set_NAME(cursor.getString(1));

					user.set_AGE(Integer.parseInt(cursor.getString(2)));

					user.set_EMAIL(cursor.getString(3));

					user.set_DrNAME(cursor.getString(4));

					user.set_DrPHONE(cursor.getString(5));

					user.set_ADDRESS(cursor.getString(6));

					user.set_EMERGENCY(cursor.getString(7));

					users.add(user);

				} while (cursor.moveToNext());

			}

			cursor.close();

			db.close();

		}

		return users;

	}

	public boolean checkExactlyOneRowInDatabase()

	{

		boolean status = false;

		boolean cu = ((getRecordCount(TBL_USER) == MAX_ROWS) ? true : false);

		boolean ca = ((getRecordCount(TBL_ACTIVITIES) == MAX_ROWS) ? true
				: false);

		boolean cam = ((getRecordCount(TBL_ACTIVITY_MEAL) == MAX_ACTIVITIES) ? true
				: false);

		boolean cai = ((getRecordCount(TBL_ACTIVITY_INSULIN) == MAX_ACTIVITIES) ? true
				: false);

		boolean cab = ((getRecordCount(TBL_ACTIVITY_BG) == MAX_ACTIVITIES) ? true
				: false);

		boolean cae = ((getRecordCount(TBL_ACTIVITY_EXERCISE) == MAX_ACTIVITIES) ? true
				: false);

		boolean cas = ((getRecordCount(TBL_ACTIVITY_SLEEP) == MAX_ACTIVITIES) ? true
				: false);

		boolean cs = ((getRecordCount(TBL_SUGGESTIONS) == MAX_ROWS) ? true
				: false);

		boolean cid = ((getRecordCount(TBL_INSULIN_DOSAGE) == MAX_INSULIN_DOSAGE) ? true
				: false);

		if (cu && ca && cam && cai && cab && cae && cas && cs && cid)

			status = true;

		return status;

	}

	public int getRecordCount(String TABLE_NAME) {

		int c = -1;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = null;

		if (db != null) {

			try {

				String count = "SELECT  * FROM " + TABLE_NAME;

				cursor = db.rawQuery(count, null);

				// return count

				c = cursor.getCount();

			} catch (Exception e) {

				c = -1;

				return c;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}

		return c;

	}

	// Returns 1 on success else 0

	public long insertActivitySchedule(

	List<ActivityScheduleDetail> activityDetail) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		long activity_id = -1;

		try {

			for (int i = 0; i < activityDetail.size(); i++) {

				activity_id = -1;

				values = new ContentValues();

				values.put("ACTIVITY_SUBTYPE", activityDetail.get(i)

				.get_SubType());

				values.put("ACTIVITY_TIME", activityDetail.get(i).get_Time());

				switch (activityDetail.get(i).get_Type()) {

				case 0:

					activity_id = db.update(TBL_ACTIVITY_MEAL, values, " aid="
							+ this.amid.get(i), null);

					break;

				case 1:

					activity_id = db.update(TBL_ACTIVITY_INSULIN, values,
							" aid=" + this.aiid.get(i), null);

					break;

				case 2:

					activity_id = db.update(TBL_ACTIVITY_BG, values, " aid="
							+ this.abid.get(i), null);

					break;

				case 3:

					activity_id = db

					.update(TBL_ACTIVITY_EXERCISE, values,
							" aid=" + this.aeid.get(i), null);

					break;

				case 4:

					activity_id = db.update(TBL_ACTIVITY_SLEEP, values, " aid="
							+ this.asid.get(i), null);

					break;
				case 5:

					activity_id = db.update(TBL_SUGGESTIONS, values, " aid="
							+ this.sid, null);

					break;

				}

			}

		}

		catch (Exception e) {

			activity_id = -1;

			return activity_id;

		} finally {

			db.close();

		}

		return activity_id;

	}

	// Returns 1 on success else 0
	public long updateUserProfiling(boolean profileLoaded) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		long pid = -1;

		if (db != null) {

			try {

				values = new ContentValues();

				values.put("PROFILE_LOADED", profileLoaded);

				pid = db.update(TBL_USER_PROFILING, values, " upid="
						+ this.upid, null);

			}

			catch (Exception e) {

				pid = -1;

				return pid;

			} finally {

				db.close();

			}

		}

		return pid;

	}

	// Returns 1 on success else 0
	public long updateMeal(String type, String details) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		long pid = -1;

		if (db != null) {

			try {

				values = new ContentValues();

				values.put("FOOD_DETAILS", details);

				pid = db.update(TBL_ACTIVITY_MEAL, values,
						" ACTIVITY_SUBTYPE=\"" + type + "\"", null);

			}

			catch (Exception e) {

				pid = -1;

				return pid;

			} finally {

				db.close();

			}

		}

		return pid;

	}

	public boolean getUserProfiling()

	{

		SQLiteDatabase db = this.getReadableDatabase();

		String status = null;

		Cursor cursor = null;

		boolean retVal = false;

		if (db != null) {

			try {

				String query = "SELECT * FROM " + TBL_USER_PROFILING;

				cursor = db.rawQuery(query, null);

				cursor.getCount();

				if (cursor.moveToFirst())

					status = cursor.getString(cursor
							.getColumnIndex("PROFILE_LOADED"));

			} catch (Exception e) {

				return false;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}

		if (status != null && status.equals("1"))

			retVal = true;

		return retVal;

	}

	public int isUserCreated()

	{

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = null;

		int ID = -1;

		if (db != null) {

			// db.close();

			try {

				String query = "SELECT DUMMY FROM " + TBL_USER;

				cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst())

					ID = cursor.getInt(cursor.getColumnIndex("DUMMY"));

			} catch (Exception e) {

				ID = -1;

				return ID;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}
		return ID;
	}

	public int getPrimaryKeyID(String TABLE_NAME, String Primary_Key)

	{

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = null;

		int keyID = -1;

		if (db != null) {

			// db.close();

			try {

				String query = "SELECT " + Primary_Key + " FROM " + TABLE_NAME;

				cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst())

					keyID = cursor.getInt(1);

			} catch (Exception e) {

				keyID = -1;

				return keyID;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}

		return keyID;

	}

	public String getEmergencyNumber()

	{

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = null;

		String emergency = "";

		if (db != null) {

			try {

				String query = "SELECT EMERGENCY FROM " + TBL_USER;

				cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst())

					emergency = cursor.getString(cursor
							.getColumnIndex("EMERGENCY"));

			} catch (Exception e) {

				emergency = "";

				return emergency;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}

		return emergency;

	}

	// Returns 1 on success else 0
	public long insertBGReading(MonitoringReadings reading) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		long reading_id = -1;

		if (db != null) {

			try {

				values = new ContentValues();

				// TIMESTAMP DATETIME, INSULIN INTEGER, BGLUCOSE INTEGER, FOOD
				// TEXT, MISCELLANEOUS TEXT

				values.put("DATESTAMP", reading.get_Date());

				values.put("TIMESTAMP", reading.get_Timestamp());

				values.put("INSULIN", reading.get_Insulin());

				values.put("BGLUCOSE", reading.get_Reading());

				values.put("FOOD", reading.get_Meal());

				values.put("MISCELLANEOUS", reading.get_Misc());

				reading_id = db.insert(TBL_MONITORING, null, values);

			}

			catch (Exception e) {

				reading_id = -1;

				return reading_id;

			} finally {

				db.close();

			}

		}

		return reading_id;

	}

	public long insertInsulinDosage(List<ModelInsulin> insulinDetails) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		long id = -1;

		try {

			for (int i = 0; i < insulinDetails.size(); i++) {

				id = -1;

				values = new ContentValues();

				values.put("TIME_INSTANCE", insulinDetails.get(i).getText());

				values.put("UNITS", insulinDetails.get(i).getUnits());

				values.put("ENABLED",
						Boolean.toString(insulinDetails.get(i).isSelected()));

				id = db.update(TBL_INSULIN_DOSAGE, values, " iid=" + (i + 1),
						null);
			}

		} catch (Exception e) {

			id = -1;

			return id;

		} finally {

			db.close();

		}

		return id;

	}

	public boolean insertOneRowInAllTables() {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values;

		boolean status = false;

		if (db != null) {

			try {

				if (getRecordCount(TBL_USER) < MAX_ROWS) {

					values = new ContentValues();

					values.put("NAME", "Dummy");
					values.put("DUMMY", 1);
					uid = db.insert(TBL_USER, null, values);

				}

				if (getRecordCount(TBL_ACTIVITIES) < MAX_ROWS)

				{

					values = new ContentValues();

					values.put("ACTIVITY_NAME", "Dummy");

					aid = db.insert(TBL_ACTIVITIES, null, values);

				}

				if (getRecordCount(TBL_ACTIVITY_MEAL) < MAX_ACTIVITIES)

				{

					values = new ContentValues();

					values.put("ACTIVITY_SUBTYPE", "Dummy");

					for (int i = 0; i < MAX_ACTIVITIES; i++)

						amid.add(db.insert(TBL_ACTIVITY_MEAL, null, values));

				}

				if (getRecordCount(TBL_ACTIVITY_INSULIN) < MAX_ACTIVITIES)

				{

					values = new ContentValues();

					values.put("ACTIVITY_SUBTYPE", "Dummy");

					for (int i = 0; i < MAX_ACTIVITIES; i++)

						aiid.add(db.insert(TBL_ACTIVITY_INSULIN, null, values));

				}

				if (getRecordCount(TBL_ACTIVITY_BG) < MAX_ACTIVITIES)

				{

					values = new ContentValues();

					values.put("ACTIVITY_SUBTYPE", "Dummy");

					for (int i = 0; i < MAX_ACTIVITIES; i++)

						abid.add(db.insert(TBL_ACTIVITY_BG, null, values));

				}

				if (getRecordCount(TBL_ACTIVITY_EXERCISE) < MAX_ACTIVITIES)

				{

					values = new ContentValues();

					values.put("ACTIVITY_SUBTYPE", "Dummy");

					for (int i = 0; i < MAX_ACTIVITIES; i++)

						aeid.add(db.insert(TBL_ACTIVITY_EXERCISE, null, values));

				}

				if (getRecordCount(TBL_ACTIVITY_SLEEP) < MAX_ACTIVITIES)

				{

					values = new ContentValues();

					values.put("ACTIVITY_SUBTYPE", "Dummy");

					for (int i = 0; i < MAX_ACTIVITIES; i++)

						asid.add(db.insert(TBL_ACTIVITY_SLEEP, null, values));

				}

				if (getRecordCount(TBL_SUGGESTIONS) < MAX_ROWS)

				{

					values = new ContentValues();

					values.put("ACTIVITY_SUBTYPE", "Dummy");

					sid = db.insert(TBL_SUGGESTIONS, null, values);

				}

				if (getRecordCount(TBL_USER_PROFILING) < MAX_ROWS)

				{

					values = new ContentValues();

					values.put("PROFILE_LOADED", 0);

					upid = db.insert(TBL_USER_PROFILING, null, values);

				}

				if (getRecordCount(TBL_INSULIN_DOSAGE) < MAX_INSULIN_DOSAGE)

				{

					values = new ContentValues();

					values.put("TIME_INSTANCE", "Dummy");

					for (int i = 0; i < MAX_INSULIN_DOSAGE; i++)

						asid.add(db.insert(TBL_INSULIN_DOSAGE, null, values));

				}

				if (getRecordCount(TBL_USER) > 0
						&& getRecordCount(TBL_ACTIVITIES) > 0
						&& getRecordCount(TBL_ACTIVITY_MEAL) > 0
						&& getRecordCount(TBL_ACTIVITY_INSULIN) > 0
						&& getRecordCount(TBL_ACTIVITY_BG) > 0
						&& getRecordCount(TBL_ACTIVITY_EXERCISE) > 0
						&& getRecordCount(TBL_ACTIVITY_SLEEP) > 0
						&& getRecordCount(TBL_SUGGESTIONS) > 0
						&& getRecordCount(TBL_USER_PROFILING) > 0
						&& getRecordCount(TBL_INSULIN_DOSAGE) > 0)

					status = true;

			}

			catch (Exception e) {

				return status;

			} finally {

				db.close();
				status = loadAllFoods();
				
			}

		}

		return status;

	}

	public ArrayList<ModelInsulin> getInsulinDosage() {
		ArrayList<ModelInsulin> insulinDosages = new ArrayList<ModelInsulin>();

		ModelInsulin dosage = null;

		SQLiteDatabase db = this.getReadableDatabase();

		String tableToRead = TBL_INSULIN_DOSAGE;
		Cursor cursor = null;

		if (db != null) {

			try {

				String query = "SELECT * FROM " + tableToRead;

				cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst()) {

					while (!cursor.isAfterLast()) {
						dosage = new ModelInsulin();
						// TIME_INSTANCE TEXT, UNITS INTEGER, ENABLED BOOLEAN)
						String timeInstance = cursor.getString(cursor
								.getColumnIndex("TIME_INSTANCE"));
						if (!timeInstance.equals("Dummy")) {
							dosage.setText(timeInstance);
							boolean selected = (cursor.getString(cursor
									.getColumnIndex("ENABLED")).equals("true")) ? true
									: false;
							dosage.setSelected(selected);
							dosage.setUnits(cursor.getInt(cursor
									.getColumnIndex("UNITS")));
							insulinDosages.add(dosage);
						}
						cursor.moveToNext();
					}

				}

			} catch (Exception e) {

				return insulinDosages;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}
		return insulinDosages;
	}

	public ArrayList<MonitoringReadings> getMonitoringReadings(String fromDate,
			String toDate) {

		SQLiteDatabase db = this.getReadableDatabase();

		MonitoringReadings reading = null;

		ArrayList<MonitoringReadings> readings = new ArrayList<MonitoringReadings>();

		Cursor cursor = null;

		if (db != null) {

			try {

				String query = "SELECT * FROM " + TBL_MONITORING
						+ " WHERE DATE(DATESTAMP) BETWEEN DATE('" + fromDate
						+ "') AND DATE('" + toDate + "')";

				cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst()) {

					while (!cursor.isAfterLast()) {
						reading = new MonitoringReadings();

						reading.set_MID(cursor.getInt(cursor
								.getColumnIndex("MID")));

						reading.set_Date(cursor.getString(cursor
								.getColumnIndex("DATESTAMP")));

						reading.set_Timestamp(cursor.getString(cursor
								.getColumnIndex("TIMESTAMP")));

						reading.set_Insulin(cursor.getInt(cursor
								.getColumnIndex("INSULIN")));

						reading.set_Meal(cursor.getString(cursor
								.getColumnIndex("FOOD")));

						reading.set_Misc(cursor.getString(cursor
								.getColumnIndex("MISCELLANEOUS")));

						reading.set_Reading(cursor.getInt(cursor
								.getColumnIndex("BGLUCOSE")));

						readings.add(reading);
						cursor.moveToNext();
					}

				}

			} catch (Exception e) {

				return readings;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}

		return readings;

	}

	public ArrayList<Model> getAlarms(int activityType) {
		ArrayList<Model> alarms = new ArrayList<Model>();

		Model alarm = null;

		SQLiteDatabase db = this.getReadableDatabase();

		String tableToRead = "";
		Cursor cursor = null;

		switch (activityType) {
		case CommonMethods.ACTIVITY_MEAL:
			tableToRead = TBL_ACTIVITY_MEAL;
			break;

		case CommonMethods.ACTIVITY_INSULIN:
			tableToRead = TBL_ACTIVITY_INSULIN;
			break;

		case CommonMethods.ACTIVITY_BGCHECKING:
			tableToRead = TBL_ACTIVITY_BG;
			break;

		case CommonMethods.ACTIVITY_EXERCISE:
			tableToRead = TBL_ACTIVITY_EXERCISE;
			break;

		case CommonMethods.ACTIVITY_SLEEP:
			tableToRead = TBL_ACTIVITY_SLEEP;
			break;

		case CommonMethods.ACTIVITY_SUGGESTION:
			tableToRead = TBL_SUGGESTIONS;
			break;
		}

		if (db != null) {

			try {

				String query = "SELECT * FROM " + tableToRead;

				cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst()) {

					while (!cursor.isAfterLast()) {
						alarm = new Model();
						// ACTIVITY_SUBTYPE TEXT, ACTIVITY_TIME TEXT)
						String activity = cursor.getString(cursor
								.getColumnIndex("ACTIVITY_SUBTYPE"));
						if (!activity.equals("Dummy")) {
							alarm.setName(activity);
							String time = cursor.getString(cursor
									.getColumnIndex("ACTIVITY_TIME"));
							int hour = -1;
							int minute = -1;

							if (time != "" && time != null && !time.equals("")) {
								String hour_minute[] = time.split(":");
								hour = Integer.parseInt(hour_minute[0]);
								minute = Integer.parseInt(hour_minute[1]);
								alarm.setSelected(true);
							} else {
								Calendar calendar = Calendar.getInstance();
								hour = calendar.get(Calendar.HOUR_OF_DAY);
								minute = calendar.get(Calendar.MINUTE);
								alarm.setSelected(false);
							}
							alarm.setHour(hour);
							alarm.setMinute(minute);

							alarms.add(alarm);
						}
						cursor.moveToNext();
					}

				}

			} catch (Exception e) {

				return alarms;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}
		return alarms;
	}

	public ArrayList<FoodModel> getMealPlan() {
		ArrayList<FoodModel> model = new ArrayList<FoodModel>();

		FoodModel meal = null;

		SQLiteDatabase db = this.getReadableDatabase();

		String tableToRead = TBL_ACTIVITY_MEAL;
		Cursor cursor = null;
		if (db != null) {

			try {

				String query = "SELECT * FROM " + tableToRead;

				cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst()) {

					while (!cursor.isAfterLast()) {
						meal = new FoodModel();

						String activity = cursor.getString(cursor
								.getColumnIndex("ACTIVITY_SUBTYPE"));
						String foodDetails = cursor.getString(cursor
								.getColumnIndex("FOOD_DETAILS"));
						if (foodDetails != null && activity != null
								&& !activity.equals("Dummy")
								&& !foodDetails.equals("")) {
							meal.set_FoodType(activity);
							meal.set_FoodName(foodDetails);
							model.add(meal);
						}
						cursor.moveToNext();
					}

				}

			} catch (Exception e) {

				return model;

			} finally {

				if (cursor != null)

					cursor.close();

				db.close();

			}

		}
		return model;
	}

	public SQLiteDatabase getReadableDatabase() {

		SQLiteDatabase db = null;

		try {

			db = SQLiteDatabase.openDatabase(commonMethods.APP_PATH
					+ File.separator + commonMethods.DB_FOLDER

					+ File.separator + commonMethods.DB_NAME, null,
					SQLiteDatabase.OPEN_READONLY);

		}

		catch (Exception e) {

		}

		finally {

			// if (db!=null)

			// db.close();

		}

		return db;

	}

	public SQLiteDatabase getWritableDatabase() {

		SQLiteDatabase db = null;

		try {

			db = SQLiteDatabase.openOrCreateDatabase(commonMethods.APP_PATH
					+ File.separator + commonMethods.DB_FOLDER

					+ File.separator + commonMethods.DB_NAME, null);

			if (db != null)

				onCreate(db);

		} catch (Exception e) {

		} finally {

			// if (db!=null)

			// db.close();

		}

		return db;

	}

}