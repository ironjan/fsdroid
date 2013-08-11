package com.github.ironjan.fsupb.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.Card;
import com.github.ironjan.fsupb.R;
import com.github.ironjan.fsupb.cards.entities.Library;
import com.github.ironjan.fsupb.cards.views.LibraryCardView;
import com.github.ironjan.fsupb.cards.views.LibraryCardView_;

public class LibraryCard extends Card {

	private final Library library;

	public LibraryCard(Library lib) {
		super(lib.getName(), lib.getDescription(), R.drawable.ic_status_unknown);
		this.library = lib;
	}

	@Override
	public View getCardContent(Context pContext) {
		LibraryCardView view = LibraryCardView_.build(pContext);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.bind(library);
		return view;
	}

}
