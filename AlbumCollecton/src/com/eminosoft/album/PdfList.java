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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PdfList extends Activity {

	String position;
	String catname;
	Button category;
	ListView lv;
	public DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	Cursor cursor;
	String query;
	int count;
	int columnIndex;
	String[] names;
	String[] path;
	int pathIndex;
	int i = 0;
	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listpicker);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle bu = this.getIntent().getExtras();
		position = bu.getString("positionofcat");
		catname = bu.getString("nameofcat");
		backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();

			}
		});
		lv = (ListView) findViewById(R.id.listpick);
		mDbHelper = new DatabaseHelper(PdfList.this);
		mDb = mDbHelper.getWritableDatabase();
		query = "SELECT * FROM pdfthumb WHERE id=" + position;

		cursor = mDb.rawQuery(query, null);
		count = cursor.getCount();
		if (count <= 0) {
			cursor.close();
			mDb.close();
			Toast.makeText(PdfList.this, "There is no pdfs Thumbnails",
					Toast.LENGTH_SHORT).show();
			onBackPressed();
		} else {

			names = new String[count];
			path = new String[count];
			columnIndex = cursor.getColumnIndex("pdfname");
			pathIndex = cursor.getColumnIndex("pdfpath");
			cursor.moveToFirst();
			while (count > 0) {

				names[i] = cursor.getString(columnIndex);
				path[i] = cursor.getString(pathIndex);
				cursor.moveToNext();
				count--;
				i++;
			}
			cursor.close();
			mDb.close();

			// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			// android.R.layout.simple_list_item_1, names);
			lv.setAdapter(new Myadapter());
			lv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent in = new Intent(PdfList.this, PdfViwer.class);
					in.putExtra("pathOfPdf", path[arg2]);
					startActivity(in);

				}
			});
		}
	}

	public class Myadapter extends BaseAdapter {

		public int getCount() {
			return names.length;
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
			arryText.setText(names[position]);
			v.setBackgroundResource(R.drawable.listimage);

			return v;

		}

	}

}
