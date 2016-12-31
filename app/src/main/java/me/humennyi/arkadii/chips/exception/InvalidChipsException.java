package me.humennyi.arkadii.chips.exception;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 12/30/16.
 */

public class InvalidChipsException extends ChipsException {

    private final List<Integer> positions;
    private final List<Chips> chips;
    private List<Pair<Integer, Chips>> chipsPairs;

    public InvalidChipsException(String message, List<Pair<Integer, Chips>> chipsPairs) {
        super(message);
        this.chipsPairs = chipsPairs;
        positions = new ArrayList<>();
        for (Pair<Integer, Chips> chip : chipsPairs) {
            positions.add(chip.first);
        }
        chips = new ArrayList<>();
        for (Pair<Integer, Chips> chip : chipsPairs) {
            chips.add(chip.second);
        }
    }

    public List<Pair<Integer, Chips>> getChipsPairs() {
        return chipsPairs;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public List<Chips> getChips() {
        return chips;
    }
}
