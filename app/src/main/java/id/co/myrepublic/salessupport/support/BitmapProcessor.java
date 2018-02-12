package id.co.myrepublic.salessupport.support;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.graphics.BitmapCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains bitmap processing function, such as compression
 *
 * @author Fahreza Tamara
 */

public class BitmapProcessor {

    // Max size in KB
    private static final long MAX_SIZE = 2048;
    // The new size we want to scale to
    private static final int REQUIRED_SIZE=1024;

    /**
     * Create temp image file
     * @return
     */
    public static File createTempBitmapFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/salessupport/");// the file path

        //if it doesn't exist the folder will be created
        if(!folder.exists()) {
            if(!folder.getParentFile().exists())
                folder.getParentFile().mkdirs();
            boolean a = folder.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "salessupport_"+ timeStamp + "_";
        File image_file = null;

        try {
            image_file = File.createTempFile(imageFileName,".jpg",folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image_file;
    }

    /**
     * Reduce size of Bitmap until less than 2MB , it will shrink it's resolution first, if the size still >2MB, reduce the quality
     * @param bitmapFile
     * @return
     */
    public static File compressAndResize(File bitmapFile) {
        long size = bitmapFile.length() / 1024;
        if(size > MAX_SIZE) {
            Bitmap decodedBitmap = decodeFile(bitmapFile);
            int bitmapByteCount= BitmapCompat.getAllocationByteCount(decodedBitmap) / 1024;
            if(bitmapByteCount > MAX_SIZE) {
                bitmapFile = compressBitmap(bitmapFile,decodedBitmap,70);
            } else {
                bitmapFile = compressBitmap(bitmapFile,decodedBitmap,-1);
            }
        }
        return bitmapFile;
    }

    // Decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Compress bitmap , reducing it's quality but make it smaller size
     * @param file, will be replaced
     * @param quality if specified -1, it will not compressed, 0 for smallest size, 100 max quality
     * @return
     */
    public static File compressBitmap(File file, Bitmap selectedImageBitmap, int quality) {
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            if(quality == -1) {
                selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100 , new FileOutputStream(file));
            } else {
                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, quality , new FileOutputStream(file));
            }

            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
