package com.thebaileybrew.baileybrewrecipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thebaileybrew.baileybrewrecipes.dummy.DummyContent;
import com.thebaileybrew.baileybrewrecipes.models.Recipe;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An activity representing a list of recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {
    private List<Recipe> recipes;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private final static String RECIPE_PREFERENCE_LOAD = "share_prefs_load";
    private final static String RECIPE_INITIAL_LOAD = "initial_load";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        SharedPreferences sharedPrefs = getSharedPreferences(RECIPE_PREFERENCE_LOAD, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        prefsEditor.putBoolean(RECIPE_INITIAL_LOAD, true);
        prefsEditor.apply();

        if (sharedPrefs.getBoolean(RECIPE_INITIAL_LOAD, true)) {
            prefsEditor.putBoolean(RECIPE_INITIAL_LOAD, false);

            //TODO: Parse JSON DATA into ROOM Database
            Runnable loadDB = new Runnable() {
                @Override
                public void run() {

                }
            };
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipeCardRecycler(this, recipes, new RecipeCardRecycler.RecipeClickHandler() {
            @Override
            public void onClick(View view, Recipe recipe) {
                if(mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(RecipeDetailFragment.ARG_ITEM_ID, recipe.getRecipeId());
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipe.getRecipeId());
                    startActivity(intent);
                }

            }
        }, mTwoPane));

    }

}
