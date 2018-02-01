package id.co.myrepublic.salessupport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

public class FragmentCustomerProfile extends Fragment implements View.OnClickListener, AsyncTaskListener {

    private static final String ASYNC_SPINNER_ITEMS = "spinnerItems";
    private static final String SALUTATION_RESULT_KEY = "salutation";
    private static final String NATIONALITY_RESULT_KEY = "nationality";
    private static final String GENDER_RESULT_KEY = "gender";

    private Button btnConfirm;
    private CustomSpinner spinnerSalutation;
    private CustomSpinner spinnerNationality;
    private CustomSpinner spinnerGender;
    private LinearLayout scrollContentLayout;

    private ApiConnectorAsyncOperation asop;
    private HashMap<String,Object> formValues = new HashMap<String,Object>();
    private Boolean isAlreadyLoaded = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_customer_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));

        btnConfirm = (Button) getActivity().findViewById(R.id.customer_btn_confirm);
        btnConfirm.setOnClickListener(this);
        scrollContentLayout = (LinearLayout) getActivity().findViewById(R.id.customer_scroll_layout);
        spinnerGender = (CustomSpinner) getActivity().findViewById(R.id.customer_spinner_gender);
        spinnerSalutation = (CustomSpinner) getActivity().findViewById(R.id.customer_spinner_salutation);
        spinnerNationality = (CustomSpinner) getActivity().findViewById(R.id.customer_spinner_nationality);

//        if(!isAlreadyLoaded) {
//            isAlreadyLoaded = true;
//            GlobalVariables gVar = GlobalVariables.getInstance();
//            String sessionId = gVar.getSessionKey();
//
//
//            Map<Object, Object> paramMap = new HashMap<Object, Object>();
//            paramMap.put("session_id", sessionId);
//
//            UrlParam urlParamSalutation = new UrlParam();
//            urlParamSalutation.setUrl(AppConstant.GET_SALUTATION_API_URL);
//            urlParamSalutation.setParamMap(paramMap);
//            urlParamSalutation.setResultKey(SALUTATION_RESULT_KEY);
//
//            UrlParam urlParamNationality = new UrlParam();
//            urlParamNationality.setUrl(AppConstant.GET_CUSTOMER_NATIONALITIES_API_URL);
//            urlParamNationality.setParamMap(paramMap);
//            urlParamNationality.setResultKey(NATIONALITY_RESULT_KEY);
//
//            UrlParam urlParamGender = new UrlParam();
//            urlParamGender.setUrl(AppConstant.GET_GENDER_API_URL);
//            urlParamGender.setParamMap(paramMap);
//            urlParamGender.setResultKey(GENDER_RESULT_KEY);
//
//            asop = new ApiConnectorAsyncOperation(getContext(), ASYNC_SPINNER_ITEMS, AsyncUiDisplayType.NONE);
//            asop.setListener(this);
//            asop.execute(urlParamSalutation,urlParamNationality,urlParamGender);
//
//            spinnerNationality.runProgress();
//            spinnerGender.runProgress();
//            spinnerSalutation.runProgress();
//        } else {
//            populateItem(homePassList);
//        }
    }

    @Override
    public void onClick(View view) {
        formValues = FormExtractor.extractValues(getContext(),scrollContentLayout,true);
        boolean valid = (boolean) formValues.get(Validator.VALIDATION_KEY_RESULT);
        if(valid) {
            Bundle bundle = this.getArguments();
            bundle.putSerializable("customerData",formValues);

            Fragment fragment = new FragmentCustomerUpload();
            fragment.setArguments(bundle);

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, fragment.getClass().getName());
            ft.addToBackStack(fragment.getClass().getName());
            ft.commit();

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {

    }
}
