package com.thebaileybrew.baileybrewrecipes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.thebaileybrew.baileybrewrecipes.database.RecipeRepository;
import com.thebaileybrew.baileybrewrecipes.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenLoading extends AppCompatActivity {
    private final static String TAG = SplashScreenLoading.class.getSimpleName();

    private final static String RECIPE_PREFERENCE_LOAD = "share_prefs_load";
    private final static String RECIPE_INITIAL_LOAD = "initial_load";
    private final static int SPLASH_DELAY = 3000;
    private final static int FLASH_SPLASH_DELAY = 1000;

    private Runnable loadDB;
    private Runnable noLoadDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final RecipeRepository recipeRepository = new RecipeRepository(getApplication());
        setRunnables(recipeRepository);


        SharedPreferences sharedPrefs = getSharedPreferences(RECIPE_PREFERENCE_LOAD, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        prefsEditor.putBoolean(RECIPE_INITIAL_LOAD, true);
        prefsEditor.apply();

        if (sharedPrefs.getBoolean(RECIPE_INITIAL_LOAD, true)) {
            prefsEditor.putBoolean(RECIPE_INITIAL_LOAD, false);
            Log.e(TAG, "onCreate: yes initial load" );
            new Handler().postDelayed(loadDB,SPLASH_DELAY);

        } else {
            Log.e(TAG, "onCreate: no initial load" );
            new Handler().postDelayed(noLoadDB, FLASH_SPLASH_DELAY);
        }
        Log.e(TAG, "onCreate: requesting intent");
        requestIntent();
    }

    public void setRunnables(final RecipeRepository recipeRepository) {
        loadDB = new Runnable() {
            @Override
            public void run() {
                JsonUtils.extractJsonDataToRoom(loadJsonFromAsset(), recipeRepository);
            }
        };
        noLoadDB = new Runnable() {
            @Override
            public void run() {
                JsonUtils.extractJsonDataToRoom(loadJsonFromAsset(), recipeRepository);
            }
        };
    }

    public void requestIntent() {
        Intent openRecipes = new Intent(SplashScreenLoading.this, RecipeListActivity.class);
        startActivity(openRecipes);
    }


    public String loadJsonFromAsset() {
        String jsonString = null;
        try {
            InputStream inputStream = getApplication().getAssets().open("baking.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ie) {
            ie.printStackTrace();
            return null;
        }
        return jsonString;
    }
}
