package de.upb.fsmi.fsdroid.fragments;

import android.support.v4.app.*;
import android.support.v7.app.*;

import com.fima.cardsui.views.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.cards.*;
import de.upb.fsmi.fsdroid.cards.entities.*;

@EFragment(R.layout.fragment_licenses)
public class LibrariesFragment extends Fragment {

    @StringArrayRes
    String[] libraryLicenses, libraryNames, libraryLinks, libraryDescriptions;

    @StringRes
    String about;
    Library[] libraries;

    @ViewById
    CardUI cardsview;

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
                about);
    }

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
