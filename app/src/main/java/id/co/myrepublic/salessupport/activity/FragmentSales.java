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

    private Button btnConfirm;
    private Dialog dialog;
    private LinearLayout scrollContentLayout;
    private CustomEditText editTextsalesCode;
    private CustomEditText editTextSalesName;
    private CustomSpinner spinnerKnowUs;

    private HashMap<String,Object> formValues = new HashMap<String,Object>();
    List<Channels> channelsList = new ArrayList<Channels>();
    Boolean isAlreadyLoaded = false;

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
        editTextSalesName = (CustomEditText) getActivity().findViewById(R.id.sales_editText_sales_agent_name);
        editTextsalesCode.setInputOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    editTextSalesName.setInputValue(editTextsalesCode.getInputValue()+"");
                    editTextSalesName.setInputAnimation(GlobalVariables.getInstance().getFadeInAnim(),500);
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
            asop.setListener(this);
            asop.execute(urlParam);

            spinnerKnowUs.runProgress();
            btnConfirm.setEnabled(false);

        } else {
            populateKnowUsSpinner(channelsList);
        }
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
        Homepass homepass = (Homepass) bundle.getSerializable("homepassData");

        final String address = homepass.getHomepassAddressView()+"\n"
                +homepass.getDistrict()+"\n"
                +homepass.getComplex();

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
                        nextFragment((String)result[0]);
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
                nextFragment(address);
            }
        });


        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    private void nextFragment(String address) {
        Bundle bundle = this.getArguments();
        bundle.putSerializable("salesData",formValues);
        bundle.putString("BillingAddress",address);

        Fragment fragment = new FragmentCustomerProfile();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        dialog.dismiss();
    }


    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        Map<String,MainModel> resultMap = (Map<String,MainModel>) result;
        if("salesFormData".equals(taskName)) {
            MainModel<Channels> model = resultMap.get(RESULT_KEY_KNOW_US);
            if(model != null) {
                model = StringUtil.convertJsonNodeIntoObject(model, Channels[].class);
                channelsList = model.getListObject();
                btnConfirm.setEnabled(true);
                populateKnowUsSpinner(channelsList);
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
