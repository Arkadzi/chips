package com.example.chipsdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsView;
import me.humennyi.arkadii.chips.adapter.ChipsAdapter;
import me.humennyi.arkadii.chips.adapter.ChipsIdHolder;

/**
 * Created by arkadii on 1/1/17.
 */

public class ActorAdapter extends ChipsAdapter<Actor> {
    public ActorAdapter(Context context,
                        ChipsIdHolder suggestionIdHolder, ChipsIdHolder chipsIdHolder,
                        ChipsIdHolder invalidChipsIdHolder,
                        @Nullable List<Actor> suggestions, ChipsView chipsView) {
        super(context, suggestionIdHolder, chipsIdHolder, invalidChipsIdHolder, suggestions, chipsView);
    }

    @Nullable
    @Override
    protected View getPopupView(ViewGroup parent, int position, Actor item) {
        return getLayoutInflater().inflate(R.layout.popup_view, parent, false);
    }

    @Override
    protected boolean hasPopup() {
        return true;
    }

    @Override
    public Chips instantiateNewChips(String text) {
        return new Actor(text, "", 0);
    }
}
