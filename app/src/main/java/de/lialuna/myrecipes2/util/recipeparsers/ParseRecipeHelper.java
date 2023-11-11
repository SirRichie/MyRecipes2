package de.lialuna.myrecipes2.util.recipeparsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

import de.lialuna.myrecipes2.RecipeApplication;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.util.recipeparsers.exceptions.HostNotSupportedException;

public class ParseRecipeHelper {

    public interface ParseRecipeCallback {
        public void onComplete(Result<Recipe> recipeResult);
    }

    public static void downloadAndParseAsync(URL url, ParseRecipeCallback callback) {
        RecipeApplication.parseRecipeExecutorService.execute(() -> {
            Result<Recipe> result = downloadAndParse(url);
            RecipeApplication.mainThreadHandler.post(() -> {
                callback.onComplete(result);
            });

        });
    }

    public static Result<Recipe> downloadAndParse(URL url) {
        try {
            RecipeParser parser = findParser(url);
            Document document = Jsoup.parse(url, 10000);
            Recipe recipe = parser.parseRecipe(document);
            return new Result.Success<>(recipe);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    private static RecipeParser findParser(URL url) {
        RecipeParser parser = RecipeParser.parsers.get(url.getHost());
        if (parser == null) {
            throw new HostNotSupportedException("No parser available for " + url.getHost());
        }
        return parser;
    }
}
