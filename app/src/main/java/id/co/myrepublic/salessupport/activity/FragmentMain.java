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
import id.co.myrepublic.salessupport.support.BitmapProcessor;
import id.co.myrepublic.salessupport.util.GlobalVariables;
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

//        testCheckboxes = (Checkboxes) getActivity().findViewById(R.id.test_checkboxes);
//        Map<String,Object> dataMap = new HashMap<String,Object>();
//        dataMap.put("test1","value A");
//        dataMap.put("test2","value B");
//        dataMap.put("test3","value C");
//        testCheckboxes.setEntriesMap(dataMap);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String,Object> planData = FormExtractor.extractValues(getContext(),layoutLogo,false);
//                HashMap<String,CheckboxParam> checkboxMap = (HashMap<String, CheckboxParam>) planData.get("test_checkboxes");
//                CheckboxParam param = checkboxMap.get(AbstractWidget.CHECKBOX_TAG+"test2");
//            }
//        });




        //new AsyncTest().execute(new UrlParam());


    }

    class AsyncTest extends AsyncTask<Object, Integer, List<UrlResponse>> {

        final File file = new File("/storage/emulated/0/Download/IMG_20160417_193743.jpg");
        final Map<Object,Object> paramMap = new HashMap<Object,Object>();

        public  byte[] compress(String data) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data.getBytes());
            gzip.close();
            byte[] compressed = bos.toByteArray();
            bos.close();
            return compressed;
        }

        @Override
        protected List<UrlResponse> doInBackground(Object... objects) {
            final GlobalVariables gvar = GlobalVariables.getInstance();

            String url = "https://boss-st.myrepublic.co.id/api/mobile-order-upload/167812/2/56907d10-4223-4b0d-94d4-bc3602d3d17f";

            Uri fileUri = Uri.fromFile(file);
            Bitmap selectedImageBitmap = BitmapProcessor.decodeFile(file);


            try {
                File folder = Environment.getExternalStoragePublicDirectory("/salessupport/");
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "salessupport_"+ timeStamp + "_";
                //write the bytes in file
                File file =  File.createTempFile(imageFileName,".jpg",folder);
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80 , new FileOutputStream(file));
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


//            paramMap.put("session_id", gvar.getString(AppConstant.COOKIE_SESSION_KEY, ""));
//            UrlResponse response = URLConnector.doConnectMultipart(url, paramMap,file,gvar.getString(AppConstant.COOKIE_FULL_STRING,""),null);
            boolean exist = file.exists();

            return null;
        }
    }
}
