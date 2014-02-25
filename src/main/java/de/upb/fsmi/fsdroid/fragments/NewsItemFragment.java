package de.upb.fsmi.fsdroid.fragments;

import android.support.v4.app.*;
import android.webkit.*;
import android.widget.*;

import org.androidannotations.annotations.*;

import de.upb.fsmi.fsdroid.*;

@EFragment(R.layout.fragment_news_item)
public class NewsItemFragment extends Fragment {
    @ViewById
    TextView newsDate;
    @ViewById
    WebView newsContent;
}
