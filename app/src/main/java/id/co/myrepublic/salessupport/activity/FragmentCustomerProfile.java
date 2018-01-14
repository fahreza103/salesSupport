package id.co.myrepublic.salessupport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.support.Validator;

public class FragmentCustomerProfile extends Fragment implements View.OnClickListener {

    private Button btnConfirm;
    private LinearLayout scrollContentLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_customer_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));

        btnConfirm = (Button) getActivity().findViewById(R.id.customer_btn_confirm);
        btnConfirm.setOnClickListener(this);

        scrollContentLayout = (LinearLayout) getActivity().findViewById(R.id.customer_scroll_layout);
    }

    @Override
    public void onClick(View view) {
        boolean valid = Validator.validate(getContext(),scrollContentLayout);
        if(valid) {
            Bundle bundle = this.getArguments();
            HashMap<String,Object> customerData = FormExtractor.extractValues(getContext(),scrollContentLayout);
            bundle.putSerializable("customerData",customerData);

            Fragment fragment = new FragmentCustomerUpload();
            fragment.setArguments(bundle);

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, fragment.getClass().getName());
            ft.addToBackStack(fragment.getClass().getName());
            ft.commit();

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
