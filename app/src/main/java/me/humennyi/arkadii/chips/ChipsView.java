package me.humennyi.arkadii.chips;

/**
 * Created by arkadii on 12/20/16.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import java.util.List;

import me.humennyi.arkadii.chips.adapter.ChipsAdapter;

public class ChipsView extends MultiAutoCompleteTextView implements OnItemClickListener {
    public static final int IGNORED_POSITION = -1;
    public static final String SUPER_STATE = "superState";
    public static final String CHIPS = "chips";
    private final List<String> separators = new ArrayList<>();
    private final ArrayList<Chips> chips = new ArrayList<>();

    public ChipsView(Context context) {
        super(context);
        init(context, null);
    }

    public ChipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChipsView(Context context, AttributeSet attrs,
                     int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.ChipsView);
            final int id = array.getResourceId(R.styleable.ChipsView_separators, 0);

            if (id != 0) {
                final String[] values = getResources().getStringArray(id);
                for (String value : values) {
                    Log.e("separator", "\'" + value + "\'");
                    if (!value.contains(",")) {
                        separators.add(value);
                    }
                }
            }
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
        setOnItemClickListener(this);
        addTextChangedListener(textWatcher);
        setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String source = s.toString();
            if (count - before > 0) {
                if (source.endsWith(",")) {
                    setChipsAfterCommaInput(start, source);
                } else {
                    String separator = endsWithSeparator(source);
                    if (separator != null) {
                        setChipsAfterSeparatorInput(source.substring(0, source.length() - separator.length()));
                    }
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (count > 0 && after == 0) {
                removeLastChips(s, start);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String endsWithSeparator(String source) {
        for (String availableSeparator : separators) {
            if (source.endsWith(availableSeparator)) {
                return availableSeparator;
            }
        }
        return null;
    }

    private void removeLastChips(CharSequence s, int start) {
        if (start > 0) {
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
                    setChips(firstPart + lastPart, chips.size() - 1, null);
                }
            }
        }
    }

    private void setChipsAfterCommaInput(int start, String source) {
        int lastIndexOfComma = source.substring(0, start).lastIndexOf(",");
        String trim = source.substring(lastIndexOfComma + 1, start).trim().replace("\n", " ");

        if (!trim.isEmpty()) {
            String newSource = source.substring(0, lastIndexOfComma + 1) + " " + trim + ", ";
            setChips(newSource, IGNORED_POSITION, trim);
        } else {
            String newSource = source.substring(0, lastIndexOfComma + 1) + " ";
            setChips(newSource, IGNORED_POSITION, null);
        }
    }

    private void setChipsAfterSeparatorInput(String source) {
        int lastIndexOfComma = source.lastIndexOf(',');
        String newChipsText = source.substring(lastIndexOfComma + 1).trim().replace("\n", " ");
        if (!newChipsText.isEmpty()) {
            setChips(source + ", ", IGNORED_POSITION, newChipsText);
        }
    }

    private void setChips(CharSequence source, int editedPosition, @Nullable String newChips) {
        String text = source.toString().replace("\n", " ");
        text = trimInside(text);
        if (text.contains(",")) {
            String chipsNames[] = text.split(", ");
            ChipsAdapter adapter = (ChipsAdapter) getAdapter();
            syncChipsListWithSpannableText(editedPosition, newChips, chipsNames, adapter);
            final SpannableStringBuilder ssb = prepareSpannableText(text, chipsNames, adapter);
            setText(ssb);
            setSelection(getText().length());
        } else {
            setText("");
        }
    }

    @NonNull
    private String trimInside(String text) {
        while (text.contains("  ")) {
            text = text.replace("  ", " ");
        }
        return text;
    }

    @NonNull
    private SpannableStringBuilder prepareSpannableText(String text, String[] chipsNames,
                                                        ChipsAdapter adapter) {
        final SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        int spanStart = 0;
        for (int i = 0; i < chipsNames.length; i++) {
            String chipsName = chipsNames[i];
            Drawable chipsDrawable = adapter.getChipsDrawable(getChips(i));
            OnClickListener chipsClickListener = adapter.getChipsClickListener(i);
            ClickableImageSpanWrapper.wrap(ssb, chipsDrawable, chipsClickListener,
                    spanStart, spanStart + chipsName.length() + 1);
            spanStart = spanStart + chipsName.length() + 2;
        }
        return ssb;
    }

    private void syncChipsListWithSpannableText(int editedPosition, @Nullable String newChips,
                                                String[] chipsNames, ChipsAdapter adapter) {
        if (chips.size() < chipsNames.length) {
            if (editedPosition != IGNORED_POSITION) {
                chips.add(adapter.getItem(editedPosition).copyValid());
            } else if (newChips != null) {
                chips.add(new Chips(newChips, 0, false));
            }
        } else if (chips.size() > chipsNames.length && editedPosition != IGNORED_POSITION) {
            chips.remove(editedPosition);
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

    public Chips getChips(int position) {
        return chips.get(position);
    }
}
