package me.humennyi.arkadii.chips;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by arkadii on 12/21/16.
 */

public class Chips implements Serializable {
    private String text;
    @DrawableRes
    private int drawableId;
    private boolean isValid;

    public Chips(String text, int drawableId) {
        this(text, drawableId, true);
    }

    public Chips(String text, int drawableId, boolean isValid) {
        this.text = text;
        this.drawableId = drawableId;
        this.isValid = isValid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Chips copyValid() {
        return new Chips(text, drawableId, true);
    }
//
//    @Override
//    public String toString() {
//        return "Chips{" +
//                "text='" + text + '\'' +
//                '}';
//    }


    @Override
    public String toString() {
        return "Chips{" +
                "text='" + text + '\'' +
                ", drawableId=" + drawableId +
                ", isValid=" + isValid +
                '}';
    }

//    protected Chips(Parcel in) {
//        Object[] objects = in.readArray(Object[].class.getClassLoader());
//        text = (String) objects[0];
//        drawableId = (int) objects[1];
//        isValid = (boolean) objects[2];
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(@NonNull Parcel dest, int flags) {
//        dest.writeArray(new Object[] {text, drawableId, isValid});
//    }
//
//    public static final Parcelable.Creator<Chips> CREATOR = new Parcelable.Creator<Chips>() {
//        @Override
//        public Chips createFromParcel(Parcel in) {
//            return new Chips(in);
//        }
//
//        @Override
//        public Chips[] newArray(int size) {
//            return new Chips[size];
//        }
//    };
}
