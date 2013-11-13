package de.upb.fsmi.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

import de.upb.fsmi.R;

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
