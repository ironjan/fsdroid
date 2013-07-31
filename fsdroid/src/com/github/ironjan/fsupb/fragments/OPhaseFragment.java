package com.github.ironjan.fsupb.fragments;

import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.fima.cardsui.views.CardUI;
import com.github.ironjan.fsupb.FSDroid;
import com.github.ironjan.fsupb.R;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_ophase)
public class OPhaseFragment extends SherlockFragment implements TabListener {
	@ViewById
	CardUI cardsview;
	private Tab tabMo, tabDi, tabMi;

	@AfterViews
	void initAB() {
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		addTabs(actionBar);
	}

	@Override
	public void onPause() {
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.removeAllTabs();
		super.onPause();
	}

	private void addTabs(ActionBar actionBar) {
		tabMo = actionBar.newTab().setText("Montag").setTabListener(this);
		actionBar.addTab(tabMo);

		tabDi = actionBar.newTab().setText("Dienstag").setTabListener(this);
		actionBar.addTab(tabDi);

		tabMi = actionBar.newTab().setText("Mittwoch").setTabListener(this);
		actionBar.addTab(tabMi);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		try {
			FSDroid drawerActivity = (FSDroid) getActivity();
			if (drawerActivity != null) {
				drawerActivity.closeDrawer();
			}
		} catch (ClassCastException e) { /* nothing to do */
		}
		if (cardsview == null) {
			return;
		}

		cardsview.clearCards();

		int selectedPosition = tab.getPosition();
		if (tab.equals(tabMo)) {
			cardsview.addCard(new TestCard(selectedPosition, selectedPosition));
		} else if (tab.equals(tabDi)) {
			cardsview.addCard(new TestCard(selectedPosition, selectedPosition));
		} else if (tab.equals(tabMi)) {
			cardsview.addCard(new TestCard(selectedPosition, selectedPosition));
		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
