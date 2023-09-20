package de.lialuna.myrecipes2.util.recipeparsers;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.entity.Step;

public class ChefkochRecipeParser implements RecipeParser {

    private static final String TAG = "ChefkochRecipeParser";

    @Override
    public Recipe parseRecipe(Document doc) {
        return new Recipe(getTitle(doc), getSteps(doc), getIngredients(doc), new ArrayList<>());
    }

    private String getTitle(Document doc) {
        Element titleElement = doc.selectFirst("article h1");
        return titleElement.text();
    }

    private List<Ingredient> getIngredients(Document doc) {
        List<Ingredient> result = new ArrayList<>();
        Elements ingredientElements = doc.select(".ingredients   tr");
        for (Element element : ingredientElements) {
            result.add(getIngredient(element));
        }
        return result;
    }

    private Ingredient getIngredient(Element ingredientElement) {
        Ingredient ingredient;
        if (ingredientElement.selectFirst("h3") != null) {
            // this is an ingredient group
            String groupName = ingredientElement.child(0).text().trim();
            ingredient = new Ingredient("", groupName);
            ingredient.setGroupIdentifier(true);
        } else {
            String amount = ingredientElement.child(0).text().trim();
            String ingredientName = ingredientElement.child(1).text().trim();
            ingredient = new Ingredient(amount, ingredientName);
        }


        // chefkoch uses the <b> tag for ingredient groups
//        if (ingredientElement.child(1).children().size() > 0 && ingredientElement.child(1).child(0).tagName().equals("b")) {
//            ingredient.setGroupIdentifier(true);
//        }

        return ingredient;
    }

    private List<Step> getSteps(Document doc) {
        List<Step> result = new ArrayList<>();

        Element stepsElement = doc.select("article.ds-box > div.ds-box").get(2);
        Log.d(TAG, stepsElement.toString());

        // memorize if we encountered a linebreak to know when to end a Step
        boolean previousNodeWasLinebreak = false;
        StringBuilder builder = new StringBuilder();

        for (Node current : stepsElement.childNodes()) {
            if (isEmptyText(current))
                continue; //ignore empty text nodes

            if (isLinebreak(current) ) {    // check for br tag
                if (previousNodeWasLinebreak) {
                    // the previous node was a line break as well, create a new step
                    Step step = new Step(builder.toString().trim());
                    builder.setLength(0); // clear the buffer
                    result.add(step);
                    previousNodeWasLinebreak = false;
                } else {
                    previousNodeWasLinebreak = true;
                    builder.append("\n");
                }
            }

            if (current instanceof TextNode) {
                TextNode textNode = (TextNode) current;
                builder.append(textNode.text());
            }
        }

        if (builder.length() > 0) {
            result.add(new Step(builder.toString().trim()));
        }

        return result;
    }

    private boolean isLinebreak(Node node) {
        return node instanceof Element && ((Element) node).tagName().equals("br");
    }

    private boolean isEmptyText(Node node) {
        return node instanceof  TextNode && ((TextNode) node).isBlank();
    }

}
