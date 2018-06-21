package com.prakritifresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ThankYouFragment extends Fragment {
    FragmentManager mFragmentManager;
    View view;

    class C01991 implements OnClickListener {
        C01991() {
        }

        public void onClick(View v) {
            mFragmentManager.beginTransaction().replace(R.id.containerView, new TabFragment()).commit();
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.thankyou_layout, null);
        mFragmentManager = getFragmentManager();
        ((Button) view.findViewById(R.id.homepage)).setOnClickListener(new C01991());
        return view;
    }
}
