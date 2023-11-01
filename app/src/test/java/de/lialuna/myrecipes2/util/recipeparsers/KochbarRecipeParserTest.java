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

public class KochbarRecipeParserTest {

    private static final URL url;
    private static AbstractRecipeParser parser;

    static {
        try {
            url = new URL("https://www.kochbar.de/rezept/423368/Boeuf-a-la-mode-ein-wenig-aufwendig-aber-das-Ergebnis-ist-einfach-nur-lecker.html#zutatenrechner/personen/6");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    static Document doc;

    @BeforeClass
    public static void parseDocument() throws IOException {
        doc = Jsoup.parse(url, 100000);
        parser = new KochbarRecipeParser();
    }

    @Test
    public void getTitle() {
        String title = parser.getTitle(doc);
        assertEquals(title, "Boeuf á la mode - ein wenig aufwendig, aber das Ergebnis ist einfach nur lecker");
    }

    @Test
    public void getIngredients_TestCount() {
        List<Ingredient> ingredients = parser.getIngredients(doc);
        assertEquals(21, ingredients.size());
    }

    @Test
    public void getSteps_TestCount() {
        List<Step> steps = parser.getSteps(doc);
        assertEquals(8, steps.size());
    }
}