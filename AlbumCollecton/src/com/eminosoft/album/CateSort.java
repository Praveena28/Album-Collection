package com.eminosoft.album;

import java.util.Map;

import com.eminosoft.album.MainActivity.Database;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CateSort extends Activity {

	ListView lv;
	Map<String, String> categorys;
	String category_names[];
	public Database mDbHelp;
	public static SQLiteDatabase mD;
	Cursor cursor;
	int count;
	String[] sortcatnam;
	String[] catid;
	int idofcat;
	int i = 0;
	int columnIndex;
	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categorys);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		lv = (ListView) findViewById(R.id.listView1);

		backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();

			}
		});
		mDbHelp = new Database(CateSort.this);
		mD = mDbHelp.getWritableDatabase();
		String query = "SELECT * FROM Categories ORDER BY categoryname";
		cursor = mD.rawQuery(query, null);

		count = cursor.getCount();
		columnIndex = cursor.getColumnIndex("categoryname");
		idofcat = cursor.getColumnIndex("id");
		sortcatnam = new String[count];
		catid = new String[count];
		cursor.moveToFirst();
		while (count > 0) {

			sortcatnam[i] = cursor.getString(columnIndex);
			catid[i] = cursor.getString(idofcat);
			cursor.moveToNext();

			count--;
			i++;
		}
		if (catid.length > 0) {
			System.out.println("count in if" + count);
			lv.setAdapter(new Myadapter());
			mD.close();
			cursor.close();

		} else {
			System.out.println("count in else" + count);
			mD.close();
			cursor.close();
		}

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent in = new Intent(CateSort.this, SortedSelectCate.class);
				in.putExtra("catname", sortcatnam[position]);
				in.putExtra("sortedcatid", catid[position]);
				startActivity(in);

			}
		});

	}

	public class Myadapter extends BaseAdapter {

		public int getCount() {
			return sortcatnam.length;
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
			arryText.setText(sortcatnam[position]);
			v.setBackgroundResource(R.drawable.listimage);

			return v;

		}

	}

}
