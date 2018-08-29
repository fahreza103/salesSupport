package id.co.myrepublic.salessupport.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Homepass list fragment
 */

public class FragmentHomepass extends AbstractFragment implements AsyncTaskListener {

    private static final String HOMEPASS_TASK_NAME = "homepassSearch";
    private static final String RESULT_KEY_HOMEPASS = "homepass";
    private static final String RESULT_KEY_CUSTOMER_CLASS = "customerClass";
    private static final String RESULT_KEY_COMPANY_TYPE = "companyType";
    private static final String RESULT_KEY_COMPANY_SIZE = "companySize";

    private ListView listViewHomepass;
    private Dialog dialog;
    private TabHost host;
    private List<Homepass> homePassList = new ArrayList<Homepass>();
    private RelativeLayout footerLayout;

    private Map<Object,Object> customerClassMap;
    private List<String> companyTypeList = new ArrayList<String>();
    private List<String> companySizeList = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_homepass, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_homepass));

        listViewHomepass = (ListView) getActivity().findViewById(R.id.listHompass);
        footerLayout = (RelativeLayout) getActivity().findViewById(R.id.homepass_list_footer);


        if(!isAlreadyLoaded) {
            footerLayout.setVisibility(View.GONE);
            GlobalVariables gVar = GlobalVariables.getInstance();
            String sessionId = gVar.getSessionKey();

            // Get Bundle data from previous fragment
            Bundle bundle = this.getArguments();
            String clusterId = bundle.getString("cluster_id");
            String address = bundle.getString("address");

            Map<Object, Object> paramMap = new HashMap<Object, Object>();
            paramMap.put("cluster_id", clusterId);
            paramMap.put("address", address);

            UrlParam urlParamHomepass = new UrlParam();
            urlParamHomepass.setUrl(AppConstant.GET_HOMEPASS_API_URL);
            urlParamHomepass.setParamMap(paramMap);
            urlParamHomepass.setResultClass(Homepass[].class);
            urlParamHomepass.setResultKey(RESULT_KEY_HOMEPASS);

            UrlParam urlParamCustomerClass = new UrlParam();
            urlParamCustomerClass.setUrl(AppConstant.GET_CUSTOMER_TYPE_API_URL);
            urlParamCustomerClass.setParamMap(paramMap);
            urlParamCustomerClass.setResultClass(Map[].class);
            urlParamCustomerClass.setResultKey(RESULT_KEY_CUSTOMER_CLASS);

            UrlParam urlParamCompanyType = new UrlParam();
            urlParamCompanyType.setUrl(AppConstant.GET_BUSINESS_TYPE_API_URL);
            urlParamCompanyType.setParamMap(paramMap);
            urlParamCompanyType.setResultClass(String[].class);
            urlParamCompanyType.setResultKey(RESULT_KEY_COMPANY_TYPE);

            UrlParam urlParamCompanySize = new UrlParam();
            urlParamCompanySize.setUrl(AppConstant.GET_BUSINESS_SIZE_API_URL);
            urlParamCompanySize.setParamMap(paramMap);
            urlParamCompanySize.setResultClass(String[].class);
            urlParamCompanySize.setResultKey(RESULT_KEY_COMPANY_SIZE);

            doApiCallAsyncTask(HOMEPASS_TASK_NAME, AsyncUiDisplayType.SCREEN,
                    urlParamHomepass,urlParamCustomerClass,urlParamCompanyType,urlParamCompanySize);

        } else {
            populateItem(homePassList);
        }
    }

    private void showOrderDialog(final Homepass homepass) {
        final Bundle bundle = this.getArguments();

        // custom dialog
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_homepass_so);

        // set the custom dialog components - text, spinner
        final CustomSpinner spinnerCustomerClass = (CustomSpinner) dialog.findViewById(R.id.dialogitem_spinner_customer_class);
        final LinearLayout companyLayout = (LinearLayout) dialog.findViewById(R.id.dialogitem_layout_company);
        final CustomSpinner spinnerCompanyType = (CustomSpinner) dialog.findViewById(R.id.dialogitem_spinner_business_type);
        final CustomSpinner spinnerCompanySize = (CustomSpinner) dialog.findViewById(R.id.dialogitem_spinner_business_size);

        // set spinner data
        CommonRowAdapter<String> companyTypeAdapter = new CommonRowAdapter<String>(companyTypeList,getContext());
        companyTypeAdapter.setSpinner(true);
        spinnerCompanyType.setAdapter(companyTypeAdapter);

        CommonRowAdapter<String> companySizeAdapter = new CommonRowAdapter<String>(companySizeList,getContext());
        companySizeAdapter.setSpinner(true);
        spinnerCompanySize.setAdapter(companySizeAdapter);

        Button okButton = (Button) dialog.findViewById(R.id.dialogitem_btn_ok);
        TextView textView = (TextView) dialog.findViewById(R.id.homepass_dialog_txt_choose_action);
        if(customerClassMap != null) {
            CommonRowAdapter adapter = new CommonRowAdapter(customerClassMap, getContext());
            adapter.setSpinner(true);
            spinnerCustomerClass.setAdapter(adapter);
        } else {
            okButton.setEnabled(false);
            textView.setText("Insufficient permission");
        }

        final TextView txtHomepassName = (TextView) dialog.findViewById(R.id.dialogitem_txt_homepass_value);
        txtHomepassName.setText(homepass.getHomepassAddressView());

        initTabHost();

        spinnerCustomerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = spinnerCustomerClass.getSelectedItem()+"";
                if("RES".equals(value)) {
                    companyLayout.setVisibility(View.GONE);
                } else {
                    companyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final Button cancelButton = (Button) dialog.findViewById(R.id.dialogitem_btn_cancel);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // if button is clicked, call API insert cluster
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelected = ""+spinnerCustomerClass.getSelectedItem();

                // if non-res, validate and extract the form
                if(!"RES".equals(itemSelected)) {
                    HashMap<String,Object> formValues = FormExtractor.extractValues(getContext(),companyLayout,true);
                    boolean valid = (boolean) formValues.get(Validator.VALIDATION_KEY_RESULT);
                    if(valid) {
                        bundle.putSerializable("companyInfo",formValues);
                    } else {
                        return;
                    }
                }

                bundle.putSerializable("homepassDataService",homepass);
                bundle.putString("customerClassification",itemSelected);

                openFragmentExistingBundle(bundle,new FragmentSales());
                dialog.dismiss();
            }
        });


        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    private void initTabHost() {
        host = (TabHost) dialog.findViewById(R.id.homepass_dialog_tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        View tabIndicator = LayoutInflater.from(getContext()).inflate(R.layout.tab_indicator, host.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.tabhost_indicator_title)).setText(this.getString(R.string.tabhost_create_order));
        ((ImageView) tabIndicator.findViewById(R.id.tabhost_indicator_icon)).setImageResource(R.drawable.tabhost_icon_addorder);

        spec.setIndicator(tabIndicator);
        spec.setContent(R.id.create_order);
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        tabIndicator = LayoutInflater.from(getContext()).inflate(R.layout.tab_indicator, host.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.tabhost_indicator_title)).setText(this.getString(R.string.tabhost_create_survey));
        ((ImageView) tabIndicator.findViewById(R.id.tabhost_indicator_icon)).setImageResource(R.drawable.tabhost_icon_survey);

        spec.setIndicator(tabIndicator);
        spec.setContent(R.id.create_survey);
        host.addTab(spec);
    }


    @Override
    public void onAsyncTaskApiSuccess(Map<String,MainResponse> resultMap,String taskName) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        if(HOMEPASS_TASK_NAME.equals(taskName)) {
            MainResponse homepassModel = resultMap.get(RESULT_KEY_HOMEPASS);
            if(homepassModel != null) {
                homePassList = homepassModel.getListObject();
                int i = 1;
                for(Homepass homepass : homePassList) {
                    homepass.setHomepassAddressView(
                               homepass.getStreet()+" Block "+
                               homepass.getBlock()+" No. "+
                               homepass.getHomeNumber()
                    );
                    homepass.setNo(i);
                    homepass.setActiveText(homepass.getActive() ? getResources().getString(R.string.fragment_homepass_status_available) :
                            getResources().getString(R.string.fragment_homepass_status_taken));
                    i++;
                }
                populateItem(homepassModel.getListObject());
                footerLayout.startAnimation(gVar.getTopDownAnim());
                footerLayout.setVisibility(View.VISIBLE);
            }

            MainResponse<Map<Object,Object>> customerClassModel = resultMap.get(RESULT_KEY_CUSTOMER_CLASS);
            if(customerClassModel != null) {
                List<Map<Object,Object>> listMap = customerClassModel.getListObject();
                if(listMap.size() > 0) {
                    customerClassMap = listMap.get(0);
                }
            }

            MainResponse<String> companyTypeModel = resultMap.get(RESULT_KEY_COMPANY_TYPE);
            if(customerClassModel != null) {
                companyTypeList = companyTypeModel.getListObject();
            }

            MainResponse<String> companySizeModel = resultMap.get(RESULT_KEY_COMPANY_SIZE);
            if(companySizeModel != null) {
                companySizeList = companySizeModel.getListObject();
            }
        }
    }

    private void populateItem(final List<Homepass> listObject) {
        CommonRowAdapter<Homepass> adapter = new CommonRowAdapter<Homepass>(listObject,getActivity().getApplicationContext());
        listViewHomepass.setAdapter(adapter);
        listViewHomepass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Homepass homepass = listObject.get(position);
                if(homepass.getActive()) {
                    showOrderDialog(homepass);
                } else {
                    Toast.makeText(getContext(), getResources().getText(R.string.fragment_homepass_already_taken),
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
