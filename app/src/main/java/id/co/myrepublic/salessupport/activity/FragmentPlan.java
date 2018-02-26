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
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import id.co.myrepublic.salessupport.response.Catalog;
import id.co.myrepublic.salessupport.response.CatalogItem;
import id.co.myrepublic.salessupport.response.Homepass;
import id.co.myrepublic.salessupport.response.MainResponse;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.widget.AbstractWidget;
import id.co.myrepublic.salessupport.widget.CheckboxParam;
import id.co.myrepublic.salessupport.widget.Checkboxes;
import id.co.myrepublic.salessupport.widget.CustomSpinner;


public class FragmentPlan extends AbstractFragment implements View.OnClickListener, AsyncTaskListener, AdapterView.OnItemSelectedListener {

    private static final String PLAN_DATA_TASK_NAME = "catalog_";
    private static final String PLAN_VALIDATION_TASK_NAME = "validation";
    private static final String TAG_MAIN_BUNDLE = "101";
    private static final String TAG_TV_BUNDLE = "102";
    private static final String TAG_TV_ADDON_BUNDLE = "103";
    private static final String TAG_INTERNET_ADDON = "104";
    private static final String TAG_PROMO_BUNDLE = "105";
    private static final String TAG_ONT_BUNDLE = "106";
    private static final String TAG_STB_BUNDLE = "107";
    private static final String TAG_HARDWARE_BUNDLE = "108";
    private static final String RESULT_KEY_CATALOG = "fetchCatalog";
    private static final String RESULT_KEY_VALIDATION =  "orderValidation";
    private Button btnConfirm;
    private LinearLayout scrollContentLayout;
    private CustomSpinner spinnerInternet;
    private CustomSpinner spinnerTv;
    private CustomSpinner spinnerStb;
    private CustomSpinner spinnerPromotion;
    private CustomSpinner spinnerOnt;
    private CustomSpinner spinnerHardware;
    private Checkboxes  checkBoxesAlaCarte;
    private Checkboxes  checkBoxesInternetAddon;
    private TextView textViewError;
    private ScrollView scrollView;
    private Catalog catalog = new Catalog();
    private int planLoaded = 0;

    private String[] checkboxesTag;

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
        spinnerOnt = (CustomSpinner) getActivity().findViewById(R.id.plan_spinner_ont_package);
        spinnerHardware = (CustomSpinner) getActivity().findViewById(R.id.plan_spinner_hardware);
        checkBoxesAlaCarte = (Checkboxes) getActivity().findViewById(R.id.plan_checkboxes_alacarte);
        checkBoxesInternetAddon = (Checkboxes) getActivity().findViewById(R.id.plan_checkboxes_internet_addon);
        scrollView = (ScrollView) getActivity().findViewById(R.id.plan_scroll_view);
        textViewError = (TextView) getActivity().findViewById(R.id.plan_textview_error);

        spinnerInternet.setOnItemSelectedListener(this);
        spinnerTv.setOnItemSelectedListener(this);


