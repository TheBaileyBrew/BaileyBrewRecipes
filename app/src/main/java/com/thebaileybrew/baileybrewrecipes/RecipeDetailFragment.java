package com.thebaileybrew.baileybrewrecipes;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thebaileybrew.baileybrewrecipes.database.RecipeRepository;
import com.thebaileybrew.baileybrewrecipes.dummy.DummyContent;
import com.thebaileybrew.baileybrewrecipes.models.Ingredient;
import com.thebaileybrew.baileybrewrecipes.models.Recipe;
import com.thebaileybrew.baileybrewrecipes.models.Step;
import com.thebaileybrew.baileybrewrecipes.utils.DisplayMetricsUtils;
import com.thebaileybrew.baileybrewrecipes.utils.objects.SliderLayoutManager;
import com.thebaileybrew.baileybrewrecipes.utils.objects.SliderViewHolder;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private CollapsingToolbarLayout appBarLayout;
    private Step currentStep = null;

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mExoPlayerView;

    private LinearLayout layoutBottomSheet;
    private RecyclerView ingredientList;
    private BottomSheetBehavior sheetBehavior;
    private FloatingActionButton fab;


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
            fab = (FloatingActionButton) this.getActivity().findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            });
            layoutBottomSheet = (LinearLayout) this.getActivity().findViewById(R.id.bottom_sheet_ingredients);
            ingredientList = (RecyclerView) this.getActivity().findViewById(R.id.ingredient_list);
            sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
            sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheetView, int newState) {
                    switch(newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheetView, float slideOffsetValue) {

                }
            });
            mExoPlayerView = (PlayerView) this.getActivity().findViewById(R.id.exoplayer);
            mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.menu_icon));

            appBarLayout = (CollapsingToolbarLayout) this.getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getRecipeName());
            }


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        int startValue = 0;
        int endValue = 0;
        int ingValue = 0;
        int ingEndValue = 0;

        // Show the recipe steps via Recycler Step Counter.
        if (mRecipe != null) {
            switch (getArguments().getInt(ARG_ITEM_ID)) {
                case 1: //Nutella Pie (7)
                    startValue = 0;
                    endValue = 7;
                    ingValue = 0;
                    ingEndValue = 9;
                    break;
                case 2: //Brownies (10)
                    startValue = 7;
                    endValue = 17;
                    ingValue = 9;
                    ingEndValue = 19;
                    break;
                case 3: //Yellow Cake (13)
                    startValue = 17;
                    endValue = 30;
                    ingValue = 19;
                    ingEndValue = 29;
                    break;
                case 4: //Cheese Cake (13)
                    startValue = 30;
                    endValue = 43;
                    ingValue = 29;
                    ingEndValue = 38;
                    break;
            }
            final List<Step> allSteps = mRecipe.getSteps().subList(startValue,endValue);
            List<Ingredient> allIngredients = mRecipe.getRecipeIngredients().subList(ingValue,ingEndValue);
            Log.e(TAG, "onCreate: step size: " + allSteps.size() );
            Log.e(TAG, "onCreate: ingredients: " + allIngredients.size());
            final RecyclerView stepCounterRecycler = rootView.findViewById(R.id.recycler_step_counter);
            int recyclerPadding = DisplayMetricsUtils.getScreenWidth(BaileyBrewRecipes.getContext()) / 2
                    - DisplayMetricsUtils.displayToPixel(BaileyBrewRecipes.getContext(), 50);
            stepCounterRecycler.setPadding(recyclerPadding,0, recyclerPadding, 0);
            SliderLayoutManager layoutManager =
                    new SliderLayoutManager(BaileyBrewRecipes.getContext(), new SliderLayoutManager.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(int position) {
                            Step selectedStep = allSteps.get(position);
                            if (currentStep != null) {
                                if (selectedStep.getStepId() != currentStep.getStepId()) {
                                    releasePlayer();
                                }
                            }
                            ((TextView) rootView.findViewById(R.id.step_title)).setText(selectedStep.getStepShortDescription());
                            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(selectedStep.getFullDescription());
                            currentStep = selectedStep;
                            initializeExoPlayer(selectedStep);
                        }
                    });
            SliderViewHolder recyclerAdapter =
                    new SliderViewHolder(BaileyBrewRecipes.getContext(), allSteps, new SliderViewHolder.SliderClickHandler() {
                        @Override
                        public void onClick(View view, Step step) {
                            if (currentStep != null) {
                                if (step.getStepId() != currentStep.getStepId()) {
                                    releasePlayer();
                                }
                            }
                            Log.e(TAG, "onClick: current step" + step.getStepId() );
                            int position = stepCounterRecycler.getChildLayoutPosition(view);
                            currentStep = step;
                            stepCounterRecycler.smoothScrollToPosition(position);
                            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(step.getFullDescription());
                            initializeExoPlayer(step);
                        }
                    });
            recyclerAdapter.setSteps(allSteps);
            stepCounterRecycler.setLayoutManager(layoutManager);
            stepCounterRecycler.setAdapter(recyclerAdapter);


            ingredientList.setLayoutManager(new LinearLayoutManager(BaileyBrewRecipes.getContext(), RecyclerView.VERTICAL, false));
            ingredientList.setAdapter(new IngredientAdapter(BaileyBrewRecipes.getContext(), allIngredients));

            stepCounterRecycler.smoothScrollToPosition(0);
        }

        return rootView;
    }

    private void initializeExoPlayer(Step currentStep) {
        if (currentStep == null) {
            Log.e(TAG, "initializeExoPlayer: initial load");
        } else {
            Uri stepUri = Uri.parse(currentStep.getStepVideoUrl());
            if (stepUri == null) {
                Log.e(TAG, "initializeExoPlayer: no uri data");
                releasePlayer();
            } else if (TextUtils.isEmpty(String.valueOf(stepUri))) {
                Log.e(TAG, "initializeExoPlayer: no value in URI");
                releasePlayer();
            } else {
                if (mExoPlayer == null) {
                    Log.e(TAG, "initializeExoPlayer: uri loaded is: " + String.valueOf(stepUri));
                    //Create player instance
                    TrackSelector track = new DefaultTrackSelector();
                    LoadControl loadControl = new DefaultLoadControl();
                    mExoPlayer = ExoPlayerFactory.newSimpleInstance(BaileyBrewRecipes.getContext(), track, loadControl);
                    mExoPlayerView.setPlayer(mExoPlayer);

                    String userAgent = Util.getUserAgent(BaileyBrewRecipes.getContext(), "Bailey Brew Recipes");
                    MediaSource mediaSource = new ExtractorMediaSource(stepUri,
                            new DefaultDataSourceFactory(BaileyBrewRecipes.getContext(), userAgent),
                            new DefaultExtractorsFactory(), null, null);
                    mExoPlayer.prepare(mediaSource);
                    mExoPlayer.setPlayWhenReady(true);
                }
            }
        }
    }

    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public Recipe getCurrentRecipe() {
        return mRecipe;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
