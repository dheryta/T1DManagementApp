package com.t1dm.t1dmanagementapp;



public class MonitoringReadings {

	private String _Date;
	
	private String _Timestamp;

	private int _Reading;
	
	private int _Insulin;
	
	private String _Meal;
	
	private String _Misc;
	
	private int _MID;
	
	
	
	public String get_Date() {

		return _Date;

	}

	public void set_Date(String _Date) {

		this._Date = _Date;

	}
	
	public String get_Timestamp() {

		return _Timestamp;

	}

	public void set_Timestamp(String _Timestamp) {

		this._Timestamp = _Timestamp;

	}

	public int get_Reading() {

		return _Reading;

	}

	public void set_Reading(int _Reading) {

		this._Reading = _Reading;

	}

	public int get_Insulin() {
		return _Insulin;
	}

	public void set_Insulin(int _Insulin) {
		this._Insulin = _Insulin;
	}

	public String get_Meal() {
		return _Meal;
	}

	public void set_Meal(String _Meal) {
		this._Meal = _Meal;
	}

	public String get_Misc() {
		return _Misc;
	}

	public void set_Misc(String _Misc) {
		this._Misc = _Misc;
	}

	public int get_MID() {
		return _MID;
	}

	public void set_MID(int _MID) {
		this._MID = _MID;
	}

	

}