package me.humennyi.arkadii.chips.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsView;

/**
 * Created by arkadii on 12/30/16.
 */
public class SimpleChipsAdapter extends ChipsAdapter<Chips> {
    public SimpleChipsAdapter(Context context, ChipsIdHolder suggestionIdHolder, ChipsIdHolder chipsIdHolder, ChipsIdHolder invalidChipsIdHolder, @Nullable List<Chips> suggestions, ChipsView chipsView) {
        super(context, suggestionIdHolder, chipsIdHolder, invalidChipsIdHolder, suggestions, chipsView);
    }

    @Nullable
    @Override
    protected View getPopupView(ViewGroup parent, int position, Chips item) {
        return null;
    }

    @Override
    protected boolean hasPopup() {
        return false;
    }

    @Override
    public Chips instantiateNewChips(String text) {
        return new Chips(text, 0);
    }
}
