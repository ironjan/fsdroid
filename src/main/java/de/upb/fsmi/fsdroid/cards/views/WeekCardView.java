package de.upb.fsmi.fsdroid.cards.views;

import android.content.*;
import android.content.res.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;

import java.util.*;

import de.upb.fsmi.fsdroid.*;

@EViewGroup(R.layout.card_week)
public class WeekCardView extends RelativeLayout {

    @ViewById(R.id.txtWeek)
    TextView txtWeek;

    String oddWeek, evenWeek;


    public WeekCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            Resources resources = getContext().getResources();
            oddWeek = resources.getString(R.string.oddWeek);
            evenWeek = resources.getString(R.string.evenWeek);
        }
    }

    @AfterViews
    void bindDate(){
        if(isInEditMode()){
            return;
        }

        Calendar cal = Calendar.getInstance();
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        boolean weekIsEven = week % 2 == 0;
        if(weekIsEven){
            txtWeek.setText(evenWeek);
        }
        else{
            txtWeek.setText(oddWeek);
        }
    }

}
