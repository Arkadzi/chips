package com.example.chipsdemo;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 12/30/16.
 */

public class ChipsSubclass extends Chips {
    private int someField;


    public ChipsSubclass(String text, int drawableId, int someField) {
        super(text, drawableId);
        this.someField = someField;
    }

    public int getSomeField() {
        return someField;
    }

    public void setSomeField(int someField) {
        this.someField = someField;
    }

    @Override
    public Chips copy() {
        return new ChipsSubclass(getText(), getDrawableId(), someField);
    }

    @Override
    public String toString() {
        return "someField = " + someField + ", text = " + getText();
    }
}
