package com.thebaileybrew.baileybrewrecipes.utils.objects;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class SliderLayoutManager extends LinearLayoutManager {
    public SliderLayoutManager(Context context) {
        super(context);
    }

    public SliderLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
}
