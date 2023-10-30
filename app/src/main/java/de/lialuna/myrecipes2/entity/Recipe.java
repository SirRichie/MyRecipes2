package de.lialuna.myrecipes2.entity;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Tobias on 05.01.2018.
 */

// public class Recipe implements Comparable<Recipe>, Serializable, Parcelable {
public class Recipe implements Comparable<Recipe> {

    private String dbID;
    private String title;
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private List<Category> categories;
    private String imageURL;
    @Exclude
    private byte[] thumbnail;

    public Recipe() {
    }

    public Recipe(String title) {
        this.title = title;
        steps = new ArrayList<>();
        ingredients = new ArrayList<>();
        categories = new ArrayList<>();
    }

    public Recipe(String title, List<Step> steps, List<Ingredient> ingredients, List<Category> categories) {
        this.title = title;
        this.steps = steps;
        this.ingredients = ingredients;
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Recipe addStep(Step step) {
        steps.add(step);
        return this;
    }

    public Recipe addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public void removeIngredient(int position) {
        ingredients.remove(position);
    }

    public void removeStep(int position) {
        steps.remove(position);
    }

    @Exclude // make sure firestore does not store this
    public List<String> getIngredientNames() {
        return getIngredients().stream().map(Ingredient::getIngredient).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Recipe{" +
                ", dbID='" + dbID + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Recipe o) {
        return title.toLowerCase().compareTo(o.title.toLowerCase());
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        Collections.sort(this.categories);
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    @Exclude
    public byte[] getThumbnail() {
        return thumbnail;
    }

    @Exclude
    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    // for firestore
    public String getThumbnailForFirestore() {
        return thumbnail == null ? null : Base64.encodeToString(thumbnail, Base64.DEFAULT);
    }

    // for firestore
    public void setThumbnailForFirestore(String representation) {
        if (representation != null) {
            thumbnail = Base64.decode(representation, Base64.DEFAULT);
        }
    }

    /** Parcelable **/
/*

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dbID);
        dest.writeString(title);
        dest.writeList(steps);
        dest.writeList(ingredients);
        dest.writeList(categories);
        dest.writeString(imageURL);
    }

    public static final Creator<Recipe> CREATOR
            = new Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        dbID = in.readString();
        title = in.readString();
        steps = new ArrayList<>();
        ingredients = new ArrayList<>();
        categories = new ArrayList<>();
        in.readList(steps, getClass().getClassLoader());
        in.readList(ingredients, getClass().getClassLoader());
        in.readList(categories, getClass().getClassLoader());
        imageURL = in.readString();
    }


*/

}
