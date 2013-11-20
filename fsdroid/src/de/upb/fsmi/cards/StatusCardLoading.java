package de.upb.fsmi.cards;

import android.content.*;
import android.util.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.*;

import de.upb.fsmi.cards.views.*;

public class StatusCardLoading extends Card {

	public StatusCardLoading() {
		super();
		Log.v(StatusCardLoading.class.getSimpleName(), "constructed");
	}

	@Override
	public View getCardContent(final Context context) {
		Log.v(StatusCardLoading.class.getSimpleName(), "getCardContent");
		final StatusCardLoadingView view = StatusCardLoadingView_.build(
				context, null, 0);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		Log.v(StatusCardLoading.class.getSimpleName(), "getCardContent " + view);

		return view;
	}

}
