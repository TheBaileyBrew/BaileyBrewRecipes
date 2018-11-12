package com.thebaileybrew.baileybrewrecipes.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.thebaileybrew.baileybrewrecipes.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeRepository {
    private static final String TAG = RecipeRepository.class.getSimpleName();

    private RecipeDao mRecipeDao;
    private List<Recipe> mRecipes;

    public RecipeRepository(Application application) {
        RecipeDatabase db = RecipeDatabase.getDatabase(application);
        mRecipeDao = db.recipeDao();
    }

    public List<Recipe> getRecipes() {
        mRecipes = mRecipeDao.getRecipes();
        return mRecipes;
    }

    public Recipe getSingleRecipe(int id) {
        try {
            return new checkForDatabaseRecordAsyncTask(mRecipeDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException ee) {
            Log.e(TAG, "getSingleRecipe: Interruption", ee);
            return null;
        }
    }

    private static class checkForDatabaseRecordAsyncTask extends AsyncTask<Integer, Void, Recipe> {
        private RecipeDao mRecipeDao;

        checkForDatabaseRecordAsyncTask(RecipeDao recipeDao) {
            this.mRecipeDao = recipeDao;
        }

        @Override
        protected Recipe doInBackground(Integer... integers) {
            int currentRecipe = integers[0];

            return mRecipeDao.getSingleRecipe(currentRecipe);
        }
    }
}
