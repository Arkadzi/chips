package me.humennyi.arkadii.chips;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

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
        ArrayAdapter<String> aaStr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, words);
        macTv.setAdapter(aaStr);
        Log.e("Span", "click " + macTv);

        editText = (EditText) findViewById(R.id.editText);

    }
}
