package com.t1dm.t1dmanagementapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;

public class SlideSideMenu extends Activity implements OnClickListener {
	Button slideButton, b1, b2, b3;
		 SlidingDrawer slidingDrawer;
		 
		 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_slide_side_menu);
		  slideButton = (Button) findViewById(R.id.slideButton);
		  slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
		  b1 = (Button) findViewById(R.id.Button01);
		  b2 = (Button) findViewById(R.id.Button02);
		  b3 = (Button) findViewById(R.id.Button03);
		  b1.setOnClickListener(this);
		  b2.setOnClickListener(this);
		  b3.setOnClickListener(this);
		  
		  slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
		   @Override
		   public void onDrawerOpened() {
		    slideButton.setText("V");
		   }
		  });
		  slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
		   @Override
		   public void onDrawerClosed() {
		    slideButton.setText("^");
		   }
		  });
		 }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		 
		 
		}