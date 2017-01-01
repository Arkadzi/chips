package com.example.chipsdemo;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 12/30/16.
 */

public class Actor extends Chips {
    private String name;
    private String surname;

    public Actor(String name, String surname, int drawableId) {
        super(null, drawableId);
        setData(name, surname);
    }

    public void setData(String name, String surname) {
        this.name = name;
        this.surname = surname;
        setText(name + " " + surname);
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    @Override
    public String[] getSuggestionText() {
        return new String[]{getName(), getSurname()};
    }

    @Override
    public Chips copy() {
        return new Actor(name, surname, getDrawableId());
    }

    @Override
    public String toString() {
        return "text = " + getText();
    }
}
