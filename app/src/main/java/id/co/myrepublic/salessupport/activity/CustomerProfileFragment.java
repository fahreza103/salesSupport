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
import android.widget.EditText;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.DatePickerBuilder;

public class CustomerProfileFragment extends Fragment implements View.OnClickListener {

    private Button btnConfirm;
    private EditText dateOfBirth;

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

        dateOfBirth = (EditText) getActivity().findViewById(R.id.customer_editText_dob);
        DatePickerBuilder datePickerBuilder = new DatePickerBuilder(dateOfBirth,getContext());

        btnConfirm = (Button) getActivity().findViewById(R.id.customer_btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = new CustomerUploadFragment();
        //fragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
