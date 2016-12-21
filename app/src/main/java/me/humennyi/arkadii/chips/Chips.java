package me.humennyi.arkadii.chips;

import android.support.annotation.DrawableRes;

/**
 * Created by arkadii on 12/21/16.
 */

public class Chips {
    private int id = -1;
    private String text;
    @DrawableRes
    private int drawableId;

    public Chips(String text, int drawableId) {
        this.text = text;
        this.drawableId = drawableId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
