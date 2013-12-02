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

	public AvailableFoodsAdapter(Activity context, List<FoodModel> values,
			int resource) {
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
			viewHolder.foodDetail = (TextView) view
					.findViewById(R.id.tvFoodDetail);
			viewHolder.ratingBar = (RatingBar) view
					.findViewById(R.id.ratingBar);

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
							FoodModel elementCheckBox = (FoodModel) viewHolder.checkbox
									.getTag();
							elementCheckBox.setSelected(buttonView.isChecked());
						}
					});
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(values.get(position));
			((ViewHolder) view.getTag()).foodName.setTag(values.get(position));
			((ViewHolder) view.getTag()).foodDetail
					.setTag(values.get(position));
			((ViewHolder) view.getTag()).ratingBar.setTag(values.get(position));
		}

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.ratingBar.setVisibility(View.INVISIBLE);
		holder.checkbox.setChecked(values.get(position).isSelected());
		holder.foodName.setText(values.get(position).get_FoodName());
		holder.foodDetail.setText("Qty(g/oz):"+values.get(position).get_Quantity() + ","
				+ "Carbs(g):"+values.get(position).get_Carbs() + ","
				//+ "Calories"+ values.get(position).get_Calories() + ","
				+ "GI:"+values.get(position).get_GI() + ","
				+ "GL:"+values.get(position).get_GL());
		if (values.get(position).get_GL() != null
				&& !values.get(position).get_GL().equals("")) {
			Double gl = Double.parseDouble(values.get(position).get_GL());

			if (gl >= 20) {
				holder.foodName.setBackgroundColor(Color.RED);
				holder.foodDetail.setBackgroundColor(Color.RED);
			} else if (gl >= 11 && gl < 19) {
				holder.foodName.setBackgroundColor(Color.YELLOW);
				holder.foodDetail.setBackgroundColor(Color.YELLOW);
			} else {
				holder.foodName.setBackgroundColor(Color.GREEN);
				holder.foodDetail.setBackgroundColor(Color.GREEN);
			}
		}
		return view;
	}

}
