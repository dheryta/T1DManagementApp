package com.t1dm.t1dmanagementapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class PlotReadings extends Activity {
	
	private CommonMethods commonMethods = new CommonMethods();
	private T1DMApplication appContext;

	private Spinner spinnerFromDay, spinnerFromMonth, spinnerFromYear;
	private Spinner spinnerToDay, spinnerToMonth, spinnerToYear;
	
	private int currentDay, currentMonth, currentYear;
	private int selectedToDay, selectedToMonth, selectedToYear;
	private int selectedFromDay, selectedFromMonth, selectedFromYear;
	
	private OnItemSelectedListener itemSelectedListener;
	
	private List<Integer> fromYears, toYears;
	private List<Integer> fromMonths, toMonths;
	private List<Integer> fromDays, toDays;
	
	private ArrayAdapter<Integer> toDayAdapter, fromDayAdapter;
	private ArrayAdapter<Integer> toMonthAdapter, fromMonthAdapter;
	private ArrayAdapter<Integer> toYearAdapter, fromYearAdapter;

	
	private Button btnSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plot_readings);
		
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());
		
		loadDaysMonthsYears();
		
		loadSpinners();
		btnSearch = (Button)findViewById(R.id.btnSearch);
		
		btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String month = (selectedFromMonth<10)?0+Integer.toString(selectedFromMonth):Integer.toString(selectedFromMonth);
				String day = (selectedFromDay<10)?0+Integer.toString(selectedFromDay):Integer.toString(selectedFromDay);
				
				String fromDate = selectedFromYear+"-"+month+"-"+day;
				
				month = (selectedToMonth<10)?0+Integer.toString(selectedToMonth):Integer.toString(selectedToMonth);
				day = (selectedToDay<10)?0+Integer.toString(selectedToDay):Integer.toString(selectedToDay);

				String toDate = selectedToYear+"-"+month+"-"+day;
				Intent intent = new Intent(appContext.getContext(), ReadingsChart.class);
				intent.putExtra("FromDate", fromDate);
				intent.putExtra("ToDate", toDate);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_done, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return true;
	}

	
	private void loadSpinners(){
		spinnerFromDay = (Spinner) findViewById(R.id.spinnerFromDay);
		spinnerFromMonth = (Spinner) findViewById(R.id.spinnerFromMonth);
		spinnerFromYear = (Spinner) findViewById(R.id.spinnerFromYear);
		
		spinnerToDay = (Spinner) findViewById(R.id.spinnerToDay);
		spinnerToMonth = (Spinner) findViewById(R.id.spinnerToMonth);
		spinnerToYear = (Spinner) findViewById(R.id.spinnerToYear);
		
		setSpinnerListeners();
		
		loadSpinnerData();		

	}
	
	private void loadDaysMonthsYears(){
		Calendar calendar = Calendar.getInstance();
		this.currentDay = calendar.get(Calendar.DATE);
		this.currentMonth = calendar.get(Calendar.MONTH)+1;
		this.currentYear = calendar.get(Calendar.YEAR);
		
		this.selectedFromDay = this.currentDay;
		this.selectedFromMonth = this.currentMonth;
		this.selectedFromYear = this.currentYear;

		this.selectedToDay = this.currentDay;
		this.selectedToMonth = this.currentMonth;
		this.selectedToYear = this.currentYear;

		fromYears = generateYears();
		toYears = generateYears();
		
		fromMonths = generateMonths();
		toMonths = generateMonths();
		
		fromDays = generateDays(currentMonth, currentYear);
		toDays = generateDays(currentMonth, currentYear);
	}
	
	private void setSelectedValue(int id, int position){
		switch(id){
		case R.id.spinnerFromDay:	
			this.selectedFromDay = this.fromDayAdapter.getItem(position);
			break;
		case R.id.spinnerFromMonth:
			this.selectedFromMonth = this.fromMonthAdapter.getItem(position);
			this.fromDays = generateDays(this.selectedFromMonth, this.selectedFromYear);
			fromDayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.fromDays);
			fromDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			fromDayAdapter.notifyDataSetChanged();
			spinnerFromDay.setAdapter(fromDayAdapter);
			spinnerFromDay.setSelection(this.fromDayAdapter.getPosition(1));
			break;
		case R.id.spinnerFromYear:
			this.selectedFromYear = this.fromYears.get(position);
			this.fromDays = generateDays(this.selectedFromMonth, this.selectedFromYear);
			fromDayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.fromDays);
			fromDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			fromDayAdapter.notifyDataSetChanged();
			spinnerFromDay.setAdapter(fromDayAdapter);
			spinnerFromDay.setSelection(this.fromDayAdapter.getPosition(1));
			break;
		case R.id.spinnerToDay:
			this.selectedToDay = this.toDays.get(position);
			break;
		case R.id.spinnerToMonth:
			this.selectedToMonth = this.toMonths.get(position);
			this.toDays = generateDays(this.selectedToMonth, this.selectedToYear);
			toDayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.toDays);
			toDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			toDayAdapter.notifyDataSetChanged();
			spinnerToDay.setAdapter(toDayAdapter);
			spinnerToDay.setSelection(this.toDayAdapter.getPosition(1));
			break;
		case R.id.spinnerToYear:
			this.selectedToYear = this.toYears.get(position);
			this.toDays = generateDays(this.selectedToMonth, this.selectedToYear);
			toDayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.toDays);
			toDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			toDayAdapter.notifyDataSetChanged();
			spinnerToDay.setAdapter(toDayAdapter);
			spinnerToDay.setSelection(this.toDayAdapter.getPosition(1));
			break;
		}
	}
	
	private void setSpinnerListeners(){
		itemSelectedListener = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id ) {
			
				int pid = parent.getId();
				setSelectedValue(pid, position);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		
		spinnerFromDay.setOnItemSelectedListener(itemSelectedListener);
		spinnerFromMonth.setOnItemSelectedListener(itemSelectedListener);
		spinnerFromYear.setOnItemSelectedListener(itemSelectedListener);
		
		spinnerToDay.setOnItemSelectedListener(itemSelectedListener);
		spinnerToMonth.setOnItemSelectedListener(itemSelectedListener);
		spinnerToYear.setOnItemSelectedListener(itemSelectedListener);
	}
	
	private void loadSpinnerData(){
		
		fromDayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.fromDays);
		fromMonthAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.fromMonths);
		fromYearAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.fromYears);

		toDayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.toDays);
		toMonthAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.toMonths);
		toYearAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, this.toYears);

		fromDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		toDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		spinnerFromDay.setAdapter(fromDayAdapter);
		spinnerFromMonth.setAdapter(fromMonthAdapter);
		spinnerFromYear.setAdapter(fromYearAdapter);
		
		spinnerToDay.setAdapter(toDayAdapter);
		spinnerToMonth.setAdapter(toMonthAdapter);
		spinnerToYear.setAdapter(toYearAdapter);
		
		
		spinnerFromDay.setSelection(this.fromDays.indexOf(this.currentDay));
		spinnerFromMonth.setSelection(this.fromMonths.indexOf(this.currentMonth));
		spinnerFromYear.setSelection(this.fromYears.indexOf(this.currentYear));
	
		spinnerToDay.setSelection(this.toDays.indexOf(this.currentDay));
		spinnerToMonth.setSelection(this.toMonths.indexOf(this.currentMonth));
		spinnerToYear.setSelection(this.toYears.indexOf(this.currentYear));
		

	}
	
	private List<Integer> generateDays(int month, int year){
		List<Integer> days = new ArrayList<Integer>();
		int maxDate = 0;
		if (month == 2 && isLeap(year))
			maxDate = 29;
		if (month == 2 && !isLeap(year))
			maxDate = 28;
		if (month == 1 || month ==3 || month == 5 || month == 7 || month == 8 || month ==10 || month == 12)
			maxDate = 31;
		if (month == 4 || month ==6 || month == 9 || month == 11)
			maxDate = 30;
		
		for (int day = 1; day <= maxDate; day++)
			days.add(day);
		return days;
	}
	
	private List<Integer> generateMonths(){
		List<Integer> months = new ArrayList<Integer>();
		for(int month=1;month<=12;month++)
			months.add(month);
		
		return months;
	}
	
	private List<Integer> generateYears(){
		List<Integer> years = new ArrayList<Integer>();
		for(int year=2012;year<=2040;year++)
			years.add(year);
		
		return years;
	}
	
	private boolean isLeap(int year){
		boolean retVal = false;
	
		if ( (year % 400 == 0) || (year % 4 == 0))
			retVal = true;
				
		return retVal;
	}
}
