package id.co.myrepublic.salessupport.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import id.co.myrepublic.salessupport.util.AsyncOperation;
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

        AsyncOperation asop = new AsyncOperation("getClusterDetail");
        asop.setListener(this);
        asop.execute(urlParam);

        listViewClusterDetail =(ListView)getActivity().findViewById(R.id.listClusterDetail);

        // Floating Button
        fabCompetitor = (FloatingActionButton) getActivity().findViewById(R.id.fab_addcompetitor);
        fabCompetitor.setVisibility(View.GONE);
        fabCompetitor.setOnClickListener(this);

        fabSearch = (FloatingActionButton) getActivity().findViewById(R.id.fab_homepassSearch);
        fabSearch.setVisibility(View.GONE);
        fabSearch.setOnClickListener(this);

    }

    @Override
    public void onDestroyView ()
    {
        super.onDestroyView();
        try{
            fabCompetitor.setVisibility(View.GONE);
            String backStateName = this.getClass().getName();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            ClusterDetailDataFragment fragment = ((ClusterDetailDataFragment) getFragmentManager().findFragmentByTag(backStateName));
            if(fragment != null) {
                trans.remove(fragment);
                trans.commit();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

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



    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        if("getClusterDetail".equals(taskName)) {
            fabCompetitor.setVisibility(View.VISIBLE);
            fabSearch.setVisibility(View.VISIBLE);
            if(result != null) {
                MainModel<ResponseClusterInformation> model = StringUtil.convertStringToObject("" + result, ResponseClusterInformation.class);
                ResponseClusterInformation rci = model.getObject();
                mapResponse = rci.getCluster();

                competitorList = rci.getCompetitorList();
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

        AsyncOperation asop = new AsyncOperation("insertCompetitor");
        asop.setListener(this);
        asop.execute(urlParam);

    }

    @Override
    public void onDialogOkPressed(DialogInterface dialog, int which, Object... result) {

    }

    @Override
    public void onDialogCancelPressed(DialogInterface dialog, int which) {}
}
