package com.github.ironjan.fsupb.fragments;

import android.content.Intent;
import android.provider.ContactsContract;

import com.actionbarsherlock.app.SherlockFragment;
import com.fima.cardsui.views.CardUI;
import com.github.ironjan.fsupb.R;
import com.github.ironjan.fsupb.cards.ContactCard;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_contact)
@OptionsMenu(R.menu.menu_contact)
public class ContactFragment extends SherlockFragment {

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
