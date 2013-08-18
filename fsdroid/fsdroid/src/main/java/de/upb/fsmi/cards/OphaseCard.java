package de.upb.fsmi.cards;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.*;

import de.upb.fsmi.cards.entities.*;
import de.upb.fsmi.cards.views.*;

public class OphaseCard extends Card {

	final OPhaseAppointment appointment;

	public OphaseCard(OPhaseAppointment appointment) {
		super();
		this.appointment = appointment;
	}

	@Override
	public View getCardContent(Context pContext) {
		OphaseCardView view = OphaseCardView_.build(pContext);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		view.bind(appointment);
		return view;
	}

}
