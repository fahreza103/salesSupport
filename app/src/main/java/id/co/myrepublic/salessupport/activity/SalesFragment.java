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
import android.widget.Button;
import android.widget.TextView;

import id.co.myrepublic.salessupport.R;

/**
 * Created by myrepublicid on 30/11/17.
 */

public class SalesFragment extends Fragment implements View.OnClickListener{

    private Button btnConfirm;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_sales, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));

        btnConfirm = (Button) getActivity().findViewById(R.id.sales_btn_confirm);
        btnConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        showAddressDialog();
    }

    private void showAddressDialog() {
        // custom dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_address_confirmation);

        final TextView txtServiceAddress = (TextView) dialog.findViewById(R.id.dialogitem_txt_sales_service_address_value);


        Button cancelButton = (Button) dialog.findViewById(R.id.dialogitem_btn_cancel);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.dialogitem_btn_no);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.dialogitem_btn_ok);
        // if button is clicked, call API insert cluster
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String itemSelected = ""+spinnerCustomerClass.getSelectedItem();
//                Bundle bundle = new Bundle();
//                bundle.putString("homepassId",itemSelected);

                Fragment fragment = new CustomerProfileFragment();
//                fragment.setArguments(bundle);

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
}
