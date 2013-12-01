package com.t1dm.t1dmanagementapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;





import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
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
						sel=sel+","+value.get_FoodName();
			}
			
			Intent intent=new Intent(this, MealSubType.class);
			intent.putExtra("Foods", sel);			
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	private List<FoodModel> getModel(String search) {
		
		List<FoodModel> list = appContext.getDbHandler().getMatchingFoodItems(search);
		
		return list;
	}
}
