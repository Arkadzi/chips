package me.humennyi.arkadii.chips;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.adapter.ChipsAdapter;
import me.humennyi.arkadii.chips.adapter.ChipsIdHolder;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] words = new String[]{
                "qweqwe", "asdasdasd", "zxczxczxczxc", "zxczxczxczxczxczxczxc"
        };
        imageView = (ImageView) findViewById(R.id.image_view);
        MultiAutoCompleteTextView macTv = (MultiAutoCompleteTextView) this.findViewById(R.id.mac_tv);
//        ArrayAdapter<String> aaStr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, words);
        ChipsIdHolder suggestionsIdHolder = new ChipsIdHolder(android.R.layout.simple_list_item_1, android.R.id.text1, 0);
        ChipsIdHolder chipsIdHolder = new ChipsIdHolder(R.layout.chips, R.id.tvText, R.id.ivIcon);
        ChipsIdHolder invalidChipsIdHolder = new ChipsIdHolder(R.layout.chips_invalid, R.id.tvText, 0);
        List<Chips> list = new ArrayList<>();
        list.add(new Chips("qweqwe", R.mipmap.ic_launcher));
        list.add(new Chips("asdasdasd", R.mipmap.ic_launcher));
        list.add(new Chips("zxczxczxczxc", R.mipmap.ic_launcher));
        list.add(new Chips("zxczxczxczxczxc", R.mipmap.ic_launcher));
        macTv.setAdapter(new ChipsAdapter(this, suggestionsIdHolder, chipsIdHolder, invalidChipsIdHolder, list));
        editText = (EditText) findViewById(R.id.editText);
    }


}
