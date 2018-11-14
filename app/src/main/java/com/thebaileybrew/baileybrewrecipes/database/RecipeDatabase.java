package com.thebaileybrew.baileybrewrecipes.database;


import android.content.Context;

import com.thebaileybrew.baileybrewrecipes.models.Recipe;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({ListTypeConverter.class, ListIngredientConverter.class})
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

    private static RecipeDatabase INSTANCE;
    private static String DATABASE_NAME = "recipes";

    static RecipeDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
