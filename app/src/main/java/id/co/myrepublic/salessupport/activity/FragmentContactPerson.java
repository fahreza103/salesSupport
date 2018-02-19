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
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.widget.CustomEditText;

/**
 * Contact person fragment
 */

public class FragmentContactPerson extends Fragment implements View.OnClickListener, TabHost.OnTabChangeListener {

    private Button btnConfirm;
    private Button btnAuthorizedPersonCopy;
    private Button btnTechnicalContactCopy;
    private TabHost host;


    private LinearLayout layoutAuthorized;
    private CustomEditText editTextAuthorizedFullName;
    private CustomEditText editTextAuthorizedPreferredName;
    private CustomEditText editTextAuthorizedDesignation;
    private CustomEditText editTextAuthorizedKtp;
    private CustomEditText editTextAuthorizedDob;
    private CustomEditText editTextAuthorizedEmail;
    private CustomEditText editTextAuthorizedMobile;
    private CustomEditText editTextAuthorizedWorkPhone;

    private LinearLayout layoutTechnicalContact;
    private CustomEditText editTextTechinalContactFullName;
    private CustomEditText editTextTechinalContactPreferredName;
    private CustomEditText editTextTechinalContactDesignation;
    private CustomEditText editTextTechinalContactKtp;
    private CustomEditText editTextTechinalContactDob;
    private CustomEditText editTextTechinalContactEmail;
    private CustomEditText editTextTechinalContactMobile;
    private CustomEditText editTextTechinalContactWorkPhone;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_authorized_officer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));

        layoutAuthorized = (LinearLayout) getActivity().findViewById(R.id.authorized_officer_layout_form) ;
        editTextAuthorizedFullName = (CustomEditText) getActivity().findViewById(R.id.authorized_officer_edittext_full_name);
        editTextAuthorizedPreferredName = (CustomEditText) getActivity().findViewById(R.id.authorized_officer_edittext_preferred_name);
        editTextAuthorizedDesignation = (CustomEditText) getActivity().findViewById(R.id.authorized_officer_edittext_designation);
        editTextAuthorizedKtp = (CustomEditText) getActivity().findViewById(R.id.authorized_officer_edittext_ktp);
        editTextAuthorizedDob = (CustomEditText) getActivity().findViewById(R.id.authorized_officer_edittext_dob);
        editTextAuthorizedEmail = (CustomEditText) getActivity().findViewById(R.id.authorized_officer_edittext_email);
        editTextAuthorizedMobile = (CustomEditText) getActivity().findViewById(R.id.authorized_officer_edittext_mobile_phone);
        editTextAuthorizedWorkPhone = (CustomEditText) getActivity().findViewById(R.id.authorized_officer_edittext_work_phone);

        layoutTechnicalContact = (LinearLayout) getActivity().findViewById(R.id.technical_contact_layout_form) ;
        editTextTechinalContactFullName = (CustomEditText) getActivity().findViewById(R.id.technical_contact_edittext_full_name);
        editTextTechinalContactPreferredName = (CustomEditText) getActivity().findViewById(R.id.technical_contact_edittext_preferred_name);
        editTextTechinalContactDesignation = (CustomEditText) getActivity().findViewById(R.id.technical_contact_edittext_designation);
        editTextTechinalContactKtp = (CustomEditText) getActivity().findViewById(R.id.technical_contact_edittext_ktp);
        editTextTechinalContactDob = (CustomEditText) getActivity().findViewById(R.id.technical_contact_edittext_dob);
        editTextTechinalContactEmail = (CustomEditText) getActivity().findViewById(R.id.technical_contact_edittext_email);
        editTextTechinalContactMobile = (CustomEditText) getActivity().findViewById(R.id.technical_contact_edittext_mobile_phone);
        editTextTechinalContactWorkPhone = (CustomEditText) getActivity().findViewById(R.id.technical_contact_edittext_work_phone);

        btnConfirm = (Button) getActivity().findViewById(R.id.authorized_officer_btn_confirm);
        btnConfirm.setOnClickListener(this);
        btnAuthorizedPersonCopy = (Button) getActivity().findViewById(R.id.authorized_officer_btn_copy);
        btnAuthorizedPersonCopy.setOnClickListener(this);
        btnTechnicalContactCopy = (Button) getActivity().findViewById(R.id.technical_contact_btn_copy);
        btnTechnicalContactCopy.setOnClickListener(this);

        initTab();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.authorized_officer_btn_confirm :
                HashMap<String,Object> formAuthorizedValues = FormExtractor.extractValues(getContext(),layoutAuthorized,true);
                boolean validAuthorized = (boolean) formAuthorizedValues.get(Validator.VALIDATION_KEY_RESULT);

                HashMap<String,Object> formTechnicalContactValues = FormExtractor.extractValues(getContext(),layoutTechnicalContact,true);
                boolean validTechnicalContact = (boolean) formTechnicalContactValues.get(Validator.VALIDATION_KEY_RESULT);

                if(validAuthorized && validTechnicalContact) {
                    Bundle bundle = this.getArguments();
                    bundle.putSerializable("authorizedOfficerData",formAuthorizedValues);
                    bundle.putSerializable("technicalContactData",formTechnicalContactValues);

                    Fragment fragment = new FragmentCustomerUpload();
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.left_from_right,R.anim.right_from_left, R.anim.left_from_right,R.anim.right_from_left);
                    ft.replace(R.id.content_frame, fragment, fragment.getClass().getName());
                    ft.addToBackStack(fragment.getClass().getName());
                    ft.commit();

                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.authorized_officer_btn_copy :
                Bundle bundle = this.getArguments();
                HashMap<String,Object> customerMap = (HashMap<String, Object>) bundle.getSerializable("customerData");
                editTextAuthorizedFullName.setInputValue(""+customerMap.get("customer_txt_full_name"));
                editTextAuthorizedPreferredName.setInputValue(""+customerMap.get("customer_editText_prefered_name"));
                editTextAuthorizedKtp.setInputValue(""+customerMap.get("customer_editText_id_type"));
                editTextAuthorizedDob.setInputValue(""+customerMap.get("customer_editText_dob"));
                editTextAuthorizedEmail.setInputValue(""+customerMap.get("customer_editText_email"));
                editTextAuthorizedMobile.setInputValue(""+customerMap.get("customer_editText_mobilephone"));
                editTextAuthorizedWorkPhone.setInputValue(""+customerMap.get("customer_editText_workphone"));
                break;
            case R.id.technical_contact_btn_copy :
                editTextTechinalContactFullName.setInputValue(""+editTextAuthorizedFullName.getInputValue());
                editTextTechinalContactPreferredName.setInputValue(""+editTextAuthorizedPreferredName.getInputValue());
                editTextTechinalContactKtp.setInputValue(""+editTextAuthorizedKtp.getInputValue());
                editTextTechinalContactDob.setInputValue(""+editTextAuthorizedDob.getInputValue());
                editTextTechinalContactEmail.setInputValue(""+editTextAuthorizedEmail.getInputValue());
                editTextTechinalContactDesignation.setInputValue(""+editTextAuthorizedDesignation.getInputValue());
                editTextTechinalContactMobile.setInputValue(""+editTextAuthorizedMobile.getInputValue());
                editTextTechinalContactWorkPhone.setInputValue(""+editTextAuthorizedWorkPhone.getInputValue());
                break;
        }
    }

    private void initTab() {
        host = (TabHost) getActivity().findViewById(R.id.authorized_person_tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab Authorized Officer");
        View tabIndicator = LayoutInflater.from(getContext()).inflate(R.layout.tab_indicator, host.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.tabhost_indicator_title)).setText(this.getString(R.string.tabhost_authorized_officer));

        spec.setIndicator(tabIndicator);
        spec.setContent(R.id.authorized_officer);
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Technical Contact");
        tabIndicator = LayoutInflater.from(getContext()).inflate(R.layout.tab_indicator, host.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.tabhost_indicator_title)).setText(this.getString(R.string.tabhost_technical_contact));

        spec.setIndicator(tabIndicator);
        spec.setContent(R.id.techincal_contact);
        host.addTab(spec);

        host.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        GlobalVariables gVar = GlobalVariables.getInstance();
        layoutAuthorized.setAnimation(gVar.getFadeInAnim());
        layoutTechnicalContact.setAnimation(gVar.getFadeInAnim());
    }
}
