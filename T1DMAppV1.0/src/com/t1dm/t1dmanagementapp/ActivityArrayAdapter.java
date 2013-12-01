/* 
* Created On: Oct 19, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class ActivityArrayAdapter extends ArrayAdapter<Model> {
		private final Activity context;
		private final List<Model> values;
		private int resource;
		private int hourOfDay, minute;
		
		static class ViewHolder {
			protected TextView text;
			protected TextView time;
			protected CheckBox checkbox;
		}

		public ActivityArrayAdapter(Activity context, List<Model> values, int resource) {
			super(context, resource, values);
			this.context = context;
			this.values = values;
			this.resource = resource;
		/*	if (context.getClass().getSimpleName().equals("MealSchedule"))
				resource= R.layout.activity_meal_schedule;
			else if (context.getClass().getSimpleName().equals("InsulinSchedule"))
				resource= R.layout.activity_insulin_schedule;
			else if (context.getClass().getSimpleName().equals("BGCheckingSchedule"))
				resource= R.layout.activity_bgchecking_schedule;
			else if (context.getClass().getSimpleName().equals("ExerciseSchedule"))
				resource= R.layout.activity_exercise_schedule;
			else if (context.getClass().getSimpleName().equals("SleepSchedule"))
				resource= R.layout.activity_sleep_schedule;
			else if (context.getClass().getSimpleName().equals("SuggestionSchedule"))
				resource= R.layout.activity_suggestion_schedule;*/
			
				
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				LayoutInflater inflator = context.getLayoutInflater();
				view = inflator.inflate(resource, null);
				
				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.text = (TextView) view.findViewById(R.id.textView1);
				viewHolder.time = (TextView) view.findViewById(R.id.textTime);
				
				viewHolder.text.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View view) {
						//Toast.makeText(view.getContext(),viewHolder.text.getText() , Toast.LENGTH_LONG).show();
						Calendar calendar = Calendar.getInstance();
						TimePickerDialog tp = new TimePickerDialog(context, new OnTimeSetListener() {							
							@Override
							public void onTimeSet(TimePicker view, int hour, int min) {
								hourOfDay = hour;
								minute = min;
								String AM_PM ;
								
						        if(hour < 12) {
						            AM_PM = "AM";
						        } else {
						            AM_PM = "PM";
						        }

								Model elementTimePicker = (Model) viewHolder.time.getTag();
								elementTimePicker.setTime(hourOfDay+":"+minute);
								elementTimePicker.setHour(hourOfDay);
								elementTimePicker.setMinute(minute);
								
								viewHolder.time.setText(((hourOfDay>12)?hourOfDay-12:hourOfDay)+":"+minute+" "+AM_PM);
										
							}
						}, calendar.get(Calendar.HOUR_OF_DAY) , calendar.get(Calendar.MINUTE), false);
						tp.show();
					}
				});
				
				viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkBoxInsulin);
				
				viewHolder.checkbox
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(CompoundButton buttonView,
									boolean isChecked) {
								Model elementCheckBox = (Model) viewHolder.checkbox.getTag();
								elementCheckBox.setSelected(buttonView.isChecked());							
								viewHolder.text.setClickable(isChecked);
								viewHolder.time.setClickable(isChecked);	
								
							}
						});
				view.setTag(viewHolder);
				viewHolder.checkbox.setTag(values.get(position));
				viewHolder.text.setTag(values.get(position));
				viewHolder.time.setTag(values.get(position));
				//viewHolder.timePicker.setTag(values.get(position));
			} else {
				view = convertView;
				((ViewHolder) view.getTag()).checkbox.setTag(values.get(position));
				((ViewHolder) view.getTag()).time.setTag(values.get(position));
				((ViewHolder) view.getTag()).text.setTag(values.get(position));
			}
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.checkbox.setChecked(values.get(position).isSelected());
			holder.text.setText(values.get(position).getName());
			String AM_PM ;
			
	        if(values.get(position).getHour() < 12) {
	            AM_PM = "AM";
	        } else {
	            AM_PM = "PM";
	        }
	        
			holder.time.setText(((values.get(position).getHour() >=12)? 
					values.get(position).getHour()-12:values.get(position).getHour())
					+ ":" + values.get(position).getMinute() + " " + AM_PM);
			holder.text.setClickable(values.get(position).isSelected());
			holder.time.setClickable(values.get(position).isSelected());		
			
			Model elementTimePicker = (Model) holder.time.getTag();
			elementTimePicker.setTime(values.get(position).getHour()+":"+values.get(position).getMinute());
			elementTimePicker.setHour(values.get(position).getHour());
			elementTimePicker.setMinute(values.get(position).getMinute());
			
			

			return view;
		}
	}