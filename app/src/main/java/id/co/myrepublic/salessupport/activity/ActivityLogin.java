package id.co.myrepublic.salessupport.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.model.Particulars;
import id.co.myrepublic.salessupport.model.ResponseUserSelect;
import id.co.myrepublic.salessupport.support.AbstractAsyncOperation;
import id.co.myrepublic.salessupport.support.DialogBuilder;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;


/**
 * Created by myrepublicid on 27/9/17.
 */

public class ActivityLogin extends AppCompatActivity implements AsyncTaskListener, View.OnClickListener {

    private ProgressBar mProgressBar;
    private ProgressBar centerProgressBar;
    private TextView centerTextView;
    private ImageView centerProgressLogo;
    private TextView txtErrorTitle;
    private TextView txtErrorCode;
    private TextView txtErrorDescription;
    private Context context;
    private WebView browser;
    private Button btnRetry;
    private LinearLayout errorFailConnectLayout;
    private Button btnErrorRetry;

    private Map<String,String> headerMap = new HashMap<String,String>();
    private Map<String,String> cookieMap = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        setGlobalValue();

        centerProgressBar = (ProgressBar) findViewById(R.id.login_center_progressbar);
        centerTextView = (TextView) findViewById(R.id.login_center_progressbar_text);
        centerProgressLogo = (ImageView) findViewById(R.id.login_center_progressbar_icon);

        browser = (WebView) findViewById(R.id.login_webview_login);
        btnRetry = (Button) findViewById(R.id.login_btn_retry);
        errorFailConnectLayout = (LinearLayout) findViewById(R.id.login_layout_weberror);
        txtErrorTitle = (TextView) findViewById(R.id.login_txt_error_title);
        txtErrorCode = (TextView) findViewById(R.id.login_txt_error_code);
        txtErrorDescription = (TextView) findViewById(R.id.login_txt_error_description);
        btnErrorRetry = (Button) findViewById(R.id.login_btn_error_retry);
        mProgressBar = (ProgressBar) findViewById(R.id.login_progressbar);

        btnRetry.setVisibility(View.GONE);
        browser.setVisibility(View.GONE);
        btnRetry.setOnClickListener(this);
        btnErrorRetry.setOnClickListener(this);

        headerMap.put("display_forgot_password","false");
        browser.getSettings().setAppCacheEnabled(false);

        doCheckSession();

        context = getApplicationContext();
        mProgressBar.setMax(100);

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showErrorWebView(errorCode,"Error",description);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.i("INFO","URL Request : "+url);
                // Success Login , redirect to https://boss-st.myrepublic.co.id/system
                if(url.contains("system")) {
                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.i("INFO", "All the cookies in a string:" + cookies);

                    // Extract Cookie
                    cookieMap = StringUtil.extractCookie(cookies);
                    // Put to Session
                    GlobalVariables sm = GlobalVariables.getInstance();
                    sm.putString(AppConstant.COOKIE_SESSION_KEY,cookieMap.get(AppConstant.COOKIE_SESSION_KEY));
                    sm.putString(AppConstant.COOKIE_USERID_KEY,cookieMap.get(AppConstant.COOKIE_USERID_KEY));
                    sm.putString(AppConstant.COOKIE_USERTYPE_KEY,cookieMap.get(AppConstant.COOKIE_USERTYPE_KEY));

                    // Check Permission
                    Map<Object,Object> paramMap = new HashMap<Object,Object>();
                    paramMap.put("session_id",cookieMap.get(AppConstant.COOKIE_SESSION_KEY));

                    UrlParam urlParam = new UrlParam();
                    urlParam.setUrl(AppConstant.CHECK_PERMISSION);
                    urlParam.setParamMap(paramMap);

                    ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation("checkPermission", AsyncUiDisplayType.SCREEN);
                    asop.setListener(ActivityLogin.this);
                    asop.execute(urlParam);

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
        browser.getSettings().setSaveFormData(false);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setUseWideViewPort(true);
        mProgressBar.setProgress(0);

    }

