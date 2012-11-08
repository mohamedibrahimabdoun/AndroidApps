package app.selfcare.pkg;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class MyCustom {
public byte[] ConvertToByte(Bitmap bm) {
	ByteArrayOutputStream bos=new ByteArrayOutputStream();
	bm.compress(CompressFormat.PNG, 0, bos);
	return bos.toByteArray();	
}

}
