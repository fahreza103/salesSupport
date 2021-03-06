package id.co.myrepublic.salessupport.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import id.co.myrepublic.salessupport.response.Cluster;
import id.co.myrepublic.salessupport.response.MainResponse;
import id.co.myrepublic.salessupport.support.AbstractAsyncOperation;
import id.co.myrepublic.salessupport.support.ApiConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.DialogBuilder;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;


/**
 * Cluster data list
 */

public class FragmentClusterData extends AbstractFragment implements  View.OnClickListener, DialogListener, AdapterView.OnItemClickListener {

    private static final String CLUSTER_TASK_NAME = "getCluster";
    private ListView listViewCluster;
    private CommonRowAdapter<Cluster> clusterAdapter;
    private List<Cluster> dataModels = new ArrayList<Cluster>();
    private List<Cluster> dataSearchResult = new ArrayList<Cluster>();

    private LinearLayout fabLayout;
    private FloatingActionButton fabSearch;
    private FloatingActionButton fabRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_cluster, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_cluster));

        // Get Bundle data from previous fragment
        Bundle bundle = this.getArguments();
        String areaCode = bundle.getString("areaCode", null);

        // get citylist from API
        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("area_id",areaCode);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_CLUSTER_API_URL);
        urlParam.setParamMap(paramMap);
        urlParam.setResultClass(Cluster[].class);

        fabLayout = (LinearLayout) getActivity().findViewById(R.id.cluster_layout_floating);
        fabRefresh = (FloatingActionButton)getActivity().findViewById(R.id.fab_clusterRefresh);
        fabSearch = (FloatingActionButton)getActivity().findViewById(R.id.fab_clusterSearch);
        listViewCluster =(ListView)getActivity().findViewById(R.id.listCluster);

        if(!isAlreadyLoaded) {
            fabLayout.setVisibility(View.GONE);
            doApiCallAsyncTask(CLUSTER_TASK_NAME,urlParam,AsyncUiDisplayType.SCREEN);
        } else {
            fabLayout.setVisibility(View.VISIBLE);
            listViewCluster.setAdapter(clusterAdapter);
        }


        fabRefresh.setOnClickListener(this);
        fabSearch.setOnClickListener(this);


        dataSearchResult.clear();
        listViewCluster.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Cluster dataModel= null;
        if(dataSearchResult.size()>0) {
            dataModel = dataSearchResult.get(position);
        } else {
            dataModel = dataModels.get(position);
        }

        Bundle bundle = this.getArguments();
        bundle.putString("clusterName", dataModel.getClusterName());
        bundle.putSerializable("cluster",dataModel);

        openFragmentExistingBundle(bundle,new FragmentClusterDetailData());
    }



    @Override
    public void onAsyncTaskApiSuccess(Map<String, MainResponse> resultMap, String taskName) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        MainResponse<Cluster> model = resultMap.get(AbstractAsyncOperation.DEFAULT_RESULT_KEY);
        if(model != null) {
            fabLayout.setVisibility(View.VISIBLE);
            fabLayout.startAnimation(gVar.getLeftRightAnim());
            dataModels = model.getListObject();
            createCluster(dataModels);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_clusterRefresh :
                dataSearchResult.clear();
                createCluster(dataModels);
                break;
            case R.id.fab_clusterSearch :
                DialogBuilder db = DialogBuilder.getInstance();
                db.setDialogListener(this);
                db.createInputDialog(getContext(),getString(android.R.string.search_go),
                        getString(R.string.dialog_cluster_searchname));
                break;
        }
    }

    private void createCluster(List<Cluster> data) {
        clusterAdapter = new CommonRowAdapter<Cluster>(data,getActivity().getApplicationContext());
        listViewCluster.setAdapter(clusterAdapter);
    }

    @Override
    public void onDialogOkPressed(DialogInterface dialog, int which,Object... result) {
        String searchKey = ""+result[0];
        dataSearchResult = new ArrayList<Cluster>();
        for(Cluster cluster : dataModels) {
            if(cluster.getClusterName() != null && cluster.getClusterName().toLowerCase().contains(searchKey.toLowerCase())) {
                dataSearchResult.add(cluster);
            }
        }
        createCluster(dataSearchResult);
    }

    @Override
    public void onDialogCancelPressed(DialogInterface dialog, int which) {}


}
