package de.upb.fsmi.cards.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.entities.Library;

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
