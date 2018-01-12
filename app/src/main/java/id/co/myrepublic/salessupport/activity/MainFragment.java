package id.co.myrepublic.salessupport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.GlobalVariables;

/**
 * Created by myrepublicid on 4/10/17.
 */

public class MainFragment extends Fragment {

    private LinearLayout layoutLogo;
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


        Button btn = (Button) getActivity().findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean result = Validator.validate(layoutLogo,getContext());
            }
        });
    }
}
