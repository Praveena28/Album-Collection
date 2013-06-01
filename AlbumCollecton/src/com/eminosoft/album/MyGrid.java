package com.eminosoft.album;

import java.io.File;
import java.util.ArrayList;

import com.eminosoft.album.Category.DatabaseHelper;
import com.eminosoft.album.Category.ImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyGrid extends Activity {
	private static final String DATABASE_TABLE3_PATH = "path";

	GridView gv;
	public String cat_name;
	public String image_path;
	String[] projection = { MediaStore.Images.Media._ID };
	Uri uri;
	// int imgcount;

	String nameOfCat;
	String idOfCat;

	Button backButton;
	Button catName;

	public DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	public static ArrayList<String> paths = new ArrayList<String>();
	ImageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Bundle b = getIntent().getExtras();
		nameOfCat = b.getString("catname");
		idOfCat = b.getString("sortedcatid");
		backButton = (Button) findViewById(R.id.backbutton);
		gv = (GridView) findViewById(R.id.gridView1);
		catName = (Button) findViewById(R.id.sort_by);
		catName.setText(nameOfCat);

		mDbHelper = new DatabaseHelper(MyGrid.this);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();

			}
		});

		gridLoad();
		if (paths.size() <= 0) {
			Toast.makeText(MyGrid.this, "There is no images",
					Toast.LENGTH_SHORT).show();
			onBackPressed();
		} else {

			gv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					Intent i = new Intent(getApplicationContext(),
							FullImage.class);
					i.putExtra("id", position);
					i.putExtra("sortedcatid", idOfCat);

					startActivity(i);

				}

			});

		}
	}

	public void gridLoad() {

		String getpathsquery = "SELECT category" + idOfCat + " FROM "
				+ DATABASE_TABLE3_PATH;

		mDb = mDbHelper.getReadableDatabase();
		Cursor pathCursor = mDb.rawQuery(getpathsquery, null);
		int pathcolumanIndex = pathCursor.getColumnIndex("category" + idOfCat);

		if (pathCursor.moveToFirst()) {
			pathCursor.moveToFirst();

			for (int i = 0; i < pathCursor.getCount(); i++) {

				System.out.println(i + "count of for loop"
						+ pathCursor.getString(pathcolumanIndex) + "-----"
						+ idOfCat);

				if (pathCursor.getString(pathcolumanIndex) != null) {

					File fa = new File(pathCursor.getString(pathcolumanIndex));
					if (fa.exists())
						paths.add(pathCursor.getString(pathcolumanIndex));
				}
				pathCursor.moveToNext();
			}

			// if (adapter != null)
			// adapter.notifyDataSetInvalidated();
			pathCursor.close();
			mDb.close();

			if (paths.size() > 0) {

				gv.setAdapter(new ImageAdapter(MyGrid.this, paths));

			}
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// cursor.close();
	}

}
