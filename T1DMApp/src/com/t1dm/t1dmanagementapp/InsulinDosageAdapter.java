/* 
* Created On: Oct 19, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


public class InsulinDosageAdapter extends ArrayAdapter<ModelInsulin> {
		private final Activity context;
		private final List<ModelInsulin> values;

		static class ViewHolder {
			protected TextView text;
			protected EditText units;
			protected CheckBox checkbox;
			protected TextView unitLabel;
		}

		public InsulinDosageAdapter(Activity context, List<ModelInsulin> values) {
			super(context, R.layout.activity_manage_insulin_dosage, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				LayoutInflater inflator = context.getLayoutInflater();
				view = inflator.inflate(R.layout.activity_manage_insulin_dosage, null);
				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.text = (TextView) view.findViewById(R.id.textView1);
				viewHolder.unitLabel = (TextView) view.findViewById(R.id.textView2);
				viewHolder.units = (EditText) view.findViewById(R.id.editTextGlucoseLevel);
				viewHolder.units.setEnabled(false);
				
				viewHolder.units.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View arg0, boolean hasFocus) {
						if (!hasFocus){
							final EditText etUnits = (EditText) arg0;
							ModelInsulin elementCheckBox = (ModelInsulin) viewHolder.units.getTag();
							try{
							elementCheckBox.setUnits(Integer.parseInt(etUnits.getText().toString()));
							}catch(Exception e){}
						}
						
					}
				});
				
				viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkBoxInsulin);
				viewHolder.checkbox
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(CompoundButton buttonView,
									boolean isChecked) {
								ModelInsulin elementCheckBox = (ModelInsulin) viewHolder.checkbox.getTag();
								elementCheckBox.setSelected(buttonView.isChecked());							
								viewHolder.units.setEnabled(isChecked);							
								
							}
						});
				view.setTag(viewHolder);
				viewHolder.checkbox.setTag(values.get(position));
				viewHolder.units.setTag(values.get(position));
				viewHolder.text.setTag(values.get(position));
				viewHolder.unitLabel.setTag(values.get(position));
			} else {
				view = convertView;
				((ViewHolder) view.getTag()).checkbox.setTag(values.get(position));
				((ViewHolder) view.getTag()).units.setTag(values.get(position));
				((ViewHolder) view.getTag()).text.setTag(values.get(position));
				((ViewHolder) view.getTag()).unitLabel.setTag(values.get(position));
			}
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.text.setText(values.get(position).getText());
			holder.checkbox.setChecked(values.get(position).isSelected());
			holder.units.setText(Integer.toString(values.get(position).getUnits()));
			holder.unitLabel.setText("Units");
			return view;
		}
	}