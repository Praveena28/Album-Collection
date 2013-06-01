package com.eminosoft.album;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class Category extends Activity {
	// for database

	public DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	private static final String DATABASE_CREATE_TABLE = "create table if not exists List(id text,listname text,listcomm text,listdate text,path text);";
	private static final String DATABASE_CREATE_TABLE1 = "create table if not exists imagepath(name text,path text);";
	private static final String DATABASE_CREATE_TABLE2 = "create table if not exists pdfthumb(id text,pdfname text,pdfpath text);";
	private static final String DATABASE_CREATE_PATH = "create table if not exists path(category0 text,category1 text,category2 text,category3 text,category4 text,category5 text,category6 text,category7 text,category8 text,category9 text);";

	private static final String DATABASE_NAME = "mydataList.sqlite";
	private static final String DATABASE_TABLE = "List";
	private static final String DATABASE_TABLE1 = "imagepath";
	private static final String DATABASE_TABLE2 = "pdfthumb";
	private static final String DATABASE_TABLE3_PATH = "path";

	private static final int DATABASE_VERSION = 2;

	public Button backbutton;
	Context mCtx;
	MediaScannerConnection mScanner;
	// for storing image path

	public Bitmap bitmap;

	public static Button category;

	public static Button camera;
	File direct;
	Uri outputFileUri;
	protected String path;
	protected boolean taken;

	public static String name_of_category = null;
	public static String position;
	protected static final String PHOTO_TAKEN = "photo_taken";
	ProgressDialog pg;
	Button pdfConvert;
	// for grid
	private Cursor cursor;
	// private Integer cursorCount;
	GridView gv;
	public String cat_name;
	public String image_path;
	String[] projection = { MediaStore.Images.Media._ID };
	Uri uri;

	// for pdf
	List<String> listImgNaes = new ArrayList<String>();
	String name;
	Button pdfViewer;

	// for list
	Button list;
	Button saveList;
	EditText ed_name;
	EditText ed_comment;
	EditText ed_calender;
	String commentString;
	String listname;
	String listnumber;
	ImageView _image;
	String listdate;
	boolean backFlag = false;
	public String date;
	String imagePath;
	Button edit;
	int j;

	// for data base

	public static ArrayList<String> paths = new ArrayList<String>();
	ImageAdapter adapter;

	// public static int width = 500;
	// public static int hight = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cam);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		backbutton = (Button) findViewById(R.id.backbutton);
		camera = (Button) findViewById(R.id.camera);
		edit = (Button) findViewById(R.id.edit);
		category = (Button) findViewById(R.id.category);
		list = (Button) findViewById(R.id.list);

		Bundle b = this.getIntent().getExtras();
		name_of_category = b.getString("nameofcategory");
		position = b.getString("position");
		category.setText(name_of_category);
		path = "/ImageDataBase/category" + position;

		mDbHelper = new DatabaseHelper(Category.this);

		//
		backbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();

			}
		});

		gv = (GridView) findViewById(R.id.gridview);
		gridLoad();

		// gv.setOnItemClickListener(new OnItemClickListener() {
		//
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		//
		// Intent in = new Intent(Category.this, CarasoleImages.class);
		// in.putExtra("position", position);
		//
		// startActivity(in);
		//
		// }
		//
		// });

		pdfConvert = (Button) findViewById(R.id.pdf);
		pdfConvert.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (paths.size() <= 0) {
					Toast.makeText(Category.this,
							"There is no images to convert the pdf",
							Toast.LENGTH_SHORT).show();
				} else {
					Date date = new Date();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss");
					String newPicpdf = df.format(date);
					String pdfName = name_of_category + newPicpdf;

					PdfConvert pdf = new PdfConvert(pdfName);
					pdf.execute();

				}
			}

		});

		pdfViewer = (Button) findViewById(R.id.pdfThumbnail);
		pdfViewer.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent in = new Intent(Category.this, PdfList.class);
				in.putExtra("positionofcat", position);
				in.putExtra("nameofcat", name_of_category);

				startActivity(in);

			}

		});

		list.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent in = new Intent(Category.this, PickerList.class);
				in.putExtra("positionofcat", position);
				startActivity(in);

			}
		});

		System.out.println(path + "hai_category_path");

		edit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// cursor.close();
				Intent in = new Intent(Category.this, Editname.class);
				in.putExtra("passname", name_of_category);
				in.putExtra("position", position);
				startActivity(in);
				finish();

			}
		});

		camera.setOnClickListener(new ButtonClickHandler());

	}

	public boolean pdfCreation(String docName) {

		boolean boolPdf = false;

		fileNames();

		try {

			Document doc = new Document(PageSize.A4);
			File file = new File(Environment.getExternalStorageDirectory(),
					"/ImageDataBase" + "/" + docName + ".pdf");

			System.out.println(file);
			PdfWriter.getInstance(doc, new FileOutputStream(file));
			doc.open();

			Rectangle pageSize = new Rectangle(500, 700);
			doc.setPageSize(pageSize);
			doc.setMargins(0f, 0f, 0f, 0f);
			doc.newPage();

			// doc.add(new Paragraph("Android world.."));

			for (int i = 0; i < listImgNaes.size(); i++) {

				String fileName = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + path + "/" + listImgNaes.get(i);

				Image image = Image.getInstance(fileName);

				Rectangle r = doc.getPageSize();
				image.scaleToFit(r.getWidth(), r.getHeight());

				image.setAlignment(Element.ALIGN_CENTER);
				doc.setPageSize(pageSize);
				doc.setMargins(0f, 0f, 0f, 0f);
				doc.add(image);
				doc.newPage();

			}

			doc.close();
			mDb = mDbHelper.getWritableDatabase();
			ContentValues initialValues = new ContentValues();
			initialValues.put("id", position);

			initialValues.put("pdfname", docName);
			initialValues.put("pdfpath", file.toString());

			mDb.insert(DATABASE_TABLE2, null, initialValues);

			boolPdf = true;
			mDb.close();
			System.out.println("pdf true");

			Toast.makeText(Category.this,
					" PDF file is created for the images", Toast.LENGTH_SHORT)
					.show();

		} catch (Exception e) {

			Toast.makeText(Category.this,
					" PDF file is already created for the images",
					Toast.LENGTH_SHORT).show();
			Log.e("Make PDF", "Could not make pdf file..!");
			boolPdf = false;
		}
		
		pg.dismiss();
		return boolPdf;

	}

	private void fileNames() {

		File sdCardRoot = Environment.getExternalStorageDirectory();
		System.out.println(sdCardRoot);
		File yourDir = new File(sdCardRoot, path);
		for (File f : yourDir.listFiles()) {
			if (f.isFile())
				name = f.getName();
			listImgNaes.add(name);

		}
	}

	public void gridLoad() {
		paths.clear();
		System.out.println(paths.size());

		// image_path =
		// MediaStore.Images.Media.DATA.concat(" like '/mnt/sdcard")
		// + path + "%' ";
		//
		// cursor = getContentResolver().query(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
		// image_path, null, null);
		// cursor.registerDataSetObserver(null);
		// columnIndex =
		// cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

		// System.out.println("cursor count" + cursor.getCount());

		// cursorCount = cursor.getCount();
		// System.out.println(cursorCount);
		String getpathsquery = "SELECT category" + position + " FROM "
				+ DATABASE_TABLE3_PATH;

		mDb = mDbHelper.getReadableDatabase();
		Cursor pathCursor = mDb.rawQuery(getpathsquery, null);
		int pathcolumanIndex = pathCursor.getColumnIndex("category" + position);

		if (pathCursor.moveToFirst()) {
			pathCursor.moveToFirst();

			for (int i = 0; i < pathCursor.getCount(); i++) {

				System.out.println(i + "count of for loop"
						+ pathCursor.getString(pathcolumanIndex) + "-----"
						+ position);

				if (pathCursor.getString(pathcolumanIndex) != null) {

					File f = new File(pathCursor.getString(pathcolumanIndex));
					if (f.exists()) {
						paths.add(pathCursor.getString(pathcolumanIndex));
					}
				}
				pathCursor.moveToNext();
			}

			// if (adapter != null)
			// adapter.notifyDataSetInvalidated();
			pathCursor.close();
			mDb.close();
			gv.setAdapter(new ImageAdapter(Category.this, paths));

		}

		else {
			pathCursor.close();
			mDb.close();
			Toast.makeText(Category.this, "no images", Toast.LENGTH_SHORT)
					.show();
		}

		// if (cursorCount > 0) {
		//
		// } else {
		// Toast.makeText(Category.this, "make some pictures",
		// Toast.LENGTH_SHORT).show();
		// }
	}

	public static class ImageAdapter extends BaseAdapter {
		private Context context;
		ArrayList<String> paths = new ArrayList<String>();

		public ImageAdapter(Context c, ArrayList<String> paths) {
			context = c;
			this.paths = paths;
		}

		public int getCount() {

			return paths.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {

			System.out.println(position);
			return position;

		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView = new ImageView(context);
			if (convertView == null) {
				// imageView = new ImageView(mContext);
				// imageView.setBackgroundResource(R.drawable.imageborder);
				imageView.setLayoutParams(new GridView.LayoutParams(130, 130));
				imageView.setAdjustViewBounds(true);

				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

				imageView.setPadding(6, 6, 6, 6);

			} else {
				imageView = (ImageView) convertView;
			}

			// imageView.setImageResource(mThumbIds[position]);
			Matrix mat = new Matrix();
			mat.postRotate(90);

			Bitmap bit = decodeSampledBitmapFromResource(
					context.getResources(), paths.get(position), 150, 150);
			Bitmap bMapRotate = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
					bit.getHeight(), mat, true);

			imageView.setImageBitmap(bMapRotate);

			// imageView.setImageBitmap(deccodeSampleResourse(ge));
			return imageView;
		}

	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			String imagepath, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagepath, options);
		// BitmapFactory.decodeResource(res, imagepath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imagepath, options);
	}

	private static int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;

	}

	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			startCameraActivity();
			// if (cursorCount < 9) {
			// cursor.close();
			// Log.i("MakeMachine", "ButtonClickHandler.onClick()");
			// startCameraActivity();
			// } else {
			// Toast.makeText(Category.this, "Album is Full",
			// Toast.LENGTH_LONG).show();
			// }

		}
	}

	public void startCameraActivity() {

		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss");
		String newPicFile = df.format(date);

		// date = Calendar.getInstance().getTime().toGMTString()
		// .replace("GMT", "").replaceAll(" ", "").replaceAll(":", "");
		direct = new File(Environment.getExternalStorageDirectory() + path
				+ "/" + newPicFile + ".jpg");

		SharedPreferences path_shared = Category.this.getSharedPreferences(
				"path", MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor ed = path_shared.edit();
		ed.putString("mypath", direct.toString());
		ed.commit();
		ed.putBoolean("phototaken", true);
		ed.commit();

		System.out.println(date + "iiiiiiiiii" + direct);

		outputFileUri = Uri.fromFile(direct);

		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("image data base", "resultCode: " + resultCode);
		switch (resultCode) {
		case 0:
			File file = new File(outputFileUri.getPath());
			boolean deleted = file.delete();
			System.out.println(deleted);

			Log.i("image data based", "User cancelled");
			break;

		case -1:
			onPhotoTaken();

			break;
		}
	}

	protected void onPhotoTaken() {

		Log.i("MakeMachine", "onPhotoTaken");

		System.out.println("executed on photo taken");

		taken = true;
		SharedPreferences path_shared = Category.this.getSharedPreferences(
				"path", MODE_WORLD_WRITEABLE);
		imagePath = path_shared.getString("mypath", "0");
		if (path_shared.getBoolean("phototaken", false)) {

			mDb = mDbHelper.getWritableDatabase();
			String query = "INSERT INTO " + DATABASE_TABLE3_PATH
					+ " ( category" + position + ") VALUES ('" + imagePath
					+ "')";
			System.out.println(query);
			// + (" category" + position) + "VALUES('" + imagePath + "')";

			mDb.execSQL(query);
			// mDb.rawQuery(query, null);
			mDb.close();

			SharedPreferences.Editor ed = path_shared.edit();

			ed.putBoolean("phototaken", false);
			ed.commit();
		}

		scanMedia(imagePath);

		// File newFile = new File(imagePath);
		// Uri outputFileUri = Uri.fromFile(newFile);
		// sendBroadcast(new Intent(
		// Intent.ACTION_MEDIA_MOUNTED,
		// Uri.parse("file://" +imagePath )));
		// Environment.getExternalStorageDirectory();

		// mScanner = new MediaScannerConnection(Category.this,
		// new MediaScannerConnection.MediaScannerConnectionClient() {
		// public void onMediaScannerConnected() {
		// System.out.println(outputFileUri.getPath() + "hai");
		// mScanner.scanFile(outputFileUri.getPath(), null /* mimeType */);
		// }

		// public void onScanCompleted(String path, Uri uri) {
		// // we can use the uri, to get the newly added image, but
		// // it will return path to full sized image
		// // e.g. content://media/external/images/media/7
		// // we can also update this path by replacing media by
		// // thumbnail to get the thumbnail
		// // because thumbnail path would be like
		// // content://media/external/images/thumbnail/7
		// // But the thumbnail is created after some delay by
		// // Android OS
		// // So you may not get the thumbnail. This is why I
		// // started new UI thread
		// // and it'll only run after the current thread
		// // completed.
		// if (path.equals(outputFileUri.getPath())) {
		// mScanner.disconnect();
		// // we need to create new UI thread because, we can't
		// // update our mail thread from here
		// // Both the thread will run one by one, see
		// // documentation of android
		// Category.this.runOnUiThread(new Runnable() {
		// public void run() {
		//
		// }
		// });
		// }
		// }
		// });
		// mScanner.connect();

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.list);

		_image = (ImageView) findViewById(R.id.listpicture);
		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_comment = (EditText) findViewById(R.id.ed_comm);
		ed_calender = (EditText) findViewById(R.id.ed_cal);

		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy (kk:mm:ss)");
		listdate = df.format(date);

		ed_calender.setText(listdate);

		Button save = (Button) findViewById(R.id.list_button);

		mDb = mDbHelper.getWritableDatabase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", name_of_category);

		initialValues.put("path", imagePath);

		mDb.insert(DATABASE_TABLE1, null, initialValues);
		mDb.close();
		save.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				backFlag = true;
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ed_name.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(ed_comment.getWindowToken(), 0);
				listname = ed_name.getText().toString();

				commentString = ed_comment.getText().toString();

				System.out.println(listname + "haiiiiii" + listnumber
						+ "-------" + commentString);
				listname = ed_name.getText().toString();
				if (listname.length() == 0) {
					System.out.println(listname.length());

					ed_name.setError("please enter the name");

				} else if (commentString.length() == 0) {
					System.out.println(listname.length());

					ed_comment.setError("please enter the Comment");

				} else {
					dataLoad(imagePath, listname, listdate, commentString);
					// mDb.close();
					backFlag = false;
					onCreate(null);
				}

			}
		});

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

		System.out.println(date + "this is date");
		Matrix mat = new Matrix();
		mat.postRotate(90);
		Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), mat, true);

		_image.setImageBitmap(bMapRotate);

	}

	private void scanMedia(String path) {
		File file = new File(path);
		Uri uri = Uri.fromFile(file);
		Intent scanFileIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
		sendBroadcast(scanFileIntent);

	}

	private void dataLoad(String imagepath, String listname, String listdate,
			String commentString) {
		mDb = mDbHelper.getWritableDatabase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("id", position);

		initialValues.put("listname", listname);
		initialValues.put("listcomm", commentString);
		initialValues.put("listdate", listdate);
		initialValues.put("path", imagepath);
		mDb.insert(DATABASE_TABLE, null, initialValues);
		mDb.close();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		// cursor.close();
		Intent in = new Intent(Category.this, CategoryPicker.class);
		startActivity(in);
		finish();

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("prudhvi", "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(Category.PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(Category.PHOTO_TAKEN, taken);
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE_TABLE);
			db.execSQL(DATABASE_CREATE_TABLE1);
			db.execSQL(DATABASE_CREATE_TABLE2);
			db.execSQL(DATABASE_CREATE_PATH);
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

	public void close() {

		mDb.close();
	}

	class PdfConvert extends AsyncTask<String, String, String> {

		ProgressDialog pg;
		String docName;
		boolean boolPdf = false;

		public PdfConvert(String pdfName) {
			docName = pdfName;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pg = new ProgressDialog(Category.this);
			pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pg.setMessage("Creating, Please wait");
			pg.show();
		}

		@Override
		protected String doInBackground(String... params) {

			fileNames();

			j = listImgNaes.size();
			System.out.println(listImgNaes.size() + "length........");

			try {

				Document doc = new Document(PageSize.A4);
				File file = new File(Environment.getExternalStorageDirectory(),
						"/ImageDataBase" + "/" + docName + ".pdf");

				System.out.println(file);
				PdfWriter.getInstance(doc, new FileOutputStream(file));
				doc.open();

				Rectangle pageSize = new Rectangle(400, 400);

				doc.setPageSize(pageSize);
				doc.setMargins(0f, 0f, 0f, 0f);
				doc.newPage();

				// doc.add(new Paragraph("Android world.."));

				for (int i = 0; i < listImgNaes.size(); i++) {

					String fileName = Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ path
							+ "/"
							+ listImgNaes.get(i);

					Image image = Image.getInstance(fileName);

					Rectangle r = doc.getPageSize();
					image.scaleToFit(r.getWidth(), r.getHeight());

					image.setAlignment(Element.ALIGN_CENTER);

					image.setRotationDegrees(90);

					doc.setPageSize(pageSize);
					doc.setMargins(0f, 0f, 0f, 0f);
					doc.add(image);
					doc.newPage();

				}

				doc.close();
				mDb = mDbHelper.getWritableDatabase();
				ContentValues initialValues = new ContentValues();
				initialValues.put("id", position);

				initialValues.put("pdfname", docName);
				initialValues.put("pdfpath", file.toString());

				mDb.insert(DATABASE_TABLE2, null, initialValues);

				boolPdf = true;

				System.out.println("pdf true");

			} catch (Exception e) {

				boolPdf = false;
			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pg.dismiss();
			listImgNaes.clear();

			if (boolPdf == true) {
				mDb.close();
				Toast.makeText(Category.this,
						" PDF file is created for the images",
						Toast.LENGTH_SHORT)

				.show();
			} else {
				Toast.makeText(Category.this,
						" PDF file is not  created for the images",
						Toast.LENGTH_SHORT)

				.show();
			}

		}

	}

	@Override
	protected void onDestroy() {

		adapter = null;
		paths.clear();

		super.onDestroy();
	}

}
