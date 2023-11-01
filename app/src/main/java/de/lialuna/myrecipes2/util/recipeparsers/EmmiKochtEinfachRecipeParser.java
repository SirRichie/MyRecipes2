package de.lialuna.myrecipes2.util.recipeparsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;

public class EmmiKochtEinfachRecipeParser extends AbstractRecipeParser {

    private static final String TAG = "EmmiKochtEinfachParser";

    @Override
    protected String getTitle(Document doc) {
        Element titleElement = doc.selectFirst("h1.title");
        return titleElement.text();
    }

    @Override
    protected List<Ingredient> getIngredients(Document doc) {
        List<Ingredient> result = new ArrayList<>();
        Elements ingredientGroups = doc.select("div.wprm-recipe-ingredient-group");
        ingredientGroups.remove(0);
        for (Element group : ingredientGroups) {
            result.addAll(processIngredientGroup(group));
        }
        return result;
    }

    private List<Ingredient> processIngredientGroup(Element groupElement) {
        List<Ingredient> result = new ArrayList<>();
        Element groupName = groupElement.selectFirst("h4");
        if (groupName != null) {
            String text = groupName.text().trim();
            if (!text.isEmpty()) {
                Ingredient groupIngredient = new Ingredient("", text);
                groupIngredient.setGroupIdentifier(true);
                result.add(groupIngredient);
            }
        }
        Elements ingredientElements = groupElement.select("li");
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
