package com.thebaileybrew.baileybrewrecipes.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.thebaileybrew.baileybrewrecipes.RecipeCardRecycler;
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

    public List<Recipe> getRecipes(RecipeCardRecycler adapter) {
        try {
            return new getAllRecipesAsyncTask(mRecipeDao, adapter).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException ie) {
            Log.e(TAG, "getRecipes: interrupted", ie);
            return null;
        }
    }

    private static class getAllRecipesAsyncTask extends  AsyncTask<Void, Void, List<Recipe>> {
        private RecipeDao mRecipeDao;
        private RecipeCardRecycler mAdapter;

        getAllRecipesAsyncTask(RecipeDao recipeDao, RecipeCardRecycler adapter) {
            this.mRecipeDao = recipeDao;
            this.mAdapter = adapter;
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            mAdapter.setRecipeCollection(mRecipeDao.getRecipes());
            return mRecipeDao.getRecipes();
        }
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


    public void insertRecipe(Recipe recipe) {
        new populateDatabaseWithRecipesAsyncTask(mRecipeDao).execute(recipe);
    }

    private static class populateDatabaseWithRecipesAsyncTask extends AsyncTask<Recipe, Void, Void> {
        private RecipeDao mRecipeDao;

        populateDatabaseWithRecipesAsyncTask(RecipeDao recipeDao) {
            this.mRecipeDao = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            Log.e(TAG, "doInBackground: recipe loading");
            Recipe currentRecipe = recipes[0];
            mRecipeDao.insertRecipe(currentRecipe);

            return null;
        }
    }
}
