package id.co.myrepublic.salessupport.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.CommonRowAdapter;
import id.co.myrepublic.salessupport.model.CommonItem;

/**
 * Created by myrepublicid on 28/11/17.
 */

public class FragmentHomepass extends Fragment {

    private ListView listViewHomepass;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_homepass, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_homepass));

        listViewHomepass = (ListView) getActivity().findViewById(R.id.listHompass);


        // Get Bundle data from previous fragment
        Bundle bundle = this.getArguments();
        List<String> homepassList = bundle.getStringArrayList("homepassList");

        final List<CommonItem> listViewData = new ArrayList<CommonItem>();
        for(int i=1;i<=homepassList.size();i++) {
            String value = homepassList.get(i-1);

            CommonItem ci = new CommonItem();
            ci.setKey(""+i);
            ci.setValue(value);
            listViewData.add(ci);
        }

        CommonRowAdapter<CommonItem> adapter = new CommonRowAdapter<CommonItem>(listViewData,getActivity().getApplicationContext());
        adapter.setWidthText(40);
        listViewHomepass.setAdapter(adapter);
        listViewHomepass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonItem commonItem = listViewData.get(position);
                showOrderDialog(commonItem);
            }
        });
    }

    private void showOrderDialog(CommonItem commonItem) {

        // custom dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_homepass_so);


        // set the custom dialog components - text, spinner
        final Spinner spinnerCustomerClass = (Spinner) dialog.findViewById(R.id.dialogitem_spinner_customer_class);

        final TextView txtHomepassName = (TextView) dialog.findViewById(R.id.dialogitem_txt_homepass_value);
        txtHomepassName.setText(commonItem.getValue());

        initTabHost();

        Button cancelButton = (Button) dialog.findViewById(R.id.dialogitem_btn_cancel);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.dialogitem_btn_ok);
        // if button is clicked, call API insert cluster
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelected = ""+spinnerCustomerClass.getSelectedItem();
                Bundle bundle = new Bundle();
                bundle.putString("homepassId",itemSelected);

                Fragment fragment = new FragmentSales();
                fragment.setArguments(bundle);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                dialog.dismiss();
            }
        });


        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    private void initTabHost() {
        TabHost host = (TabHost) dialog.findViewById(R.id.homepass_dialog_tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        View tabIndicator = LayoutInflater.from(getContext()).inflate(R.layout.tab_indicator, host.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.tabhost_indicator_title)).setText(this.getString(R.string.tabhost_create_order));
        ((ImageView) tabIndicator.findViewById(R.id.tabhost_indicator_icon)).setImageResource(R.drawable.tabhost_icon_addorder);

        spec.setIndicator(tabIndicator);
        spec.setContent(R.id.create_order);
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        tabIndicator = LayoutInflater.from(getContext()).inflate(R.layout.tab_indicator, host.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.tabhost_indicator_title)).setText(this.getString(R.string.tabhost_create_survey));
        ((ImageView) tabIndicator.findViewById(R.id.tabhost_indicator_icon)).setImageResource(R.drawable.tabhost_icon_survey);

        spec.setIndicator(tabIndicator);
        spec.setContent(R.id.create_survey);
        host.addTab(spec);
    }


}
