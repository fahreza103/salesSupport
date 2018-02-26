package id.co.myrepublic.salessupport.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.CommonRowAdapter;
import id.co.myrepublic.salessupport.response.CatalogItem;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.widget.CheckboxParam;
import id.co.myrepublic.salessupport.widget.Checkboxes;
import id.co.myrepublic.salessupport.widget.CustomEditText;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

/**
 * Created by myrepublicid on 4/10/17.
 */

public class FragmentMain extends Fragment {

    private LinearLayout layoutLogo;
    private LinearLayout footerBar;
    private CustomSpinner customSpinner;
    private CustomEditText customEditText;
    private Button btn;
    private Checkboxes testCheckboxes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.layoutLogo = (LinearLayout) getActivity().findViewById(R.id.content_main_layout_logo);
        Animation anim = GlobalVariables.getInstance().getFadeInAnim();
        anim.setDuration(2000);
        layoutLogo.startAnimation(anim);


        this.footerBar = (LinearLayout) getActivity().findViewById(R.id.footer_bar);
        this.footerBar.setVisibility(View.GONE);

        customEditText = (CustomEditText) getActivity().findViewById(R.id.test_edittext);
        customSpinner = (CustomSpinner) getActivity().findViewById(R.id.test_spinner);
        testCheckboxes = (Checkboxes) getActivity().findViewById(R.id.test_checkboxes);
        btn = (Button) getActivity().findViewById(R.id.test_button);

//        List<CatalogItem> catalogItemList = new ArrayList<CatalogItem>();
//        CatalogItem catalogItem = new CatalogItem();
//        catalogItem.setName("Test Data");
//        catalogItem.setId("1");
//        catalogItemList.add(catalogItem);
//
//        CatalogItem catalogItem2 = new CatalogItem();
//        catalogItem2.setName("Test Data 2");
//        catalogItem2.setId("2");
//        catalogItemList.add(catalogItem2);
//
//
//        CatalogItem catalogItem3 = new CatalogItem();
//        catalogItem3.setName("Test Data 3");
//        catalogItem3.setId("3");
//        catalogItemList.add(catalogItem3);
//
//        CommonRowAdapter adapter = new CommonRowAdapter(catalogItemList,getContext());
//        customSpinner.setAdapter(adapter);
//
//
//        Map<String,Object> dataMap = new HashMap<String,Object>();
//        dataMap.put("test_edittext","Test data");
//        dataMap.put("test_spinner",catalogItem2);
//        //dataMap.put("test_checkboxes")
//
//        FormExtractor.putValues(dataMap,layoutLogo);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map<String,Object> resMap = FormExtractor.extractValues(getContext(),layoutLogo,false);
//                Map<String,CheckboxParam> checkboxParamMap = (Map<String, CheckboxParam>) resMap.get("test_checkboxes");
//            }
//        });

//        final File file = new File("/storage/emulated/0/Download/IMG_20160417_193743.jpg");
//        final GlobalVariables gvar = GlobalVariables.getInstance();
//
//        UrlParam urlParam = new UrlParam();
//        urlParam.setResultKey("ts");
//        urlParam.setFile(file);
//        urlParam.setUrl(AppConstant.UPLOAD_ORDER_DOCUMENT_API_URL+"/167812/1/"+gvar.getSessionKey());
//
//        ApiMultipartConnectorAsyncOperation amsop = new ApiMultipartConnectorAsyncOperation(getContext(),"task", AsyncUiDisplayType.DIALOG);
//        amsop.setDialogMsg("Upload File");
//        amsop.execute(urlParam);

        //new AsyncTest().execute(new UrlParam());

    }



}
