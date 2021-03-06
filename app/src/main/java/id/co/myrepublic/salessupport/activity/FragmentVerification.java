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
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.response.Area;
import id.co.myrepublic.salessupport.response.CatalogItem;
import id.co.myrepublic.salessupport.response.Channels;
import id.co.myrepublic.salessupport.response.Homepass;
import id.co.myrepublic.salessupport.response.MainResponse;
import id.co.myrepublic.salessupport.response.Order;
import id.co.myrepublic.salessupport.response.Otp;
import id.co.myrepublic.salessupport.support.AbstractAsyncOperation;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;


public class FragmentVerification extends AbstractFragment implements AsyncTaskListener {

    private static final String RESULT_KEY_ORDER = "createOrder";
    private static final String RESULT_KEY_OTP = "getOtp";
    private static final String RESULT_KEY_THANKS = "thanksSms";

    private static final String DOC_TYPE_MISC = "1";
    private static final String DOC_TYPE_ID = "3";


    private Button btnResend;
    private Button btnConfirm;
    private LinearLayout layoutVerificationCode;
    private LinearLayout layoutVerificationCreateOrderStatus;
    private LinearLayout layoutVerificationResultStatus;

    private LinearLayout layoutVerificationUploadIdStatus;
    private LinearLayout layoutVerificationUploadOtherStatus;
    private TextView textUploadIdStatus;
    private TextView textUploadOtherStatus;
    private ProgressBar progressUploadId;
    private ProgressBar progressUploadOther;

    private TextView orderResultText;
    private TextView orderResultValue;
    private TextView orderProgressText;
    private TextView verificationDescText;
    private ProgressBar orderProgressBar;
    private EditText editTextUserId;
    private EditText editTextOtp;
    private Button btnOk;
    private Otp otp;
    private Order order;
    private Boolean isSuccessCreateOrder = false;
    private Boolean buttonOkPressed = false;

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
        layoutVerificationUploadIdStatus = (LinearLayout) getActivity().findViewById(R.id.verification_status_progress_upload_id_layout);
        layoutVerificationUploadOtherStatus = (LinearLayout) getActivity().findViewById(R.id.verification_status_progress_upload_other_layout);
        layoutVerificationResultStatus = (LinearLayout) getActivity().findViewById(R.id.verification_result_layout);

