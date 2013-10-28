package de.upb.fsmi.fragments;

import android.content.*;
import android.provider.*;
import android.support.v4.app.*;
import android.support.v7.app.*;

import com.fima.cardsui.views.*;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;

@EFragment(R.layout.fragment_contact)
@OptionsMenu(R.menu.menu_contact)
public class ContactFragment extends Fragment {

	@StringRes
	String fsmiAddress, fsmiMail, fsmiTelephone, fsmiQueryAddress,contact;

	@Override
	public void onResume() {
		super.onResume();
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setTitle(contact);
		}
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
