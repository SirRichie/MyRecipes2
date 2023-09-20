package de.lialuna.myrecipes2.util.recipeparsers;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.entity.Step;


public abstract class AbstractRecipeParser implements RecipeParser {

    @Override
    public Recipe parseRecipe(Document doc) {
        return new Recipe(getTitle(doc), getSteps(doc), getIngredients(doc), new ArrayList<>());
    }

    protected abstract List<Ingredient> getIngredients(Document doc);

    protected abstract List<Step> getSteps(Document doc);

    protected abstract String getTitle(Document doc);
}
