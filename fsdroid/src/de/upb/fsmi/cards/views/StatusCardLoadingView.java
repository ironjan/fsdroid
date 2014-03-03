package de.upb.fsmi.cards.views;

import android.content.*;
import android.util.*;
import android.widget.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.*;

@EViewGroup(R.layout.card_status_loading)
public class StatusCardLoadingView extends RelativeLayout {

	public StatusCardLoadingView(final Context context,
			final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

}
