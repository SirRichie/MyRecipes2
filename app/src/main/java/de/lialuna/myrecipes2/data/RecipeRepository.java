package de.lialuna.myrecipes2.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.util.Constants;

public class RecipeRepository {
    public interface RecipeRepositoryDataObserver {
        public void recipesLoaded(List<Recipe> recipes);
    }

    public interface RecipeRepositoryAuthObserver {
        public void authSuccessful(FirebaseUser user);

        public void authFailed();
    }

    public static final String TAG = "RecipeRepository";

    public static void authenticate(String email, String password, RecipeRepositoryAuthObserver observer) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // sign in with a fixed user to allow for securing Firestore
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information

                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "signInWithEmail:success " + user.getUid() + " | " + user.getDisplayName());
                        observer.authSuccessful(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        // Toast.makeText(MainActivity.this, "Authentication failed.",  Toast.LENGTH_SHORT).show();
                        observer.authFailed();
                    }
                });
    }

    public static void loadRecipes(RecipeRepositoryDataObserver observer) {
        // ensure recipes is initialized
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.DB_RECIPE_COLLECTION)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    // manually construct the list so we can get the database ID
                    List<Recipe> newRecipes = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Recipe r = snapshot.toObject(Recipe.class);
                        r.setDbID(snapshot.getId());
                        newRecipes.add(r);
                    }
                    Collections.sort(newRecipes);
                    Log.d(TAG, "received list of recipies from db with size " + newRecipes.size());
                    observer.recipesLoaded(newRecipes);
                });
    }

    public static void addRecipe(Recipe recipe) {
        if (!isNewRecipe(recipe))
            throw new IllegalArgumentException("Can only add recipes that were not previously stored");

        FirebaseFirestore.getInstance().collection(Constants.DB_RECIPE_COLLECTION)
                .add(recipe)
                .addOnSuccessListener(documentReference -> {
                    recipe.setDbID(documentReference.getId());
                });
    }

    public static void updateRecipe(Recipe recipe) {
        if (isNewRecipe(recipe))
            throw new IllegalArgumentException("Can only update recipes that were previously stored");

        FirebaseFirestore.getInstance().collection(Constants.DB_RECIPE_COLLECTION)
                .document(recipe.getDbID())
                .set(recipe);
    }

    public static void removeRecipe(Recipe recipe) {
        if (isNewRecipe(recipe))
            throw new IllegalStateException("Cannot delete non-existing recipe");

        FirebaseFirestore.getInstance().collection(Constants.DB_RECIPE_COLLECTION)
                .document(recipe.getDbID())
                .delete();
    }

    // TODO might be able to delete this
    public static void saveOrStoreToDB(Recipe recipe) {
        CollectionReference recipes = FirebaseFirestore.getInstance().collection(Constants.DB_RECIPE_COLLECTION);
        DocumentReference recipeRef = null;

        if (isNewRecipe(recipe)) {
            // create it in the database
            recipeRef = recipes.document();
            recipe.setDbID(recipeRef.getId());
        } else {
            recipeRef = recipes.document(recipe.getDbID());
        }

        /*if (imageChanged) { // only do image handling if the image changed
            // find image
            String url = "images/" + recipe.getValue().getDbID() + ".jpg";
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(url);
            if (imageUri != null) {
                // upload image
                imageRef.putFile(imageUri);
                // set URL
                recipe.getValue().setImageURL(url);
            } else {
                imageRef.delete(); // may fail, but silently
                recipe.getValue().setImageURL(null);
            }
        }*/ // TODO maybe add image handling later again

        // store recipe
        recipeRef.set(recipe);
    }

    public static boolean isNewRecipe(Recipe recipe) {
        return recipe.getDbID() == null;
    }

}
