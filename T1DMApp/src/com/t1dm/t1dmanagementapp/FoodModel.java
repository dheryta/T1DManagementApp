package com.t1dm.t1dmanagementapp;

public class FoodModel {
	private String _FoodName;
	private String _Quantity;
	private int _Carbs;
	private int _GI;
	private int _GL;
	private int _Calories;
	private boolean selected;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int get_Calories() {
		return _Calories;
	}
	public void set_Calories(int _Calories) {
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
	public int get_Carbs() {
		return _Carbs;
	}
	public void set_Carbs(int _Carbs) {
		this._Carbs = _Carbs;
	}
	public int get_GI() {
		return _GI;
	}
	public void set_GI(int _GI) {
		this._GI = _GI;
	}
	public int get_GL() {
		return _GL;
	}
	public void set_GL(int _GL) {
		this._GL = _GL;
	}
	
	
}
