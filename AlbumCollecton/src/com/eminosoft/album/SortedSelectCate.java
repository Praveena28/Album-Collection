package com.eminosoft.album;

import com.eminosoft.album.Category.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SortedSelectCate extends Activity {
	ImageButton imButton;
	public DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	String nameOfCat;
	String idOfCat;
	TextView tv;
	TextView noOfLists;
	Cursor cursor;
	int count;
	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catsort);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle bi = getIntent().getExtras();

		nameOfCat = bi.getString("catname");
		idOfCat = bi.getString("sortedcatid");
		backButton = (Button) findViewById(R.id.backbutton);
		tv = (TextView) findViewById(R.id.catView1);
		imButton = (ImageButton) findViewById(R.id.imageButton1);
		noOfLists = (TextView) findViewById(R.id.listscount);
		tv.setText(nameOfCat);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();

			}
		});

		String query = "SELECT listname FROM List WHERE id=" + idOfCat;
		mDbHelper = new DatabaseHelper(SortedSelectCate.this);
		mDb = mDbHelper.getWritableDatabase();

		cursor = mDb.rawQuery(query, null);
		count = cursor.getCount();
		noOfLists.setText(Integer.toString(count));

		imButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				cursor.close();
				mDb.close();

				Intent in = new Intent(SortedSelectCate.this, MyGrid.class);
				in.putExtra("catname", nameOfCat);
				in.putExtra("sortedcatid", idOfCat);
				startActivity(in);

			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		cursor.close();
		mDb.close();

	}

}
