package com.eminosoft.album;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	public Button catagory;
	public Button sort;
	Button sharing;
	public Database mDbHelp;
	public static SQLiteDatabase mD;
	private static final String DATABASE_CREATE_TABLE = "create table if not exists Categories(id text,categoryname text);";

	private static final String DATABASE_NAME = "Catdata.sqlite";
	private static final String DATABASE_TABLE = "Categories";
	private static final int DATABASE_VERSION = 3;
	Context mCtx;
	Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		catagory = (Button) findViewById(R.id.catagory);
		sharing = (Button) findViewById(R.id.control3);
		sort = (Button) findViewById(R.id.control2);

		sharing.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent in = new Intent(MainActivity.this, Sharing.class);
				startActivity(in);

			}
		});

		mDbHelp = new Database(MainActivity.this);
		mD = mDbHelp.getWritableDatabase();
		String query = "SELECT categoryname FROM Categories";

		cursor = mD.rawQuery(query, null);
		if ((cursor.getCount() <= 0)) {
			catCreate();
			mD.close();
			cursor.close();

		} else {
			mD.close();
			cursor.close();
		}

		catagory.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String newFolder = "/ImageDataBase";
				String extStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				File myNewFolder = new File(extStorageDirectory + newFolder);
				if (!myNewFolder.exists()) {
					myNewFolder.mkdir();
				}

				Intent in = new Intent(MainActivity.this, CategoryPicker.class);

				startActivity(in);
				finish();

			}
		});
		sort.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent in = new Intent(MainActivity.this, Sorting.class);
				startActivity(in);
				finish();

			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		finish();
	}

	private void catCreate() {

		ContentValues initialValues = new ContentValues();

		initialValues.put("id", "0");
		initialValues.put("categoryname", "Friends");
		mD.insert(DATABASE_TABLE, null, initialValues);

		initialValues.put("id", "1");
		initialValues.put("categoryname", "Personal");
		mD.insert(DATABASE_TABLE, null, initialValues);

		initialValues.put("id", "2");
		initialValues.put("categoryname", "Family");
		mD.insert(DATABASE_TABLE, null, initialValues);
		initialValues.put("id", "3");
		initialValues.put("categoryname", "Tour");
		mD.insert(DATABASE_TABLE, null, initialValues);

		initialValues.put("id", "4");
		initialValues.put("categoryname", "Flowers");
		mD.insert(DATABASE_TABLE, null, initialValues);
		initialValues.put("id", "5");
		initialValues.put("categoryname", "category5");
		mD.insert(DATABASE_TABLE, null, initialValues);
		initialValues.put("id", "6");
		initialValues.put("categoryname", "category6");
		mD.insert(DATABASE_TABLE, null, initialValues);
		initialValues.put("id", "7");
		initialValues.put("categoryname", "category7");
		mD.insert(DATABASE_TABLE, null, initialValues);
		initialValues.put("id", "8");
		initialValues.put("categoryname", "category8");
		mD.insert(DATABASE_TABLE, null, initialValues);
		initialValues.put("id", "9");
		initialValues.put("categoryname", "category9");

		mD.insert(DATABASE_TABLE, null, initialValues);

	}

	public static class Database extends SQLiteOpenHelper {

		Database(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE_TABLE);
			System.out.println("All db created:");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("TAG", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS all");
			onCreate(db);
		}
	}

}
