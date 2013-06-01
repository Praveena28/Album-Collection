package com.eminosoft.album;

import android.app.Activity;
import android.os.Bundle;

public class NewCategory extends Activity {

	String position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = this.getIntent().getExtras();
		position = b.getString("position");

	}

}
