package com.t1dm.t1dmanagementapp;



import java.io.File;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;



public class UserDetailsActivity extends Activity {

	private T1DMApplication appContext;
	private CommonMethods commonMethods = new CommonMethods();
	private MediaPlayer   mediaPlayer = null;
	private MediaRecorder  mediaRecorder = null;
	
	private String recordingName = commonMethods.APP_PATH + File.separator + commonMethods.RECORDING_FOLDER 
			+ File.separator + commonMethods.RECORDING_NAME;

	private boolean recording, playing;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_details);
		appContext = ((T1DMApplication) getApplication());
		appContext.setDbHandler(new DatabaseHandler());
		loadUserDetails();
		
	}

	

	@Override

	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.user_details, menu);
		return true;

	}



	@Override

	public boolean onOptionsItemSelected(MenuItem item) {



		switch (item.getItemId()) {

		case R.id.userDetails_Save:

			if (appContext.getDbHandler().checkExactlyOneRowInDatabase()) {

				btnSaveListener();

			} else {
				Toast.makeText(appContext.getContext(),
						"T1DM says, oops please restart", Toast.LENGTH_LONG)
						.show();
				finish();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);



	}


	private void loadUserDetails() {
		UserDetails user = appContext.getDbHandler().getUserDetail();
		if (user != null ) {
			TextView tvTemp;
			EditText etTemp;

			etTemp = (EditText) findViewById(R.id.txtAddress);
			etTemp.setText(user.get_ADDRESS());

			etTemp = (EditText) findViewById(R.id.txtAge);
			etTemp.setText( Integer.toString(user.get_AGE()));

			etTemp = (EditText) findViewById(R.id.txtDrName);
			etTemp.setText(user.get_DrNAME());
			
			etTemp = (EditText) findViewById(R.id.txtDrPhone);
			etTemp.setText(user.get_DrPHONE());
	
			etTemp = (EditText) findViewById(R.id.txtEmail);
			etTemp.setText(user.get_EMAIL());

			etTemp = (EditText) findViewById(R.id.txtEmergencyPhone);
			etTemp.setText(user.get_EMERGENCY());

			etTemp = (EditText) findViewById(R.id.txtName);
			if (!user.get_NAME().equals("Dummy"))
			etTemp.setText(user.get_NAME());
		}
	}

	private void btnSaveListener() {

		boolean raiseError = false;

		try {

			if (appContext.getDbHandler().getRecordCount("TBL_USER") == 1)

				Toast.makeText(appContext.getContext(),

						"T1DM says, only 1 user can use me !!!!",

						Toast.LENGTH_LONG).show();

			else {



				UserDetails userDetails = new UserDetails();

				TextView tvTemp;

				EditText etTemp;



				etTemp = (EditText) findViewById(R.id.txtAddress);

				if (etTemp.getText().toString().length() > 5)

					userDetails.set_ADDRESS(etTemp.getText().toString());

				else

					raiseError = true;



				tvTemp = (TextView) findViewById(R.id.txtAge);

				if (tvTemp.getText().toString().length() > 0)

					userDetails.set_AGE(Integer.parseInt(tvTemp.getText()

							.toString()));

				else

					raiseError = true;



				tvTemp = (TextView) findViewById(R.id.txtDrName);

				if (tvTemp.getText().toString().length() > 3)

					userDetails.set_DrNAME(tvTemp.getText().toString());

				else

					raiseError = true;



				etTemp = (EditText) findViewById(R.id.txtDrPhone);

				if (etTemp.getText().toString().length() >= 10

						&& PhoneNumberUtils.isGlobalPhoneNumber(etTemp

								.getText().toString()))

					userDetails.set_DrPHONE(etTemp.getText().toString());

				else

					raiseError = true;



				etTemp = (EditText) findViewById(R.id.txtEmail);

				if (checkEmail(etTemp.getText().toString()))

					userDetails.set_EMAIL(etTemp.getText().toString());

				else

					raiseError = true;



				etTemp = (EditText) findViewById(R.id.txtEmergencyPhone);

				if (etTemp.getText().toString().length() >= 10

						&& PhoneNumberUtils.isGlobalPhoneNumber(etTemp

								.getText().toString()))

					userDetails.set_EMERGENCY(etTemp.getText().toString());

				else

					raiseError = true;



				tvTemp = (TextView) findViewById(R.id.txtName);

				if (tvTemp.getText().toString().length() > 3)

					userDetails.set_NAME(tvTemp.getText().toString());

				else

					raiseError = true;



				if (!raiseError

						&& appContext.getDbHandler().insertUser(userDetails) > 0) {

					Toast.makeText(

							appContext.getContext(),

							"T1DM says, " + tvTemp.getText().toString()

									+ " can proceed further", Toast.LENGTH_LONG)

							.show();

					startActivity(new Intent(appContext.getContext(),

							HomeScreen.class));

				} else

					Toast.makeText(appContext.getContext(),

							"T1DM says, you missed something !!!!",

							Toast.LENGTH_LONG).show();

			}

		} catch (Exception e) {

			Toast.makeText(appContext.getContext(),

					"T1DM says, you missed something !!!!", Toast.LENGTH_LONG)

					.show();

		}

	}

	private boolean checkEmail(CharSequence email) {

		if (email == null) {

			return false;

		} else {

			return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

		}

	}
}