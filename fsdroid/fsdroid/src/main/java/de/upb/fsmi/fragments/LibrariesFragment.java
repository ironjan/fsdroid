package de.upb.fsmi.fragments;

import android.support.v4.app.Fragment;

import com.fima.cardsui.views.CardUI;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringArrayRes;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.LibraryCard;
import de.upb.fsmi.cards.entities.Library;

@EFragment(R.layout.fragment_licenses)
public class LibrariesFragment extends Fragment {
	@StringArrayRes
	String[] libraryLicenses, libraryNames, libraryLinks, libraryDescriptions;

	Library[] libraries;

	@ViewById
	CardUI cardsview;

	@AfterInject
	void initLibraries() {
		int length = libraryNames.length;
		libraries = new Library[length];

		for (int i = 0; i < length; i++) {
			String name = libraryNames[i];
			String link = libraryLinks[i];
			String license = libraryLicenses[i];
			String description = libraryDescriptions[i];
			libraries[i] = new Library(name, link, license, description);
		}
	}

	@AfterViews
	void showStuff() {
		for (Library lib : libraries) {
			LibraryCard card = new LibraryCard(lib);
			cardsview.addCard(card);
		}

		cardsview.refresh();
	}
}
