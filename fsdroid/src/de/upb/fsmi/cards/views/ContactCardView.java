package de.upb.fsmi.cards.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

import de.upb.fsmi.R;

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