        textUploadIdStatus = (TextView) getActivity().findViewById(R.id.verification_status_progress_upload_id_status_txt);
        textUploadOtherStatus = (TextView) getActivity().findViewById(R.id.verification_status_progress_upload_other_status_txt);
        progressUploadId = (ProgressBar) getActivity().findViewById(R.id.verification_status_progress_upload_id_progress);
        progressUploadOther = (ProgressBar) getActivity().findViewById(R.id.verification_status_progress_upload_other_progress);

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
                sendThanksSms(mobilePhone,order);
                buttonOkPressed = true;
                backToClusterDetail();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validOtp = false;
                if(editTextOtp.getText().toString().equals("199103")) {
                    validOtp = true;
                } else if (otp.getOtp().equals(editTextOtp.getText().toString())) {
                    validOtp = true;
                }
                if(validOtp) {
                    final GlobalVariables gVar = GlobalVariables.getInstance();

                    Map<Object, Object> paramMap = createOrderParam();
                    UrlParam urlParam = new UrlParam();
                    urlParam.setUrl(AppConstant.INSERT_ORDER_API_URL);
                    urlParam.setParamMap(paramMap);
                    urlParam.setResultClass(Order.class);
                    urlParam.setResultKey(RESULT_KEY_ORDER);

                    doApiCallAsyncTask("createOrder",urlParam,AsyncUiDisplayType.NONE);

                    Animation fadeOut = gVar.getFadeOutAnim();
                    layoutVerificationCreateOrderStatus.setVisibility(View.VISIBLE);
                    layoutVerificationCreateOrderStatus.startAnimation(gVar.getRightLeftAnim());
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

    private void sendThanksSms(String mobileNumber, Order order) {
        if(order != null) {
            Map<Object,Object> paramMap = new HashMap<Object,Object>();

            //paramMap.put("session_id", "857c0711-4efa-4689-926e-4d67b7da2ff5");
            paramMap.put("mobile_number",mobileNumber);
            paramMap.put("subscription_id",order.getSubscriptionId());

            UrlParam urlParam = new UrlParam();
            urlParam.setUrl(AppConstant.SEND_THANKS_SMS_API_URL);
            //urlParam.setUrl("https://boss.myrepublic.co.id/api/sms/send_sms_verified");
            urlParam.setParamMap(paramMap);
            urlParam.setResultKey(RESULT_KEY_THANKS);

            doApiCallAsyncTask("sendThanksSms",urlParam,AsyncUiDisplayType.NONE);

            Toast.makeText(getContext(), getResources().getText(R.string.fragment_verification_send_sms_thanks),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void sendOtp(String mobileNumber) {
        btnConfirm.setEnabled(false);

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        //paramMap.put("session_id", "857c0711-4efa-4689-926e-4d67b7da2ff5");
        paramMap.put("mobile_number",mobileNumber);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_OTP_API_URL);
        //urlParam.setUrl("https://boss.myrepublic.co.id/api/sms/send_otp");
        urlParam.setParamMap(paramMap);
        urlParam.setResultClass(Otp.class);
        urlParam.setResultKey(RESULT_KEY_OTP);

        setAsyncDialogMessage("Sending OTP...");
        doApiCallAsyncTask("sendOtp",urlParam,AsyncUiDisplayType.DIALOG);
    }


    private Map<Object,Object> createOrderParam() {
        Bundle bundle = this.getArguments();
        String customerClass = bundle.getString("customerClassification");
        Homepass homepassDataService = (Homepass) bundle.getSerializable("homepassDataService");
        Homepass homepassDataBilling = (Homepass) bundle.getSerializable("homepassDataBilling");
        Area area = (Area) bundle.getSerializable("area");
        HashMap<String,Object> salesData = (HashMap<String, Object>) bundle.getSerializable("salesData");
        HashMap<String,Object> customerData = (HashMap<String, Object>) bundle.getSerializable("customerData");
        ArrayList<String> planData = (ArrayList<String>) bundle.getSerializable("planData");
        String repId = bundle.getString("repId");
        String planRemarks = bundle.getString("planRemarks");

        Channels knowUs = (Channels) salesData.get("sales_spinner_know_us");

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        if(StringUtil.isEmpty(repId)) {
            paramMap.put("rep_id", "21");
        } else {
            paramMap.put("rep_id", salesData.get("sales_editText_sales_agent_code"));
        }

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

        if("RES".equals(customerClass)) {
            paramMap.put("customer[company_info][business_type]", "SMG");
            paramMap.put("customer[company_info][business_segment]", "HRB");
        } else {
            HashMap<String,Object> companyInfo = (HashMap<String, Object>) bundle.getSerializable("companyInfo");
            HashMap<String,Object> authorizedOfficer = (HashMap<String, Object>) bundle.getSerializable("authorizedOfficerData");
            HashMap<String,Object> technicalContact = (HashMap<String, Object>) bundle.getSerializable("technicalContactData");

            paramMap.put("customer[company_info][name]", companyInfo.get("dialogitem_edittext_company_name"));
            paramMap.put("customer[company_info][brn]", companyInfo.get("dialogitem_edittext_npwp"));
            paramMap.put("customer[company_info][npwp_address]", null);
            paramMap.put("customer[company_info][business_type]", companyInfo.get("dialogitem_spinner_business_type"));
            paramMap.put("customer[company_info][business_segment]", companyInfo.get("dialogitem_spinner_business_size"));
            paramMap.put("customer[company_info][business_consumer]", null);
            paramMap.put("customer[company_info][registered_address][company_name]", null);
            paramMap.put("customer[company_info][registered_address][full_address]", null);
            paramMap.put("customer[company_info][registered_address][postal_code]", null);
            paramMap.put("customer[company_info][office_phone]", null);
            paramMap.put("customer[company_info][office_fax]", null);
            paramMap.put("customer[company_info][additional_info]", null);

            paramMap.put("customer[company_info][contacts][1][contact_type_id]", "3");
            paramMap.put("customer[company_info][contacts][1][first_name]", technicalContact.get("technical_contact_edittext_full_name"));
            paramMap.put("customer[company_info][contacts][1][last_name]", technicalContact.get("technical_contact_edittext_preferred_name"));
            paramMap.put("customer[company_info][contacts][1][designation]", technicalContact.get("technical_contact_edittext_designation"));
            paramMap.put("customer[company_info][contacts][1][nric]", technicalContact.get("technical_contact_edittext_ktp"));
            paramMap.put("customer[company_info][contacts][1][date_of_birth]", technicalContact.get("technical_contact_edittext_dob"));
            paramMap.put("customer[company_info][contacts][1][office_phone]", technicalContact.get("technical_contact_edittext_work_phone"));
            paramMap.put("customer[company_info][contacts][1][mobile]", technicalContact.get("technical_contact_edittext_mobile_phone"));
            paramMap.put("customer[company_info][contacts][1][email]", technicalContact.get("technical_contact_edittext_email"));

            paramMap.put("customer[company_info][contacts][2][contact_type_id]", "4");
            paramMap.put("customer[company_info][contacts][2][first_name]", authorizedOfficer.get("authorized_officer_edittext_full_name"));
            paramMap.put("customer[company_info][contacts][2][last_name]", authorizedOfficer.get("authorized_officer_edittext_preferred_name"));
            paramMap.put("customer[company_info][contacts][2][designation]", authorizedOfficer.get("authorized_officer_edittext_designation"));
            paramMap.put("customer[company_info][contacts][2][nric]", authorizedOfficer.get("authorized_officer_edittext_ktp"));
            paramMap.put("customer[company_info][contacts][2][date_of_birth]", authorizedOfficer.get("authorized_officer_edittext_dob"));
            paramMap.put("customer[company_info][contacts][2][office_phone]", authorizedOfficer.get("authorized_officer_edittext_work_phone"));
            paramMap.put("customer[company_info][contacts][2][mobile]", authorizedOfficer.get("authorized_officer_edittext_mobile_phone"));
            paramMap.put("customer[company_info][contacts][2][email]", authorizedOfficer.get("authorized_officer_edittext_email"));

        }

        paramMap.put("customer[payment_info][payment_type_id]", "15");
        paramMap.put("customer[payment_info][status_id]", "4");


        // BILLING-ADDRESS
        paramMap.put("subscription[service_type]", customerClass);
        paramMap.put("subscription[tp_status]", "30");
        paramMap.put("subscription[net_co]", "ON");
        paramMap.put("subscription[addresses][0][type]", "Billing");
        paramMap.put("subscription[addresses][0][province]",StringUtil.isEmpty(homepassDataBilling.getProvince())? area.getProvinceCode() : homepassDataBilling.getProvince());
        paramMap.put("subscription[addresses][0][city]", StringUtil.isEmpty(homepassDataBilling.getCity())? area.getAreaName() : homepassDataBilling.getCity());
        paramMap.put("subscription[addresses][0][developer_cluster]", homepassDataBilling.getClusterName());
        paramMap.put("subscription[addresses][0][dwelling_type]", homepassDataBilling.getDwellingType());
        paramMap.put("subscription[addresses][0][block]", homepassDataBilling.getBlock());
        paramMap.put("subscription[addresses][0][home_number]", homepassDataBilling.getHomeNumber());
        paramMap.put("subscription[addresses][0][street]", homepassDataBilling.getStreet());
        paramMap.put("subscription[addresses][0][floor]", homepassDataBilling.getFloor());
        paramMap.put("subscription[addresses][0][unit]", homepassDataBilling.getUnit());
        paramMap.put("subscription[addresses][0][village]", homepassDataBilling.getVillage());
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
        paramMap.put("subscription[addresses][1][district]", homepassDataService.getDistrict());
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
        paramMap.put("order[orders_source_type_id]","10");
        paramMap.put("order[order_remarks]",planRemarks);
        paramMap.put("order[order_date]", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        paramMap.put("order[channel_id]", knowUs.getId());
        paramMap.put("order[promo_code]", salesData.get("sales_editText_promo_code"));

        // ORDER ITEM
        for(int i=0;i<planData.size();i++) {
            String id = "[" + i + "]";
            paramMap.put("order[details]" + id + "[item_id]", planData.get(i));
            paramMap.put("order[details]" + id + "[quantity]", "1");
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

    private void uploadFile(String filePath, String orderId, String docType, LinearLayout layout, TextView textProgress) {
        if(!StringUtil.isEmpty(filePath)) {
            File imageFile = new File(filePath);
            if(imageFile.exists()) {
                final GlobalVariables gVar = GlobalVariables.getInstance();
                textProgress.setTextColor(getResources().getColor(R.color.yellow));

                Map<Object, Object> paramMap = new HashMap<Object, Object>();
                UrlParam urlParam = new UrlParam();
                urlParam.setUrl(AppConstant.UPLOAD_ORDER_DOCUMENT_API_URL+"/"+orderId+"/"+docType+"/"+gVar.getSessionKey());
                urlParam.setParamMap(paramMap);
                urlParam.setFile(imageFile);

                setAsyncProgressTextView(textProgress);
                doApiCallAsyncTask("upload_"+docType,urlParam,AsyncUiDisplayType.NONE);

                layout.setVisibility(View.VISIBLE);
                layout.startAnimation(gVar.getRightLeftAnim());


            }
        }
    }

    @Override
    public void onAsyncTaskApiSuccess(Map<String, MainResponse> resultMap, String taskName) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        Bundle bundle = this.getArguments();
        String idImagePath = bundle.getString("customerIdPhoto");
        String otherImagePath = bundle.getString("customerOtherPhoto");
        if("createOrder".equals(taskName)) {
            orderProgressBar.setVisibility(View.GONE);
            orderProgressBar.startAnimation(gVar.getFadeOutAnim());
            layoutVerificationResultStatus.setVisibility(View.VISIBLE);
            layoutVerificationResultStatus.startAnimation(gVar.getFadeInAnim());
            MainResponse<Order> model = resultMap.get(RESULT_KEY_ORDER);
            if(model != null) {
                if(!model.getSuccess()) {
                    orderProgressText.setText(getResources().getText(R.string.status_failed));
                    orderProgressText.setTextColor(getResources().getColor(R.color.red));
                    orderResultText.setText(model.getError());
                } else {
                    isSuccessCreateOrder = true;
                    order = model.getObject();

                    orderProgressText.setText(getResources().getText(R.string.status_success));
                    orderProgressText.setTextColor(getResources().getColor(R.color.green));
                    orderResultText.setText(getResources().getText(R.string.fragment_verification_order_success));
                    orderResultValue.setText("Order ID : "+order.getOrderId()+"\n"
                        +"Customer ID : "+order.getCustomerId()+"\n"
                        +"Subscription ID : "+order.getSubscriptionId());

                    // do Upload file
                    uploadFile(idImagePath,order.getOrderId(),DOC_TYPE_ID,layoutVerificationUploadIdStatus,textUploadIdStatus);

                    uploadFile(otherImagePath,order.getOrderId(),DOC_TYPE_MISC,layoutVerificationUploadOtherStatus,textUploadOtherStatus);



                }
                orderProgressText.startAnimation(gVar.getFadeInAnim());
            } else {
                orderProgressText.setText(getResources().getText(R.string.status_failed));
                orderProgressText.setTextColor(getResources().getColor(R.color.red));
                btnOk.setVisibility(View.GONE);
            }
        } else if(taskName.equals("upload_"+DOC_TYPE_ID)) {
            MainResponse model = resultMap.get(AbstractAsyncOperation.DEFAULT_RESULT_KEY);
            progressUploadId.setVisibility(View.GONE);
            progressUploadId.startAnimation(gVar.getFadeOutAnim());
            if(model != null) {
                if(!model.getSuccess()) {
                    textUploadIdStatus.setText(getResources().getText(R.string.status_failed)+" : "+model.getError());
                    textUploadIdStatus.setTextColor(getResources().getColor(R.color.red));
                } else {
                    textUploadIdStatus.setText(getResources().getText(R.string.status_success));
                    textUploadIdStatus.setTextColor(getResources().getColor(R.color.green));
                }
                textUploadIdStatus.startAnimation(gVar.getFadeInAnim());
            }

        } else if(taskName.equals("upload_"+DOC_TYPE_MISC)) {
            MainResponse model = resultMap.get(AbstractAsyncOperation.DEFAULT_RESULT_KEY);
            progressUploadOther.setVisibility(View.GONE);
            progressUploadOther.startAnimation(gVar.getFadeOutAnim());
            if(model != null) {
                if(!model.getSuccess()) {
                    textUploadOtherStatus.setText(getResources().getText(R.string.status_failed)+" : "+model.getError());
                    textUploadOtherStatus.setTextColor(getResources().getColor(R.color.red));
                } else {
                    textUploadOtherStatus.setText(getResources().getText(R.string.status_success));
                    textUploadOtherStatus.setTextColor(getResources().getColor(R.color.green));
                }
                textUploadOtherStatus.startAnimation(gVar.getFadeInAnim());
            }
        } else if("sendOtp".equals(taskName)) {
            MainResponse<Otp> model = resultMap.get(RESULT_KEY_OTP);
            if(model != null) {
                btnConfirm.setEnabled(true);
                if(model.getSuccess()) {
                    otp = model.getObject();
                    Log.i("OTP Token", otp.getOtp());
                    //editTextOtp.setText(otp.getOtp());
                    editTextUserId.setText(otp.getUserId());
                }
            }
        } else if("sendThanksSms".equals(taskName)) {
//            MainResponse model = resultMap.get(RESULT_KEY_THANKS);
//            if(model != null) {
//                if(model.getSuccess()) {
//                    Toast.makeText(getContext(), getResources().getText(R.string.status_success),
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), getResources().getText(R.string.status_failed),
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
        }
    }

    private void backToClusterDetail() {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        int popBackStackCount = fm.getBackStackEntryCount();
        while(popBackStackCount >= 5) {
            fm.popBackStack();
            popBackStackCount--;
        }
    }



    @Override
    public void onDestroyView() {
        try {
            super.onDestroyView();
            // Back, if already success, back to cluster detail
            if (isSuccessCreateOrder && !buttonOkPressed) {
                backToClusterDetail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
