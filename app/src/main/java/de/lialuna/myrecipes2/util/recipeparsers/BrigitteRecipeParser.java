package de.lialuna.myrecipes2.util.recipeparsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;

public class BrigitteRecipeParser extends AbstractRecipeParser {

    @Override
    protected List<Ingredient> getIngredients(Document doc) {
        List<Ingredient> result = new ArrayList<>();
        Elements ingredientElements = doc.select("x-portions.recipe-ingredients__list");

        String amount = "";
        List<Element> ingredients = ingredientElements.get(0).children();
        for (Element e : ingredients) {
            if (e.hasClass("recipe-ingredients__separator")) {
                // ingredient group
                String ingredientName = e.text().trim();
                if (ingredientName.endsWith(":")) {
                    ingredientName = ingredientName.substring(0, ingredientName.length());
                }
                Ingredient newIngredient = new Ingredient("", ingredientName);
                newIngredient.setGroupIdentifier(true);
                result.add(newIngredient);
            } else if (e.hasClass("recipe-ingredients__label")) {
                // ingredient name
                int parsedAmount = 0;
                if (!amount.equals("")) {
                    try {
                        parsedAmount = Integer.parseInt(amount);
                    } catch (Exception exception) {
                        // parsedAmount is already 0, so silently ignore
                    }
                }

                if (parsedAmount == 1) {
                    amount += " " + shortenUnit(e.getElementsByClass("recipe-ingredients__unit-singular").text());
                } else {
                    amount += " " + shortenUnit(e.getElementsByClass("recipe-ingredients__unit-plural").text());
                }
                Elements remainingText = e.getElementsByTag("span");
                // skip first 2
                remainingText.remove(0);
                remainingText.remove(0);
                String ingredientName = remainingText.text();

                result.add(new Ingredient(amount, ingredientName));
            } else {
                // ingredient amount
                amount = e.text().trim();
            }
        }

        /*
        for (Element e : ingredientElements) {
            result.addAll(handleBlock(e));
        }

         */


        return result;
    }

    @Override
    protected List<Step> getSteps(Document doc) {
        List<Step> result = new ArrayList<>();
        Elements stepElements = doc.select("div.group--preparation-steps li.group__text-element");
        for (Element e : stepElements) {
            result.add(new Step(e.text().trim()));
        }
        return result;
    }

    @Override
    protected String getTitle(Document doc) {
        return doc.selectFirst("h2.title__heading").text().trim();

    }

    private List<Ingredient> handleBlock(Element block) {
        List<Ingredient> result = new ArrayList<>();
        Elements headline = block.getElementsByClass("o-article__ingredient-block-headline");
        if (headline != null && headline.size() == 1) {
            String text = headline.text().trim();
            Ingredient groupIngredient = new Ingredient("", text.substring(0, text.length() - 1)); // strip :
            groupIngredient.setGroupIdentifier(true);
            result.add(groupIngredient);
        }

        Elements ingredients = block.getElementsByClass("a-list-item-recipe");
        for (Element e : ingredients) {
            result.add(getIngredient(e));
        }

        return result;
    }

    private Ingredient getIngredient(Element ingredientElement) {
        String measure = ingredientElement.child(0).text().trim();
        String unitAndName = ingredientElement.child(1).text().trim();


        // if there is no measure, no further processing is required
        if (measure.isEmpty()) {
            return new Ingredient("", unitAndName);
        } else {
            String name;
            String unit;
            // Brigitte has a weird way of joining the unit and the name, try to separate them
            int index = unitAndName.indexOf(" ");
            if (index == -1) {  // no space
                name = unitAndName;
                unit = "";
            } else {
                name = unitAndName.substring(index + 1);
                unit = unitAndName.substring(0, index);
            }
            unit = shortenUnit(unit);
            return new Ingredient(measure + " " + unit, name);
        }
    }

    private String shortenUnit(String unit) {
        switch (unit) {
            case "Gramm":
                return "g";
            case "Kilogramm":
                return "kg";
            case "Milliliter":
                return "ml";
            case "Liter":
                return "l";
            default:
                return unit;
        }
    }
}
