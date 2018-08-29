package id.co.myrepublic.salessupport.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.response.MainResponse;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;

/**
 * Abstract fragment, contains operation that has been used by all fragments
 *
 * @author Fahreza Tamara
 */

public abstract class AbstractFragment extends Fragment implements AsyncTaskListener {

    protected String asyncDialogMessage;
    protected TextView asyncProgressTextView;
    protected ApiConnectorAsyncOperation asop;
    protected boolean isAlreadyLoaded;
    protected String formKey;
    protected HashMap<String,Object> formData;


    /**
     * Open fragment with bundle data
     * @param bundle
     * @param fragment
     */
    protected void openFragmentExistingBundle(Bundle bundle, Fragment fragment)  {
        openFragment(bundle,fragment);
    }

    /**
     * Open fragment without bundle data
     * @param fragment
     */
    protected void openFragmentNewBundle(Fragment fragment)  {
        Bundle bundle = new Bundle();
        openFragment(bundle,fragment);
    }

    /**
     * Open fragment by specified argument on fragment class
     * @param bundle
     * @param fragment
     */
    private void openFragment(Bundle bundle, Fragment fragment)  {
        fragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.left_from_right,R.anim.right_from_left, R.anim.left_from_right,R.anim.right_from_left);
        ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    /**
     * Do call API via asyncTask (one request per task), if not file defined the request will be using url form encoded, otherwise if file present
     * it will using multipart form request
     * @param type
     */
    protected void doApiCallAsyncTask(String taskName, UrlParam urlParam, AsyncUiDisplayType type) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();

        if(urlParam.getParamMap().get("session_id") == null)
            urlParam.getParamMap().put("session_id",sessionId);

        asop = new ApiConnectorAsyncOperation(getContext(),taskName, type);
        asop.setDialogMsg(asyncDialogMessage);
        asop.setProgressTextView(asyncProgressTextView);
        asop.setListener(this);
        asop.execute(urlParam);
    }

    /**
     * Do call API via asyncTask (Multiple request per task), by using array of UrlParam, you can specified different request Url and request parameters
     * @param urlParams
     * @param type
     */
    protected void doApiCallAsyncTask(String taskName, AsyncUiDisplayType type,UrlParam... urlParams) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();
        for(UrlParam urlParam :urlParams) {
            if(urlParam.getParamMap().get("session_id") == null)
                urlParam.getParamMap().put("session_id",sessionId);
        }

        asop = new ApiConnectorAsyncOperation(getContext(),taskName, type);
        asop.setDialogMsg(asyncDialogMessage);
        asop.setProgressTextView(asyncProgressTextView);
        asop.setListener(this);
        asop.execute(urlParams);

    }

    /**
     * Invoked when asyncTask is called and complete it's operation, just ignore if you didn't call asyncTask
     * @param result
     * @param taskName
     */
    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        if(result != null) {
            this.isAlreadyLoaded = true;
            if (result instanceof Map) {
                onAsyncTaskApiSuccess((Map<String, MainResponse>) result, taskName);
            }
        }  else {
            onAsyncTaskApiFailed("No result returned from task",taskName);
        }
    }

    public void onAsyncTaskApiSuccess(Map<String,MainResponse> result, String taskName){}

    public void onAsyncTaskApiFailed(String message, String taskName){}


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(asop != null && !asop.isCancelled()) {
            asop.cancel(true);
            ActivityMain.loadingFrame.setVisibility(View.GONE);
        }

        // save into session
        GlobalVariables gVar = GlobalVariables.getInstance();
        if(formData != null && !StringUtil.isEmpty(formKey)) {
            Map<String,Object> sessionFormData = (Map<String, Object>) StringUtil.convertStringToObject(gVar.getString(AppConstant.SESSION_ORDER_DATA_KEY,""),Map.class);
            if(sessionFormData == null) {
                sessionFormData = new HashMap<String,Object>();
            }
            sessionFormData.put(formKey,formData);
            // put back to string and save
            String dataStr = StringUtil.convertObjectToString(sessionFormData);
            gVar.putString(AppConstant.SESSION_ORDER_DATA_KEY,dataStr);


        }
    }

    protected HashMap<String,Object> getSessionDataForm() {
        GlobalVariables gVar = GlobalVariables.getInstance();
        if(!StringUtil.isEmpty(this.formKey)) {
            String formDataStr = gVar.getString(AppConstant.SESSION_ORDER_DATA_KEY,"");
            HashMap<String, Object> sessionOrderMap =  (HashMap<String, Object>) StringUtil.convertStringToObject(formDataStr,Map.class);
            HashMap<String, Object> formDataMap = null;
            if(sessionOrderMap != null) {
                formDataMap = (HashMap<String, Object>) sessionOrderMap.get(this.formKey);
                this.formData = formDataMap;
            }
            return formDataMap;
        }
        return null;
    }

    public String getAsyncDialogMessage() {
        return asyncDialogMessage;
    }

    /**
     * Set this message if AsyncUiType.Dialog is used
     * @param asyncDialogMessage
     */
    public void setAsyncDialogMessage(String asyncDialogMessage) {
        this.asyncDialogMessage = asyncDialogMessage;
    }

    public TextView getAsyncProgressTextView() {
        return asyncProgressTextView;
    }

    /**
     * Set this to show percentage progress, if AsyncUiType.None is used
     * @param asyncProgressTextView
     */
    public void setAsyncProgressTextView(TextView asyncProgressTextView) {
        this.asyncProgressTextView = asyncProgressTextView;
    }
}
