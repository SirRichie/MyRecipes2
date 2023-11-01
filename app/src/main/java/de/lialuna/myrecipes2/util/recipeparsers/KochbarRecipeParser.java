package de.lialuna.myrecipes2.util.recipeparsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;

public class KochbarRecipeParser extends AbstractRecipeParser {

    private static final String TAG = "KochbarRecipeParser";

    @Override
    protected List<Ingredient> getIngredients(Document doc) {
        List<Ingredient> result = new ArrayList<>();
        Elements ingredientElements = doc.select("div.recipe-content table");

        for (Element element : ingredientElements) {

        /*    if (element.children().size() < 2)
                continue; // ignore headings etc.
            result.add(getIngredient(element));

         */
            result.addAll(processTable(element));
        }


        return result;
    }

    private List<Ingredient> processTable(Element tableElement) {
        List<Ingredient> result = new ArrayList<>();
        Element header = tableElement.selectFirst("thead");
        if (header != null && !header.text().trim().isEmpty()) {
            Ingredient ingredient = new Ingredient("", header.text().trim());
            ingredient.setGroupIdentifier(true);
            result.add(ingredient);
        }

        Elements tableRows = tableElement.select("tbody tr");
        for (Element row : tableRows) {
            result.add(processTableRow(row));
        }

        return result;
    }

    private Ingredient processTableRow(Element row) {
        String amount = row.child(1).text().trim();
        String ingredientName = row.child(0).text().trim();
        return new Ingredient(amount, ingredientName);
    }

    private Ingredient getIngredient(Element element) {
        return new Ingredient(element.child(0).text().trim(), element.child(1).text().trim());
    }


    @Override
    protected List<Step> getSteps(Document doc) {
        List<Step> result = new ArrayList<>();
        Elements stepElements = doc.select("section.jt-preparation p span");
        for (Element stepElement : stepElements) {
            result.add(new Step(stepElement.text().trim()));
        }

        return result;
    }

    @Override
    protected String getTitle(Document doc) {
        return doc.selectFirst("h2.recipe-head-headline").text().trim();
    }
}
