package com.t1dm.t1dmanagementapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BodyRequirements extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_body_requirements);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.body_requirements, menu);
		return true;
	}

}
