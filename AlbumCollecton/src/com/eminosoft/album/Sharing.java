package com.eminosoft.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Sharing extends Activity {

	Button goTo;
	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharinghelp);
		backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();

			}
		});
		goTo = (Button) findViewById(R.id.shar_help);
		goTo.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent in = new Intent(Sharing.this, DBRoulette.class);
				startActivity(in);

			}
		});

	}

}
