package com.example.chipsdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsValidator;
import me.humennyi.arkadii.chips.ChipsView;
import me.humennyi.arkadii.chips.adapter.ChipsAdapter;
import me.humennyi.arkadii.chips.adapter.ChipsIdHolder;
import me.humennyi.arkadii.chips.adapter.SimpleChipsAdapter;
import me.humennyi.arkadii.chips.exception.InvalidChipsException;

public class MainActivity extends AppCompatActivity {

    private ChipsView chipsView1;
    private ChipsView chipsView2;
    private ChipsView chipsView3;
    private ActorAdapter adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareChipsViews();
//        prepareChipsView2();
//        prepareChipsView3();
    }

    private void prepareChipsViews() {
        chipsView1 = (ChipsView) findViewById(R.id.chipsView1);
        chipsView2 = (ChipsView) findViewById(R.id.chipsView2);
        chipsView3 = (ChipsView) findViewById(R.id.chipsView3);
        ChipsIdHolder suggestionsIdHolder = new ChipsIdHolder(android.R.layout.simple_list_item_1, android.R.id.text1, 0);
        ChipsIdHolder chipsIdHolder = new ChipsIdHolder(R.layout.chips1, R.id.tvText, R.id.ivIcon);
        ChipsIdHolder invalidChipsIdHolder = new ChipsIdHolder(R.layout.chips_invalid1, R.id.tvText, 0);

        List<Chips> list1 = new ArrayList<>();
        list1.add(new Chips("qweqwe", R.drawable.me_gusta));
        list1.add(new Chips("asdasdasd", R.drawable.me_gusta));
        list1.add(new Chips("zxczxczxczxc", R.drawable.down_arrow));
        list1.add(new Chips("zxczxczxczxczxc", R.drawable.down_arrow));

        List<Actor> list2 = new ArrayList<>();
        list2.add(new Actor("QuentinQuentinQuentin", "Tarantino", R.drawable.me_gusta));
        list2.add(new Actor("Ashton", "Kutcher", R.drawable.me_gusta));
        list2.add(new Actor("Zack", "Snyder", R.drawable.down_arrow));
        list2.add(new Actor("Anthony", "Hopkins", R.drawable.down_arrow));

        initFirstView(suggestionsIdHolder, chipsIdHolder, invalidChipsIdHolder, list1);
        initSecondView(suggestionsIdHolder, chipsIdHolder, invalidChipsIdHolder, list2);
        initThirdView(suggestionsIdHolder, chipsIdHolder, invalidChipsIdHolder, list2);
    }

    private void initFirstView(ChipsIdHolder suggestionsIdHolder, ChipsIdHolder chipsIdHolder, ChipsIdHolder invalidChipsIdHolder, List<Chips> chips) {
        ChipsAdapter<Chips> adapter = new SimpleChipsAdapter(this, suggestionsIdHolder,
                chipsIdHolder, invalidChipsIdHolder, chips, chipsView1);
        chipsView1.setAdapter(adapter);
    }

    private void initSecondView(final ChipsIdHolder suggestionsIdHolder, final ChipsIdHolder chipsIdHolder, final ChipsIdHolder invalidChipsIdHolder, final List<Actor> actors) {
        findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Chips> chips;
                String title;
                try {
                    title = "All chips are valid";
                    chips = chipsView2.getChips();
                } catch (InvalidChipsException e) {
                    title = "There are invalid chips";
                    chips = e.getChips();
                }
                ListDialogFragment listDialog = ListDialogFragment.newInstance(chips, title);
                listDialog.show(getSupportFragmentManager(), ListDialogFragment.TAG);
            }
        });

        ChipsAdapter<Actor> adapter = new ChipsAdapter<Actor>(this, suggestionsIdHolder,
                chipsIdHolder, invalidChipsIdHolder, actors, chipsView2) {
            @Nullable
            @Override
            protected View getPopupView(ViewGroup parent, int position, Actor item) {
//                return getLayoutInflater().inflate(R.layout.popup_view, parent, false);
                return null;
            }

            @Override
            protected boolean hasPopup() {
                return false;
            }

            @Override
            public Chips instantiateNewChips(String text) {
                return new Actor(text, "", 0);
            }
        };

        adapter.setChipsValidator(new ChipsValidator<Actor>() {
            @Override
            public boolean isValid(Actor chips) {
                return !chips.getSurname().isEmpty();
            }
        });
        chipsView2.setAdapter(adapter);
    }

    private void initThirdView(ChipsIdHolder suggestionsIdHolder, ChipsIdHolder chipsIdHolder, ChipsIdHolder invalidChipsIdHolder, List<Actor> list2) {
        adapter3 = new ActorAdapter(this, suggestionsIdHolder,
                chipsIdHolder, invalidChipsIdHolder, list2, chipsView3);
        adapter3.setChipsValidator(new ChipsValidator<Actor>() {
            @Override
            public boolean isValid(Actor chips) {
                return !chips.getSurname().isEmpty();
            }
        });
        chipsView3.setAdapter(adapter3);
    }


    public void onDataChanged(int position, Actor actor) {
        adapter3.updateItem(position, actor);
    }

    public class ActorAdapter extends ChipsAdapter<Actor> {

        public ActorAdapter(Context context,
                            ChipsIdHolder suggestionIdHolder, ChipsIdHolder chipsIdHolder,
                            ChipsIdHolder invalidChipsIdHolder,
                            @Nullable List<Actor> suggestions, ChipsView chipsView) {
            super(context, suggestionIdHolder, chipsIdHolder, invalidChipsIdHolder, suggestions, chipsView);
        }

        @Nullable
        @Override
        protected View getPopupView(ViewGroup parent, final int position, final Actor item) {

            View view = getLayoutInflater().inflate(R.layout.popup_view, parent, false);
            ((TextView) view.findViewById(R.id.popup_text1)).setText(item.getName());
            ((TextView) view.findViewById(R.id.popup_text2)).setText(item.getSurname());
            if (item.getDrawableId() > 0) {
                ((ImageView) view.findViewById(R.id.popup_image)).setImageResource(item.getDrawableId());
            }
            view.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditDialogFragment dialogFragment = EditDialogFragment.newInstance(position, item);
                    dialogFragment.show(getSupportFragmentManager(), EditDialogFragment.TAG);
                }
            });
            return view;
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
}
