package pkg.imagetodb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;


public class ImageToSQLiteDBActivity extends Activity {
	/*
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setContentView(R.layout.main); }
	 */

	private ImageView mMain;
	private ImageDbAdapter mDbAdapter;
	private static final int CAMERA_PIC_REQUEST = 1337;
	
	ImageView imageview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Intent CamIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		imageview = (ImageView) findViewById(R.id.idimage);
		
		mMain = (ImageView) findViewById(R.id.ivMain);
		insertToDB();
		queryFromDB();
	}

	private void insertToDB() {
		mDbAdapter = new ImageDbAdapter(this);
		mDbAdapter.open();

		byte[] image = Utilities.getBytes(BitmapFactory.decodeResource(
				getResources(), R.drawable.sumire02));

		mDbAdapter.insert(image);
	}

	private void queryFromDB() {
		byte[] image = mDbAdapter.fetchSingle(1);
		mMain.setImageBitmap(Utilities.getImage(image));
		mDbAdapter.close();
	}

}