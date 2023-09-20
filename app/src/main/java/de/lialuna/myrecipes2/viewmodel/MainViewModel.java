package de.lialuna.myrecipes2.viewmodel;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lialuna.myrecipes2.entity.Category;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.util.Constants;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    private ListenerRegistration categoriesListenerRegistration;
    private ListenerRegistration recipesListenerRegistration;

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<Recipe>> recipes;

    public LiveData<List<Category>> getCategories() {
        if (categories == null) {
            categories = new MutableLiveData<>();
            subscribeToCategories();
        }
        return categories;
    }
    public LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<>();
            subscribeToRecipes();
        }
        return recipes;
    }

    private void subscribeToRecipes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // recipes = new MutableLiveData<>();
        recipesListenerRegistration = db
                .collection(Constants.DB_RECIPE_COLLECTION)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Listening to recipes failed: ", e);
                        return;
                    }
                    // manually construct the list so we can get the database ID
                    List<Recipe> newRecipes = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Recipe r = snapshot.toObject(Recipe.class);
                        r.setDbID(snapshot.getId());
                        newRecipes.add(r);
                    }
                    Collections.sort(newRecipes);
                    // publish the new result
                    recipes.postValue(newRecipes);
                });
    }

    private void subscribeToCategories() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // categories = new MutableLiveData<>();
        categoriesListenerRegistration = db
                .collection(Constants.DB_CATEGORY_COLLECTION)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed: ", e);
                        return;
                    }
                    List<Category> newCategories = documentSnapshots.toObjects(Category.class);
                    categories.postValue(newCategories);
                });

    }

    @Override
    protected void onCleared() {
        categoriesListenerRegistration.remove();    // make sure to stop listening to firestore
        recipesListenerRegistration.remove();
        // mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        // mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        super.onCleared();
    }
}
