package id.co.myrepublic.salessupport.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.CommonRowAdapter;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.response.AddressPrefix;
import id.co.myrepublic.salessupport.response.Channels;
import id.co.myrepublic.salessupport.response.Dealer;
import id.co.myrepublic.salessupport.response.Homepass;
import id.co.myrepublic.salessupport.response.MainResponse;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.widget.CustomEditText;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

/**
 * App Sales fragment
 *
 * @author Fahreza Tamara
 */

public class FragmentSales extends AbstractFragment implements View.OnClickListener, AsyncTaskListener {

    private static final String SALES_DATA_TASK_NAME = "salesFormData";
    private static final String DEALER_TASK_NAME = "dealerData";

    private static final String RESULT_KEY_KNOW_US = "fetchKnowUs";
    private static final String RESULT_KEY_DWELLING_TYPE = "fetchDwellingType";
    private static final String RESULT_KEY_ADDRESS_PREFIX = "fetchAddressPrefix";
    private static final String RESULT_KEY_DEALER  = "fetchDealer";

    private Button btnConfirm;
    private LinearLayout scrollContentLayout;
    private CustomEditText editTextsalesCode;
    private CustomEditText editTextSalesName;
    private CustomSpinner spinnerKnowUs;

    private Map<String,Object> dwellingTypeMap = new HashMap<String,Object>();
    private List<AddressPrefix> addressPrefixList = new ArrayList<AddressPrefix>();
    private List<Channels> channelsList = new ArrayList<Channels>();
    private String salesCode;
    private String repId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_sales, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));


        //this.formKey = SALES_DATA_TASK_NAME;

        GlobalVariables gVar = GlobalVariables.getInstance();

        scrollContentLayout = (LinearLayout) getActivity().findViewById(R.id.sales_scroll_layout);
        btnConfirm = (Button) getActivity().findViewById(R.id.sales_btn_confirm);
        btnConfirm.setOnClickListener(this);

        spinnerKnowUs = (CustomSpinner) getActivity().findViewById(R.id.sales_spinner_know_us);
        editTextsalesCode = (CustomEditText) getActivity().findViewById(R.id.sales_editText_sales_agent_code);
        editTextsalesCode.setInputValue(gVar.getString(AppConstant.COOKIE_LOGIN_NAME,"1"));
        salesCode = editTextsalesCode.getInputValue().toString();
        editTextSalesName = (CustomEditText) getActivity().findViewById(R.id.sales_editText_sales_agent_name);
        editTextsalesCode.setInputOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // Lost focus and new input value not same with the old
                if (!hasFocus && !editTextsalesCode.getInputValue().equals(salesCode)) {
                    btnConfirm.setEnabled(false);
                    salesCode = editTextsalesCode.getInputValue().toString();
                    getDealer(salesCode);
                }
            }
        });

        if(!isAlreadyLoaded) {
            Bundle bundle = this.getArguments();
            String clusterId = bundle.getString("cluster_id");
            String address = bundle.getString("address");


            UrlParam urlParamChannels = new UrlParam();
            urlParamChannels.setUrl(AppConstant.GET_CHANNELS_API_URL);
            urlParamChannels.setResultClass(Channels[].class);
            urlParamChannels.setResultKey(RESULT_KEY_KNOW_US);

            UrlParam urlParamDwelling = new UrlParam();
            urlParamDwelling.setUrl(AppConstant.GET_DWELLING_TYPE_API_URL);
            urlParamDwelling.setResultClass(Map.class);
            urlParamDwelling.setResultKey(RESULT_KEY_DWELLING_TYPE);

            UrlParam urlParamAddressPrefix = new UrlParam();
            urlParamAddressPrefix.setUrl(AppConstant.GET_ADDRESS_PREFIX_API_URL);
            urlParamAddressPrefix.setResultClass(AddressPrefix[].class);
            urlParamAddressPrefix.setResultKey(RESULT_KEY_ADDRESS_PREFIX);

            doApiCallAsyncTask(SALES_DATA_TASK_NAME,AsyncUiDisplayType.NONE,urlParamChannels,urlParamDwelling,urlParamAddressPrefix);
            spinnerKnowUs.runProgress();
            btnConfirm.setEnabled(false);

            getDealer(salesCode);

        } else {
            populateKnowUsSpinner(channelsList);
        }
    }

    private void getDealer(String repId) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();
        btnConfirm.setEnabled(false);

        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("rep_id",repId);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_DEALER_API_URL);
        urlParam.setParamMap(paramMap);
        urlParam.setResultClass(Dealer[].class);
        urlParam.setResultKey(RESULT_KEY_DEALER);

        setAsyncDialogMessage("Load Sales Agent...");
        doApiCallAsyncTask(DEALER_TASK_NAME,urlParam,AsyncUiDisplayType.DIALOG);

    }

    @Override
    public void onClick(View v) {
        this.formData = FormExtractor.extractValues(getContext(),scrollContentLayout,true);
        boolean result = (boolean) formData.get(Validator.VALIDATION_KEY_RESULT);
        if(result)
            showAddressDialog();
    }

    private void showBillingAddressDialog(final String address) {
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_billing_address);

        Button btnConfirm = (Button) dialog.findViewById(R.id.dialogitem_btn_confirm);
        Button btnCancel = (Button) dialog.findViewById(R.id.dialogitem_btn_cancel);
        CustomSpinner spinnerDwellingType = (CustomSpinner) dialog.findViewById(R.id.billing_spinner_dwelling);
        CustomSpinner spinnerAddressPrefix = (CustomSpinner) dialog.findViewById(R.id.billing_spinner_address_prefix);

        CommonRowAdapter dwellingAdapter = new CommonRowAdapter(dwellingTypeMap,getContext());
        dwellingAdapter.setSpinner(true);
        CommonRowAdapter addressPrefixAdapter = new CommonRowAdapter(addressPrefixList,getContext());
        addressPrefixAdapter.setSpinner(true);

        spinnerDwellingType.setAdapter(dwellingAdapter);
        spinnerAddressPrefix.setAdapter(addressPrefixAdapter);

        final LinearLayout billingAddressLayout = (LinearLayout) dialog.findViewById(R.id.billing_address_scroll_layout);
        final Bundle bundle = this.getArguments();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> billingAddressValues = FormExtractor.extractValues(getContext(),billingAddressLayout,true);
                boolean result = (boolean) billingAddressValues.get(Validator.VALIDATION_KEY_RESULT);
                if(result) {

                    //Homepass serviceAddress =  bundle.getSerializable("homepassDataService") == null ? new Homepass() : (Homepass) bundle.getSerializable("homepassDataService");
                    Homepass billingAddress = new Homepass();

                    if(billingAddressValues.get("billing_spinner_address_prefix") != null) {
                        AddressPrefix addressPrefix = (AddressPrefix) billingAddressValues.get("billing_spinner_address_prefix");
                        billingAddress.setAddressPrefix(addressPrefix.getAddressPrefix());
                    }

                    billingAddress.setActive(true);
                    billingAddress.setBlock(""+billingAddressValues.get("billing_txt_dialog_block"));
                    billingAddress.setDwellingType(""+billingAddressValues.get("billing_spinner_dwelling"));
                    billingAddress.setProvince(""+billingAddressValues.get("billing_txt_dialog_province"));
                    billingAddress.setCity(""+billingAddressValues.get("billing_txt_dialog_city"));
                    billingAddress.setClusterName(""+billingAddressValues.get("billing_txt_dialog_cluster"));
                    billingAddress.setDistrict(""+billingAddressValues.get("billing_txt_dialog_district"));
                    billingAddress.setVillage(""+billingAddressValues.get("billing_txt_dialog_village"));
                    billingAddress.setBlock(""+billingAddressValues.get("billing_txt_dialog_block"));
                    billingAddress.setHomeNumber(""+billingAddressValues.get("billing_txt_dialog_home_number"));
                    billingAddress.setMainAddress(""+billingAddressValues.get("billing_txt_dialog_main_address"));
                    billingAddress.setFloor(""+billingAddressValues.get("billing_txt_dialog_floor"));
                    billingAddress.setUnit(""+billingAddressValues.get("billing_txt_dialog_unit"));
                    billingAddress.setPostalcode(""+billingAddressValues.get("billing_txt_dialog_postal_code"));
                    billingAddress.setRt(""+billingAddressValues.get("billing_txt_dialog_rt"));
                    billingAddress.setRw(""+billingAddressValues.get("billing_txt_dialog_rw"));
                    billingAddress.setComplex(""+billingAddressValues.get("billing_txt_dialog_developer_complex"));
                    billingAddress.setBuildingName(""+billingAddressValues.get("billing_txt_dialog_building_name"));
                    billingAddress.setDeveloperSector(""+billingAddressValues.get("billing_txt_dialog_developer_sector"));
                    dialog.dismiss();
                    nextFragment(address,billingAddress , false);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    private void showAddressDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_address_confirmation);

        // Get address info from previous fragment
        Bundle bundle = this.getArguments();
        final Homepass homepass = (Homepass) bundle.getSerializable("homepassDataService");

        final String address = homepass.getHomepassAddressView()+" "+ homepass.getPostalcode()+"\n"
                +homepass.getDistrict()+"\n"
                +homepass.getComplex()+"\n"
                +"Block "+homepass.getBlock()+", "
                +"homenumber "+homepass.getHomeNumber()+"\n"
                +"RT "+homepass.getRt()+" / RW "+homepass.getRw();

        final TextView txtServiceAddress = (TextView) dialog.findViewById(R.id.dialogitem_txt_sales_service_address_value);
        txtServiceAddress.setText(address);

        Button cancelButton = (Button) dialog.findViewById(R.id.dialogitem_btn_cancel);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.dialogitem_btn_no);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showBillingAddressDialog(address);
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.dialogitem_btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                nextFragment(address,homepass,true);
            }
        });


        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    private void nextFragment(String address, Homepass billingAddress, boolean sameAddress) {
        Bundle bundle = this.getArguments();
        bundle.putSerializable("salesData",formData);
        bundle.putString("BillingAddress",address);
        bundle.putBoolean("identicalAddress",sameAddress);
        bundle.putSerializable("homepassDataBilling",billingAddress);
        bundle.putString("repId",repId);

        openFragmentExistingBundle(bundle,new FragmentCustomerProfile());
    }


    @Override
    public void onAsyncTaskApiSuccess(Map<String,MainResponse> resultMap, String taskName) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        if(SALES_DATA_TASK_NAME.equals(taskName)) {
            MainResponse<Channels> model = resultMap.get(RESULT_KEY_KNOW_US);
            if(model != null) {
                channelsList = model.getListObject();
                populateKnowUsSpinner(channelsList);
            }

            MainResponse<Map> modelDwelling = resultMap.get(RESULT_KEY_DWELLING_TYPE);
            if(modelDwelling != null) {
                dwellingTypeMap = modelDwelling.getObject();
            }

            MainResponse<AddressPrefix> modelAddressPrefix = resultMap.get(RESULT_KEY_ADDRESS_PREFIX);
            if(modelAddressPrefix != null) {
                addressPrefixList = modelAddressPrefix.getListObject();
            }

            // load session
            if(getSessionDataForm() != null) {
                FormExtractor.putValues(formData,scrollContentLayout);
            }

        } else if(DEALER_TASK_NAME.equals(taskName)) {
            repId = "";
            MainResponse<Dealer> model = resultMap.get(RESULT_KEY_DEALER);
            btnConfirm.setEnabled(true);
            if(model != null) {
                List<Dealer> dealerList = model.getListObject();
                if(dealerList.size() > 0) {
                    editTextSalesName.setInputValue(dealerList.get(0).getCompanyName());
                    repId = editTextsalesCode.getInputValue()+"";
                } else {
                    editTextSalesName.setInputValue("Sales Agent Not Found!");
                }
                editTextSalesName.startAnimation(gVar.getFadeInAnim());
            }
        }
    }

    private void populateKnowUsSpinner(List<Channels> channelsList) {
        CommonRowAdapter<Channels> adapter = new CommonRowAdapter<Channels>(channelsList,getContext(),R.layout.row_item_common);
        adapter.setSpinner(true);
        adapter.setTextGravity(Gravity.CENTER_VERTICAL);

        spinnerKnowUs.setAdapter(adapter);

    }
}
