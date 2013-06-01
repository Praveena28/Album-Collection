package com.eminosoft.album;

import com.eminosoft.album.Category.DatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SortedList extends Activity {
	public DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	String position;
	String catName;
	Button nameOfCat;
	ListView lv;
	Cursor cursor;
	String query;
	int count;
	String[] listnames;
	int columnIndex;
	Button backButton;
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sortedlist);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Bundle b = getIntent().getExtras();
		position = b.getString("idofcat");
		catName = b.getString("categoryname");
		backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});

		nameOfCat = (Button) findViewById(R.id.sortlistbutt);
		nameOfCat.setText(catName);

		mDbHelper = new DatabaseHelper(SortedList.this);
		mDb = mDbHelper.getWritableDatabase();
		// [id, listname, listcomm, listdate, path]
		query = "SELECT listname FROM List  where id='" + position + "'"
				+ "ORDER BY" + " listname";

		cursor = mDb.rawQuery(query, null);
		count = cursor.getCount();
		if (count <= 0) {
			Toast.makeText(SortedList.this, "there is no lists",
					Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
		listnames = new String[count];
		System.out.println("aaaaaaa" + count);
		columnIndex = cursor.getColumnIndex("listname");
		String[] myroo = cursor.getColumnNames();

		System.out.println(myroo + "======================"
				+ cursor.getColumnCount() + cursor.getColumnIndex("path"));

		cursor.moveToFirst();

		while (count > 0) {

			listnames[i] = cursor.getString(columnIndex);

			cursor.moveToNext();
			count--;
			i++;
		}

		final ListView lv = (ListView) findViewById(R.id.sortlistlistView1);

		lv.setAdapter(new Myadapter());
		// cursor.close();

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int listposition, long arg3) {

				cursor.close();
				mDb.close();
				String seletedItem = listnames[listposition];
				Intent in = new Intent(SortedList.this, Listedit.class);
				in.putExtra("seletedItem", seletedItem);
				in.putExtra("catposition", position);
				in.putExtra("categoryName", catName);
				Boolean myflag = false;
				in.putExtra("flagof", myflag);
				startActivity(in);
				finish();

			}
		});
	}

	public class Myadapter extends BaseAdapter {

		public int getCount() {
			return listnames.length;
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
			arryText.setText(listnames[position]);
			v.setBackgroundResource(R.drawable.listimage);

			return v;

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		cursor.close();
		mDb.close();

	}

}
