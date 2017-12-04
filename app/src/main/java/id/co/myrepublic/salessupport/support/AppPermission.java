package id.co.myrepublic.salessupport.support;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Manage Permission, especially for SDK >= 23 it must using runtime permission
 *
 */
public class AppPermission {

    private Context ctx;
    private AppPermission(){}

    private static final int PERMISSION_REQUEST_CODE = 1;


    public static void requestPermission(Activity activityClass) {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission(activityClass))
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                doRequestPermission(activityClass); // Code for permission
            }
        }
    }

    private  static boolean checkPermission(Activity activityClass) {
        int result = ContextCompat.checkSelfPermission(activityClass, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private static void doRequestPermission(Activity activityClass) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activityClass, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activityClass, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activityClass, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}
