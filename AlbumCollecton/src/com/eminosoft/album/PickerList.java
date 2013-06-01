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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PickerList extends Activity {
	int i = 0;
	public Context mCtx;
	public DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	static final String DATABASE_CREATE_TABLE = "create table List(id text,listname text,listcomm text,listdate text,path text);";
	static final String DATABASE_NAME = "Listdata.sqlite";
	String DATABASE_TABLE = "List";
	public int DATABASE_VERSION = 3;

	int columnIndex;
	Cursor cursor;
	String listnames[];
	String position;
	String query;
	int count;
	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.listpicker);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle b = getIntent().getExtras();
		position = b.getString("positionofcat");
		backButton = (Button) findViewById(R.id.backbutton);

		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});

		mDbHelper = new DatabaseHelper(PickerList.this);
		mDb = mDbHelper.getWritableDatabase();
		// [id, listname, listcomm, listdate, path]

		query = "SELECT listname FROM List WHERE id=" + position;

		cursor = mDb.rawQuery(query, null);
		count = cursor.getCount();
		if (count <= 0) {

			cursor.close();
			mDb.close();

			Toast.makeText(PickerList.this, "There is no Lists ",
					Toast.LENGTH_SHORT).show();
			onBackPressed();

		} else {

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

			cursor.close();
			mDb.close();

			final ListView lv = (ListView) findViewById(R.id.listpick);

			lv.setAdapter(new Myadapter());

			lv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int listposition, long arg3) {

					String seletedItem = listnames[listposition];
					Intent in = new Intent(PickerList.this, Listedit.class);
					in.putExtra("seletedItem", seletedItem);
					in.putExtra("catposition", position);

					Boolean myflag = true;
					in.putExtra("flagof", myflag);
					startActivity(in);
					finish();

				}
			});

		}
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

}
