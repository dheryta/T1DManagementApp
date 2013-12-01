/* 
* Created On: Oct 19, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

public class ModelInsulin {

	private String text;
	private int units;
	private boolean selected;
	
	public ModelInsulin(){
		
	}
	
	public ModelInsulin(String text) {
		this.text = text;
		selected = false;
		units=0;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getUnits() {
		return units;
	}
	public void setUnits(int units) {
		this.units = units;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
