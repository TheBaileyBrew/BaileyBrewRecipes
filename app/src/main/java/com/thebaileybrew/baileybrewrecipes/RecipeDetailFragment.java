package com.thebaileybrew.baileybrewrecipes;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.thebaileybrew.baileybrewrecipes.database.RecipeRepository;
import com.thebaileybrew.baileybrewrecipes.dummy.DummyContent;
import com.thebaileybrew.baileybrewrecipes.models.Recipe;
import com.thebaileybrew.baileybrewrecipes.models.Step;
import com.thebaileybrew.baileybrewrecipes.utils.DisplayMetricsUtils;
import com.thebaileybrew.baileybrewrecipes.utils.objects.SliderLayoutManager;
import com.thebaileybrew.baileybrewrecipes.utils.objects.SliderViewHolder;

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();

    public static final String ARG_ITEM_ID = "recipe_id";
    private Recipe mRecipe;
    private RecipeRepository recipeRepository;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
        recipeRepository = new RecipeRepository(BaileyBrewRecipes.getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            int recipeId = getArguments().getInt(ARG_ITEM_ID);
            Log.e(TAG, "onCreate: id: " + recipeId );
            mRecipe = recipeRepository.getSingleRecipe(recipeId);

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) this.getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getRecipeName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        // Show the recipe steps via Recycler Step Counter.
        if (mRecipe != null) {
            List<Step> allSteps = mRecipe.getSteps();
            Log.e(TAG, "onCreate: step size: " + allSteps.size() );
            RecyclerView stepCounterRecycler = rootView.findViewById(R.id.recycler_step_counter);
            int recyclerPadding = DisplayMetricsUtils.getScreenWidth(BaileyBrewRecipes.getContext()) / 2
                    - DisplayMetricsUtils.displayToPixel(BaileyBrewRecipes.getContext(), 12);
            stepCounterRecycler.setPadding(recyclerPadding,0, recyclerPadding, 0);
            SliderLayoutManager layoutManager =
                    new SliderLayoutManager(BaileyBrewRecipes.getContext());
            SliderViewHolder recyclerAdapter =
                    new SliderViewHolder(BaileyBrewRecipes.getContext(), allSteps, new SliderViewHolder.SliderClickHandler() {
                        @Override
                        public void onClick(View view, Step step) {
                            Log.e(TAG, "onClick: current step" + step.getStepId() );
                            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(step.getStepShortDescription());
                        }
                    });
            recyclerAdapter.setSteps(allSteps);
            stepCounterRecycler.setLayoutManager(layoutManager);
            stepCounterRecycler.setAdapter(recyclerAdapter);
        }

        return rootView;
    }
}
