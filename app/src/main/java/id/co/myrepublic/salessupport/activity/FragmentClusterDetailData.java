package id.co.myrepublic.salessupport.activity;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import id.co.myrepublic.salessupport.listener.DialogListener;
import id.co.myrepublic.salessupport.model.CommonItem;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.model.ClusterDetail;
import id.co.myrepublic.salessupport.support.AbstractAsyncOperation;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.DialogBuilder;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;


/**
 * Created by myrepublicid on 26/9/17.
 */

public class FragmentClusterDetailData extends Fragment implements AsyncTaskListener , View.OnClickListener, DialogListener{

    private ListView listViewClusterDetail;
    private CommonRowAdapter<CommonItem> clusterDetailAdapter;
    private LinearLayout fabLayout;
    private FloatingActionButton fabCompetitor;
    private FloatingActionButton fabSearch;

    private Map<Object, Object> mapResponse;
    private List<CommonItem> dataModels = new ArrayList<CommonItem>();
    private List<String> competitorList = new ArrayList<String>();
    private Dialog dialog;
    private String clusterName;
    private ApiConnectorAsyncOperation asop;

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
        GlobalVariables gVar = GlobalVariables.getInstance();

        // Floating Button
        fabLayout = (LinearLayout) getActivity().findViewById(R.id.cluster_layout_floating);
        fabCompetitor = (FloatingActionButton) getActivity().findViewById(R.id.fab_addcompetitor);
        fabCompetitor.setOnClickListener(this);

        fabSearch = (FloatingActionButton) getActivity().findViewById(R.id.fab_homepassSearch);
        fabSearch.setOnClickListener(this);




        // Get Bundle data from previous fragment
        Bundle bundle = this.getArguments();
        clusterName = bundle.getString("clusterName", null);


        // get citylist from API
        String sessionId = gVar.getSessionKey();

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id",sessionId);
        paramMap.put("cluster_name",clusterName);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_CLUSTERDETAIL_API_URL);
        urlParam.setParamMap(paramMap);


        listViewClusterDetail =(ListView)getActivity().findViewById(R.id.listClusterDetail);

        String caller = getCallerFragment();
        if(dataModels.size() == 0) {
            toggleViewFloatingButton(View.GONE);
            asop = new ApiConnectorAsyncOperation(getContext(),"getClusterDetail", AsyncUiDisplayType.SCREEN);
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
        fabLayout.setVisibility(visible);
        if(visible == View.VISIBLE) {
            GlobalVariables gVar = GlobalVariables.getInstance();
            fabLayout.startAnimation(gVar.getLeftRightAnim());
        }
    }

    private void populateItem(ClusterDetail rci) {
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

        Map<String,MainModel> resultMap = (Map<String,MainModel>) result;
        MainModel<ClusterDetail> model = resultMap.get(AbstractAsyncOperation.DEFAULT_RESULT_KEY);
        if("getClusterDetail".equals(taskName)) {
            toggleViewFloatingButton(View.VISIBLE);

            if(model != null) {
                model = StringUtil.convertJsonNodeIntoObject(model, ClusterDetail.class);
                ClusterDetail rci = model.getObject();
                populateItem(rci);
            }
        } else if ("insertCompetitor".equals(taskName)) {
            dialog.dismiss();
            if(model != null) {
                Boolean success = model.getSuccess();
                if(success) {
                    Toast.makeText(getActivity(), "Insert Competitor Success",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Insert Competitor Failed "+model.getError(),
                            Toast.LENGTH_LONG).show();
                }
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

        Toast.makeText(getActivity(), "Insert Cluster Competitor...",
                Toast.LENGTH_LONG).show();

        ApiConnectorAsyncOperation asop = new ApiConnectorAsyncOperation(getContext(),"insertCompetitor", AsyncUiDisplayType.SCREEN);
        asop.setListener(this);
        asop.execute(urlParam);

    }

    @Override
    public void onDialogOkPressed(DialogInterface dialog, int which, Object... result) {
        // pass homepass_id and search address value

        Bundle bundle = this.getArguments();
        bundle.putString("cluster_id",clusterName);
        bundle.putString("address", (String) result[0]);

        Fragment fragment = new FragmentHomepass();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.left_from_right,R.anim.right_from_left, R.anim.left_from_right,R.anim.right_from_left);
        ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDialogCancelPressed(DialogInterface dialog, int which) {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(asop != null && !asop.isCancelled()) {
            asop.cancel(true);
            ActivityMain.loadingFrame.setVisibility(View.GONE);
        }
    }
}
