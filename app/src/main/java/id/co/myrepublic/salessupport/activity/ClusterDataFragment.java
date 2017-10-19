package id.co.myrepublic.salessupport.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.ClusterAdapter;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.listener.DialogListener;
import id.co.myrepublic.salessupport.model.Cluster;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.support.DialogBuilder;
import id.co.myrepublic.salessupport.util.AsyncOperation;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;


/**
 * Created by myrepublicid on 26/9/17.
 */

public class ClusterDataFragment extends Fragment implements AsyncTaskListener, View.OnClickListener, DialogListener, AdapterView.OnItemClickListener {

    private ListView listViewCluster;
    private ClusterAdapter clusterAdapter;
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
        SharedPreferences sp = getActivity().getSharedPreferences(AppConstant.SESSION_KEY, Context.MODE_PRIVATE);
        String sessionId = sp.getString(AppConstant.COOKIE_SESSION_KEY,null);

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id",sessionId);
        paramMap.put("area_id",areaCode);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.GET_CLUSTER_API_URL);
        urlParam.setParamMap(paramMap);

        AsyncOperation asop = new AsyncOperation("getCluster");
        asop.setListener(this);
        asop.execute(urlParam);

        listViewCluster =(ListView)getActivity().findViewById(R.id.listCluster);

        fabLayout = (LinearLayout) getActivity().findViewById(R.id.cluster_layout_floating);
        fabRefresh = (FloatingActionButton)getActivity().findViewById(R.id.fab_clusterRefresh);
        fabSearch = (FloatingActionButton)getActivity().findViewById(R.id.fab_clusterSearch);

        fabLayout.setVisibility(View.GONE);
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

        Fragment fragment = new ClusterDetailDataFragment();

        Bundle bundle = new Bundle();
        bundle.putString("clusterName", dataModel.getClusterName());
        fragment.setArguments(bundle);

        String backStateName = this.getClass().getName();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment,backStateName);
        ft.addToBackStack(backStateName);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDestroyView ()
    {
        super.onDestroyView();
        try{
            String backStateName = this.getClass().getName();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            ClusterDataFragment fragment = ((ClusterDataFragment) getFragmentManager().findFragmentByTag(backStateName));
            if(fragment != null) {
                trans.remove(fragment);
                trans.commit();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onAsyncTaskComplete(Object result, String taskName) {
        fabLayout.setVisibility(View.VISIBLE);
        // Convert to Object
        if(result != null) {
            MainModel<Cluster> model = StringUtil.convertStringToObject("" + result, Cluster[].class);
            if(model.getSuccess()) {
                dataModels = model.getListObject();
            } else {
                // Error on session (expired or invalid)
                if(AppConstant.SESSION_VALIDATION.equals(model.getAction())) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
            createCluster(dataModels);
        } else {
            DialogBuilder db = DialogBuilder.getInstance();
            db.createAlertDialog(getContext(), getString(R.string.dialog_title_error),
                    getString(R.string.dialog_content_failedconnect));
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
        clusterAdapter = new ClusterAdapter(data,getActivity().getApplicationContext());
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