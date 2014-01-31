package de.upb.fsmi.fsdroid.cards.views;

import android.content.*;
import android.net.*;
import android.view.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;

import de.upb.fsmi.fsdroid.*;

@EViewGroup(R.layout.card_contact)
public class ContactCardView extends RelativeLayout {

    private static final double FSMI_LON = 8.77127;
    private static final double FSMI_LAT = 51.70696;

    @ViewById
    TextView txtTelephone, txtMail, txtAddress;

    @StringRes
    String fsmiAddress, fsmiMail, fsmiTelephone, fsmiQueryAddress;

    @AfterViews
    void bindActions() {
        txtTelephone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pV) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"
                        + fsmiTelephone));
                getContext().startActivity(intent);
            }
        });

        txtMail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pV) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri
                        .parse("mailto:" + fsmiMail + "?subject=Betreff..."));
                getContext().startActivity(intent);
            }
        });
        txtAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pV) {
                //
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"
                        + FSMI_LAT + "," + FSMI_LON + "?q=" + fsmiQueryAddress));
                getContext().startActivity(intent);
            }

        });
    }

    public ContactCardView(Context pContext) {
        super(pContext);
    }

}
