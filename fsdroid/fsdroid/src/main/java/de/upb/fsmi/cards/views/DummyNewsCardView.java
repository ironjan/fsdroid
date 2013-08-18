package de.upb.fsmi.cards.views;

import android.content.*;
import android.net.*;
import android.widget.*;

import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

import de.upb.fsmi.*;

@EViewGroup(R.layout.card_news_dummy)
public class DummyNewsCardView extends RelativeLayout {
	@StringRes
	String die_fachschaft_de;

	@Click({ R.id.imgNewsLink, R.id.dummyNewsExplanation })
	void goToWebsite() {
		Uri fsmiWebsite = Uri.parse(die_fachschaft_de);
		Intent intent = new Intent(Intent.ACTION_VIEW, fsmiWebsite);
		getContext().startActivity(intent);
	}

	public DummyNewsCardView(Context pContext) {
		super(pContext);
	}

}
