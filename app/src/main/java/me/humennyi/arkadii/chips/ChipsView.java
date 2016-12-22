package me.humennyi.arkadii.chips;

/**
 * Created by arkadii on 12/20/16.
 */


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;

import me.humennyi.arkadii.chips.adapter.ChipsAdapter;

public class ChipsView extends MultiAutoCompleteTextView implements OnItemClickListener {
    public static final int NO_NEW_CHIPS = -1;
    public static final String SUPER_STATE = "superState";
    public static final String CHIPS = "chips";
    private final ArrayList<Chips> chips = new ArrayList<>();

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
            String source = s.toString();
            Log.e("ChipsView", "source = \'" + source + "\' before = " + before + " " + count);
            if (before == 0 && count > 0) {
                if (source.endsWith(". ") || source.endsWith("  ")) {
                    int lastIndexOfComma = source.lastIndexOf(",");
                    String trim = source.substring(lastIndexOfComma + 1, start).trim();
                    if (!trim.isEmpty()) {
                        Log.e("ChipsView", "trim = \'" + trim + "\'");
                        String newSource = source.substring(0, lastIndexOfComma + 1) + " " + trim + ", ";
                        setChips(newSource, NO_NEW_CHIPS, trim);
                    }
                } else if (source.endsWith(",")) {
                    int lastIndexOfComma = source.substring(0, start).lastIndexOf(",");
                    String trim = source.substring(lastIndexOfComma + 1, start).trim();
                    if (!trim.isEmpty()) {
                        Log.e("ChipsView", "trim = \'" + trim + "\'");
                        String newSource = source.substring(0, lastIndexOfComma + 1) + " " + trim + ", ";
                        setChips(newSource, NO_NEW_CHIPS, trim);
                    } else {
                        String newSource = source.substring(0, lastIndexOfComma + 1) + " ";
                        setChips(newSource, NO_NEW_CHIPS, null);
                    }
                }
            }
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
                        setChips(firstPart + lastPart, chips.size() - 1, null);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void setChips(CharSequence source, int position, @Nullable String newChips) {
        String text = source.toString();
        while (text.contains("  ")) {
            text = text.replace("  ", " ");
        }
        Log.e("setChips", text);
        if (text.contains(",")) {

            final SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            String chipsNames[] = text.split(", ");
            Log.e("ChipsView", position + " " + chips);
            ChipsAdapter adapter = (ChipsAdapter) getAdapter();
            if (chips.size() < chipsNames.length) {
                if (position != NO_NEW_CHIPS) {
                    chips.add(adapter.getItem(position).copyValid());
                } else if (newChips != null){
                    chips.add(new Chips(newChips, 0, false));
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
        setChips(getText(), position, null);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelection(this.length());
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE, super.onSaveInstanceState());
        bundle.putParcelableArrayList(CHIPS, chips);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            restoreChipsList(bundle);
            state = bundle.getParcelable(SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    private void restoreChipsList(Bundle bundle) {
        ArrayList<Parcelable> chipsList = bundle.getParcelableArrayList(CHIPS);
        this.chips.clear();
        if (chipsList != null) {
            for (Parcelable chips : chipsList) {
                this.chips.add((Chips) chips);
            }
        }
    }
}
