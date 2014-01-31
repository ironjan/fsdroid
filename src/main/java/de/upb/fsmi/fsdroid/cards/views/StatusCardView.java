package de.upb.fsmi.fsdroid.cards.views;

import android.content.*;
import android.content.res.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;

import de.upb.fsmi.fsdroid.*;

@EViewGroup(R.layout.card_status)
public class StatusCardView extends RelativeLayout implements
        CustomView<Integer> {

    @ViewById(R.id.txtStatus)
    TextView txtStatus;

    @ViewById(R.id.txtDescriptionStatus)
    TextView txtDescriptionStatus;

    @ViewById(R.id.imgStatus)
    ImageView imgStatus;

    String[] stati, statiDescriptions;

    public StatusCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            Resources resources = getContext().getResources();
            stati = resources.getStringArray(R.array.stati);
            statiDescriptions = resources.getStringArray(R.array.statiDescriptions);
        }
    }


    @Override
    public void bind(Integer status) {
        Log.v(StatusCardView.class.getSimpleName(), "Binding status=" + status);

        final int statusInt = status.intValue();

        imgStatus.setImageLevel(statusInt);
        txtStatus.setText(stati[statusInt]);
        txtDescriptionStatus.setText(statiDescriptions[statusInt]);

        invalidate();
    }

}
