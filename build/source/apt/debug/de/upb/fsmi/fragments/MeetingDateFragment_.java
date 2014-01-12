//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.
//


package de.upb.fsmi.fragments;

import java.util.Date;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.upb.fsmi.R.layout;
import de.upb.fsmi.helper.DataKeeper_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class MeetingDateFragment_
    extends MeetingDateFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.empty_view, container, false);
        }
        return contentView_;
    }

    private void init_(Bundle savedInstanceState) {
        dataKeeper = DataKeeper_.getInstance_(getActivity());
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static MeetingDateFragment_.FragmentBuilder_ builder() {
        return new MeetingDateFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        initCardView();
    }

    @Override
    public void refreshDisplayedData() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                MeetingDateFragment_.super.refreshDisplayedData();
            }

        }
        );
    }

    @Override
    public void displayDate(final Date nextMeetingDate) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                MeetingDateFragment_.super.displayDate(nextMeetingDate);
            }

        }
        );
    }

    @Override
    public void initCardView() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                MeetingDateFragment_.super.initCardView();
            }

        }
        );
    }

    @Override
    public void refreshDate() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    MeetingDateFragment_.super.refreshDate();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public MeetingDateFragment build() {
            MeetingDateFragment_ fragment_ = new MeetingDateFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
