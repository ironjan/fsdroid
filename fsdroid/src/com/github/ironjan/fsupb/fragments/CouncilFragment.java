package com.github.ironjan.fsupb.fragments;

import android.util.Log;

import com.actionbarsherlock.app.SherlockFragment;
import com.fima.cardsui.views.CardUI;
import com.github.ironjan.fsupb.R;
import com.github.ironjan.fsupb.cards.ConcilMemberCard;
import com.github.ironjan.fsupb.cards.entities.CouncilJobs;
import com.github.ironjan.fsupb.cards.entities.CouncilMember;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_council)
public class CouncilFragment extends SherlockFragment {
	@ViewById
	CardUI cardsview;

	@AfterViews
	void showCouncilMembers() {
		for (int i = 0; i < 10; i++) {

			CouncilMember cm = new CouncilMember("NN" + i, "RN" + i, "CS" + i,
					i+"@mail.fsmi" , "cba... " + i, CouncilJobs.NORMAL);
			ConcilMemberCard cmc = new ConcilMemberCard();
			cmc.bind(cm);
			cardsview.addCard(cmc);
			Log.v(CouncilFragment.class.getSimpleName(), "Added card " + i);
		}
		cardsview.refresh();
	}
}
