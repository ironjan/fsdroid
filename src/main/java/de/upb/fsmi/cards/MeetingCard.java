package de.upb.fsmi.cards;

import java.util.*;

import org.slf4j.*;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.*;

import de.upb.fsmi.cards.views.*;

public class MeetingCard extends Card {

	private Date date;
	private final static Logger LOGGER = LoggerFactory
			.getLogger(MeetingCard.class);

	public MeetingCard(Date date) {
		super();
		this.date = date;
		LOGGER.trace("Created new {} with date={}",
				MeetingCard.class.getSimpleName(), date);
	}

	@Override
	public View getCardContent(Context context) {
		MeetingCardView view = MeetingCardView_.build(context, null, 0);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.bind(date);
		return view;
	}

	public void setDate(Date date) {
		LOGGER.trace("Setting date to {}", date);
		this.date = date;
	}
}