        if(!isAlreadyLoaded) {
            loadPlan(TAG_ONT_BUNDLE);
            loadPlan(TAG_MAIN_BUNDLE);
            loadPlan(TAG_TV_BUNDLE);
            loadPlan(TAG_STB_BUNDLE);
            loadPlan(TAG_TV_ADDON_BUNDLE);
            loadPlan(TAG_HARDWARE_BUNDLE);
            loadPlan(TAG_INTERNET_ADDON);
            loadPlan(TAG_PROMO_BUNDLE);



            spinnerHardware.runProgress();
            spinnerOnt.runProgress();
            spinnerStb.runProgress();
            spinnerInternet.runProgress();
            spinnerPromotion.runProgress();
            spinnerTv.runProgress();
            checkBoxesAlaCarte.runProgress();
            checkBoxesInternetAddon.runProgress();
            btnConfirm.setEnabled(false);

            // Get tag, but remove public IP since it's not part of TV Package Bundle
            //checkboxesTag = getContext().getResources().getStringArray(R.array.alacarte_arrays);
            //checkboxesTag = Arrays.copyOf(checkboxesTag, checkboxesTag.length-1);
        } else {
            populateSpinner(spinnerInternet,catalog.getInternetItems());
            populateSpinner(spinnerTv,catalog.getTvItems());
            populateSpinner(spinnerStb,catalog.getStbItems());
            populateSpinner(spinnerPromotion,catalog.getPromotions());
            populateSpinner(spinnerHardware,catalog.getRouterItems());
            populateSpinner(spinnerOnt,catalog.getOntItems());
            populateCheckboxes(checkBoxesAlaCarte,catalog.getVasItems());
            populateCheckboxes(checkBoxesInternetAddon,catalog.getInternetAddonItems());
        }
        //checkSelectedItem();

    }

    private void loadPlan(String tagType) {
        GlobalVariables gVar = GlobalVariables.getInstance();

        Bundle bundle = this.getArguments();
        String customerClass = bundle.getString("customerClassification");
        String clusterId = bundle.getString("cluster_id");
        Homepass homepassDataService = (Homepass) bundle.getSerializable("homepassDataService");

        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("homepassdetailid",homepassDataService.getHomepassDetailId());
        //paramMap.put("customer_type",customerClass);
        paramMap.put("customer_type",customerClass);
        paramMap.put("rep_id",gVar.getString(AppConstant.COOKIE_REP_ID,"1"));
        paramMap.put("tag[0]",tagType);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_ORDER_CATALOG_API_URL);
        urlParam.setParamMap(paramMap);
        urlParam.setResultClass(Catalog.class);
        urlParam.setResultKey(RESULT_KEY_CATALOG);

        doApiCallAsyncTask(PLAN_DATA_TASK_NAME+tagType,urlParam,AsyncUiDisplayType.NONE);
    }

    private void checkSelectedItem() {
        // To avoid classCast Exception, because the spinner using runProgress that will generate 1 item with String
        // so before the spinner generate catalogItem from API, the item is one string
        if(spinnerInternet.getSelectedItem() instanceof CatalogItem) {
            CatalogItem catalogItem = (CatalogItem) spinnerInternet.getSelectedItem();
            if (catalogItem != null) {
                if (StringUtil.isEmpty(catalogItem.getName()) || AbstractWidget.SPINNER_EMPTY_TEXT.equals(catalogItem.getName())) {
                    toggleInternet(false);
                } else {
                    toggleInternet(true);
                }
            } else {
                toggleInternet(false);
            }
        }

        if(spinnerTv.getSelectedItem() instanceof CatalogItem) {
            CatalogItem catalogItem = (CatalogItem) spinnerTv.getSelectedItem();
            if (catalogItem != null) {
                if (StringUtil.isEmpty(catalogItem.getName()) || AbstractWidget.SPINNER_EMPTY_TEXT.equals(catalogItem.getName())) {
                    toggleAlaCarte(false);
                } else {
                    toggleAlaCarte(true);
                }
            } else {
                toggleAlaCarte(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        HashMap<String,Object> planData = FormExtractor.extractValues(getContext(),scrollContentLayout,true);
        boolean valid = (boolean) planData.get(Validator.VALIDATION_KEY_RESULT);
        if(valid) {
            // Validate from API
            Bundle bundle = this.getArguments();
            bundle.putString("planRemarks",""+planData.get("plan_editText_remarks"));
            validateApi(planData);
        }
    }

    private void validateApi(HashMap<String, Object> planData) {
        Bundle bundle = this.getArguments();
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();
        ArrayList<String> planList = new ArrayList<String>();

        textViewError.setVisibility(View.GONE);
        // Collect plan ID
        if(planData.get("plan_spinner_internet_package") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_internet_package");
            if(!StringUtil.isEmpty(item.getId())) {
                planList.add(item.getId());
            }
        }

        if(planData.get("plan_spinner_tv_package") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_tv_package");
            if(!StringUtil.isEmpty(item.getId())) {
                planList.add(item.getId());
            }
        }

        if(planData.get("plan_spinner_stb_package") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_stb_package");
            if(!StringUtil.isEmpty(item.getId())) {
                planList.add(item.getId());
            }
        }

        if(planData.get("plan_spinner_promotion") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_promotion");
            if(!StringUtil.isEmpty(item.getId())) {
                planList.add(item.getId());
            }
        }

        if(planData.get("plan_spinner_ont_package") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_ont_package");
            if(!StringUtil.isEmpty(item.getId())) {
                planList.add(item.getId());
            }
        }

        if(planData.get("plan_spinner_hardware") != null) {
            CatalogItem item = (CatalogItem) planData.get("plan_spinner_hardware");
            if(!StringUtil.isEmpty(item.getId())) {
                planList.add(item.getId());
            }
        }

        if(planData.get("plan_checkboxes_alacarte") != null) {
            Map<String,CheckboxParam> alaCarteMap = (Map<String,CheckboxParam>) planData.get("plan_checkboxes_alacarte");
            for (Map.Entry<String, CheckboxParam> entry : alaCarteMap.entrySet()) {
                CheckboxParam checkboxParam = entry.getValue();
                if(checkboxParam != null) {
                    if(checkboxParam.getChecked()) {
                        CatalogItem item = new CatalogItem();
                        if(!StringUtil.isEmpty(item.getId())) {
                            planList.add(item.getId());
                        }
                    }
                }
            }
        }

        if(planData.get("plan_checkboxes_internet_addon") != null) {
            Map<String,CheckboxParam> serviceMap = (Map<String,CheckboxParam>) planData.get("plan_checkboxes_internet_addon");
            for (Map.Entry<String, CheckboxParam> entry : serviceMap.entrySet()) {
                CheckboxParam checkboxParam = entry.getValue();
                if(checkboxParam != null) {
                    if(checkboxParam.getChecked()) {
                        CatalogItem item = new CatalogItem();
                        if(!StringUtil.isEmpty(item.getId())) {
                            planList.add(item.getId());
                        }
                    }
                }
            }
        }
        bundle.putStringArrayList("planData",planList);

        // Send request to API
        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id", sessionId);
        for(int i=0; i<planList.size();i++) {
            paramMap.put("items["+ i +"]",planList.get(i));
        }

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.VALIDATE_ORDER_API_URL);
        urlParam.setParamMap(paramMap);
        urlParam.setResultKey(RESULT_KEY_VALIDATION);

        setAsyncDialogMessage("Validating Order");
        doApiCallAsyncTask(PLAN_VALIDATION_TASK_NAME,urlParam,AsyncUiDisplayType.DIALOG);

    }


    @Override
    public void onAsyncTaskApiSuccess(Map<String,MainResponse> resultMap, String taskName) {
        if(taskName.contains(PLAN_DATA_TASK_NAME)) {
            MainResponse<Catalog> model = resultMap.get(RESULT_KEY_CATALOG);
            if(model!=null) {
                String tagId = taskName.split("_")[1];
                Catalog catalogItem = model.getObject();
                List<CatalogItem> catalogItemList = catalogItem.getData();

                if(tagId.equals(TAG_MAIN_BUNDLE)) {
                    populateSpinner(spinnerInternet,catalogItemList);
                    catalog.setInternetItems(catalogItemList);
                    planLoaded++;
                } else if(tagId.equals(TAG_HARDWARE_BUNDLE)) {
                    populateSpinner(spinnerHardware,catalogItemList);
                    catalog.setRouterItems(catalogItemList);
                    planLoaded++;
                } else if(tagId.equals(TAG_INTERNET_ADDON)) {
                    populateCheckboxes(checkBoxesInternetAddon,catalogItemList);
                    catalog.setInternetAddonItems(catalogItemList);
                    planLoaded++;
                } else if(tagId.equals(TAG_ONT_BUNDLE)) {
                    populateSpinner(spinnerOnt,catalogItemList);
                    catalog.setOntItems(catalogItemList);
                    planLoaded++;
                } else if(tagId.equals(TAG_PROMO_BUNDLE)) {
                    populateSpinner(spinnerPromotion,catalogItemList);
                    catalog.setPromotions(catalogItemList);
                    planLoaded++;
                } else if(tagId.equals(TAG_STB_BUNDLE)) {
                    populateSpinner(spinnerStb,catalogItemList);
                    catalog.setStbItems(catalogItemList);
                    planLoaded++;
                } else if(tagId.equals(TAG_TV_ADDON_BUNDLE)) {
                    populateCheckboxes(checkBoxesAlaCarte,catalogItemList);
                    catalog.setVasItems(catalogItemList);
                    planLoaded++;
                } else if(tagId.equals(TAG_TV_BUNDLE)) {
                    catalog.setTvItems(catalogItemList);
                    populateSpinner(spinnerTv,catalogItemList);
                    planLoaded++;
                }

            }

            if(planLoaded >= 8) {
                checkSelectedItem();
                btnConfirm.setEnabled(true);
            }
        } else if(PLAN_VALIDATION_TASK_NAME.equals(taskName)) {
            MainResponse model = resultMap.get(RESULT_KEY_VALIDATION);
            if(model != null) {
                if(model.getSuccess()) {
                    Bundle bundle = this.getArguments();
                    openFragmentExistingBundle(bundle,new FragmentVerification());
                } else {
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    textViewError.requestFocus();
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(model.getError());
                }
            }
        }



//        if("planFormData".equals(taskName)) {
//            MainResponse<Catalog> model = resultMap.get(RESULT_KEY_CATALOG);
//            if(model != null) {
//                model = StringUtil.convertJsonNodeIntoObject(model, Catalog.class);
//                catalog = model.getObject();
//                populateSpinner(spinnerInternet,catalog.getInternetItems());
//                populateSpinner(spinnerTv,catalog.getTvItems());
//                populateSpinner(spinnerStb,catalog.getStbItems());
//                populateSpinner(spinnerPromotion,catalog.getPromotions());
//
//                btnConfirm.setEnabled(true);
//            }
//        }
    }

    private void populateSpinner(CustomSpinner spinner,List<CatalogItem> catalogList) {
        catalogList = new ArrayList<>(catalogList);
        CatalogItem catalogItem = new CatalogItem();
        catalogItem.setName(AbstractWidget.SPINNER_EMPTY_TEXT);
        catalogList.add(0,catalogItem);

        CommonRowAdapter adapter = new CommonRowAdapter(catalogList,getContext(),R.id.rowitem_maintext);
        adapter.setSpinner(true);
        adapter.setTextGravity(Gravity.CENTER_VERTICAL);

        spinner.setAdapter(adapter);
        //spinner.setSelection(1);

    }

    private void populateCheckboxes (Checkboxes checkboxes, List<CatalogItem> catalogList) {
        Map<String,Object> dataMap = new HashMap<String,Object>();
        if(catalogList != null && catalogList.size() > 0) {
            for(CatalogItem catalogItem : catalogList) {
                dataMap.put(catalogItem.getName(),catalogItem.getId());
            }
        }
        checkboxes.setEntriesMap(dataMap);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        checkSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void toggleInternet(Boolean active) {
        spinnerTv.setInputEnabled(active);
        spinnerHardware.setInputEnabled(active);
        spinnerPromotion.setInputEnabled(active);
        checkBoxesInternetAddon.setCheckEnabled(active);

        if(!active) {
            spinnerTv.setSelectedIndex(0);
            spinnerHardware.setSelectedIndex(0);
            spinnerPromotion.setSelectedIndex(0);
            checkBoxesInternetAddon.setChecked(active);
        }
    }

    private void toggleAlaCarte(Boolean active) {
        spinnerStb.setInputEnabled(active);
        checkBoxesAlaCarte.setCheckEnabled(active);

        if(!active) {
            spinnerStb.setSelectedIndex(0);
            checkBoxesAlaCarte.setChecked(active);
            spinnerStb.setValidator(null);
            spinnerStb.setError("");
        } else {
            spinnerStb.setValidator(Validator.VALIDATION_REQUIRED);
        }
    }
}
