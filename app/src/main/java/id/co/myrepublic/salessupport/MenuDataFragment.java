package id.co.myrepublic.salessupport;

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

import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.adapter.AreaAdapter;
import id.co.myrepublic.salessupport.model.Area;
import id.co.myrepublic.salessupport.model.City;


/**
 * Created by myrepublicid on 26/9/17.
 */

public class MenuDataFragment extends Fragment  {

    private static View view;
    private ListView listViewCity;
    private AreaAdapter areaAdapter;
    private List<City> dataModels = new ArrayList<City>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_city, container, false);

        return view;

    }

    @Override
    public void onDestroyView ()
    {
        super.onDestroyView();
        try{
            String backStateName = this.getClass().getName();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            MenuDataFragment fragment = ((MenuDataFragment) getFragmentManager().findFragmentByTag(backStateName));
            if(fragment != null) {
                trans.remove(fragment);
                trans.commit();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_city));

        listViewCity=(ListView)getActivity().findViewById(R.id.listData);


        // get citylist from API
//        SharedPreferences sp = getActivity().getSharedPreferences(AppConstant.SESSION_KEY, Context.MODE_PRIVATE);
//        String sessionId = sp.getString(AppConstant.COOKIE_SESSION_KEY,null);
//
//        Map<Object,Object> paramMap = new HashMap<Object,Object>();
//        paramMap.put("session_id",sessionId);
//        paramMap.put("province_id","STA0001");
//
//        UrlParam urlParam = new UrlParam();
//        urlParam.setUrl(AppConstant.GET_CITY_API_URL);
//        urlParam.setParamMap(paramMap);
//
//        AsyncOperation asop = new AsyncOperation();
//        asop.setListener(this);
//        asop.execute(urlParam);

        final List<Area> dataModels = Area.createDataDummy();
        areaAdapter = new AreaAdapter(dataModels,getActivity().getApplicationContext());
        listViewCity.setAdapter(areaAdapter);

        listViewCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Area dataModel= dataModels.get(position);

//                Snackbar.make(view, dataModel.getCityName()+"\n"+dataModel.getArpu()+"\n"+dataModel.getActiveSubs(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();

                Fragment fragment = new ClusterDataFragment();

                Bundle bundle = new Bundle();
                bundle.putString("areaName", dataModel.getAreaName());
                bundle.putString("areaId", dataModel.getAreaId());
                bundle.putString("areaCode", dataModel.getAreaCode());
                fragment.setArguments(bundle);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

//    @Override
//    public void onAsyncTaskComplete(Object result) {
//        // Convert to Object
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            MainModel<City> model = mapper.readValue((String)result,
//                    mapper.getTypeFactory().constructParametricType(MainModel.class, City.class));
//            dataModels = model.getResponse();
//
//            areaAdapter = new AreaAdapter(dataModels,getActivity().getApplicationContext());
//            listViewCity.setAdapter(areaAdapter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
