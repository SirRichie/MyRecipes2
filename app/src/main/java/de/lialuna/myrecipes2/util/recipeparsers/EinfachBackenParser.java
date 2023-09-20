package de.lialuna.myrecipes2.util.recipeparsers;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;

public class EinfachBackenParser extends AbstractRecipeParser {

    private static final String TAG = "EinfachBackenParser";

    @Override
    protected String getTitle(Document doc) {
        Element titleElement = doc.selectFirst("h1.recipe--heading__title");
        return titleElement.text();
    }

    @Override
    protected List<Ingredient> getIngredients(Document doc) {
        List<Ingredient> result = new ArrayList<>();
        Elements ingredientElements = doc.select("div.recipe--full__ingredients");
        for (Element element : ingredientElements) {
            result.addAll(getIngredientGroup(element));
        }
        return result;
    }

    private List<Ingredient> getIngredientGroup(Element ingredientElement) {
        List<Ingredient> result = new ArrayList<>();
        // ingredients are organized structually into groups, so first grab the group name then iterate through the children

        String groupName = ingredientElement.text().trim();
        Ingredient ingredient = new Ingredient("", groupName);
        ingredient.setGroupIdentifier(true);
        result.add(ingredient);

        Elements ingredientsElements = ingredientElement.select(".ingredients-wrapper");
        for (Element element : ingredientsElements) {
            result.add(getIngredient(element));
        }


        return result;


    }

    private Ingredient getIngredient(Element ingredientElement) {
        try {
            String rawValues = ingredientElement.selectFirst("ingredient").attributes().getIgnoreCase("raw-values");
            JSONObject parsedValues = new JSONObject(rawValues);

            String amount = parsedValues.getString("quantity");
            if ("0".equals(amount)) {   // ignore empty values
                amount = "";
            } else if (parsedValues.has("unitLabelPlural")) {
                amount += " " + parsedValues.getString("unitLabelPlural");
            }
            String ingredient = parsedValues.getString("labelPlural");

            return new Ingredient(amount, ingredient);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Ingredient("","");

    }

    @Override
    protected List<Step> getSteps(Document doc) {
        List<Step> result = new ArrayList<>();
        Elements stepElements = doc.select("div.recipe-step__text");
        for (Element stepElement : stepElements) {
            result.add(new Step(stepElement.text().trim()));
        }

        return result;
    }


}
