package id.co.myrepublic.salessupport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.Map;

import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.util.StringUtil;


/**
 * Created by myrepublicid on 27/9/17.
 */

public class LoginActivity extends AppCompatActivity {

//    private EditText editTextUsername;
//    private EditText editTextPassword;
//    private Button btnLogin;
//    private static final String username = "fahreza";
//    private static final String password = "fahreza";
    private ProgressBar mProgressBar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check session, if has session and valid, directly go to mainactivity, otherwise login page


        context = getApplicationContext();

        mProgressBar = (ProgressBar) findViewById(R.id.login_progressbar);
        mProgressBar.setMax(100);

        WebView browser = (WebView) findViewById(R.id.login_webview_login);
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.i("INFO","URL Request : "+url);
                // Success Login , redirect to https://boss-st.myrepublic.co.id/system
                if(url.contains("system")) {
                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.i("INFO", "All the cookies in a string:" + cookies);

                    // Extract Cookie
                    Map<String,String> cookieMap = StringUtil.extractCookie(cookies);
                    // Put to Session
                    SharedPreferences sp = getSharedPreferences(AppConstant.SESSION_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(AppConstant.COOKIE_SESSION_KEY,cookieMap.get(AppConstant.COOKIE_SESSION_KEY));
                    editor.putString(AppConstant.COOKIE_USERID_KEY,cookieMap.get(AppConstant.COOKIE_USERID_KEY));
                    editor.putString(AppConstant.COOKIE_USERTYPE_KEY,cookieMap.get(AppConstant.COOKIE_USERTYPE_KEY));
                    editor.commit();

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return super.shouldOverrideUrlLoading(view,url);
            }

            @Override
            public void onPageFinished(WebView view, String url){
            }
        });

        browser.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress){
                // Update the progress bar with page loading progress
                mProgressBar.setProgress(newProgress);
                setTitle("Loading...");
                if(newProgress == 100){
                    // Hide the progressbar
                    mProgressBar.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                }
                super.onProgressChanged(view,newProgress);
            }
        });

        // Enable the javascript
        browser.getSettings().setJavaScriptEnabled(true);
        browser.loadUrl(AppConstant.LOGIN_URL);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setUseWideViewPort(true);
        mProgressBar.setProgress(0);

    }

//    @Override
//    public void onClick(View view) {
//        if(password.equals(editTextPassword.getText().toString()) && username.equals(editTextUsername.getText().toString())) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
//            builder1.setMessage(getString(R.string.activity_login_invalid));
//            builder1.setCancelable(true);
//            builder1.setPositiveButton(
//                    "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert11 = builder1.create();
//            alert11.show();
//        }
//    }
}
