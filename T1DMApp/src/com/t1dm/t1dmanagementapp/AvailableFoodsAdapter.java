package com.t1dm.t1dmanagementapp;

import java.util.List;

import com.t1dm.t1dmanagementapp.ActivityArrayAdapter.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;

public class AvailableFoodsAdapter extends ArrayAdapter<FoodModel> {

	private final Activity context;
	private final List<FoodModel> values;
	private int resource;
	
	
	static class ViewHolder {
		protected CheckBox checkbox;
		protected TextView foodName;
		protected TextView foodDetail;
		protected RatingBar ratingBar;
	}
	
	public AvailableFoodsAdapter(Activity context, List<FoodModel> values, int resource) {
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
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.cbFood);
			viewHolder.foodName = (TextView) view.findViewById(R.id.tvFoodName);
			viewHolder.foodDetail = (TextView) view.findViewById(R.id.tvFoodDetail);
			viewHolder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
			
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(values.get(position));
			viewHolder.foodName.setTag(values.get(position));
			viewHolder.foodDetail.setTag(values.get(position));
			viewHolder.ratingBar.setTag(values.get(position));
			
			viewHolder.checkbox
			.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					FoodModel elementCheckBox = (FoodModel) viewHolder.checkbox.getTag();
					elementCheckBox.setSelected(buttonView.isChecked());
				}
			});
		} else {
			view = convertView;			
			((ViewHolder) view.getTag()).checkbox.setTag(values.get(position));
			((ViewHolder) view.getTag()).foodName.setTag(values.get(position));
			((ViewHolder) view.getTag()).foodDetail.setTag(values.get(position));
			((ViewHolder) view.getTag()).ratingBar.setTag(values.get(position));
		}
		
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.foodName.setText(values.get(position).get_FoodName());
		holder.foodDetail.setText(values.get(position).get_Quantity()+","+values.get(position).get_Carbs()
				+","+values.get(position).get_Calories()+","+values.get(position).get_GI()
				+","+values.get(position).get_GL());
		if (values.get(position).get_GI() >= 70)
			holder.ratingBar.setRating(Float.parseFloat("0.0"));		
		else if (values.get(position).get_GI() >= 50 && values.get(position).get_GI() < 70)
			holder.ratingBar.setRating(Float.parseFloat("0.5"));
		else
			holder.ratingBar.setRating(Float.parseFloat("1"));
		return view;
	}

	
}
