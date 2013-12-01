package com.t1dm.t1dmanagementapp;

import java.util.List;

import com.t1dm.t1dmanagementapp.AvailableFoodsAdapter.ViewHolder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;



public class MealPlanAdapter extends ArrayAdapter<FoodModel> {

	private final Activity context;
	private final List<FoodModel> values;
	private int resource;
	
	
	static class ViewHolder {
		protected TextView foodDetail;		
	}
	
	public MealPlanAdapter(Activity context, List<FoodModel> values, int resource) {
		super(context, resource, values);
		this.context = context;
		this.resource = resource;
		this.values = values;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(resource, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.foodDetail = (TextView) view.findViewById(R.id.tvMealDetails);
			
			
			view.setTag(viewHolder);
			viewHolder.foodDetail.setTag(values.get(position));					
			
		}else {
			view = convertView;
			((ViewHolder) view.getTag()).foodDetail.setTag(values.get(position));			
		}	
		
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.foodDetail.setText( values.get(position).get_FoodType()+":  "+ values.get(position).get_FoodName());		
		return view;
	
	}
}