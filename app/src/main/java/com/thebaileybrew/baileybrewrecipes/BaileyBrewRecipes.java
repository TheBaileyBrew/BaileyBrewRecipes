package com.thebaileybrew.baileybrewrecipes;

import android.app.Application;

public class BaileyBrewRecipes extends Application {

    private static BaileyBrewRecipes mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static BaileyBrewRecipes getContext() {
        return mContext;
    }
}
