package com.t1dm.t1dmanagementapp;

import java.util.List;

import android.os.Bundle;
import android.widget.Toast;
import android.app.ListActivity;

public class ShowMealPlan extends ListActivity {

	private T1DMApplication appContext;
	private List<FoodModel> model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
		
		model = getModel();
		if (model.size()>0){
		MealPlanAdapter adapter = new MealPlanAdapter(this,
				model, R.layout.activity_show_meal_plan);
		setListAdapter(adapter);
		}else{
			Toast.makeText(this, "T1DM says, oops please add a meal plan", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private List<FoodModel> getModel() {		
		
		List<FoodModel> foodModel = appContext.getDbHandler().getMealPlan();
		
		return foodModel;
	}

}
