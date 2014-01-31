package de.upb.fsmi.fsdroid;

import android.support.v4.app.*;
import android.view.*;

import de.upb.fsmi.fsdroid.fragments.*;

/**
 * Used for navigation by drawer
 */
final class DrawerNavigationHelper {

    private static LibrariesFragment_ librariesFragment;
    private static ContactFragment_ contactFragment;
    private static NewsFragment_ newsFragment;
    private static OverviewFragment_ overviewFragment;

    /**
     * Switches the activity's content to the one navigated to.
     *
     * @param activity    the "drawer's activity"
     * @param clickedView the clicked drawer item
     */
    public static void navigate(DrawerActivity activity, View clickedView) {
        TestFragment tf = new TestFragment();
        switch (clickedView.getId()) {
            case R.id.drawerItemNews:
                activity.switchContentTo(getNewsFragment());
                break;
            case R.id.drawerItemMisc:
                activity.switchContentTo(getOverviewFragment());
                break;
            case R.id.drawerItemContact:
                activity.switchContentTo(getContactFragment());
                break;
            case R.id.drawerItemAbout:
                activity.switchContentTo(getLibraryFragment());
                break;
            default:
                tf.setText("default?!");
                activity.switchContentTo(tf);
                break;
        }
    }

    private static Fragment getOverviewFragment() {
        if (overviewFragment == null) {
            overviewFragment = new OverviewFragment_();
        }
        return overviewFragment;
    }

    private static LibrariesFragment_ getLibraryFragment() {
        if (librariesFragment == null) {
            librariesFragment = new LibrariesFragment_();
        }
        return librariesFragment;
    }

    private static ContactFragment_ getContactFragment() {
        if (contactFragment == null) {
            contactFragment = new ContactFragment_();
        }
        return contactFragment;
    }

    private static NewsFragment_ getNewsFragment() {
        newsFragment = new NewsFragment_();
        return newsFragment;
    }

}