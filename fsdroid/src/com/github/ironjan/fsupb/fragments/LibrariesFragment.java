package com.github.ironjan.fsupb.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.fima.cardsui.views.CardUI;
import com.github.ironjan.fsupb.R;
import com.github.ironjan.fsupb.cards.LibraryCard;
import com.github.ironjan.fsupb.cards.entities.Library;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringArrayRes;

@EFragment(R.layout.fragment_licenses)
public class LibrariesFragment extends SherlockFragment {
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
