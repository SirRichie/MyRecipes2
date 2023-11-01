package de.lialuna.myrecipes2.util.recipeparsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;


public class EssenTrinkenRecipeParser extends AbstractRecipeParser {

    private static final String TAG = "EssenTrinkenRecipeParser";


    public String getTitle(Document doc) {
        Element titleElement = doc.selectFirst("span.title__headline");
        return titleElement.text();
    }

    public List<Ingredient> getIngredients(Document doc) {
        List<Ingredient> result = new ArrayList<>();
        Elements listElements = doc.select(".recipe-ingredients__list");
        for (Element element : listElements.first().children()) {
            Ingredient ingredient = getIngredient(element);
            if (ingredient != null)
                result.add(ingredient);
        }
        return result;
    }

    private Ingredient getIngredient(Element element) {
        if (element.hasClass("recipe-ingredients__separator")) {
            // this is a group identifier
            return getGroupIngredient(element);
        } else if (element.hasClass("recipe-ingredients__amount")){ // we need this filter since we also process the next "label) element here
            // this is a normal ingredient
            String amount = element.attributes().get("value");
            String unit = element.nextElementSibling().selectFirst(".recipe-ingredients__unit-singular").text();
            String ingredient = element.nextElementSibling().selectFirst("span[data-label]").text();
            return new Ingredient(amount + " " + unit, ingredient);
        } else {
            return null;
        }
    }

    private Ingredient getGroupIngredient(Element ingredientElement) {
        Ingredient ingredient = new Ingredient("", ingredientElement.text());
        ingredient.setGroupIdentifier(true);
        return ingredient;
    }

    private Ingredient getNormalIngredient(Element amountElement, Element labelElement) {
        return new Ingredient(amountElement.text(), labelElement.text());
    }

   /* private Ingredient getNormalIngredient(Element ingredientElement) {
        String[] parts = ingredientElement.wholeText().split("\\s{2,}");
        String ingredientName = parts[parts.length - 1];
        String amount = "";

        // we may only have an ingredient name
        if (parts.length > 1) {
            StringJoiner joiner = new StringJoiner(" ");
            for (int i = 0; i < parts.length - 1; i++) {
                joiner.add(parts[i]);
            }
            amount = joiner.toString().trim();
        }

        return new Ingredient(amount, ingredientName);
    }*/

    public List<Step> getSteps(Document doc) {
        List<Step> result = new ArrayList<>();
        Elements stepElements = doc.select("ol.group__items li");
        for (Element stepElement : stepElements) {
            result.add(getStep(stepElement));
        }

        return result;
    }

    private Step getStep(Element stepElement) {
        return new Step(stepElement.text());
    }
}
