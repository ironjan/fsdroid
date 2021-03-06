package de.upb.fsmi.fsdroid.cards.views;

import android.content.*;
import android.net.*;
import android.view.*;
import android.widget.*;

import org.androidannotations.annotations.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.cards.entities.*;

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

        if ("".equals(library.getLink())) {
            imgLibraryLink.setVisibility(View.GONE);
        } else {
            uri = Uri.parse(library.getLink());
        }
    }

    @Click(R.id.imgLibraryLink)
    void goToWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getContext().startActivity(intent);
    }

}
