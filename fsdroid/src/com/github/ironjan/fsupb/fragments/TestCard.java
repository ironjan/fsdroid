package com.github.ironjan.fsupb.fragments;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

import com.fima.cardsui.objects.*;
import com.github.ironjan.fsupb.*;

public class TestCard extends Card {

	private final String text;

	public TestCard(int i, int s) {
		super("" + i, "TestCard " + i, R.drawable.ic_status_unknown);
		text = "Testcard\n(" + s + ", " + i
				+ ")\nAnd another line of text.\nYeah \\o/";
	}

	@Override
	public View getCardContent(Context context) {
		final TextView tv = new TextView(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(params);
		tv.setText(text);
		return tv;
	}

}
