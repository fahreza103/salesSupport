package id.co.myrepublic.salessupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.adapter.ClusterAdapter;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.model.Cluster;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.model.UrlParam;
import id.co.myrepublic.salessupport.util.AsyncOperation;


/**
 * Created by myrepublicid on 26/9/17.
 */

public class ClusterDataFragment extends Fragment implements AsyncTaskListener {

    private ListView listViewCluster;
    private ClusterAdapter clusterAdapter;
    private List<Cluster> dataModels = new ArrayList<Cluster>();

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

        AsyncOperation asop = new AsyncOperation();
        asop.setListener(this);
        asop.execute(urlParam);

        listViewCluster =(ListView)getActivity().findViewById(R.id.listCluster);


        listViewCluster.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cluster dataModel= dataModels.get(position);

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
        });
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
    public void onAsyncTaskComplete(Object result) {
        // Convert to Object
        try {
            ObjectMapper mapper = new ObjectMapper();
            MainModel<Cluster> model = mapper.readValue((String)result,
                    mapper.getTypeFactory().constructParametricType(MainModel.class, Cluster.class));
            dataModels = model.getResponse();

            clusterAdapter = new ClusterAdapter(dataModels,getActivity().getApplicationContext());
            listViewCluster.setAdapter(clusterAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
