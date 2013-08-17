package de.upb.fsmi.cards.views;

import android.content.*;
import android.net.*;
import android.widget.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.entities.*;

@EViewGroup(R.layout.card_library)
public class LibraryCardView extends LinearLayout {

	@ViewById
	TextView txtLibraryDescription, txtLibraryLink, txtLibraryLicense, txtLibraryName;

	@ViewById
	ImageView imgLibraryLink;

	private Uri uri;

	public LibraryCardView(Context pContext) {
		super(pContext);
	}

	public void bind(Library library) {
		txtLibraryName.setText(library.getName());
		txtLibraryDescription.setText(library.getDescription());
		txtLibraryLicense.setText(library.getLicense());
		uri = Uri.parse(library.getLink());
	}

	@Click(R.id.imgLibraryLink)
	void goToWebsite() {
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		getContext().startActivity(intent);
	}

}
