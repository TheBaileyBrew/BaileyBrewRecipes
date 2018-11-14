package com.thebaileybrew.baileybrewrecipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thebaileybrew.baileybrewrecipes.database.RecipeRepository;
import com.thebaileybrew.baileybrewrecipes.dummy.DummyContent;
import com.thebaileybrew.baileybrewrecipes.models.Recipe;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;

/**
 * An activity representing a list of recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements RecipeCardRecycler.RecipeClickHandler {
    private static final String TAG = RecipeListActivity.class.getSimpleName();

    private List<Recipe> recipes;
    RecipeRepository recipeRepository = new RecipeRepository(getApplication());
    RecipeCardRecycler recipeCardRecycler;
    ImageView recipeImagePath;
    String currentSelection;

    private Animation scaleAndShiftUp, scaleAndShiftDown;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private boolean mSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeImagePath = findViewById(R.id.recipe_image_holder);
        setTransitionAnimations();

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

    private void setTransitionAnimations() {
        scaleAndShiftUp = AnimationUtils.loadAnimation(this, R.anim.anim_scale_in);
        scaleAndShiftDown = AnimationUtils.loadAnimation(this, R.anim.anim_scale_out);

    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        recipeCardRecycler = new RecipeCardRecycler(this, recipes, this, mTwoPane);
        recipes = recipeRepository.getRecipes(recipeCardRecycler);
        recyclerView.setAdapter(recipeCardRecycler);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

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
            if(!mSelected) {
                TextView currentTitle = view.findViewById(R.id.recipe_title);
                currentTitle.setTextColor(getColor(R.color.colorAccent));
                currentTitle.setTextSize(36);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipe.getRecipeId());
                startActivity(intent);
            }
        }
    }
}
