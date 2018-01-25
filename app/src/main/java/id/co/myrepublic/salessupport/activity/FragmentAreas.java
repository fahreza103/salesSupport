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
import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.CommonRowAdapter;
import id.co.myrepublic.salessupport.listener.DialogListener;
import id.co.myrepublic.salessupport.model.Area;
import id.co.myrepublic.salessupport.support.DialogBuilder;
import id.co.myrepublic.salessupport.util.GlobalVariables;


/**
 * Created by myrepublicid on 26/9/17.
 */

public class FragmentAreas extends Fragment implements View.OnClickListener, DialogListener {

    private static View view;
    private ListView listViewCity;
    private CommonRowAdapter<Area> areaAdapter;
    private List<Area> dataModels = new ArrayList<Area>();
    private List<Area> dataSearchResult = new ArrayList<Area>();
    private LinearLayout footerBar;

    private FloatingActionButton fabSearch;
    private FloatingActionButton fabRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_city, container, false);

        return view;

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_city));

        GlobalVariables gVar = GlobalVariables.getInstance();

        listViewCity=(ListView)getActivity().findViewById(R.id.listData);
        fabRefresh = (FloatingActionButton)getActivity().findViewById(R.id.fab_areaRefresh);
        fabSearch = (FloatingActionButton)getActivity().findViewById(R.id.fab_areaSearch);
        footerBar = (LinearLayout) getActivity().findViewById(R.id.footer_bar);
        footerBar.setVisibility(View.VISIBLE);
        footerBar.setAnimation(gVar.getTopDownAnim());

        fabRefresh.setOnClickListener(this);
        fabRefresh.startAnimation(gVar.getLeftRightAnim());
        fabSearch.setOnClickListener(this);
        fabSearch.startAnimation(gVar.getLeftRightAnim());

        dataModels = Area.createDataDummy();
        createAreas(dataModels);
        dataSearchResult.clear();

        listViewCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Area dataModel= null;
                if(dataSearchResult.size()>0) {
                    dataModel = dataSearchResult.get(position);
                } else {
                    dataModel = dataModels.get(position);
                }

//                Snackbar.make(view, dataModel.getCityName()+"\n"+dataModel.getArpu()+"\n"+dataModel.getActiveSubs(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();

                Fragment fragment = new FragmentClusterData();

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_areaRefresh :
                dataSearchResult.clear();
                createAreas(dataModels);
                break;
            case R.id.fab_areaSearch :
                DialogBuilder db = DialogBuilder.getInstance();
                db.setDialogListener(this);
                db.createInputDialog(getContext(),getString(android.R.string.search_go),
                        getString(R.string.dialog_areas_searchname));
                break;
        }
    }

    private void createAreas(List<Area> data) {
        areaAdapter = new CommonRowAdapter<Area>(data,getActivity().getApplicationContext());
        listViewCity.setAdapter(areaAdapter);
    }

    @Override
    public void onDialogOkPressed(DialogInterface dialog, int which,Object... result) {
        String searchKey = ""+result[0];
        dataSearchResult = new ArrayList<Area>();
        for(Area area : dataModels) {
            if(area.getAreaName() != null && area.getAreaName().toLowerCase().contains(searchKey.toLowerCase())) {
                dataSearchResult.add(area);
            }
        }
        createAreas(dataSearchResult);

    }

    @Override
    public void onDialogCancelPressed(DialogInterface dialog, int which) {}
}
