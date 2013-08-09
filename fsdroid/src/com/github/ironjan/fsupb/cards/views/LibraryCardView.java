package com.github.ironjan.fsupb.cards.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ironjan.fsupb.R;
import com.github.ironjan.fsupb.cards.entities.Library;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.card_library)
public class LibraryCardView extends LinearLayout {

	@ViewById
	TextView txtLibraryDescription, txtLibraryLink, txtLibraryLicense,
			txtLibraryName;

	public LibraryCardView(Context pContext) {
		super(pContext);
	}

	public void bind(Library library) {
		txtLibraryName.setText(library.getName());
		txtLibraryDescription.setText(library.getDescription());
		txtLibraryLink.setText(library.getLink());
		txtLibraryLicense.setText(library.getLicense());
	}
}
