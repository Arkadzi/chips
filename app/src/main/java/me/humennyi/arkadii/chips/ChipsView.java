package me.humennyi.arkadii.chips;

/**
 * Created by arkadii on 12/20/16.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.adapter.ChipsAdapter;

public class ChipsView extends MultiAutoCompleteTextView implements OnItemClickListener {
    public static final int NO_NEW_CHIPS = -1;
    private final List<Chips> chips = new ArrayList<>();

    public ChipsView(Context context) {
        super(context);
        init(context);
    }

    public ChipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChipsView(Context context, AttributeSet attrs,
                     int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        setOnItemClickListener(this);
        addTextChangedListener(textWatcher);
        setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.e("TextWatcher", String.format("before %s %d", s, start));

            if (count > 0 && after == 0 && start > 0) {
                String source = s.toString();
                if (start < source.length() && source.charAt(start) == ',') {
                    start++;
                }
                if (source.charAt(start - 1) == ',') {
                    int rightBorder = source.substring(0, start).lastIndexOf(',');
                    if (rightBorder > -1) {
                        String substring = source.subSequence(0, rightBorder).toString();
                        int leftBorder = substring.lastIndexOf(',');
                        String firstPart = source.substring(0, leftBorder + 1) + " ";
                        String lastPart = source.substring(rightBorder + 1);
                        Log.e("TextWatcher", String.format("\'%s\' \'%s\'", firstPart, lastPart));
                        setChips(firstPart + lastPart, chips.size() - 1);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void setChips(CharSequence source, int position) {
        String text = source.toString();
        while (text.contains("  ")) {
            text = text.replace("  ", " ");
        }
        Log.e("ChipsView", "'" + text + "'");
        if (text.contains(",")) {

            final SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            String chipsNames[] = text.split(", ");

            ChipsAdapter adapter = (ChipsAdapter) getAdapter();
            if (chips.size() < chipsNames.length) {
                if (position != NO_NEW_CHIPS) {
                    chips.add(adapter.getItem(position).copyValid());
                }
            } else if (chips.size() > chipsNames.length) {
                chips.remove(position);
            }
            int spanStart = 0;
            for (int i = 0; i < chipsNames.length; i++) {
                String chipsName = chipsNames[i];
                Chips currentChips = this.chips.get(i);
                Drawable chipsDrawable = adapter.getChipsDrawable(currentChips);
                OnClickListener chipsClickListener = adapter.getChipsClickListener(currentChips);
                ClickableImageSpanWrapper.wrap(ssb, chipsDrawable, chipsClickListener, spanStart, spanStart + chipsName.length() + 1);
                spanStart = spanStart + chipsName.length() + 2;
            }
            setText(ssb);
            setSelection(getText().length());
        } else {
            setText("");
        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        setChips(getText(), position);
    }


    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelection(this.length());
    }

    static class ClickListenerImpl implements OnClickListener {
        private String spanText;
        private Context context;

        public ClickListenerImpl(Context context, String spanText) {
            this.context = context;
            this.spanText = spanText;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, spanText, Toast.LENGTH_SHORT).show();
        }
    }
}
