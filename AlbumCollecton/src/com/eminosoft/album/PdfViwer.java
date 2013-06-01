package com.eminosoft.album;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

public class PdfViwer extends Activity {
	String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle bu = this.getIntent().getExtras();
		path = bu.getString("pathOfPdf");
		pdfFileView();

	}

	private void pdfFileView() {

		File file = new File(path);
		if (file.exists()) {
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			try {

				startActivity(intent);
				finish();
			} catch (ActivityNotFoundException e) {

			}
		}

	}

}