    @Override
    public void onAsynTaskStart(String taskName) {}

    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        Map<String,String> resultMap = (Map<String,String>) result;
        String jsonResult = resultMap.get(AbstractAsyncOperation.DEFAULT_RESULT_KEY);
        if("checkPermission".equals(taskName)) {
            boolean isPermitted = true;
            if(jsonResult != null) {
                MainModel<Map<Object,Object>> model = StringUtil.convertStringToObject(jsonResult, Map.class);
                Map<Object,Object> mapResponse = model.getObject();
                // Search for mobile app permission
                for (Map.Entry<Object, Object> entry : mapResponse.entrySet()) {
                    String value = entry.getValue() == null ? "-" : ""+ entry.getValue();
                    if(AppConstant.PERMISSION_MOBILE_APP.equals(value))  {
                        isPermitted = true;
                        break;
                    }
                }
            }

            if (isPermitted) {
                // Check User info
                Map<Object,Object> paramMap = new HashMap<Object,Object>();
                paramMap.put("session_id",cookieMap.get(AppConstant.COOKIE_SESSION_KEY));
                paramMap.put("user_id",cookieMap.get(AppConstant.COOKIE_USERID_KEY));

                UrlParam urlParam = new UrlParam();
                urlParam.setUrl(AppConstant.GET_USER_INFO);
                urlParam.setParamMap(paramMap);

                ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation("getUserInfo",AsyncUiDisplayType.SCREEN);
                asop.setListener(ActivityLogin.this);
                asop.execute(urlParam);
            } else {
                showErrorWebView(401,"Unauthorized",getString(R.string.activity_login_failed_permission));
            }

        } else if("getUserInfo".equals(taskName)) {
            if(jsonResult != null) {
                MainModel<ResponseUserSelect> model = StringUtil.convertStringToObject(jsonResult, ResponseUserSelect.class);

                Particulars particulars = model.getObject().getParticulars();
                // save to session
                GlobalVariables sm = GlobalVariables.getInstance();
                sm.putString(AppConstant.COOKIE_USERNAME_KEY,particulars.getFirstName());

                Intent intent = new Intent(context, ActivityMain.class);
                startActivity(intent);
                finish();
            }

        } else if("checkSession".equals(taskName)) {
            centerProgressBar.setVisibility(View.GONE);
            centerTextView.setVisibility(View.GONE);
            centerProgressLogo.setVisibility(View.GONE);

            if (jsonResult != null) {
                MainModel model = StringUtil.convertStringToObject(jsonResult, null);
                // Success, session valid go to main, otherwise show login form, to authenticate
                if (model.getSuccess()) {
                    Intent intent = new Intent(context, ActivityMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    browser.loadUrl(AppConstant.LOGIN_URL,headerMap);
                    browser.setVisibility(View.VISIBLE);
                }
            } else {
                DialogBuilder db = DialogBuilder.getInstance();
                db.createAlertDialog(this, getString(R.string.dialog_title_error),
                        getString(R.string.dialog_content_failedconnect));
                btnRetry.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            // Failed at check session
            case R.id.login_btn_retry :
                doCheckSession();
                break;
            // Failed at webview
            case R.id.login_btn_error_retry :
                errorFailConnectLayout.setVisibility(View.GONE);
                browser.setVisibility(View.VISIBLE);
                browser.loadUrl(AppConstant.LOGIN_URL,headerMap);
                break;
        }

    }

    private void doCheckSession() {
        btnRetry.setVisibility(View.GONE);
        errorFailConnectLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);



        // Check session, if has session and valid, directly go to mainactivity, otherwise login page
        GlobalVariables sm = GlobalVariables.getInstance();
        String sessionId = sm.getSessionKey();

        if(sessionId != null) {
            centerProgressBar.setVisibility(View.VISIBLE);
            centerTextView.setVisibility(View.VISIBLE);
            centerProgressLogo.setVisibility(View.VISIBLE);
            browser.setVisibility(View.GONE);
            ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation("checkSession",AsyncUiDisplayType.SCREEN);
            asop.setListener(this);
            asop.execute(UrlParam.createParamCheckSession(sessionId));
        } else {
            browser.setVisibility(View.VISIBLE);
            browser.loadUrl(AppConstant.LOGIN_URL,headerMap);
        }
    }

    private void showErrorWebView(int errorCode,String title, String description) {
        errorFailConnectLayout.setVisibility(View.VISIBLE);
        txtErrorTitle.setText(title);
        txtErrorCode.setText(getString(R.string.activity_login_failed_code)+" : "+errorCode);
        txtErrorDescription.setText(getString(R.string.activity_login_failed_desc)+" : "+description);

        String blankPage = "<html><body bgcolor=\"#7d17a6\"></body></html>";
        browser.loadData(blankPage, "text/html; charset=utf-8", "utf-8");
    }

    private void setGlobalValue() {
        // Set SharedPreference, and other context based values
        GlobalVariables gvar = GlobalVariables.getInstance();
        gvar.setSharedPreferences(this);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this,R.anim.fade_out);
        Animation popIn = AnimationUtils.loadAnimation(this,R.anim.pop_in);
        Animation downTop = AnimationUtils.loadAnimation(this,R.anim.down_from_top);
        Animation upBottom = AnimationUtils.loadAnimation(this,R.anim.up_from_bottom);
        Animation expandIn = AnimationUtils.loadAnimation(this, R.anim.expand_in);

        gvar.setDownTopAnim(downTop);
        gvar.setFadeInAnim(fadeIn);
        gvar.setFadeOutAnim(fadeOut);
        gvar.setPopupAnim(popIn);
        gvar.setTopDownAnim(upBottom);
        gvar.setExpandAnim(expandIn);
    }
}
