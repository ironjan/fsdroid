package de.upb.fsmi.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringArrayRes;

import de.upb.fsmi.R;

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
