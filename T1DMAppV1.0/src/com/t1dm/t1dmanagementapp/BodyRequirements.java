package com.t1dm.t1dmanagementapp;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BodyRequirements extends Activity {

	private EditText etFeet, etInch, etWeight;
	private Spinner spinnerGender, spinnerActivity;
	private EditText etBMI, etCalories, etInsulin, etCarbs;
	
	private final double SEDANTARY = 1.2;
	private final double LIGHT_ACTIVE = 1.375;
	private final double MODERATE_ACTIVE = 1.550;
	private final double VERY_ACTIVE = 1.725;
	private final double EXTREMELY_ACTIVE = 1.900;
	
	private int feet= 0, inch = 0, age = 0;
	private double weight = 0.0;
	private String gender = "Male";
	private String activityLevel = "Sedentary";
	
	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_body_requirements);
		
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
			
		etFeet = (EditText)findViewById(R.id.etFeet);
		etInch = (EditText)findViewById(R.id.etInches);
		etWeight = (EditText)findViewById(R.id.etWeight);
	
		etBMI = (EditText)findViewById(R.id.etBMI);
		etInsulin = (EditText)findViewById(R.id.etInsulin);
		etCalories = (EditText)findViewById(R.id.etCalories);
		etCarbs = (EditText)findViewById(R.id.etCarbs);
		
		spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
		spinnerActivity = (Spinner)findViewById(R.id.spinnerActivityLevel);
		
		loadSavedValues();
		
		etFeet.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
								
			}

			@Override
			public void afterTextChanged(Editable s) {
				try{
				feet = Integer.parseInt(s.toString());
				calculateBMI_BMR();
				calculateCalories();
				calculateInsulin();
				calculateCarbs();
			}catch(Exception e){}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		etInch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
								
			}

			@Override
			public void afterTextChanged(Editable s) {
				try{
				inch = Integer.parseInt(s.toString());
				calculateBMI_BMR();
				calculateCalories();				
				calculateInsulin();
				calculateCarbs();
				}catch(Exception e){}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
		});
		
		etWeight.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
								
			}

			@Override
			public void afterTextChanged(Editable s) {
				try{
				weight = Double.parseDouble(s.toString());
				calculateBMI_BMR();
				calculateCalories();
				calculateInsulin();
				calculateCarbs();
				}catch(Exception e){}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinnerGender.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
				gender = parent.getItemAtPosition(pos).toString();
				calculateBMI_BMR();
				calculateCalories();
				calculateInsulin();
				calculateCarbs();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinnerActivity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
				activityLevel = parent.getItemAtPosition(pos).toString();
				calculateBMI_BMR();
				calculateCalories();
				calculateInsulin();
				calculateCarbs();				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.body_requirements, menu);
		return true;
	}

	private String calculateBMI_BMR(){
		String BMI_BMR = "";
		try{
			double bmi = 0.0, bmr = 0.0;
			int age = 0;
			feet = Integer.parseInt(etFeet.getText().toString());
			inch = Integer.parseInt(etInch.getText().toString());
			weight = Double.parseDouble(etWeight.getText().toString());
			double height_m = ((feet * 30.48) + (inch * 2.54))/100;
			double height_cm = ((feet * 30.48) + (inch * 2.54));
			bmi = weight / (height_m * height_m);
			
			UserDetails user = appContext.getDbHandler().getUserDetail();
			age = user.get_AGE();
			
			if (gender.equals("Male"))
				bmr = (13.75*weight) + (5*height_cm) - (6.76*age) + 66;
			else if (gender.equals("Female"))
				bmr = (9.56*weight) + (1.85*height_cm) - (4.68*age) + 655;
			
			BMI_BMR = Double.toString(roundTo2Decimals(bmi))+"/"+Double.toString(roundTo2Decimals(bmr));
			
			appContext.getDbHandler().updateAndGetHeight(etFeet.getText().toString(), etInch.getText().toString());
			appContext.getDbHandler().updateAndGetWeight(Double.toString(weight));
			appContext.getDbHandler().updateAndGetGender(gender);
			appContext.getDbHandler().updateAndGetBMIBMR(BMI_BMR);
			etBMI.setText(BMI_BMR);
		}catch(Exception e){
			
		}
		return BMI_BMR;
	}
	
	private String calculateCalories(){
		String calories = "";
		try{
		String BMI_BMR = etBMI.getText().toString();
		String[] BMI_BMR_Vals = BMI_BMR.split("/");
		Double BMR = Double.parseDouble(BMI_BMR_Vals[1]);
		String[] activity_levels = getResources().getStringArray(R.array.activity_level_array);
		Double calories_d = 0.0;
		if (activityLevel.equals(activity_levels[0]))
			calories_d = SEDANTARY * BMR;
		else if (activityLevel.equals(activity_levels[1]))
			calories_d = LIGHT_ACTIVE * BMR;
		else if (activityLevel.equals(activity_levels[2]))
			calories_d = MODERATE_ACTIVE * BMR;
		else if (activityLevel.equals(activity_levels[3]))
			calories_d = VERY_ACTIVE * BMR;
		else if (activityLevel.equals(activity_levels[4]))
			calories_d = EXTREMELY_ACTIVE * BMR;
		
		calories = Double.toString( roundTo2Decimals(calories_d));
		etCalories.setText(calories);
		appContext.getDbHandler().updateAndGetCalories(calories);
		appContext.getDbHandler().updateAndGetBMIBMR(BMI_BMR);
		appContext.getDbHandler().updateAndGetActivityLevel(activityLevel);
		}catch(Exception e){}
		return calories;
	}
	
	private String calculateInsulin(){
		String Insulin = "";
		try{
			weight = Double.parseDouble(etWeight.getText().toString());
			
		double insulin = 0.55 * weight;
		Insulin = Double.toString(roundTo2Decimals(insulin));
		etInsulin.setText(Insulin);
		appContext.getDbHandler().updateAndGetInsulin(Insulin);
		appContext.getDbHandler().updateAndGetWeight(Double.toString(weight));
		}
		catch(Exception e){}
		return Insulin;
	}
	
	private String calculateCarbs(){
		String carbs = "";
		try{
		double insulin = Double.parseDouble(etInsulin.getText().toString());
		double carbs_g = roundTo2Decimals(10 * insulin);
		etCarbs.setText(Double.toString(carbs_g));
		appContext.getDbHandler().updateAndGetCarbs(Double.toString(carbs_g));
		}catch(Exception e){}
		return carbs;
	}
	
	private void loadSavedValues(){
		try{
		String height = appContext.getDbHandler().updateAndGetHeight("", "");
		String[] feet_inch = height.split(":");
		if (feet_inch.length == 2 && feet_inch[0]!=null && feet_inch[1]!=null){
		etFeet.setText(feet_inch[0]);
		etInch.setText(feet_inch[1]);
		}
		
		String weight = appContext.getDbHandler().updateAndGetWeight("");
		etWeight.setText(weight);
		
		String gender = appContext.getDbHandler().updateAndGetGender("");
		if (gender.equals("Male"))
			spinnerGender.setSelection(0);
		else if (gender.equals("Female"))
			spinnerGender.setSelection(1);
		
		String activity_level = appContext.getDbHandler().updateAndGetActivityLevel("");
		String[] activity_levels = getResources().getStringArray(R.array.activity_level_array);
		if (activity_level.equals(activity_levels[0]))
			spinnerActivity.setSelection(0);
		else if (activity_level.equals(activity_levels[1]))
			spinnerActivity.setSelection(1);
		
		String bmi_bmr = appContext.getDbHandler().updateAndGetBMIBMR("");
		etBMI.setText(bmi_bmr);
		
		String calories = appContext.getDbHandler().updateAndGetCalories("");
		etCalories.setText(calories);
		
		String insulin = appContext.getDbHandler().updateAndGetInsulin("");
		etInsulin.setText(insulin);
		String carbs = appContext.getDbHandler().updateAndGetCarbs("");
		}catch(Exception e){
			
		}
	}
	
	private double roundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
}


}
