package com.thebaileybrew.baileybrewrecipes.database;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thebaileybrew.baileybrewrecipes.models.Ingredient;
import com.thebaileybrew.baileybrewrecipes.models.Step;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class ListIngredientConverter {

    @TypeConverter
    public static List<Ingredient> stringToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {}.getType();
        List<Ingredient> ingredients = gson.fromJson(json, type);
        return ingredients;
    }

    @TypeConverter
    public static String listToString(List<Ingredient> ingredients) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        String json = gson.toJson(ingredients, type);
        return json;
    }
}
