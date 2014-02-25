package de.upb.fsmi.fsdroid.cards;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.*;

import com.fima.cardsui.objects.*;

import de.upb.fsmi.fsdroid.cards.views.*;

public class ContactCard extends Card {

    @Override
    public View getCardContent(Context pContext) {
        ContactCardView view = ContactCardView_.build(pContext);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        return view;
    }

}
