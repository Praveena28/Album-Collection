package com.eminosoft.album;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class SplasSceen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				finish();
				Intent hackbookIntent = new Intent().setClass(SplasSceen.this,
						MainActivity.class);
				startActivity(hackbookIntent);
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 3500);

	}

}
