package de.upb.fsmi.fsdroid;

import android.support.v4.app.*;

/**
 * An activity that provides drawer navigation.
 */
public interface DrawerActivity {
    /**
     * Switches the content view's content to fragment
     *
     * @param fragment the new fragment
     */
    public void switchContentTo(Fragment fragment);
}