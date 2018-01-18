package id.co.myrepublic.salessupport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;

import id.co.myrepublic.salessupport.R;


public class FragmentVerification extends Fragment {

    private Button btnResend;
    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));

        btnResend = (Button) getActivity().findViewById(R.id.verification_btn_resend);
        btnConfirm = (Button) getActivity().findViewById(R.id.verification_btn_confirm);

        Bundle bundle = this.getArguments();
        HashMap<String,Object> salesData = (HashMap<String, Object>) bundle.getSerializable("salesData");
        HashMap<String,Object> customerData = (HashMap<String, Object>) bundle.getSerializable("customerData");
        HashMap<String,Object> planData = (HashMap<String, Object>) bundle.getSerializable("planData");
        String idImagePath = bundle.getString("customerIdPhoto");
        String selfieImagePath = bundle.getString("customerSelfiePhoto");


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Back to fragment detail

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                int popBackStackCount = fm.getBackStackEntryCount();
                while(popBackStackCount >= 5) {
                    fm.popBackStack();
                    popBackStackCount--;
                }
            }
        });
    }

}
