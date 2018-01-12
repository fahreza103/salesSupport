package id.co.myrepublic.salessupport.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.CommonRowAdapter;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.listener.DialogListener;
import id.co.myrepublic.salessupport.model.CommonItem;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.model.ResponseClusterInformation;
import id.co.myrepublic.salessupport.support.DialogBuilder;
import id.co.myrepublic.salessupport.support.DownloadAsyncOperation;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;


/**
 * Created by myrepublicid on 26/9/17.
 */

public class ClusterDetailDataFragment extends Fragment implements AsyncTaskListener , View.OnClickListener, DialogListener{

    private ListView listViewClusterDetail;
    private CommonRowAdapter<CommonItem> clusterDetailAdapter;
    private FloatingActionButton fabCompetitor;
    private FloatingActionButton fabSearch;

    private Map<Object, Object> mapResponse;
    private List<CommonItem> dataModels = new ArrayList<CommonItem>();
    private List<String> competitorList = new ArrayList<String>();
    private Dialog dialog;
    private String clusterName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_cluster_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_cluster_detail));


        // Floating Button
        fabCompetitor = (FloatingActionButton) getActivity().findViewById(R.id.fab_addcompetitor);
        fabCompetitor.setOnClickListener(this);

        fabSearch = (FloatingActionButton) getActivity().findViewById(R.id.fab_homepassSearch);
        fabSearch.setOnClickListener(this);

        toggleViewFloatingButton(View.GONE);

        // Get Bundle data from previous fragment
        Bundle bundle = this.getArguments();
        clusterName = bundle.getString("clusterName", null);


        // get citylist from API
        GlobalVariables sm = GlobalVariables.getInstance();
        String sessionId = sm.getSessionKey();

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id",sessionId);
        paramMap.put("cluster_name",clusterName);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_CLUSTERDETAIL_API_URL);
        urlParam.setParamMap(paramMap);


        listViewClusterDetail =(ListView)getActivity().findViewById(R.id.listClusterDetail);

        String caller = getCallerFragment();
        if(dataModels.size() == 0) {
            DownloadAsyncOperation asop = new DownloadAsyncOperation("getClusterDetail");
            asop.setListener(this);
            asop.execute(urlParam);
        } else {
            toggleViewFloatingButton(View.VISIBLE);
            listViewClusterDetail.setAdapter(clusterDetailAdapter);
        }



    }

    private String getCallerFragment(){
        FragmentManager fm = getFragmentManager();
        int count = getFragmentManager().getBackStackEntryCount();
        return fm.getBackStackEntryAt(count-1).getName();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_addcompetitor :
                showCompetitorDialog(clusterName);
                break;
            case R.id.fab_homepassSearch :
                showSearchDialog();
                break;
        }

    }

    private void toggleViewFloatingButton(int visible) {
        fabCompetitor.setVisibility(visible);
        fabSearch.setVisibility(visible);
    }

    private void populateItem(ResponseClusterInformation rci) {
        competitorList = rci.getCompetitorList();
        mapResponse = rci.getCluster();
        if(mapResponse != null && !mapResponse.isEmpty()) {
            for (Map.Entry<Object, Object> entry : mapResponse.entrySet()) {
                String key = entry.getKey() == null ? "-" : ""+ entry.getKey();
                String value = entry.getValue() == null ? "-" : ""+ entry.getValue();

                CommonItem clusterDetail = new CommonItem();
                clusterDetail.setKey(key);
                clusterDetail.setValue(value);
                dataModels.add(clusterDetail);
            }
            clusterDetailAdapter = new CommonRowAdapter(dataModels, getActivity().getApplicationContext());
            clusterDetailAdapter.setWidthText(150);
            listViewClusterDetail.setAdapter(clusterDetailAdapter);
        } else {
            DialogBuilder db = DialogBuilder.getInstance();
            db.createAlertDialog(getContext(), getString(R.string.dialog_title_error),
                    getString(R.string.dialog_content_failedconnect));
        }
    }


    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        if("getClusterDetail".equals(taskName)) {
            toggleViewFloatingButton(View.VISIBLE);
            if(result != null) {
                MainModel<ResponseClusterInformation> model = StringUtil.convertStringToObject("" + result, ResponseClusterInformation.class);
                if(model.getSuccess()) {
                    ResponseClusterInformation rci = model.getObject();
                    populateItem(rci);
                } else {
                    // Error on session (expired or invalid)
                    if(AppConstant.SESSION_VALIDATION.equals(model.getAction())) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }

            }
        } else if ("insertCompetitor".equals(taskName)) {
            dialog.dismiss();
            if(result != null) {
                MainModel model = StringUtil.convertStringToObject("" + result, null);
                Boolean success = model.getSuccess();
                if(success) {
                    Toast.makeText(getActivity(), "Insert Success",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Insert Failed "+model.getError(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                DialogBuilder db = DialogBuilder.getInstance();
                db.createAlertDialog(getContext(), getString(R.string.dialog_title_error),
                        getString(R.string.dialog_content_failedconnect));
            }

        }
    }


    private void showSearchDialog() {
        DialogBuilder db = DialogBuilder.getInstance();
        db.setDialogListener(this);
        db.createInputDialog(getContext(),getString(android.R.string.search_go),
                getString(R.string.dialog_clusterdetail_searchhomepass));
    }

    private void showCompetitorDialog(final String clusterName) {
        final String clusterId = "" + mapResponse.get("clusterid");

        // custom dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_addcompetitor);
        dialog.setTitle(getString(R.string.dialog_addcompetitor));

        // set the custom dialog components - text, spinner
        final Spinner spinnerCompetitor = (Spinner) dialog.findViewById(R.id.dialogitem_spinner_competitor);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, competitorList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitor.setAdapter(spinnerArrayAdapter);

        final TextView txtClusterId = (TextView) dialog.findViewById(R.id.dialogitem_txt_clusterid_value);
        if(mapResponse != null) {
            txtClusterId.setText(clusterId==null || "null".equals(clusterId)? "-" : clusterId);
        }

        TextView txtClusterName = (TextView) dialog.findViewById(R.id.dialogitem_txt_clustername_value);
        txtClusterName.setText(clusterName);

        TextView txtUserId = (TextView) dialog.findViewById(R.id.dialogitem_txt_userid_value);
        txtUserId.setText(GlobalVariables.getInstance().getString(AppConstant.COOKIE_USERID_KEY,""));


        Button cancelButton = (Button) dialog.findViewById(R.id.dialogitem_btn_cancel);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.dialogitem_btn_send);
        // if button is clicked, call API insert cluster
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelected = ""+spinnerCompetitor.getSelectedItem();
                insertCluster(itemSelected,clusterId,clusterName);
            }
        });


        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    private void insertCluster(String competitorName, String clusterId, String clusterName) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();
        String userId = gVar.getString(AppConstant.COOKIE_USERID_KEY,null);

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id",sessionId);
        paramMap.put("cluster_name",clusterName);
        paramMap.put("cluster_id",clusterId);
        paramMap.put("competitor_name",competitorName);
        paramMap.put("created_by",userId);


        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.INSERT_CLUSTERINFO_API_URL);
        urlParam.setParamMap(paramMap);

        Toast.makeText(getActivity(), "Insert Cluster Information...",
                Toast.LENGTH_LONG).show();

        DownloadAsyncOperation asop = new DownloadAsyncOperation("insertCompetitor");
        asop.setListener(this);
        asop.execute(urlParam);

    }

    @Override
    public void onDialogOkPressed(DialogInterface dialog, int which, Object... result) {
        // Search List Homepass
        List<String> homepassSearch = new ArrayList<String>();
        for(CommonItem commonItem : dataModels) {
            if("List Available Homepass".equalsIgnoreCase(commonItem.getKey())) {
                String[] homepassList = commonItem.getValue().split("\n");
                if(!StringUtil.isEmpty((String) result[0])) {
                    for (String homepass : homepassList) {
                        if(homepass.toLowerCase().contains(result[0]+"")) {
                            homepassSearch.add(homepass);
                        }
                    }
                } else {
                    homepassSearch =  Arrays.asList(homepassList);
                }
                break;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("homepassList",new ArrayList<>(homepassSearch));

        Fragment fragment = new HomepassFragment();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDialogCancelPressed(DialogInterface dialog, int which) {}
}
