package com.example.clock;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ClockView clock = new ClockView(this);
		
		setContentView(clock);
		new Thread(clock).start();
	}
}
