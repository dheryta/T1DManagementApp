package com.t1dm.t1dmanagementapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class ManageInsulinDosage extends ListActivity {
	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;
	
	private String[] values = new String[] { commonMethods.SUBTYPE_BREAKFAST,
			commonMethods.SUBTYPE_BRUNCH, commonMethods.SUBTYPE_LUNCH,
			commonMethods.SUBTYPE_SNACKS, commonMethods.SUBTYPE_DINNER, commonMethods.SUBTYPE_EXTRAS };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
		
		InsulinDosageAdapter adapter = new InsulinDosageAdapter(this, getModel());
		setListAdapter(adapter);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_insulin_dosage, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuDone:
			ListView listView = getListView();
			List<ModelInsulin> insulinDosage = new ArrayList<ModelInsulin>();
			for (int i = 0; i < listView.getAdapter().getCount(); i++) {
				ModelInsulin value = (ModelInsulin) listView.getAdapter().getItem(i);
				if (value.isSelected()){
					ModelInsulin dose = new ModelInsulin(value.getText());
					dose.setSelected(true);
					dose.setUnits(value.getUnits());
					insulinDosage.add(dose);
				}else{
					ModelInsulin dose = new ModelInsulin(value.getText());
					dose.setSelected(false);					
					dose.setUnits(0);
					insulinDosage.add(dose);
				}
			}
			
			if (appContext.getDbHandler().insertInsulinDosage(insulinDosage) == 1)
				startActivity(new Intent(appContext.getContext(),	HomeScreen.class));
			else
				Toast.makeText(ManageInsulinDosage.this, "T1DM says, oops error occurred", Toast.LENGTH_LONG).show();
			
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private List<ModelInsulin> getModel() {
		List<ModelInsulin> list = appContext.getDbHandler().getInsulinDosage();
		
		if (list.size() == 0)
		for (int i = 0; i < values.length; i++)
			list.add(new ModelInsulin(values[i]));

		return list;
	}
}
