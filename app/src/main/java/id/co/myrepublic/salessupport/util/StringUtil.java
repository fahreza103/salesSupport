package id.co.myrepublic.salessupport.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.model.MainModel;

/**
 * Util class for String operations
 *
 * @author Fahreza Tamara
 */
public class StringUtil {

    public static boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }

    /**
     * get cookies value from string, currently the format is
     * value=xxx;value2=xxx
     * @param cookieStr
     * @return Map based on cookie string
     */
    public static Map<String,String> extractCookie(String cookieStr) {
        Map<String,String> cookieMap = new HashMap<String,String>();
        try {
            String[] cookies = cookieStr.split(";");
            for(String cookie : cookies) {
                String[] keyValuePair = cookie.trim().split("=");
                cookieMap.put(keyValuePair[0],keyValuePair[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cookieMap;
    }

    /**
     * using jackson, convert json string into java object
     * @param jsonString, must be json format string
     * @param clazz, if response is array, please defined the class with array type, example Cluster[].class, this clazz can be null
     *               resulting unconverted JsonNode so you can convert this later using convertJsonNodeIntoObject
     * @return java object MainModel
     */
    public static MainModel convertStringToMainModel(String jsonString, Class<?> clazz) {
        if (jsonString == null) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeFactory t = TypeFactory.defaultInstance();
            MainModel model = mapper.readValue(jsonString,MainModel.class);
            model = convertJsonNodeIntoObject(model,clazz);

            System.out.print(model);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert jsonNode object into specific object defined by the class in argument
     * @param model, if jsonNode inside this object has value, convert into specified object
     * @param clazz, if response is array, please defined the class with array type, example Cluster[].class
     */
    public static MainModel convertJsonNodeIntoObject(MainModel model , Class<?> clazz) {
        JsonNode jsonNode = model.getResponse();
        if(jsonNode != null && clazz != null) {
            ObjectMapper mapper = new ObjectMapper();
            if(jsonNode instanceof ArrayNode) {
                Object[] response = (Object[])mapper.convertValue(jsonNode, clazz);
                model.setListObject(Arrays.asList(response));
            } else {
                Object response = mapper.convertValue(jsonNode, clazz);
                model.setObject(response);
            }
        }
        return model;
    }

    /**
     * Using jackson , convert java obj to json string
     * @param obj
     * @return String represent the object
     */
    public static String convertObjectToString (Object obj) {
        String jsonInString = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonInString;
    }

    /*
 * Gets the file path of the given Uri.
 */
    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri)  {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{ split[1] };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
