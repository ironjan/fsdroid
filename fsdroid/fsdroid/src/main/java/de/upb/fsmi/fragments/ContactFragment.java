package de.upb.fsmi.fragments;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;

import com.fima.cardsui.views.CardUI;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.ContactCard;

@EFragment(R.layout.fragment_contact)
@OptionsMenu(R.menu.menu_contact)
public class ContactFragment extends Fragment {

	@StringRes
	String fsmiAddress, fsmiMail, fsmiTelephone, fsmiQueryAddress;

	@OptionsItem(R.id.ab_add_to_contacts)
	void addFsmiToContacts() {
		Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
		intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL, fsmiMail);
		intent.putExtra(ContactsContract.Intents.Insert.POSTAL, fsmiAddress);
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, fsmiTelephone);
		startActivity(intent);
	}

	@ViewById
	CardUI cardsview;

	@AfterViews
	void showContent() {
		ContactCard card = new ContactCard();
		cardsview.addCard(card);
		cardsview.refresh();
	}
}
