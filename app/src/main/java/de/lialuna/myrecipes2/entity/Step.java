package de.lialuna.myrecipes2.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Tobias on 05.01.2018.
 */

public class Step implements Serializable, Parcelable {

    private String text;

    public Step() {
    }

    public Step(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /** Parcelable **/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }

    public static final Creator<Step> CREATOR
            = new Creator<Step>() {
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    private Step(Parcel in) {
        text = in.readString();
    }
}
