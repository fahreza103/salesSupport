package id.co.myrepublic.salessupport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.CommonRowAdapter;
import id.co.myrepublic.salessupport.model.Channels;
import id.co.myrepublic.salessupport.support.FormExtractor;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.widget.AbstractWidget;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

/**
 * Created by myrepublicid on 4/10/17.
 */

public class FragmentMain extends Fragment {

    private LinearLayout layoutLogo;
    private CustomSpinner customSpinner;
    private Button button;

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
        layoutLogo.startAnimation(GlobalVariables.getInstance().getFadeInAnim());

        customSpinner = (CustomSpinner) getActivity().findViewById(R.id.test_spinner);
        Button btn = (Button) getActivity().findViewById(R.id.btn_test);

        List<Channels> channelsList = new ArrayList<Channels>();
        Channels channels = new Channels();
        channels.setName(AbstractWidget.EMPTY_SPINNER_TEXT);
        channels.setId(1);

        Channels channels2 = new Channels();
        channels2.setName("Testing");
        channels2.setId(2);

        channelsList.add(channels);
        channelsList.add(channels2);

        CommonRowAdapter adapter = new CommonRowAdapter(channelsList,getContext());
        adapter.setSpinner(true);
        customSpinner.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> data = FormExtractor.extractValue(getContext(),layoutLogo,true);
                Channels channelSelected = (Channels) data.get("test_spinner");
            }
        });
    }
}
