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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.support.ApiMultipartConnectorAsyncOperation;
import id.co.myrepublic.salessupport.support.BitmapProcessor;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.util.UrlResponse;
import id.co.myrepublic.salessupport.widget.Checkboxes;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

/**
 * Created by myrepublicid on 4/10/17.
 */

public class FragmentMain extends Fragment {

    private LinearLayout layoutLogo;
    private LinearLayout footerBar;
    private CustomSpinner customSpinner;
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

        customSpinner = (CustomSpinner) getActivity().findViewById(R.id.test_spinner);
        btn = (Button) getActivity().findViewById(R.id.test_button);



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
