package com.example.chipsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsView;
import me.humennyi.arkadii.chips.adapter.ChipsAdapter;
import me.humennyi.arkadii.chips.adapter.ChipsIdHolder;

public class MainActivity extends AppCompatActivity {

    private ChipsView chipsView1;
    private ChipsAdapter adapter;

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

        ChipsAdapter.PopupCreator popupCreator = new ChipsAdapter.PopupCreator() {
            @Override
            public View getPopupView(LayoutInflater inflater, ViewGroup parent, final Chips chips) {
                final View view = inflater.inflate(R.layout.popup_view, parent, false);
                View.OnClickListener onClickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CharSequence text = ((Button) v).getText();
                        Toast.makeText(MainActivity.this, chips.getText() + " " + text, Toast.LENGTH_SHORT).show();
                    }
                };
                view.findViewById(R.id.button1).setOnClickListener(onClickListener);
                view.findViewById(R.id.button2).setOnClickListener(onClickListener);
                view.findViewById(R.id.button3).setOnClickListener(onClickListener);
                ((TextView) view.findViewById(R.id.popup_text)).setText(chips.getText());
                if (chips.getDrawableId() > 0) {
                    ((ImageView) view.findViewById(R.id.popup_image)).setImageResource(chips.getDrawableId());
                }
                return view;
            }
        };
        adapter = new ChipsAdapter(this, popupCreator, suggestionsIdHolder,
                chipsIdHolder, invalidChipsIdHolder, list);
        chipsView1.setAdapter(adapter);
    }
}
