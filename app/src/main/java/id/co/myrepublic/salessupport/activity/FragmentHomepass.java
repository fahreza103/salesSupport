package id.co.myrepublic.salessupport.activity;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TabHost;
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
import id.co.myrepublic.salessupport.model.Homepass;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.support.AbstractAsyncOperation;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

/**
 * Created by myrepublicid on 28/11/17.
 */

public class FragmentHomepass extends Fragment implements AsyncTaskListener {

    private ListView listViewHomepass;
    private Dialog dialog;
    private TabHost host;
    private List<Homepass> homePassList = new ArrayList<Homepass>();
    private Boolean isAlreadyLoaded = false;

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

        if(!isAlreadyLoaded) {
            isAlreadyLoaded = true;
            GlobalVariables gVar = GlobalVariables.getInstance();
            String sessionId = gVar.getSessionKey();

            // Get Bundle data from previous fragment
            Bundle bundle = this.getArguments();
            String clusterId = bundle.getString("cluster_id");
            String address = bundle.getString("address");

            Map<Object, Object> paramMap = new HashMap<Object, Object>();
            paramMap.put("session_id", sessionId);
            paramMap.put("cluster_id", clusterId);
            paramMap.put("address", address);

            UrlParam urlParam = new UrlParam();
            urlParam.setUrl(AppConstant.GET_HOMEPASS_API_URL);
            urlParam.setParamMap(paramMap);

            ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation(getContext(), "homepassSearch", AsyncUiDisplayType.DIALOG);
            asop.setDialogMsg("Fetch Homepass Data");
            asop.setListener(this);
            asop.execute(urlParam);
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

        final TextView txtHomepassName = (TextView) dialog.findViewById(R.id.dialogitem_txt_homepass_value);
        txtHomepassName.setText(homepass.getHomepassAddressView());

        initTabHost();

        Button cancelButton = (Button) dialog.findViewById(R.id.dialogitem_btn_cancel);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.dialogitem_btn_ok);
        // if button is clicked, call API insert cluster
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelected = ""+spinnerCustomerClass.getSelectedItem();
                if(!"RES".equals(itemSelected)) {
                    itemSelected = "NON-RES";
                }
                bundle.putSerializable("homepassData",homepass);
                bundle.putString("customerClassification",itemSelected);

                Fragment fragment = new FragmentSales();
                fragment.setArguments(bundle);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
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
    public void onAsynTaskStart(String taskName) {}

    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        Map<String,String> resultMap = (Map<String,String>) result;
        String jsonResult = resultMap.get(AbstractAsyncOperation.DEFAULT_RESULT_KEY);
        if("homepassSearch".equals(taskName)) {
            if(jsonResult != null) {
                MainModel<Homepass> model = StringUtil.convertStringToObject(jsonResult, Homepass[].class);
                if(model.getSuccess()) {
                    homePassList = model.getListObject();
                    int i = 1;
                    for(Homepass homepass : homePassList) {
                        homepass.setHomepassAddressView(
                                homepass.getStreet()+" Block "+
                                homepass.getBlock()+" No. "+
                                homepass.getHomeNumber()
                        );
                        homepass.setNo(i);
                        i++;
                    }
                    populateItem(model.getListObject());
                } else {
                    // Error on session (expired or invalid)
                    if(AppConstant.SESSION_VALIDATION.equals(model.getAction())) {
                        Intent intent = new Intent(getContext(), ActivityLogin.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }

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
                showOrderDialog(homepass);
            }
        });
    }
}
