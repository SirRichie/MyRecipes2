package de.lialuna.myrecipes2.util.recipeparsers;

import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.lialuna.myrecipes2.entity.Ingredient;
import de.lialuna.myrecipes2.entity.Step;

public class EssenTrinkenRecipeParserTest {

    private static final URL url;
    private static AbstractRecipeParser parser;

    static {
        try {
            url = new URL("https://www.essen-und-trinken.de/rezepte/54281-rzpt-zitronentarte");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    static Document doc;

    @BeforeClass
    public static void parseDocument() throws IOException {
        doc = Jsoup.parse(url, 100000);
        parser = new EssenTrinkenRecipeParser();
    }

    @Test
    public void getTitle() {
        String title = parser.getTitle(doc);
        assertEquals(title, "Zitronentarte");
    }

    @Test
    public void getIngredients_TestCount() {
        List<Ingredient> ingredients = parser.getIngredients(doc);
        assertEquals(15, ingredients.size());
    }

    @Test
    public void getSteps_TestCount() {
        List<Step> steps = parser.getSteps(doc);
        assertEquals(3, steps.size());
    }
}