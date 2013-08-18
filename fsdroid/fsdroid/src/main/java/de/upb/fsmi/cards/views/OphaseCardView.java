package de.upb.fsmi.cards.views;

import android.content.*;
import android.widget.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.entities.*;

@EViewGroup(R.layout.card_ophase)
public class OphaseCardView extends RelativeLayout {

	public OphaseCardView(Context context) {
		super(context);
	}

	@ViewById
	TextView txtOphaseAppointment, txtOphaseLocation, txtOphaseStart, txtOphaseDateDivider,
			txtOphaseEnd;

	public void bind(OPhaseAppointment o) {
		txtOphaseAppointment.setText(o.appointment);
		txtOphaseLocation.setText(o.location);
		txtOphaseStart.setText(o.start);
		txtOphaseEnd.setText(o.end);
	}
}
