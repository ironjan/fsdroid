package de.upb.fsmi.cards.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;

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
