package com.eminosoft.album;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FullImage extends Activity {
	
	String[] projection = { MediaStore.Images.Media._ID };

	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Intent i = getIntent();

		backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});

		// Selected image id
		int position = i.getExtras().getInt("id");
		// String idOfCat = i.getExtras().getString("sortedcatid");
		//
		// String image_path = MediaStore.Images.Media.DATA
		// .concat(" like '/mnt/sdcard")
		// + "/ImageDataBase/category"
		// + idOfCat + "/" + "%' ";
		//
		// Cursor cursor = managedQuery(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
		// image_path, null, null);
		// int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

		ImageView img = (ImageView) findViewById(R.id.full_image_view);

		// cursor.moveToPosition(position);
		// int imageID = cursor.getInt(columnIndex);
		// Uri uri = Uri.withAppendedPath(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		// Integer.toString(imageID));
		// String url = uri.toString();
		// int originalImageId = Integer.parseInt(url.substring(
		// url.lastIndexOf("/") + 1, url.length()));
		//
		// Bitmap b = MediaStore.Images.Thumbnails.getThumbnail(
		// getContentResolver(), originalImageId,
		// MediaStore.Images.Thumbnails.MINI_KIND, null);
		// Matrix mat = new Matrix();
		// mat.postRotate(90);
		// Bitmap bMapRotate = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
		// b.getHeight(), mat, true);

		Matrix mat = new Matrix();
		mat.postRotate(90);
		Bitmap bit = Category.decodeSampledBitmapFromResource(getResources(),
				MyGrid.paths.get(position), 200, 200);
		Bitmap bMapRotate = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
				bit.getHeight(), mat, true);
		// img.setBackgroundResource(R.drawable.imageborder);

		// simg.setBackgroundDrawable(R.drawable.background);
		img.setImageBitmap(bMapRotate);

	}
}
