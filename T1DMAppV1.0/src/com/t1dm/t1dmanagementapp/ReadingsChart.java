package com.t1dm.t1dmanagementapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;









import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ReadingsChart extends Activity {

	private String fromDate, toDate;
	private boolean askUser;
	private long timems;
	
	private int count;
	private float average;
	private T1DMApplication appContext;

	private EditText etFromDate;
	private EditText etToDate;
	private Button btnSearch;
	private Calendar calendar;
	private DatePickerDialog dpDialog;
	
	private GraphicalView mChartView;
	private XYSeries mCurrentSeries;
	private XYSeriesRenderer mCurrentRenderer;
	
	private XYSeries mCurrentSeriesAverage;
	private XYSeriesRenderer mCurrentRendererAverage;
	
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	private CommonMethods commonMethods = new CommonMethods();

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save the current data, for instance when changing screen orientation
		outState.putSerializable("dataset", mDataset);
		outState.putSerializable("renderer", mRenderer);
		outState.putSerializable("current_series", mCurrentSeries);
		outState.putSerializable("current_renderer", mCurrentRenderer);
		
		outState.putSerializable("current_series_average", mCurrentSeriesAverage);
		outState.putSerializable("current_renderer_average", mCurrentRendererAverage);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		mDataset = (XYMultipleSeriesDataset) savedState
				.getSerializable("dataset");
		mRenderer = (XYMultipleSeriesRenderer) savedState
				.getSerializable("renderer");
		mCurrentSeries = (XYSeries) savedState
				.getSerializable("current_series");
		mCurrentRenderer = (XYSeriesRenderer) savedState
				.getSerializable("current_renderer");
		mCurrentSeriesAverage = (XYSeries) savedState
				.getSerializable("current_series_average");
		mCurrentRendererAverage = (XYSeriesRenderer) savedState
				.getSerializable("current_renderer_average");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_readings_chart);
		
		calendar = Calendar.getInstance();
		etFromDate = (EditText)findViewById(R.id.etFromDate);
		etFromDate.setInputType(InputType.TYPE_NULL); 
		etFromDate.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));	
		etToDate = (EditText)findViewById(R.id.etToDate);
		etToDate.setInputType(InputType.TYPE_NULL); 
		etToDate.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
		
		etFromDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDP(etFromDate);
								
			}
		});
		
		etToDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDP(etToDate);
								
			}
		});
		
		btnSearch = (Button)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fromDate = etFromDate.getText().toString();
				toDate = etToDate.getText().toString();
				if (fromDate!=null && toDate!=null && fromDate!="" && toDate!=""){
				List<MonitoringReadings> readings = appContext.getDbHandler().getMonitoringReadings(fromDate,
						toDate);
				count = (readings!=null)? (readings.size()):0;
				mChartView = null;
				LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
				layout.removeAllViews();
				initializePlot(readings);
				mChartView = ChartFactory.getLineChartView(ReadingsChart.this, mDataset,
						mRenderer);

				mRenderer.setClickEnabled(true);
				mRenderer.setSelectableBuffer(10);

				layout.addView(mChartView, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				}
			}
		});
		
		Intent intent = getIntent();
		fromDate = intent.getStringExtra("FromDate");
		toDate = intent.getStringExtra("ToDate");
		askUser = intent.getBooleanExtra("AskUser", false);
		timems =  intent.getLongExtra("Time_ms", 0);
		if (timems > 0){
			RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.relLayout);
			rLayout.setVisibility(View.INVISIBLE);
		}
		appContext = ((T1DMApplication) getApplicationContext());
		appContext.setContext(getApplicationContext());
		appContext.setDbHandler(new DatabaseHandler());

		List<MonitoringReadings> readings = appContext.getDbHandler().getMonitoringReadings(fromDate,
				toDate);
		count = (readings!=null)? (readings.size()):0;
		initializePlot(readings);
		if (askUser){
			AlertDialog.Builder builder = new Builder(ReadingsChart.this);
			builder.setTitle("T1DM");
			builder.setMessage("Include this plot in report?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				 
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                	String retVal = saveChartAsImage(timems);
                	new AsyncOperations(fromDate, toDate, timems, (retVal!=null)&&(retVal!="")?retVal.toString():null).execute();
                   finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                	
                	new AsyncOperations(fromDate, toDate, -1, null).execute();
                	finish();
                }
            });
            builder.show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);

			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);

			layout.addView(mChartView, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		} else {
			mChartView.repaint();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.readings_chart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuSavePlot:
			if (count > 0) {
				if (saveChartAsImage(0)!="")
					Toast.makeText(this,
							"T1DM says, image saved sucessfully !!!!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(this,
							"T1DM says, oops some error occurred !!!!",
							Toast.LENGTH_SHORT).show();
				finish();
			} else
				Toast.makeText(this,
						"T1DM says, oops no readings to plot !!!!",
						Toast.LENGTH_SHORT).show();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initializePlot(List<MonitoringReadings> readings) {

		mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();

		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.TRANSPARENT);
		mRenderer.setAxisTitleTextSize(10);
		mRenderer.setChartTitleTextSize(15);
		mRenderer.setLabelsTextSize(10);
		mRenderer.setLegendTextSize(10);
		mRenderer.setMargins(new int[] { 40, 40, 40, 30 });
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setPointSize(5);
		mRenderer.setXTitle("Instance(s)");
		mRenderer.setYTitle("Glucose Level");
		mRenderer.setYLabelsPadding(20);
		mRenderer.setShowLegend(false);
		mRenderer.setLabelsColor(Color.WHITE);

		mRenderer.setChartTitle("From: " + fromDate + " To: " + toDate);

		XYSeries series = new XYSeries("GlucoseReadings");
		XYSeries averageSeries = new XYSeries("Average");
		mDataset.addSeries(series);
		mDataset.addSeries(averageSeries);
		mCurrentSeries = series;
		mCurrentSeriesAverage =averageSeries;
		
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		XYSeriesRenderer rendererAverage = new XYSeriesRenderer();
		
		renderer.setPointStyle(PointStyle.TRIANGLE);
		renderer.setFillPoints(true);
		renderer.setDisplayChartValues(true);
		renderer.setDisplayChartValuesDistance(10);
		renderer.setColor(Color.BLUE);

		
		rendererAverage.setPointStyle(PointStyle.POINT);
		rendererAverage.setFillPoints(true);
		rendererAverage.setDisplayChartValues(false);
		rendererAverage.setDisplayChartValuesDistance(10);
		rendererAverage.setColor(Color.YELLOW);

		
		for (int i = 0; i < readings.size(); i++) {
			mCurrentSeries.add(i + 1, readings.get(i).get_Reading());
			average = average + readings.get(i).get_Reading();
		}
		
		average = (readings.size() > 0) ? average / readings.size() : 0;
		mCurrentSeriesAverage.add(0, average);
		for (int i = 0; i < readings.size(); i++) {
			mCurrentSeriesAverage.add(i + 1, average);			
		}
		mCurrentSeriesAverage.add(mCurrentSeries.getMaxX() + 1, average);
		mRenderer.setXAxisMin(0);
		mRenderer.setXAxisMax(mCurrentSeries.getMaxX() + 1);
		mRenderer.setYAxisMin(mCurrentSeries.getMinY());
		mRenderer.setYAxisMax(mCurrentSeries.getMaxY() + 50);

		mRenderer.setPanEnabled(true, true);
		double[] panLimits = { mRenderer.getXAxisMin(),
				mRenderer.getXAxisMax(), mRenderer.getYAxisMin(),
				mRenderer.getYAxisMax() };

		mRenderer.setPanEnabled(true);
		mRenderer.setPanLimits(panLimits);
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.addSeriesRenderer(rendererAverage);
		mRenderer.setShowLegend(true);
		mRenderer.setZoomButtonsVisible(false);
		
		if (readings.size() == 0)
			Toast.makeText(this, "T1DM says, oops no readings to plot !!!!",
					Toast.LENGTH_SHORT).show();
		
	}

	public String saveChartAsImage(long time_ms) {
		String retVal = "";
		View content = findViewById(R.id.chart);
		content.setDrawingCacheEnabled(true);
		Bitmap bitmap = content.getDrawingCache();
		Calendar calendar = Calendar.getInstance();
		if (time_ms == 0)
			time_ms = calendar.getTimeInMillis();
		File imagePath = new File(commonMethods.APP_PATH.toString() + File.separator
				+ commonMethods.CHARTS_FOLDER + File.separator + "Monitoring"+time_ms+".png");
		FileOutputStream ostream = null;
		try {
			if (!imagePath.exists()) {
				imagePath.createNewFile();
			}
			ostream = new FileOutputStream(imagePath);
			bitmap.compress(CompressFormat.PNG, 10, ostream);

			retVal = imagePath.toString();
			
		
		} catch (Exception e) {
			retVal = "";
			return retVal;
		} finally {

			try {
				if (ostream != null)
					ostream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			content.invalidate();
			content.setDrawingCacheEnabled(false);		
			
		}
		return retVal;
	}

	 private void showDP(final EditText tvDate){
	  		dpDialog = new DatePickerDialog(this, new OnDateSetListener() {
	  			
	  			@Override
	  			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	  				String m = (monthOfYear<10)?("0"+Integer.toString(monthOfYear)):Integer.toString(monthOfYear+1);
	  				String d = (dayOfMonth<10)?("0"+Integer.toString(dayOfMonth)):Integer.toString(dayOfMonth);
	  				tvDate.setText(year+"-"+m+"-"+d);
	  			}
	  		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	  		dpDialog.show();
	  	}
	 
	private class AsyncOperations extends AsyncTask<Void, Void, Void>{

	private	String fromDate;
	private String toDate;
	private long time_ms;
	private String imagePath;
	private boolean status;
	
	public AsyncOperations(String fromDate, String toDate, long time_ms, String imagePath){
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.time_ms = time_ms;
		this.imagePath = imagePath;
	}
	@Override
      protected void onPreExecute(){
		super.onPreExecute();
		
      }

  @Override
      protected Void doInBackground(Void... voids){

	  try {
		  if(commonMethods.isNetworkAvailable(ReadingsChart.this)){
		  commonMethods.PrepareAndSendEmail(fromDate, toDate, time_ms, (imagePath!=null)&&(imagePath!="")?imagePath.toString():null,
					ReadingsChart.this);
			
	        status = true;
		  }else
			  status = false;
	    } catch (Exception e) {
	     status = false;
	    } finally {
  			finish(); 
  		}
		return null;
      }

      @Override
      protected void onPostExecute(Void params){
    	  if(status)
    		  Toast.makeText(ReadingsChart.this, "T1DM says, report sent to registered email.", Toast.LENGTH_SHORT).show();
    	  else
    		  Toast.makeText(ReadingsChart.this, "T1DM says, oops some error occurred while sending email !!!!", Toast.LENGTH_SHORT).show();
            finish();

      }
}
	
}
