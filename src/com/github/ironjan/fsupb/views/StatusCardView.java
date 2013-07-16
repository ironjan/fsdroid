package com.github.ironjan.fsupb.views;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.github.ironjan.fsupb.*;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

@EViewGroup(R.layout.card_status)
public class StatusCardView extends RelativeLayout implements
		CustomView<Integer> {

	public StatusCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@ViewById(R.id.txtStatus)
	TextView txtStatus;

	@ViewById(R.id.txtDescriptionStatus)
	TextView txtDescriptionStatus;

	@ViewById(R.id.imgStatus)
	ImageView imgStatus;

	@StringArrayRes(R.array.stati)
	String[] stati;

	@Override
	public void bind(Integer status) {
		Log.v(StatusCardView.class.getSimpleName(), "Binding status=" + status);

		final int statusInt = status.intValue();

		imgStatus.setImageLevel(statusInt);
		txtStatus.setText(stati[statusInt]);

		int visibility = View.GONE;
		if (statusInt > 1) {
			visibility = View.VISIBLE;
		}
		txtDescriptionStatus.setVisibility(visibility);
	}

}
