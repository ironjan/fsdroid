package de.upb.fsmi.fragments;

import android.support.v4.app.Fragment;
import android.webkit.WebView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;

@EFragment(R.layout.fragment_news_item)
public class NewsItemFragment extends Fragment {
@ViewById TextView newsDate;
@ViewById WebView newsContent;
}
