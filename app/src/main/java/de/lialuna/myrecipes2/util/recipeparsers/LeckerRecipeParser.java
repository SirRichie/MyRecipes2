package de.lialuna.myrecipes2.util.recipeparsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;

public class LeckerRecipeParser extends AbstractRecipeParser {

    @Override
    protected List<Ingredient> getIngredients(Document doc) {
        List<Ingredient> result = new ArrayList<>();
        Elements ingredientElements = doc.select("ul.list--ingredients li");

        for (Element element : ingredientElements) {
            if (element.children().size() < 2)
                continue; // ignore headings etc.
            result.add(getIngredient(element));
        }

        return result;
    }

    private Ingredient getIngredient(Element element) {
        return new Ingredient(element.child(0).text().trim(), element.child(1).text().trim());
    }

    @Override
    protected List<Step> getSteps(Document doc) {
        List<Step> result = new ArrayList<>();
        Elements stepElements = doc.select("dl.list--preparation dd");
        for (Element stepElement : stepElements) {
            result.add(new Step(stepElement.text().trim()));
        }
        return result;
    }

    @Override
    protected String getTitle(Document doc) {
        return doc.selectFirst(".article-header h1").text().trim();
    }
}
