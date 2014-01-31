package de.upb.fsmi.fsdroid.cards;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.*;

import com.fima.cardsui.objects.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.cards.entities.*;
import de.upb.fsmi.fsdroid.cards.views.*;

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
