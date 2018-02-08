package id.co.myrepublic.salessupport.activity;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Spinner;
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
import id.co.myrepublic.salessupport.listener.DialogListener;
import id.co.myrepublic.salessupport.model.Channels;
import id.co.myrepublic.salessupport.model.Dealer;
import id.co.myrepublic.salessupport.model.Homepass;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.DialogBuilder;
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

public class FragmentSales extends Fragment implements View.OnClickListener, AsyncTaskListener {

    private static final String RESULT_KEY_KNOW_US = "fetchKnowUs";
    private static final String RESULT_KEY_DEALER  = "fetchDealer";

    private Button btnConfirm;
    private Dialog dialog;
    private LinearLayout scrollContentLayout;
    private CustomEditText editTextsalesCode;
    private CustomEditText editTextSalesName;
    private CustomSpinner spinnerKnowUs;

    private HashMap<String,Object> formValues = new HashMap<String,Object>();
    private List<Channels> channelsList = new ArrayList<Channels>();
    private Boolean isAlreadyLoaded = false;
    private String salesCode;

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

        scrollContentLayout = (LinearLayout) getActivity().findViewById(R.id.sales_scroll_layout);
        btnConfirm = (Button) getActivity().findViewById(R.id.sales_btn_confirm);
        btnConfirm.setOnClickListener(this);

        spinnerKnowUs = (CustomSpinner) getActivity().findViewById(R.id.sales_spinner_know_us);
        editTextsalesCode = (CustomEditText) getActivity().findViewById(R.id.sales_editText_sales_agent_code);
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
            isAlreadyLoaded = true;
            GlobalVariables gVar = GlobalVariables.getInstance();
            String sessionId = gVar.getSessionKey();

            Bundle bundle = this.getArguments();
            String clusterId = bundle.getString("cluster_id");
            String address = bundle.getString("address");

            Map<Object, Object> paramMap = new HashMap<Object, Object>();
            paramMap.put("session_id", sessionId);

            UrlParam urlParam = new UrlParam();
            urlParam.setUrl(AppConstant.GET_CHANNELS_API_URL);
            urlParam.setParamMap(paramMap);
            urlParam.setResultKey(RESULT_KEY_KNOW_US);

            ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation(getContext(), "salesFormData", AsyncUiDisplayType.NONE);
            asop.setDialogMsg("Get Dealer");
            asop.setListener(this);
            asop.execute(urlParam);

            spinnerKnowUs.runProgress();
            btnConfirm.setEnabled(false);

        } else {
            populateKnowUsSpinner(channelsList);
        }
    }

    private void getDealer(String repId) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();

        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("session_id", sessionId);
        paramMap.put("rep_id",repId);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_DEALER_API_URL);
        urlParam.setParamMap(paramMap);
        urlParam.setResultKey(RESULT_KEY_DEALER);

        ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation(getContext(), "dealerData", AsyncUiDisplayType.DIALOG);
        asop.setDialogMsg("Load Sales Agent...");
        asop.setListener(this);
        asop.execute(urlParam);
    }

    @Override
    public void onClick(View v) {
        formValues = FormExtractor.extractValues(getContext(),scrollContentLayout,true);
        boolean result = (boolean) formValues.get(Validator.VALIDATION_KEY_RESULT);
        if(result)
            showAddressDialog();
    }

    private void showAddressDialog() {
        // custom dialog
        dialog = new Dialog(getContext());
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
                DialogBuilder builder = DialogBuilder.getInstance();
                builder.createInputDialog(getContext(),"Billing Address","Enter your billing address");
                builder.setDialogListener(new DialogListener() {
                    @Override
                    public void onDialogOkPressed(DialogInterface dialog, int which, Object... result) {
                        nextFragment((String)result[0],homepass);
                    }
                    @Override
                    public void onDialogCancelPressed(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.dialogitem_btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment(address,homepass);
            }
        });


        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    private void nextFragment(String address, Homepass billingAddress) {
        Bundle bundle = this.getArguments();
        bundle.putSerializable("salesData",formValues);
        bundle.putString("BillingAddress",address);
        bundle.putSerializable("homepassDataBilling",billingAddress);

        Fragment fragment = new FragmentCustomerProfile();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.left_from_right,R.anim.right_from_left, R.anim.left_from_right,R.anim.right_from_left);
        ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        dialog.dismiss();
    }


    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        Map<String,MainModel> resultMap = (Map<String,MainModel>) result;
        if("salesFormData".equals(taskName)) {
            MainModel<Channels> model = resultMap.get(RESULT_KEY_KNOW_US);
            if(model != null) {
                model = StringUtil.convertJsonNodeIntoObject(model, Channels[].class);
                channelsList = model.getListObject();
                btnConfirm.setEnabled(true);
                populateKnowUsSpinner(channelsList);
            }
        } else if("dealerData".equals(taskName)) {
            MainModel<Dealer> model = resultMap.get(RESULT_KEY_DEALER);
            if(model != null) {
                model = StringUtil.convertJsonNodeIntoObject(model, Dealer[].class);
                List<Dealer> dealerList = model.getListObject();
                if(dealerList.size() > 0) {
                    editTextSalesName.setInputValue(dealerList.get(0).getCompanyName());
                    btnConfirm.setEnabled(true);
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
