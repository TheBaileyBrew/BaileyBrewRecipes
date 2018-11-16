package com.thebaileybrew.baileybrewrecipes.utils;

import android.util.Log;

import com.thebaileybrew.baileybrewrecipes.database.RecipeRepository;
import com.thebaileybrew.baileybrewrecipes.models.Ingredient;
import com.thebaileybrew.baileybrewrecipes.models.Recipe;
import com.thebaileybrew.baileybrewrecipes.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private final static String TAG = JsonUtils.class.getSimpleName();




    public static void extractJsonDataToRoom(String jsonInput, RecipeRepository repository) {
        List<Ingredient> recipeIngredients = new ArrayList<>();
        List<Step> recipeSteps = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonInput);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Recipe currentRecipe = new Recipe();
                String RECIPE_ID = "id";
                int recipeID = jsonObject.getInt(RECIPE_ID);
                String RECIPE_NAME = "name";
                String recipeName = jsonObject.getString(RECIPE_NAME);
                String RECIPE_SERVINGS = "servings";
                int recipeServing = jsonObject.getInt(RECIPE_SERVINGS);
                String RECIPE_IMAGE_URL = "image";
                String recipeImageUrl = jsonObject.getString(RECIPE_IMAGE_URL);
                String RECIPE_INGREDIENT_LIST = "ingredients";
                JSONArray jsonIngredientList = jsonObject.getJSONArray(RECIPE_INGREDIENT_LIST);
                for (int ing = 0; ing < jsonIngredientList.length(); ing++) {
                    //recipeIngredients.clear();
                    JSONObject jsonIngredientObject = jsonIngredientList.getJSONObject(ing);
                    Ingredient currentingredient = new Ingredient();
                    String RECIPE_INGREDIENT_NAME = "ingredient";
                    currentingredient.setIngredientName(jsonIngredientObject.getString(RECIPE_INGREDIENT_NAME));
                    String RECIPE_INGREDIENT_MEASURE = "measure";
                    currentingredient.setIngredientMeasure(jsonIngredientObject.getString(RECIPE_INGREDIENT_MEASURE));
                    String RECIPE_INGREDIENT_QTY = "quantity";
                    currentingredient.setIngredientQuantity(jsonIngredientObject.getDouble(RECIPE_INGREDIENT_QTY));
                    recipeIngredients.add(currentingredient);
                }
                String RECIPE_STEPS_LIST = "steps";
                JSONArray jsonStepList = jsonObject.getJSONArray(RECIPE_STEPS_LIST);
                for (int steps = 0; steps < jsonStepList.length(); steps++) {
                    //recipeSteps.clear();
                    Log.e(TAG, "extractJsonDataToRoom: steps length: " + jsonStepList.length() );
                    JSONObject jsonStepObject = jsonStepList.getJSONObject(steps);
                    Step currentStep = new Step();
                    String RECIPE_STEP_ID = "id";
                    currentStep.setStepId(jsonStepObject.getInt(RECIPE_STEP_ID));
                    String RECIPE_STEP_SHORT_DESC = "shortDescription";
                    currentStep.setStepShortDescription(jsonStepObject.getString(RECIPE_STEP_SHORT_DESC));
                    String RECIPE_STEP_LONG_DESC = "description";
                    currentStep.setFullDescription(jsonStepObject.getString(RECIPE_STEP_LONG_DESC));
                    String RECIPE_STEP_VIDEO_URL = "videoURL";
                    currentStep.setStepVideoUrl(jsonStepObject.getString(RECIPE_STEP_VIDEO_URL));
                    recipeSteps.add(currentStep);
                }
                //TODO Add recipe to ROOM DB
                currentRecipe.setRecipeId(recipeID);
                currentRecipe.setRecipeName(recipeName);
                currentRecipe.setRecipeServing(recipeServing);
                currentRecipe.setRecipeImage(recipeImageUrl);
                currentRecipe.setRecipeIngredients(recipeIngredients);
                currentRecipe.setSteps(recipeSteps);
                repository.insertRecipe(currentRecipe);
                Log.e(TAG, "extractJsonDataToRoom: current id, name:" + recipeID + " " + recipeName + " " + recipeIngredients.size() );



            }
        } catch (JSONException je) {
            Log.e(TAG, "extractJsonDataToRoom: problem getting recipe", je);
        }
    }
}
