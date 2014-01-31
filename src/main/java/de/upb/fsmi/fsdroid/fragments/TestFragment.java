package de.upb.fsmi.fsdroid.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

public class TestFragment extends Fragment {

    private String text;

    public TestFragment() {
        this.text = "";
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText(text);
        return tv;
    }
}
