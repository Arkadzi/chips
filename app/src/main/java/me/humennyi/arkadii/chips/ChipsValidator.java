package me.humennyi.arkadii.chips;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 12/30/16.
 */
public interface ChipsValidator<T extends Chips> {
    boolean isValid(T chips);
}
