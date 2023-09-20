package de.lialuna.myrecipes2.util.recipeparsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;

public class EmmiKochtEinfachParser extends AbstractRecipeParser {

    private static final String TAG = "EmmiKochtEinfachParser";

    @Override
    protected String getTitle(Document doc) {
        Element titleElement = doc.selectFirst("h1.post-title");
        return titleElement.text();
    }

    @Override
    protected List<Ingredient> getIngredients(Document doc) {
        List<Ingredient> result = new ArrayList<>();
        Elements ingredientElements = doc.select("ul.wprm-recipe-ingredients li");
        for (Element element : ingredientElements) {
            result.add(getIngredient(element));
        }
        return result;
    }

    private Ingredient getIngredient(Element ingredientElement) {
        String amount = "", unit = "", ingredient = "";
        Element amountElement = ingredientElement.selectFirst(".wprm-recipe-ingredient-amount");
        Element unitElement = ingredientElement.selectFirst(".wprm-recipe-ingredient-unit");
        Element ingredientNameElement = ingredientElement.selectFirst(".wprm-recipe-ingredient-name");
        if (amountElement != null)
            amount = amountElement.text();
        if (unitElement != null)
            unit = unitElement.text();
        if (ingredientNameElement != null)
            ingredient = ingredientNameElement.text();

        if (!unit.isEmpty())
            amount += " " + unit;

        return  new Ingredient(amount, ingredient);

    }

    @Override
    protected List<Step> getSteps(Document doc) {
        List<Step> result = new ArrayList<>();
        Elements stepElements = doc.select("ul.wprm-recipe-instructions li");
        for (Element stepElement : stepElements) {
            result.add(new Step(stepElement.text().trim()));
        }

        return result;
    }



}
