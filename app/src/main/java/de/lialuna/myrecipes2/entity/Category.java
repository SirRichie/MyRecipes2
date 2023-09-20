package de.lialuna.myrecipes2.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Tobias on 05.01.2018.
 */

public class Category implements Serializable, Comparable<Category>, Parcelable, Listable {
    private String name;

    public Category() {

    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Category)) return false;
        return getName().equals(((Category)obj).getName());
    }

    @Override
    public int compareTo(@NonNull Category o) {
        return name.compareTo(o.name);
    }

    @Override
    public String displayName() {
        return name;
    }

    /** Parcelable **/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public static final Creator<Category> CREATOR
            = new Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    private Category(Parcel in) {
        name = in.readString();
    }


}
