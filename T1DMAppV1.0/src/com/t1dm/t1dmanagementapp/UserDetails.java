package com.t1dm.t1dmanagementapp;

public class UserDetails {

	/*
	 * values.put("NAME", userDetails.get_NAME());

			values.put("AGE", userDetails.get_AGE());

			values.put("EMAIL", userDetails.get_EMAIL());

			values.put("DrNAME", userDetails.get_DrNAME());

			values.put("DrPHONE", userDetails.get_DrPHONE());

			values.put("ADDRESS", userDetails.get_ADDRESS());

			values.put("EMERGENCY", userDetails.get_EMERGENCY());
	 */
	
	private String _NAME;
	private int _AGE;
	private String _EMAIL;
	private String _DrNAME;
	private String _DrPHONE;
	private String _ADDRESS;
	private String _EMERGENCY;
	private boolean _useAUDIO;
	private boolean _emergencyEnabled;
	
	public boolean is_emergencyEnabled() {
		return _emergencyEnabled;
	}
	public void set_emergencyEnabled(boolean _emergencyEnabled) {
		this._emergencyEnabled = _emergencyEnabled;
	}
	
	public boolean is_useAUDIO() {
		return _useAUDIO;
	}
	public void set_useAUDIO(boolean _useAUDIO) {
		this._useAUDIO = _useAUDIO;
	}
	public String get_NAME() {
		return _NAME;
	}
	public void set_NAME(String _NAME) {
		this._NAME = _NAME;
	}
	public int get_AGE() {
		return _AGE;
	}
	public void set_AGE(int _AGE) {
		this._AGE = _AGE;
	}
	public String get_EMAIL() {
		return _EMAIL;
	}
	public void set_EMAIL(String _EMAIL) {
		this._EMAIL = _EMAIL;
	}
	public String get_DrNAME() {
		return _DrNAME;
	}
	public void set_DrNAME(String _DrNAME) {
		this._DrNAME = _DrNAME;
	}
	public String get_DrPHONE() {
		return _DrPHONE;
	}
	public void set_DrPHONE(String _DrPHONE) {
		this._DrPHONE = _DrPHONE;
	}
	public String get_ADDRESS() {
		return _ADDRESS;
	}
	public void set_ADDRESS(String _ADDRESS) {
		this._ADDRESS = _ADDRESS;
	}
	public String get_EMERGENCY() {
		return _EMERGENCY;
	}
	public void set_EMERGENCY(String _EMERGENCY) {
		this._EMERGENCY = _EMERGENCY;
	}
	
	

}
