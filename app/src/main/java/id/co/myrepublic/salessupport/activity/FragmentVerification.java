package id.co.myrepublic.salessupport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.model.Area;
import id.co.myrepublic.salessupport.model.CatalogItem;
import id.co.myrepublic.salessupport.model.Channels;
import id.co.myrepublic.salessupport.model.Homepass;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.model.Order;
import id.co.myrepublic.salessupport.model.Otp;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.widget.AbstractWidget;


public class FragmentVerification extends Fragment implements AsyncTaskListener {

    private static final String RESULT_KEY_ORDER = "createOrder";
    private static final String RESULT_KEY_OTP = "getOtp";

    private Button btnResend;
    private Button btnConfirm;
    private LinearLayout layoutVerificationCode;
    private LinearLayout layoutVerificationCreateOrderStatus;
    private LinearLayout layoutVerificationUploadStatus;
    private LinearLayout layoutVerificationResultStatus;
    private TextView orderResultText;
    private TextView orderResultValue;
    private TextView orderProgressText;
    private TextView verificationDescText;
    private ProgressBar orderProgressBar;
    private EditText editTextUserId;
    private EditText editTextOtp;
    private Button btnOk;
    private Otp otp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));

        btnResend = (Button) getActivity().findViewById(R.id.verification_btn_resend);
        btnConfirm = (Button) getActivity().findViewById(R.id.verification_btn_confirm);
        btnOk = (Button) getActivity().findViewById(R.id.verification_btn_ok);

        layoutVerificationCode = (LinearLayout) getActivity().findViewById(R.id.verification_code_layout);
        layoutVerificationCreateOrderStatus = (LinearLayout) getActivity().findViewById(R.id.verification_status_progress_create_order_layout);
        layoutVerificationUploadStatus = (LinearLayout) getActivity().findViewById(R.id.verification_status_progress_upload_layout);
        layoutVerificationResultStatus = (LinearLayout) getActivity().findViewById(R.id.verification_result_layout);
        orderResultValue = (TextView) getActivity().findViewById(R.id.verification_result_value);
        orderResultText = (TextView) getActivity().findViewById(R.id.verification_result_txt);
        orderProgressText = (TextView) getActivity().findViewById(R.id.verification_status_progress_create_order_status_txt);
        verificationDescText = (TextView) getActivity().findViewById(R.id.verification_txt_desc);
        orderProgressBar = (ProgressBar) getActivity().findViewById(R.id.verification_status_progress_create_order_progress);
        editTextOtp = (EditText) getActivity().findViewById(R.id.verification_editText_otp);
        editTextUserId =(EditText) getActivity().findViewById(R.id.verification_editText_userid);

        Bundle bundle = this.getArguments();
        HashMap<String,Object> customerData = (HashMap<String, Object>) bundle.getSerializable("customerData");
        final String mobilePhone = customerData.get("customer_editText_mobilephone")+"";
        verificationDescText.setText(getResources().getText(R.string.fragment_verification_desc)+" "+mobilePhone);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Back to cluster detail

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                int popBackStackCount = fm.getBackStackEntryCount();
                while(popBackStackCount >= 5) {
                    fm.popBackStack();
                    popBackStackCount--;
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp.getOtp().equals(editTextOtp.getText().toString())) {
                    final GlobalVariables gVar = GlobalVariables.getInstance();

                    Map<Object, Object> paramMap = createOrderParam();
                    UrlParam urlParam = new UrlParam();
                    urlParam.setUrl(AppConstant.INSERT_ORDER_API_URL);
                    urlParam.setParamMap(paramMap);
                    urlParam.setResultKey(RESULT_KEY_ORDER);

                    ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation(getContext(), "createOrder", AsyncUiDisplayType.NONE);
                    asop.setListener(FragmentVerification.this);
                    asop.execute(urlParam);

                    Animation fadeOut = gVar.getFadeOutAnim();
                    layoutVerificationCreateOrderStatus.setVisibility(View.VISIBLE);
                    layoutVerificationCreateOrderStatus.startAnimation(gVar.getFadeInAnim());
                    layoutVerificationCode.setVisibility(View.GONE);
                    layoutVerificationCode.startAnimation(fadeOut);
                } else {
                    editTextOtp.setError("Invalid OTP");
                }

            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp(mobilePhone);
            }
        });

        sendOtp(mobilePhone);
    }

    private void sendOtp(String mobileNumber) {
        btnConfirm.setEnabled(false);
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id", sessionId);
        paramMap.put("mobile_number",mobileNumber);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_OTP_API_URL);
        urlParam.setParamMap(paramMap);
        urlParam.setResultKey(RESULT_KEY_OTP);

        ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation(getContext(), "sendOtp", AsyncUiDisplayType.DIALOG);
        asop.setDialogMsg("Sending OTP...");
        asop.setListener(FragmentVerification.this);
        asop.execute(urlParam);
    }


    private Map<Object,Object> createOrderParam() {
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();

        Bundle bundle = this.getArguments();
        String customerClass = bundle.getString("customerClassification");
        Homepass homepassDataService = (Homepass) bundle.getSerializable("homepassDataService");
        Homepass homepassDataBilling = (Homepass) bundle.getSerializable("homepassDataBilling");
        Area area = (Area) bundle.getSerializable("area");
        HashMap<String,Object> salesData = (HashMap<String, Object>) bundle.getSerializable("salesData");
        HashMap<String,Object> customerData = (HashMap<String, Object>) bundle.getSerializable("customerData");
        HashMap<String,Object> planData = (HashMap<String, Object>) bundle.getSerializable("planData");
        String idImagePath = bundle.getString("customerIdPhoto");
        String selfieImagePath = bundle.getString("customerSelfiePhoto");
        Channels knowUs = (Channels) salesData.get("sales_spinner_know_us");

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id", sessionId);
        paramMap.put("rep_id", salesData.get("sales_editText_sales_agent_code"));
        paramMap.put("event_rep_id", salesData.get("sales_editText_event_rep_id"));
        paramMap.put("homepassdetailid", homepassDataService.getHomepassDetailId());
        paramMap.put("customer[type]", customerClass);
        paramMap.put("customer[referrer_customer_id]",salesData.get("sales_editText_customer_reff"));

        // CUSTOMER-PROFILE
        paramMap.put("customer[users][0][contact_type_id]", "1");
        paramMap.put("customer[users][0][salutation]", customerData.get("customer_spinner_salutation"));
        paramMap.put("customer[users][0][first_name]", customerData.get("customer_txt_full_name"));
        paramMap.put("customer[users][0][last_name]", customerData.get("customer_editText_prefered_name"));
        paramMap.put("customer[users][0][gender]", customerData.get("customer_spinner_gender"));
        paramMap.put("customer[users][0][nric]", customerData.get("customer_editText_id_type"));
        paramMap.put("customer[users][0][date_of_birth]", customerData.get("customer_editText_dob"));
        paramMap.put("customer[users][0][nationality]", customerData.get("customer_spinner_nationality"));
        paramMap.put("customer[users][0][work_phone]", customerData.get("customer_editText_workphone"));
        paramMap.put("customer[users][0][home_phone]", customerData.get("customer_editText_homephone"));
        paramMap.put("customer[users][0][mobile]", customerData.get("customer_editText_mobilephone"));
        paramMap.put("customer[users][0][email]", customerData.get("customer_editText_email"));

        // COMPANY-INFO
        paramMap.put("customer[company_info][contacts][0][contact_type_id]", "1");
        paramMap.put("customer[company_info][contacts][0][salutation]", customerData.get("customer_spinner_salutation"));
        paramMap.put("customer[company_info][contacts][0][first_name]", customerData.get("customer_txt_full_name"));
        paramMap.put("customer[company_info][contacts][0][last_name]", customerData.get("customer_editText_prefered_name"));
        paramMap.put("customer[company_info][contacts][0][gender]", customerData.get("customer_spinner_gender"));
        paramMap.put("customer[company_info][contacts][0][nric]", customerData.get("customer_editText_id_type"));
        paramMap.put("customer[company_info][contacts][0][date_of_birth]", customerData.get("customer_editText_dob"));
        paramMap.put("customer[company_info][contacts][0][office_phone]", customerData.get("customer_editText_workphone"));
        paramMap.put("customer[company_info][contacts][0][mobile]", customerData.get("customer_editText_mobilephone"));
        paramMap.put("customer[company_info][contacts][0][email]", customerData.get("customer_editText_email"));


        paramMap.put("customer[payment_info][payment_type_id]", "15");
        paramMap.put("customer[payment_info][status_id]", "4");
        paramMap.put("customer[company_info][business_type]", "SMG");
        paramMap.put("customer[company_info][business_segment]", "HRB");

        // BILLING-ADDRESS
        paramMap.put("subscription[service_type]", customerClass);
        paramMap.put("subscription[tp_status]", "30");
        paramMap.put("subscription[net_co]", "ON");
        paramMap.put("subscription[addresses][0][type]", "Billing");
        paramMap.put("subscription[addresses][0][province]",homepassDataBilling.getProvince());
        paramMap.put("subscription[addresses][0][city]", homepassDataBilling.getCity());
        paramMap.put("subscription[addresses][0][developer_cluster]", homepassDataBilling.getClusterName());
        paramMap.put("subscription[addresses][0][dwelling_type]", homepassDataBilling.getDwellingType());
        paramMap.put("subscription[addresses][0][block]", homepassDataBilling.getBlock());
        paramMap.put("subscription[addresses][0][home_number]", homepassDataBilling.getHomeNumber());
        paramMap.put("subscription[addresses][0][street]", homepassDataBilling.getStreet());
        paramMap.put("subscription[addresses][0][floor]", homepassDataBilling.getFloor());
        paramMap.put("subscription[addresses][0][unit]", homepassDataBilling.getUnit());
        paramMap.put("subscription[addresses][0][village]", homepassDataService.getVillage());
        paramMap.put("subscription[addresses][0][district]", homepassDataBilling.getDistrict());
        paramMap.put("subscription[addresses][0][postal_code]", homepassDataBilling.getPostalcode());
        paramMap.put("subscription[addresses][0][rw]", homepassDataBilling.getRw());
        paramMap.put("subscription[addresses][0][rt]", homepassDataBilling.getRt());
        paramMap.put("subscription[addresses][0][address_prefix]", homepassDataBilling.getAddressPrefix());
        paramMap.put("subscription[addresses][0][building_name]", homepassDataBilling.getBuildingName());
        paramMap.put("subscription[addresses][0][developer_complex]", homepassDataBilling.getComplex());
        paramMap.put("subscription[addresses][0][developer_sector]", homepassDataBilling.getDeveloperSector());
        paramMap.put("subscription[addresses][0][homepassdetailid]", homepassDataService.getHomepassDetailId());
        paramMap.put("subscription[addresses][0][country_code]", "IDN");

        // SERVICE-ADDRESS
        paramMap.put("subscription[addresses][1][type]", "Service");
        paramMap.put("subscription[addresses][1][province]", area.getProvinceCode());
        paramMap.put("subscription[addresses][1][city]", area.getAreaName());
        paramMap.put("subscription[addresses][1][developer_cluster]", homepassDataService.getClusterName());
        paramMap.put("subscription[addresses][1][dwelling_type]", homepassDataService.getDwellingType());
        paramMap.put("subscription[addresses][1][block]", homepassDataService.getBlock());
        paramMap.put("subscription[addresses][1][home_number]", homepassDataService.getHomeNumber());
        paramMap.put("subscription[addresses][1][street]", homepassDataService.getStreet());
        paramMap.put("subscription[addresses][1][floor]", homepassDataService.getFloor());
        paramMap.put("subscription[addresses][1][unit]", homepassDataService.getUnit());
        paramMap.put("subscription[addresses][1][village]", "");
        paramMap.put("subscription[addresses][0][district]", homepassDataService.getDistrict());
        paramMap.put("subscription[addresses][1][postal_code]", homepassDataService.getPostalcode());
        paramMap.put("subscription[addresses][1][rw]", homepassDataService.getRw());
        paramMap.put("subscription[addresses][1][rt]", homepassDataService.getRt());
        paramMap.put("subscription[addresses][1][address_prefix]", homepassDataService.getAddressPrefix());
        paramMap.put("subscription[addresses][1][building_ready]", homepassDataService.getAvailability());
        paramMap.put("subscription[addresses][1][building_name]", homepassDataService.getBuildingName());
        paramMap.put("subscription[addresses][1][developer_complex]", homepassDataService.getComplex());
        paramMap.put("subscription[addresses][1][developer_sector]", "");
        paramMap.put("subscription[addresses][1][homepassdetailid]", homepassDataService.getHomepassDetailId());
        paramMap.put("subscription[addresses][1][country_code]", "IDN");


        paramMap.put("order[type]", "New Order");
        paramMap.put("order[order_date]", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        paramMap.put("order[channel_id]", knowUs.getId());
        paramMap.put("order[promo_code]", salesData.get("sales_editText_promo_code"));

        // ORDER ITEM
        int i = 0;
        if(planData.get("plan_spinner_internet_package") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_internet_package");
            paramMap = setOrderItem(paramMap,item,i);
            if(item.getId() != null) {i++;}
        }

        if(planData.get("plan_spinner_tv_package") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_tv_package");
            paramMap = setOrderItem(paramMap,item,i);
            if(item.getId() != null) {i++;}
        }

        if(planData.get("plan_spinner_stb_package") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_stb_package");
            paramMap = setOrderItem(paramMap,item,i);
            if(item.getId() != null) {i++;}
        }

        if(planData.get("plan_spinner_promotion") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_stb_package");
            paramMap = setOrderItem(paramMap,item,i);
            if(item.getId() != null) {i++;}
        }

        if(planData.get("plan_checkboxes_alacarte") != null) {
            Map<String,Boolean> alaCarteMap = (Map<String,Boolean>) planData.get("plan_checkboxes_alacarte");
            Boolean movies = alaCarteMap.get(AbstractWidget.CHECKBOX_TAG+"_Movies");
            if(movies) {
                CatalogItem item = new CatalogItem();
                item.setId(AppConstant.ALACARTE_MOVIES_ID);
                setOrderItem(paramMap,item,i);
                i++;
            }

            Boolean sport = alaCarteMap.get(AbstractWidget.CHECKBOX_TAG+"_Sport");
            if(sport) {
                CatalogItem item = new CatalogItem();
                item.setId(AppConstant.ALACARTE_SPORT_ID);
                setOrderItem(paramMap,item,i);
                i++;
            }

            Boolean xtras = alaCarteMap.get(AbstractWidget.CHECKBOX_TAG+"_Xtras");
            if(xtras) {
                CatalogItem item = new CatalogItem();
                item.setId(AppConstant.ALACARTE_XTRATV_ID);
                setOrderItem(paramMap,item,i);
                i++;
            }

//            Boolean ip = alaCarteMap.get(AbstractWidget.CHECKBOX_TAG+"_IP Public");
//            if(ip) {
//                CatalogItem item = new CatalogItem();
//                item.setId(AppConstant.ALACARTE_PUBLICIP_ID);
//                setOrderItem(paramMap,item,i);
//                i++;
//            }


        }
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(paramMap);
            Log.i( "JSON: %s", json );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return paramMap;
    }

    private Map<Object,Object> setOrderItem (Map<Object,Object> paramMap, CatalogItem item, int i) {
        if(item.getId() != null) {
            String id = "[" + i + "]";
            paramMap.put("order[details]" + id + "[item_id]", item.getId());
//            paramMap.put("order[details]" + id + "[bundle_item_id]", null);
            paramMap.put("order[details]" + id + "[quantity]", "1");
//            paramMap.put("order[details]" + id + "[price_amount]", null);
//            paramMap.put("order[details]" + id + "[duration_id]", null);
//            paramMap.put("order[details]" + id + "[duration]", null);
//            paramMap.put("order[details]" + id + "[trigger_month]", null);
//            paramMap.put("order[details]" + id + "[proratable]", null);
//            paramMap.put("order[details]" + id + "[proration_waivable]", null);
        }
        return paramMap;
    }

    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        Map<String,MainModel> resultMap = (Map<String,MainModel>) result;
        if("createOrder".equals(taskName)) {
            orderProgressBar.setVisibility(View.GONE);
            orderProgressBar.startAnimation(gVar.getFadeOutAnim());
            layoutVerificationResultStatus.setVisibility(View.VISIBLE);
            layoutVerificationResultStatus.startAnimation(gVar.getFadeInAnim());
            MainModel<Order> model = resultMap.get(RESULT_KEY_ORDER);
            if(model != null) {
                if(!model.getSuccess()) {
                    orderProgressText.setText(getResources().getText(R.string.status_failed));
                    orderProgressText.setTextColor(getResources().getColor(R.color.red));
                    orderProgressText.startAnimation(gVar.getFadeInAnim());
                    orderResultText.setText(model.getError());
                } else {
                    model = StringUtil.convertJsonNodeIntoObject(model, Order.class);
                    Order order = model.getObject();

                    orderProgressText.setText(getResources().getText(R.string.status_success));
                    orderProgressText.setTextColor(getResources().getColor(R.color.green));
                    orderProgressText.startAnimation(gVar.getFadeInAnim());
                    orderResultText.setText(getResources().getText(R.string.fragment_verification_order_success));
                    orderResultValue.setText("Order ID : "+order.getOrderId()+"\n"
                        +"Customer ID : "+order.getCustomerId()+"\n"
                        +"Subscription ID : "+order.getSubscriptionId());
                }
            }
        } else if("sendOtp".equals(taskName)) {
            MainModel<Otp> model = resultMap.get(RESULT_KEY_OTP);
            if(model != null) {
                btnConfirm.setEnabled(true);
                model = StringUtil.convertJsonNodeIntoObject(model,Otp.class);
                if(model.getSuccess()) {
                    otp = model.getObject();
                    Log.i("OTP Token", otp.getOtp());
                    editTextOtp.setText(otp.getOtp());
                    editTextUserId.setText(otp.getUserId());
                }
            }
        }
    }
}
