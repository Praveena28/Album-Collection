package com.eminosoft.album;

import java.io.File;

import com.eminosoft.album.MainActivity.Database;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryPicker extends Activity {
	public String category;
	String path;
	String position;
	public Database mDbHelp;
	public static SQLiteDatabase mD;
	ListView lv;
	Cursor cursor;
	int count;
	String[] items;
	int i = 0;
	int columnIndex;
	Button backbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.categorys);

		lv = (ListView) findViewById(R.id.listView1);
		backbutton = (Button) findViewById(R.id.backbutton);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		backbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});

		mDbHelp = new Database(CategoryPicker.this);
		mD = mDbHelp.getWritableDatabase();

		String query = "SELECT categoryname FROM Categories";

		cursor = mD.rawQuery(query, null);
		count = cursor.getCount();
		items = new String[count];
		columnIndex = cursor.getColumnIndex("categoryname");
		cursor.moveToFirst();

		while (count > 0) {

			items[i] = cursor.getString(columnIndex);

			cursor.moveToNext();
			count--;
			i++;
		}

		if (items.length > 0) {
			lv.setAdapter(new Myadapter());
			mD.close();
			cursor.close();
		} else {
			mD.close();
			cursor.close();
		}

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				category = items[pos];
				position = Integer.toString(pos);
				path = "/ImageDataBase/category" + pos;
				System.out.println(path);
				String extStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				File myNewFolder = new File(extStorageDirectory + path);
				myNewFolder.mkdir();

				category = items[pos];

				System.out.println(position);
				Intent in = new Intent(CategoryPicker.this, Category.class);
				in.putExtra("nameofcategory", category);
				in.putExtra("position", position);// position of the category
				// in.putExtra("pathOfCategory", path);
				System.out.println(path + "CategoryPicker");
				startActivity(in);
				finish();
			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent in = new Intent(CategoryPicker.this, MainActivity.class);
		startActivity(in);
		finish();

	}

	public class Myadapter extends BaseAdapter {

		public int getCount() {
			return items.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View v = null;
			ImageView img = null;
			TextView arryText = null;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.inflate, null);
				img = (ImageView) v.findViewById(R.id.inflateimage);

				arryText = (TextView) v.findViewById(R.id.inflateText);
			}
			img.setImageResource(R.drawable.sidearrowmark);
			arryText.setText(items[position]);
			v.setBackgroundResource(R.drawable.listimage);

			return v;

		}

	}

}
