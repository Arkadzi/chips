package com.example.chipsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsView;
import me.humennyi.arkadii.chips.adapter.ChipsAdapter;
import me.humennyi.arkadii.chips.adapter.ChipsIdHolder;

public class MainActivity extends AppCompatActivity {

    private ChipsView chipsView1;
    private ChipsAdapter adapter;
    private ListPopupWindow listPopupWindow;

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

        adapter = new ChipsAdapter(this, new MyPopupCreator(), suggestionsIdHolder,
                chipsIdHolder, invalidChipsIdHolder, list, chipsView1);
        chipsView1.setAdapter(adapter);
        listPopupWindow = new ListPopupWindow(new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme));
        List<String> list1 = new ArrayList<>();
        list1.add("asd");
        listPopupWindow.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, list1));
        listPopupWindow.setModal(false);

        listPopupWindow.setAnchorView(chipsView1);
        findViewById(R.id.bShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity", "show popup");
                listPopupWindow.show();
            }
        });
        findViewById(R.id.bHide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity", "hide");
                listPopupWindow.dismiss();
            }
        });
    }



    @Override
    public void onDetachedFromWindow() {
        if (listPopupWindow != null) {
            Log.e("MainActivity", "dismiss");

            listPopupWindow.dismiss();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }
}
