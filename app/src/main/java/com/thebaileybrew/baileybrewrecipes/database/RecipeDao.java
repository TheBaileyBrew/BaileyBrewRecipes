package com.thebaileybrew.baileybrewrecipes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.thebaileybrew.baileybrewrecipes.models.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY recipe_name")
    List<Recipe> getRecipes();

    @Query("SELECT * FROM recipes WHERE recipe_id = :id")
    Recipe getSingleRecipe(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

}
