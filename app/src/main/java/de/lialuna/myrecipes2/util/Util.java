package de.lialuna.myrecipes2.util;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.lialuna.myrecipes2.entity.Recipe;

public class Util {

    public static List<String> getIngredientNamesFromRecipes(List<Recipe> recipes) {
        if (recipes == null) {
            return Collections.emptyList();
        }
        Set<String> ingredientNames = recipes.stream().collect(
                () -> new TreeSet<String>(),
                (strings, recipe) -> strings.addAll(recipe.getIngredientNames()),
                (strings, strings2) -> strings.addAll(strings2)
        );

        return ingredientNames.stream().sorted().collect(Collectors.toList());
    }
}
