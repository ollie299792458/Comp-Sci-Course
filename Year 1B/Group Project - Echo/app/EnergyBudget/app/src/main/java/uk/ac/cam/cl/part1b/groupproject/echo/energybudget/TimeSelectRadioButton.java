package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.CompoundButton;

/**
 * Created by Aiden on 17/02/2018.
 */

public class TimeSelectRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    public TimeSelectRadioButton(Context context) {
        super(context);
        setLayoutAttrs();
    }

    public TimeSelectRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutAttrs();
    }

    public TimeSelectRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutAttrs();
    }

    private void setLayoutAttrs() {
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.time_selector));
        setButtonDrawable(R.color.transparent);
        setTextColor(getResources().getColor(R.color.time_text, getContext().getTheme()));
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setTextColor(getResources().getColor(R.color.time_selected_text, getContext().getTheme()));
                } else {
                    setTextColor(getResources().getColor(R.color.time_text, getContext().getTheme()));
                }
            }
        });
    }
}
