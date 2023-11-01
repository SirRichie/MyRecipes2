package de.lialuna.myrecipes2.util.recipeparsers;


import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.lialuna.myrecipes2.entity.Recipe;


public interface RecipeParser {

    Recipe parseRecipe(Document doc);

    Map<String, RecipeParser> parsers = Collections.unmodifiableMap(new HashMap<String, RecipeParser>(){{
        put("chefkoch.de", new ChefkochRecipeParser());
        put("www.chefkoch.de", get("chefkoch.de"));
        put("www.essen-und-trinken.de", new EssenTrinkenRecipeParser());
        put("www.kochbar.de", new KochbarRecipeParser());
        put("www.brigitte.de", new BrigitteRecipeParser());
        put("www.lecker.de", new LeckerRecipeParser());
        put("emmikochteinfach.de", new EmmiKochtEinfachRecipeParser());
        put("www.einfachbacken.de", new EinfachBackenRecipeParser());
    }});
}
