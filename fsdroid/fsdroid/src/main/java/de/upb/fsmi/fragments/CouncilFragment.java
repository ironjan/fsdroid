package de.upb.fsmi.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.fima.cardsui.views.CardUI;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.ConcilMemberCard;
import de.upb.fsmi.cards.entities.CouncilJobs;
import de.upb.fsmi.cards.entities.CouncilMember;

@EFragment(R.layout.fragment_council)
public class CouncilFragment extends Fragment {
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
