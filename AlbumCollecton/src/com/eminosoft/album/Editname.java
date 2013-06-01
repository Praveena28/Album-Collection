package com.eminosoft.album;

import com.eminosoft.album.MainActivity.Database;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Editname extends Activity {
	public String name;
	public EditText tv;
	public Button ok;
	public String change;
	String position;
	public Database mDbHelp;
	Cursor cursor;
	public static SQLiteDatabase mD;
	Button backbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.name);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Bundle bi = getIntent().getExtras();
		name = bi.getString("passname");
		position = bi.getString("position");
		backbutton = (Button) findViewById(R.id.backbutton);

		tv = (EditText) findViewById(R.id.editname);
		tv.setText(name);
		ok = (Button) findViewById(R.id.ok);
		backbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();

			}
		});

		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				change = tv.getText().toString();

				if (tv.getText().toString().trim().length() == 0) {
					AlertDialog.Builder ag = new AlertDialog.Builder(
							Editname.this);
					ag.setMessage("Enter Album  name ");
					ag.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									tv.requestFocus();
								}

							});
					ag.show();

				}

				else {

					change = tv.getText().toString();
					nameOfCat(change);

					Intent in = new Intent(Editname.this, Category.class);
					in.putExtra("nameofcategory", change);
					in.putExtra("position", position);
					mD.close();
					startActivity(in);
					finish();
				}

			}

			private void nameOfCat(String name) {

				mDbHelp = new Database(Editname.this);
				mD = mDbHelp.getWritableDatabase();

				ContentValues args = new ContentValues();
				args.put("categoryname", name);
				int i = mD.update("Categories", args, "id" + "=" + position,
						null);
				System.out.println(i + "123456");

				// cursor = mD.rawQuery(upquery, null);
				Toast.makeText(Editname.this, "Category name changed",
						Toast.LENGTH_SHORT).show();

			}

		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent in = new Intent(Editname.this, Category.class);
		in.putExtra("nameofcategory", name);
		in.putExtra("position", position);

		Toast.makeText(Editname.this, "Category name not changed",
				Toast.LENGTH_SHORT).show();

		startActivity(in);
		finish();
	}

}
