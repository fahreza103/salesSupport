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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.CommonRowAdapter;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.response.Homepass;
import id.co.myrepublic.salessupport.response.MainResponse;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

public class FragmentCustomerProfile extends AbstractFragment implements View.OnClickListener, AsyncTaskListener {

    private static final String CUSTOMER_DATA_TASK_NAME = "customerData";
    private static final String SALUTATION_RESULT_KEY = "salutation";
    private static final String NATIONALITY_RESULT_KEY = "nationality";
    private static final String GENDER_RESULT_KEY = "gender";

    private Button btnConfirm;
    private CustomSpinner spinnerSalutation;
    private CustomSpinner spinnerNationality;
    private CustomSpinner spinnerGender;
    private LinearLayout scrollContentLayout;

    private List<String> salutationList = new ArrayList<String>();
    private List<String> nationalityList = new ArrayList<String>();
    private Map<Object,Object> genderMap = new HashMap<Object,Object>();


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

        if(!isAlreadyLoaded) {
            btnConfirm.setEnabled(false);

            UrlParam urlParamSalutation = new UrlParam();
            urlParamSalutation.setUrl(AppConstant.GET_SALUTATION_API_URL);
            urlParamSalutation.setResultClass(String[].class);
            urlParamSalutation.setResultKey(SALUTATION_RESULT_KEY);

            UrlParam urlParamNationality = new UrlParam();
            urlParamNationality.setUrl(AppConstant.GET_CUSTOMER_NATIONALITIES_API_URL);
            urlParamNationality.setResultClass(String[].class);
            urlParamNationality.setResultKey(NATIONALITY_RESULT_KEY);

            UrlParam urlParamGender = new UrlParam();
            urlParamGender.setUrl(AppConstant.GET_GENDER_API_URL);
            urlParamGender.setResultClass(Map.class);
            urlParamGender.setResultKey(GENDER_RESULT_KEY);

            doApiCallAsyncTask(CUSTOMER_DATA_TASK_NAME,AsyncUiDisplayType.NONE,urlParamSalutation,urlParamNationality,urlParamGender);
            spinnerNationality.runProgress();
            spinnerGender.runProgress();
            spinnerSalutation.runProgress();
        } else {
            populateSpinner(spinnerGender,genderMap);
            populateSpinner(spinnerNationality,nationalityList);
            populateSpinner(spinnerSalutation,salutationList);
        }
    }

    @Override
    public void onClick(View view) {
        HashMap<String,Object> formValues = FormExtractor.extractValues(getContext(),scrollContentLayout,true);
        boolean valid = (boolean) formValues.get(Validator.VALIDATION_KEY_RESULT);
        if(valid) {
            Bundle bundle = this.getArguments();
            bundle.putSerializable("customerData",formValues);

            String customerClass =  bundle.getString("customerClassification");

            Fragment fragment = null;
            if("RES".equals(customerClass)) {
                fragment = new FragmentCustomerUpload();
            } else {
                fragment = new FragmentContactPerson();
            }
            openFragmentExistingBundle(bundle,fragment);
        }
    }

    @Override
    public void onAsyncTaskApiSuccess(Map<String,MainResponse> resultMap, String taskName) {
        int success = 0;
        if(CUSTOMER_DATA_TASK_NAME.equals(taskName)) {
            MainResponse<String> salutationModel = resultMap.get(SALUTATION_RESULT_KEY);
            if(salutationModel != null) {
                salutationList = salutationModel.getListObject();
                populateSpinner(spinnerSalutation,salutationList);
                success++;
            }

            MainResponse<String> nationalityModel = resultMap.get(NATIONALITY_RESULT_KEY);
            if(nationalityModel != null) {
                nationalityList = nationalityModel.getListObject();
                populateSpinner(spinnerNationality,nationalityList);
                success++;
            }

            MainResponse<Map> genderModel = resultMap.get(GENDER_RESULT_KEY);
            if(genderModel != null) {
                genderMap = genderModel.getObject();
                populateSpinner(spinnerGender,genderMap);
                success++;
            }
        }

        // all must success
        if(success == 3)
            btnConfirm.setEnabled(true);
    }


    private void populateSpinner(CustomSpinner spinner, List<String> items) {
        CommonRowAdapter adapter = new CommonRowAdapter(items,getContext());
        adapter.setSpinner(true);
        spinner.setAdapter(adapter);
    }

    private void populateSpinner(CustomSpinner spinner, Map items) {
        CommonRowAdapter adapter = new CommonRowAdapter(items,getContext());
        adapter.setSpinner(true);
        spinner.setAdapter(adapter);
    }
}
