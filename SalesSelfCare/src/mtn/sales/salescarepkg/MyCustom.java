package mtn.sales.salescarepkg;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class MyCustom {
	public byte[] ConvertToByte(Bitmap bm) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 0, bos);
		return bos.toByteArray();
	}

	public static byte[] getBytes(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	public static Bitmap getImage(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}

}
