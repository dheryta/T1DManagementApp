package com.t1dm.t1dmanagementapp;

public class FoodModel {
	private String _FoodType;
	private String _FoodName;
	private String _Quantity;
	private String _Carbs;
	private String _GI;
	private String _GL;
	private String _Calories;
	private boolean selected;
	
	public String get_FoodType() {
		return _FoodType;
	}

	public void set_FoodType(String _FoodType) {
		this._FoodType = _FoodType;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String get_Calories() {
		return _Calories;
	}
	public void set_Calories(String _Calories) {
		this._Calories = _Calories;
	}
	public String get_FoodName() {
		return _FoodName;
	}
	public void set_FoodName(String _FoodName) {
		this._FoodName = _FoodName;
	}
	public String get_Quantity() {
		return _Quantity;
	}
	public void set_Quantity(String _Quantity) {
		this._Quantity = _Quantity;
	}
	public String get_Carbs() {
		return _Carbs;
	}
	public void set_Carbs(String _Carbs) {
		this._Carbs = _Carbs;
	}
	public String get_GI() {
		return _GI;
	}
	public void set_GI(String _GI) {
		this._GI = _GI;
	}
	public String get_GL() {
		return _GL;
	}
	public void set_GL(String _GL) {
		this._GL = _GL;
	}
	
	
}
