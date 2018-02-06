package id.co.myrepublic.salessupport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.model.Area;
import id.co.myrepublic.salessupport.model.Homepass;
import id.co.myrepublic.salessupport.util.GlobalVariables;


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




        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Back to cluster detail

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                int popBackStackCount = fm.getBackStackEntryCount();
                while(popBackStackCount >= 5) {
                    fm.popBackStack();
                    popBackStackCount--;
                }
            }
        });
    }


    private Map<Object,Object> createOrderParam() {
        GlobalVariables gVar = GlobalVariables.getInstance();
        String sessionId = gVar.getSessionKey();

        Bundle bundle = this.getArguments();
        String customerClass = bundle.getString("customerClassification");
        Homepass homepassDataService = (Homepass) bundle.getSerializable("homepassDataService");
        Homepass homepassDataBilling = (Homepass) bundle.getSerializable("homepassDataBilling");
        Area area = (Area) bundle.getSerializable("area");
        HashMap<String,Object> salesData = (HashMap<String, Object>) bundle.getSerializable("salesData");
        HashMap<String,Object> customerData = (HashMap<String, Object>) bundle.getSerializable("customerData");
        HashMap<String,Object> planData = (HashMap<String, Object>) bundle.getSerializable("planData");
        String idImagePath = bundle.getString("customerIdPhoto");
        String selfieImagePath = bundle.getString("customerSelfiePhoto");

        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id", sessionId);
        paramMap.put("rep_id", salesData.get("sales_editText_event_rep_id"));
        paramMap.put("homepassdetailid", homepassDataService.getHomepassDetailId());
        paramMap.put("customer[type]", customerClass);

        // CUSTOMER-PROFILE
        paramMap.put("customer[users][0][contact_type_id]", "1");
        paramMap.put("customer[users][0][salutation]", customerData.get("customer_spinner_salutation"));
        paramMap.put("customer[users][0][first_name]", customerData.get("customer_txt_full_name"));
        paramMap.put("customer[users][0][last_name]", customerData.get("customer_editText_prefered_name"));
        paramMap.put("customer[users][0][gender]", customerData.get("customer_spinner_gender"));
        paramMap.put("customer[users][0][nric]", customerData.get("customer_editText_id_type"));
        paramMap.put("customer[users][0][date_of_birth]", customerData.get("customer_editText_dob"));
        paramMap.put("customer[users][0][nationality]", customerData.get("customer_spinner_nationality"));
        paramMap.put("customer[users][0][work_phone]", customerData.get("customer_editText_workphone"));
        paramMap.put("customer[users][0][home_phone]", customerData.get("customer_editText_homephone"));
        paramMap.put("customer[users][0][mobile]", customerData.get("customer_editText_mobilephone"));
        paramMap.put("customer[users][0][email]", customerData.get("customer_editText_email"));

        paramMap.put("customer[company_info][business_type]", "SMG");
        paramMap.put("customer[company_info][business_segment]", "HRB");

        // SERVICE-ADDRESS
        paramMap.put("subscription[service_type]", customerClass);
        paramMap.put("subscription[tp_status]", "30");
        paramMap.put("subscription[net_co]", "ON");
        paramMap.put("subscription[addresses][0][type]", "Billing");
        paramMap.put("subscription[addresses][0][province]", area.getProvinceCode());
        paramMap.put("subscription[addresses][0][city]", area.getAreaCode());
        paramMap.put("subscription[addresses][0][developer_cluster]", homepassDataService.getClusterName());
        paramMap.put("subscription[addresses][0][dwelling_type]", homepassDataService.getDwellingType());
        paramMap.put("subscription[addresses][0][block]", homepassDataService.getBlock());
        paramMap.put("subscription[addresses][0][home_number]", homepassDataService.getHomeNumber());
        paramMap.put("subscription[addresses][0][street]", homepassDataService.getStreet());
        paramMap.put("subscription[addresses][0][floor]", homepassDataService.getFloor());
        paramMap.put("subscription[addresses][0][unit]", homepassDataService.getUnit());
        //paramMap.put("subscription[addresses][0][village]", homepassDataService.get);
        paramMap.put("subscription[addresses][0][postal_code]", homepassDataService.getPostalcode());
        paramMap.put("subscription[addresses][0][rw]", homepassDataService.getRw());
        paramMap.put("subscription[addresses][0][rt]", homepassDataService.getRt());
        paramMap.put("subscription[addresses][0][address_prefix]", homepassDataService.getAddressPrefix());
        paramMap.put("subscription[addresses][0][building_ready]", homepassDataService.getAvailability());
        paramMap.put("subscription[addresses][0][building_name]", homepassDataService.getBuildingName());
        paramMap.put("subscription[addresses][0][developer_complex]", homepassDataService.getComplex());
        //paramMap.put("subscription[addresses][0][developer_sector]", homepassDataService.get);
        paramMap.put("subscription[addresses][0][homepassdetailid]", homepassDataService.getHomepassDetailId());
        paramMap.put("subscription[addresses][0][country_code]", "IDN");

        // BILLING-ADDRESS
        paramMap.put("subscription[addresses][1][type]", "Service");
        paramMap.put("subscription[addresses][1][province]", "Billing");
        paramMap.put("subscription[addresses][1][city]", "Billing");
        paramMap.put("subscription[addresses][1][developer_cluster]", "Billing");
        paramMap.put("subscription[addresses][1][dwelling_type]", "Billing");
        paramMap.put("subscription[addresses][1][block]", "Billing");
        paramMap.put("subscription[addresses][1][home_number]", "Billing");
        paramMap.put("subscription[addresses][1][street]", "Billing");
        paramMap.put("subscription[addresses][1][floor]", "Billing");
        paramMap.put("subscription[addresses][1][unit]", "Billing");
        paramMap.put("subscription[addresses][1][village]", "Billing");
        paramMap.put("subscription[addresses][1][postal_code]", "Billing");
        paramMap.put("subscription[addresses][1][rw]", "Billing");
        paramMap.put("subscription[addresses][1][rt]", "Billing");
        paramMap.put("subscription[addresses][1][address_prefix]", "Billing");
        paramMap.put("subscription[addresses][1][building_name]", "Billing");
        paramMap.put("subscription[addresses][1][developer_complex]", "Billing");
        paramMap.put("subscription[addresses][1][developer_sector]", "Billing");
        paramMap.put("subscription[addresses][1][country_code]", "Billing");

        return paramMap;
    }

}
