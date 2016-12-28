package com.example.chipsdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.adapter.ChipsAdapter;

/**
 * Created by arkadii on 12/28/16.
 */
class MyPopupCreator implements ChipsAdapter.PopupCreator {
//    private MainActivity mainActivity;

//    public MyPopupCreator(MainActivity mainActivity) {
//        this.mainActivity = mainActivity;
//    }

    @Override
    public View getPopupView(LayoutInflater inflater, ViewGroup parent, final Chips chips) {
        final View view = inflater.inflate(R.layout.popup_view, parent, false);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CharSequence text = ((Button) v).getText();
//                Toast.makeText(mainActivity, chips.getText() + " " + text, Toast.LENGTH_SHORT).show();
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
}
