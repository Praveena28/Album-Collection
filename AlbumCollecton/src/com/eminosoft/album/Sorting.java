package com.eminosoft.album;

import java.util.ArrayList;
import java.util.List;

import com.eminosoft.album.MainActivity.Database;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Sorting extends Activity {

	Button categorySort;
	Spinner spinner1;
	Spinner spinner2;
	String select = "pr";
	public Database mDbHelp;
	int count;
	Cursor cursor;
	int columnIndex;
	int idofcat;
	public static SQLiteDatabase mD;
	String[] catname;
	String[] catid;
	int i = 1;
	List<String> list;
	List<String> catList;
	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// categorySort=(Button)findViewById(R.id.sort_cat);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner2.setVisibility(View.GONE);
		list = new ArrayList<String>();

		list.add("Select");
		list.add("Category");
		list.add("List");
		backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();

			}
		});

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				select = spinner1.getItemAtPosition(position).toString();
				compare(select);
			}

			public void compare(String select) {
				if (select.equals("Category")) {

					spinner2.setVisibility(View.GONE);
					Intent in = new Intent(Sorting.this, CateSort.class);
					startActivity(in);
				} else if (select.equals("List")) {
					catList = new ArrayList<String>();
					spinner2.setVisibility(View.VISIBLE);

					mDbHelp = new Database(Sorting.this);
					mD = mDbHelp.getWritableDatabase();
					String query = "SELECT categoryname FROM Categories";

					cursor = mD.rawQuery(query, null);
					count = cursor.getCount();
					System.out.println(count);
					catname = new String[count + 1];
					catid = new String[count];
					catList.add("select");

					columnIndex = cursor.getColumnIndex("categoryname");

					cursor.moveToFirst();

					while (count > 0) {
						catList.add(cursor.getString(columnIndex));
						cursor.moveToNext();
						count--;
						i++;
					}
					if (count <= 0) {
						cursor.close();
						mD.close();
					}

					ArrayAdapter<String> catadapter = new ArrayAdapter<String>(
							Sorting.this, android.R.layout.simple_spinner_item,
							catList);
					catadapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner2.setAdapter(catadapter);
					spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							String catselect = spinner2.getItemAtPosition(arg2)
									.toString();

							System.out.println(catselect
									+ "------------=============+++++");
							Boolean myflag = true;

							if (catselect.equals("select")) {
								System.out.println(catselect);
								myflag = false;
								Toast.makeText(Sorting.this,
										"select the category",
										Toast.LENGTH_SHORT).show();

							}

							else if (myflag == true) {

								Intent in = new Intent(Sorting.this,
										SortedList.class);
								int position = arg2 - 1;

								in.putExtra("categoryname", catList.get(arg2)
										.toString());
								in.putExtra("idofcat",
										Integer.toString(position));
								startActivity(in);
							}
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});

				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent in = new Intent(Sorting.this, MainActivity.class);
		startActivity(in);
		finish();
	}
}
