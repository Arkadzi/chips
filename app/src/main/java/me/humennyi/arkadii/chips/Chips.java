package me.humennyi.arkadii.chips;

import android.support.annotation.DrawableRes;

/**
 * Created by arkadii on 12/21/16.
 */

public class Chips {
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

    @Override
    public String toString() {
        return "Chips{" +
                "text='" + text + '\'' +
                '}';
    }
}
