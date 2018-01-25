package id.co.myrepublic.salessupport.activity;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import id.co.myrepublic.salessupport.model.Catalog;
import id.co.myrepublic.salessupport.model.CatalogItem;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.model.Particulars;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.widget.AbstractWidget;
import id.co.myrepublic.salessupport.widget.CustomSpinner;


public class FragmentPlan extends Fragment implements View.OnClickListener, AsyncTaskListener, AdapterView.OnItemSelectedListener {

    private Button btnConfirm;
    private LinearLayout scrollContentLayout;
    private Boolean isAlreadyLoaded = false;
    private CustomSpinner spinnerInternet;
    private CustomSpinner spinnerTv;
    private CustomSpinner spinnerStb;
    private CustomSpinner spinnerPromotion;
    private CheckBox checkBoxMovies;
    private CheckBox checkBoxSport;
    private CheckBox checkBoxExtras;
    private CheckBox checkBoxIpPublic;

    private List<CatalogItem> internetItems = new ArrayList<CatalogItem>();
    private List<CatalogItem> tvItems = new ArrayList<CatalogItem>();
    private List<CatalogItem> stbItems = new ArrayList<CatalogItem>();
    private List<CatalogItem> promotionItems = new ArrayList<CatalogItem>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));

        scrollContentLayout = (LinearLayout) getActivity().findViewById(R.id.plan_scroll_layout);

        btnConfirm = (Button) getActivity().findViewById(R.id.plan_btn_confirm);
        btnConfirm.setOnClickListener(this);

        spinnerInternet = (CustomSpinner) getActivity().findViewById(R.id.plan_spinner_internet_package);
        spinnerTv = (CustomSpinner) getActivity().findViewById(R.id.plan_spinner_tv_package);
        spinnerStb = (CustomSpinner) getActivity().findViewById(R.id.plan_spinner_stb_package);
        spinnerPromotion = (CustomSpinner) getActivity().findViewById(R.id.plan_spinner_promotion);
        checkBoxMovies = (CheckBox) getActivity().findViewById(R.id.plan_checkbox_movies);
        checkBoxExtras = (CheckBox) getActivity().findViewById(R.id.plan_checkbox_extras);
        checkBoxSport  = (CheckBox) getActivity().findViewById(R.id.plan_checkbox_sport);
        checkBoxIpPublic = (CheckBox) getActivity().findViewById(R.id.plan_checkbox_ip_public);

        spinnerTv.setOnItemSelectedListener(this);


        if(!isAlreadyLoaded) {
            isAlreadyLoaded = true;

            GlobalVariables gVar = GlobalVariables.getInstance();
            String sessionId = gVar.getSessionKey();

            Bundle bundle = this.getArguments();
            String customerClass = bundle.getString("customerClassification");
            String clusterId = bundle.getString("cluster_id");

            Map<Object, Object> paramMap = new HashMap<Object, Object>();
            paramMap.put("session_id", sessionId);
            paramMap.put("cluster_id",clusterId);
            paramMap.put("customer_type",customerClass);

            UrlParam urlParam = new UrlParam();
            urlParam.setUrl(AppConstant.GET_PRODUCT_CATALOG);
            urlParam.setParamMap(paramMap);
            urlParam.setResultKey("fetchCatalog");

            ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation(getContext(), "planFormData", AsyncUiDisplayType.DIALOG);
            asop.setDialogMsg("Load Plan Data");
            asop.setListener(this);
            asop.execute(urlParam);
        }
        checkSelectedItem();

    }

    private void checkSelectedItem() {
        CatalogItem catalogItem = (CatalogItem)spinnerTv.getSelectedItem();
        if(catalogItem != null) {
            if (StringUtil.isEmpty(catalogItem.getName()) || AbstractWidget.EMPTY_SPINNER_TEXT.equals(catalogItem.getName())) {
                toggleAlaCarte(false);
            } else {
                toggleAlaCarte(true);
            }
        }  else {
            toggleAlaCarte(false);
        }
    }

    @Override
    public void onClick(View v) {
        HashMap<String,Object> planData = FormExtractor.extractValues(getContext(),scrollContentLayout,true);
        Bundle bundle = this.getArguments();
        bundle.putSerializable("planData",planData);

        Fragment fragment = new FragmentVerification();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onAsynTaskStart(String taskName) {}

    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        Map<String,String> resultMap = (Map<String,String>) result;
        if("planFormData".equals(taskName)) {
            String catalogJsonResult = resultMap.get("fetchCatalog");
            if(catalogJsonResult != null) {
                MainModel<Catalog> model = StringUtil.convertStringToObject(catalogJsonResult, Catalog.class);
                Catalog catalog = model.getObject();
                populateSpinner(spinnerInternet,catalog.getInternetItems());
                populateSpinner(spinnerTv,catalog.getTvItems());
                populateSpinner(spinnerStb,catalog.getStbItems());
                populateSpinner(spinnerPromotion,catalog.getPromotions());
            }
        }
    }

    private void populateSpinner(CustomSpinner spinner,List<CatalogItem> catalogList) {
        catalogList = new ArrayList<>(catalogList);
        CatalogItem catalogItem = new CatalogItem();
        catalogItem.setName(AbstractWidget.EMPTY_SPINNER_TEXT);
        catalogList.add(0,catalogItem);

        CommonRowAdapter adapter = new CommonRowAdapter(catalogList,getContext(),R.id.rowitem_maintext);
        adapter.setSpinner(true);
        adapter.setTextGravity(Gravity.CENTER_VERTICAL);

        spinner.setAdapter(adapter);
        //spinner.setSelection(1);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        checkSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void toggleAlaCarte(Boolean active) {
        spinnerStb.setInputEnabled(active);
        checkBoxExtras.setEnabled(active);
        checkBoxMovies.setEnabled(active);
        checkBoxSport.setEnabled(active);

        if(!active) {
            spinnerStb.setSelectedIndex(0);
            if(checkBoxExtras.isChecked())
                checkBoxExtras.toggle();
            if(checkBoxMovies.isChecked())
                checkBoxMovies.toggle();
            if(checkBoxSport.isChecked())
                checkBoxSport.toggle();
        }
    }
}
