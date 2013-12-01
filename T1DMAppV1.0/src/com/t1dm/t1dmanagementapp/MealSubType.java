package com.t1dm.t1dmanagementapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MealSubType extends Activity {

	private Button btnAdd;
	private Intent intent;
	private String foods;
	private RadioGroup rgMealSubtype ;
	private RadioButton rbSelected ;
	
	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_meal_sub_type);
		
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
		
		rgMealSubtype = (RadioGroup)findViewById(R.id.rgMealSubtype);
		
		intent = getIntent();
		foods = intent.getStringExtra("Foods");
		btnAdd = (Button)findViewById(R.id.btnAdd);
		
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO: Update Table with these selected Foods
				int selectedId = rgMealSubtype.getCheckedRadioButtonId();
				rbSelected = (RadioButton) findViewById(selectedId);
				String subtype = rbSelected.getText().toString();
				if (!foods.equals("")){					
				if (appContext.getDbHandler().updateMeal(subtype, foods) != -1)
					Toast.makeText(MealSubType.this, "T1DM says, meal plan updated for "+subtype, Toast.LENGTH_LONG).show();
				else
					Toast.makeText(MealSubType.this, "T1DM says, oops could not update meal plan for "+subtype, Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
	}

}
