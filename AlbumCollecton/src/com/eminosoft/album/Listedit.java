package com.eminosoft.album;

import com.eminosoft.album.Category.DatabaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Listedit extends Activity {
	public DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	String position;
	String listiteam;
	int count;
	Cursor cursor;

	ImageView listimage;
	EditText listname;
	EditText listcomm;
	EditText list_date;
	String imagepath;
	String date;
	String comment;
	String catname;
	int dateindex;
	int commentindex;
	int pathindex;
	Button save;
	Button back;
	Boolean myFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		listimage = (ImageView) findViewById(R.id.listpicture);
		listname = (EditText) findViewById(R.id.ed_name);
		list_date = (EditText) findViewById(R.id.ed_cal);
		listcomm = (EditText) findViewById(R.id.ed_comm);
		save = (Button) findViewById(R.id.list_button);
		back = (Button) findViewById(R.id.backbutton);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();

			}
		});

		Bundle b = getIntent().getExtras();
		position = b.getString("catposition");
		listiteam = b.getString("seletedItem");
		catname = b.getString("categoryName");
		myFlag = b.getBoolean("flagof");
		mDbHelper = new DatabaseHelper(Listedit.this);
		mDb = mDbHelper.getWritableDatabase();
		String query = "SELECT * FROM List WHERE id='" + position + "'"
				+ " AND " + "listname='" + listiteam + "'";

		cursor = mDb.rawQuery(query, null);

		count = cursor.getCount();

		pathindex = cursor.getColumnIndex("path");
		dateindex = cursor.getColumnIndex("listdate");
		commentindex = cursor.getColumnIndex("listcomm");

		cursor.moveToFirst();
		imagepath = cursor.getString(pathindex);
		date = cursor.getString(dateindex);
		comment = cursor.getString(commentindex);
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(imagepath, options);
		Matrix mat = new Matrix();
		mat.postRotate(90);
		Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), mat, true);
		listimage.setPadding(2, 4, 2, 4);
		listimage.setBackgroundColor(808080);
		listimage.setImageBitmap(bMapRotate);

		listname.setText(listiteam);
		listcomm.setText(comment);
		list_date.setText(date);
		save.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(listcomm.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(listname.getWindowToken(), 0);

				String lName = listname.getText().toString();
				if (lName.length() == 0) {
					listname.setError("please enter the name");
				}

				else if ((listcomm.getText().toString().trim().length()) == 0) {
					listcomm.setError("please enter some comment");

				}

				else {

					ContentValues args = new ContentValues();
					args.put("listname", listname.getText().toString());
					args.put("listcomm", listcomm.getText().toString());
					args.put("listdate", list_date.getText().toString());
					int i = mDb.update("List", args, "id='" + position
							+ "' and " + "listdate='" + date + "'", null);
					System.out.println(i + "123456");

					cursor.close();
					mDb.close();

					if (myFlag == true) {
						Toast.makeText(Listedit.this, "update succifull",
								Toast.LENGTH_SHORT).show();
						Intent in = new Intent(Listedit.this, PickerList.class);
						in.putExtra("positionofcat", position);
						startActivity(in);
						finish();
					} else if (myFlag == false) {
						Toast.makeText(Listedit.this, "update succifull",
								Toast.LENGTH_SHORT).show();
						Intent in = new Intent(Listedit.this, SortedList.class);
						in.putExtra("idofcat", position);
						in.putExtra("categoryname", catname);
						startActivity(in);
						finish();
					}
				}

			}
		});

	}

	@Override
	public void onBackPressed() {
		cursor.close();
		mDb.close();
		super.onBackPressed();
		Toast.makeText(Listedit.this, "you are not updated any thing",
				Toast.LENGTH_SHORT).show();
		if (myFlag == true) {
			Intent in = new Intent(Listedit.this, PickerList.class);
			in.putExtra("positionofcat", position);
			startActivity(in);
		} else if (myFlag == false) {
			Intent in = new Intent(Listedit.this, SortedList.class);
			in.putExtra("idofcat", position);
			in.putExtra("categoryname", catname);
			startActivity(in);
		}
	}

}
