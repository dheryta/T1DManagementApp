package com.t1dm.t1dmanagementapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AvailableFoods extends ListActivity {

	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;
	private ListView foodListView;
	private EditText searchText;
	private AvailableFoodsAdapter adapter;
	private String carbs, calories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_available_foods);
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
		
		
		foodListView = (ListView)findViewById(android.R.id.list);
		adapter = new AvailableFoodsAdapter(this,
				getModel(""), R.layout.available_foods_layout);
		foodListView.setAdapter(adapter);
		
		searchText=(EditText)findViewById(R.id.searchBox);
		
		searchText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapter = new AvailableFoodsAdapter(AvailableFoods.this,
						getModel(s.toString()), R.layout.available_foods_layout);
				foodListView.setAdapter(adapter);				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.available_foods, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_addFood:
			foodListView = (ListView)findViewById(android.R.id.list);
			
			String sel="";
			for (int i=0; i<foodListView.getAdapter().getCount();i++){
				FoodModel value = (FoodModel) foodListView.getAdapter().getItem(i);
				if(value.isSelected())
						sel=value.get_FoodName()+", "+sel;
			}
			if (!sel.equals("")){
			String s=	sel.replace(sel.charAt(sel.lastIndexOf(',')), '.');
			Intent intent=new Intent(this, MealSubType.class);
			intent.putExtra("Foods", s);			
			startActivity(intent);
			}else
				Toast.makeText(AvailableFoods.this, "T1DM says, select some foods", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_showInfo:
			StringBuilder information = new StringBuilder();
			carbs = appContext.getDbHandler().updateAndGetCarbs("");
			if (carbs!=null && !carbs.equals(""))
				information.append("Your daily carbohydrate requirement(approx.:"+carbs+"grams\n\n");
			
			calories = appContext.getDbHandler().updateAndGetCalories("");
			if (calories!=null && !calories.equals(""))
				information.append("Your daily calories requirement(approx.):"+calories+"\n\n");
			
			information.append("GI Range: Low (<=55), Medium (56 - 69) and High (>=70)\n\n");
			information.append("GL Range: Low (<=10), Medium (11 - 19) and High (>=20)\n\n");
			
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("T1DM - Information");
			builder.setMessage(information);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				 
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                	//finish();
                }
            });
			builder.show();
		}
		return super.onOptionsItemSelected(item);
	}
	

	private List<FoodModel> getModel(String search) {
		
		List<FoodModel> list = appContext.getDbHandler().getMatchingFoodItems(search);
		
		return list;
	}
}
