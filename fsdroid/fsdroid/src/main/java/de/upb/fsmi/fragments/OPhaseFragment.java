package de.upb.fsmi.fragments;

import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;

import com.fima.cardsui.views.*;
import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;

@EFragment(R.layout.fragment_ophase)
public class OPhaseFragment extends Fragment implements TabListener {
	@ViewById
	CardUI cardsview;
	private Tab tabMo, tabDi, tabMi;

	@InstanceState
	int selectedTab = 0;

	@AfterViews
	void initAB() {
		ActionBar actionBar = getActionBarActivity().getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		addTabs(actionBar);
		selectedTab = selectedTab < 0 | selectedTab >= actionBar.getTabCount() ? 0 : selectedTab;
		actionBar.setSelectedNavigationItem(selectedTab);
	}

	private ActionBarActivity getActionBarActivity() {
		return (ActionBarActivity) getActivity();
	}

	@Override
	public void onPause() {
		ActionBar actionBar = getActionBarActivity().getSupportActionBar();
		selectedTab = actionBar.getSelectedNavigationIndex();
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
