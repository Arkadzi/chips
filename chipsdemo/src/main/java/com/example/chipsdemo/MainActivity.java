package com.example.chipsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsView;
import me.humennyi.arkadii.chips.adapter.ChipsAdapter;
import me.humennyi.arkadii.chips.adapter.ChipsIdHolder;

public class MainActivity extends AppCompatActivity {

    private ChipsView chipsView1;
//    private ChipsView chipsView2;
//    private ChipsView chipsView3;

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
        ChipsIdHolder suggestionsIdHolder = new ChipsIdHolder(android.R.layout.simple_list_item_1, android.R.id.text1, 0);
        ChipsIdHolder chipsIdHolder = new ChipsIdHolder(R.layout.chips1, R.id.tvText, R.id.ivIcon);
        ChipsIdHolder invalidChipsIdHolder = new ChipsIdHolder(R.layout.chips_invalid1, R.id.tvText, 0);
        List<Chips> list = new ArrayList<>();
        list.add(new Chips("qweqwe", R.drawable.me_gusta));
        list.add(new Chips("asdasdasd", R.drawable.me_gusta));
        list.add(new Chips("zxczxczxczxc", R.drawable.down_arrow));
        list.add(new Chips("zxczxczxczxczxc", R.drawable.down_arrow));

        ChipsAdapter adapter = new ChipsAdapter(this, suggestionsIdHolder, chipsIdHolder, invalidChipsIdHolder, list);
        adapter.setChipsClickListener(new ChipsAdapter.OnChipsClickListener() {
            @Override
            public void onChipsClick(int position) {
                String chipsText = chipsView1.getChips(position).getText();
                String text = position + " " + chipsText;
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        chipsView1.setAdapter(adapter);
    }
//
//    private void prepareChipsView2() {
//        chipsView2 = (ChipsView) this.findViewById(R.id.chipsView2);
//        ChipsIdHolder suggestionsIdHolder = new ChipsIdHolder(android.R.layout.simple_list_item_1, android.R.id.text1, 0);
//        ChipsIdHolder chipsIdHolder = new ChipsIdHolder(R.layout.chips2, R.id.tvText, R.id.ivIcon);
//        ChipsIdHolder invalidChipsIdHolder = new ChipsIdHolder(R.layout.chips_invalid2, R.id.tvText, 0);
//        List<Chips> list = new ArrayList<>();
//        list.add(new Chips("qweqwe", R.drawable.me_gusta));
//        list.add(new Chips("asdasdasd", R.drawable.me_gusta));
//        list.add(new Chips("zxczxczxczxc", R.drawable.me_gusta));
//        list.add(new Chips("zxczxczxczxczxc", R.drawable.me_gusta));
//
//        ChipsAdapter adapter = new ChipsAdapter(this, suggestionsIdHolder, chipsIdHolder, invalidChipsIdHolder, list);
//        adapter.setChipsClickListener(new ChipsAdapter.OnChipsClickListener() {
//            @Override
//            public void onChipsClick(int position) {
//                String chipsText = chipsView2.getChips(position).getText();
//                String text = position + " " + chipsText;
//                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
//        chipsView2.setAdapter(adapter);
//    }
//
//    private void prepareChipsView3() {
//        chipsView3 = (ChipsView) this.findViewById(R.id.chipsView3);
//        ChipsIdHolder suggestionsIdHolder = new ChipsIdHolder(android.R.layout.simple_list_item_1, android.R.id.text1, 0);
//        ChipsIdHolder chipsIdHolder = new ChipsIdHolder(R.layout.chips3, R.id.tvText, 0);
//        ChipsIdHolder invalidChipsIdHolder = new ChipsIdHolder(R.layout.chips_invalid3, R.id.tvText, 0);
//        List<Chips> list = new ArrayList<>();
//        list.add(new Chips("qweqwe", R.drawable.me_gusta));
//        list.add(new Chips("asdasdasd", R.drawable.me_gusta));
//        list.add(new Chips("zxczxczxczxc", R.drawable.me_gusta));
//        list.add(new Chips("zxczxczxczxczxc", R.drawable.me_gusta));
//
//        ChipsAdapter adapter = new ChipsAdapter(this, suggestionsIdHolder, chipsIdHolder, invalidChipsIdHolder, list);
//        adapter.setChipsClickListener(new ChipsAdapter.OnChipsClickListener() {
//            @Override
//            public void onChipsClick(int position) {
//                String chipsText = chipsView3.getChips(position).getText();
//                String text = position + " " + chipsText;
//                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
//        chipsView3.setAdapter(adapter);
//    }

}
