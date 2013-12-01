/* 
* Created On: Oct 19, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

public class Model {
	
		private String name;
		private boolean selected;
		private String time;
		private int hour;
		private int minute;
		private String meal;
		
		public String getMeal() {
			return meal;
		}

		public void setMeal(String meal) {
			this.meal = meal;
		}

		public Model(){
			
		}

		public Model(String name) {
			this.name = name;
			selected = false;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}
		
		public int getMinute() {
			return minute;
		}

		public void setMinute(int minute) {
			this.minute = minute;
		}
		
		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

	}
