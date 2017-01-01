package com.example.chipsdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareChipsView1();
//        prepareChipsView2();
//        prepareChipsView3();
    }

    private void prepareChipsView1() {
        chipsView1 = (ChipsView) this.findViewById(R.id.chipsView1);
        chipsView2 = (ChipsView) this.findViewById(R.id.chipsView2);
//        chipsView3 = (ChipsView) this.findViewById(R.id.chipsView3);
        ChipsIdHolder suggestionsIdHolder = new ChipsIdHolder(android.R.layout.simple_list_item_1, android.R.id.text1, 0);
        ChipsIdHolder chipsIdHolder = new ChipsIdHolder(R.layout.chips1, R.id.tvText, R.id.ivIcon);
        ChipsIdHolder invalidChipsIdHolder = new ChipsIdHolder(R.layout.chips_invalid1, R.id.tvText, 0);

        //Initialize first case
        List<Chips> list1 = new ArrayList<>();
        list1.add(new Chips("qweqwe", R.drawable.me_gusta));
        list1.add(new Chips("asdasdasd", R.drawable.me_gusta));
        list1.add(new Chips("zxczxczxczxc", R.drawable.down_arrow));
        list1.add(new Chips("zxczxczxczxczxc", R.drawable.down_arrow));
        ChipsAdapter<Chips> adapter1 = new SimpleChipsAdapter(this, suggestionsIdHolder,
                chipsIdHolder, invalidChipsIdHolder, list1, chipsView1);
        chipsView1.setAdapter(adapter1);
        //Initialize second case
        List<ChipsSubclass> list2 = new ArrayList<>();
        list2.add(new ChipsSubclass("qweqwe", R.drawable.me_gusta, 5));
        list2.add(new ChipsSubclass("asdasd", R.drawable.me_gusta, 3));
        list2.add(new ChipsSubclass("zxczxczxczxc", R.drawable.down_arrow, 2));
        list2.add(new ChipsSubclass("zxczxczxczxczxc", R.drawable.down_arrow, 10));
        ChipsAdapter<ChipsSubclass> adapter2 = new ChipsAdapter<ChipsSubclass>(this, suggestionsIdHolder,
                chipsIdHolder, invalidChipsIdHolder, list2, chipsView2) {
            @Nullable
            @Override
            protected View getPopupView(ViewGroup parent, ChipsSubclass item) {
                return null;
            }

            @Override
            protected boolean hasPopup() {
                return false;
            }

            @Override
            public Chips instantiateNewChips(String text) {
                return new ChipsSubclass(text, 0, 0);
            }
        };

        adapter2.setChipsValidator(new ChipsValidator<ChipsSubclass>() {
            @Override
            public boolean isValid(ChipsSubclass chips) {
                return chips.getSomeField() <= 3;
            }
        });
        chipsView2.setAdapter(adapter2);
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
                ListDialog listDialog = ListDialog.newInstance(chips, title);
                listDialog.show(getSupportFragmentManager(), ListDialog.TAG);
            }
        });

//        adapter.setChipsClickListener(new ChipsAdapter.OnChipsClickListener<Chips>() {
//            @Override
//            public void onChipsClick(int position, Chips chips) {
//                Log.e("MainActivity", position + " " + chips);
//            }
//        });
//        adapter.setChipsValidator(new ChipsValidator<Chips>() {
//            @Override
//            public boolean isValid(Chips chips) {
//                return chips.getText().length() == 5;
//            }
//        });

    }
}
