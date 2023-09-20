package de.lialuna.myrecipes2.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Tobias on 05.01.2018.
 */

public class Ingredient implements Serializable, Parcelable {
    private String amount;
//    private String unit;
    private String ingredient;
    private boolean groupIdentifier = false;

    public Ingredient() {

    }

    public Ingredient(String amount, String ingredient) {
        this.amount = amount;
//        this.unit = unit;
        this.ingredient = ingredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

//    public String getUnit() {
//        return unit;
//    }

//    public void setUnit(String unit) {
//        this.unit = unit;
//    }

    @Override
    public String toString() {
        return "Ingredient{" +
                amount + " " +
//                unit + " " +
                ingredient + "}";
    }

    /** Parcelable **/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
//        dest.writeString(unit);
        dest.writeString(ingredient);
        dest.writeInt(groupIdentifier ? 1 : 0);
    }

    public static final Creator<Ingredient> CREATOR
            = new Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private Ingredient(Parcel in) {
        amount = in.readString();
//        unit = in.readString();
        ingredient = in.readString();
        groupIdentifier = in.readInt() != 0;
    }

    public boolean isGroupIdentifier() {
        return groupIdentifier;
    }

    public void setGroupIdentifier(boolean groupIdentifier) {
        this.groupIdentifier = groupIdentifier;
    }
}
