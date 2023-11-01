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

public class ChefkochRecipeParserTest {

    private static final URL url;
    private static AbstractRecipeParser parser;

    static {
        try {
            url = new URL("https://www.chefkoch.de/rezepte/3396151505646117/Zitronen-Ueberraschungs-Torte.html");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    static Document doc;

    @BeforeClass
    public static void parseDocument() throws IOException {
        doc = Jsoup.parse(url, 100000);
        parser = new ChefkochRecipeParser();
    }

    @Test
    public void getTitle() {
        String title = parser.getTitle(doc);
        assertEquals("Zitronen-Überraschungs-Torte", title);
    }

    @Test
    public void getIngredients_TestCount() {
        List<Ingredient> ingredients = parser.getIngredients(doc);
        assertEquals(26, ingredients.size());
    }

    @Test
    public void getSteps_TestCount() {
        List<Step> steps = parser.getSteps(doc);
        assertEquals(22, steps.size());
    }
}