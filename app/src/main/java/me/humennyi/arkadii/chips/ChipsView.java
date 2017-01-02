package me.humennyi.arkadii.chips;

/**
 * Created by arkadii on 12/20/16.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.adapter.ChipsAdapter;
import me.humennyi.arkadii.chips.exception.InvalidChipsException;

public class ChipsView extends AppCompatMultiAutoCompleteTextView implements OnItemClickListener {
    public static final String SUPER_STATE = "superState";
    public static final String CHIPS = "chips";
    public static final String SPAN_SEPARATOR = ", ";
    private String separatorRegex;
    private String patternRegex = ".+";
    private final ArrayList<Chips> chips = new ArrayList<>();
    private boolean shouldRedraw;
    private ChipsValidator<Chips> textValidator = new ChipsValidator<Chips>() {
        @Override
        public boolean isValid(Chips chips) {
            return chips.getText().matches(patternRegex);
        }
    };
    private TextWatcher textWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getAdapter().hidePopup();
            String source = s.toString();
            if (count - before > 0) {
                setChips(source);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            String source = s.toString();
            String rep = source.replaceAll(separatorRegex, ",");
            if (count == 1 && after == 0 && source.endsWith(SPAN_SEPARATOR)
                    && !(rep.endsWith(",,") || rep.equals(","))) {
                source = removeChips(source, chips.size() - 1);
                shouldRedraw = true;
                setChips(source);
            }
        }
    };

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
        Log.e("Widget", "constructor " + String.valueOf(hashCode()));

        TypedArray array = null;
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.ChipsView);
            fetchAttributes(array);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
        setOnItemClickListener(this);
        addTextChangedListener(textWatcher);
        setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        setMovementMethod(LinkMovementMethod.getInstance());
//        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    }

    private void fetchAttributes(TypedArray array) {
        final int id = array.getResourceId(R.styleable.ChipsView_separators, 0);

        List<String> separators = new ArrayList<>();
        separators.add(",");
        if (id != 0) {
            final String[] values = getResources().getStringArray(id);
            for (String value : values) {
                if (!value.contains(",")) {
                    separators.add(value);
                }
            }
        }
        separatorRegex = "(" + TextUtils.join("|", separators) + ")+[ |\n]*";

        String pattern = array.getString(R.styleable.ChipsView_pattern);
        if (pattern != null) {
            patternRegex = pattern;
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            boolean isPopupHidden = getAdapter().hidePopup();
            if (isPopupHidden) return true;
        }
        return super.dispatchKeyEvent(event);
    }


    private String removeChips(String source, int position) {
        chips.remove(position);
        String[] split = source.split(SPAN_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i != position) {
                builder.append(split[i]).append(SPAN_SEPARATOR);
            }
        }
        return builder.toString();
    }

    private void setChips(String source) {
        if (!source.isEmpty()) {
            source = prepareSource(source);
            if (source.isEmpty()) return;

            String[] chipsText = source.split(SPAN_SEPARATOR);
            int chipsCount = chipsText.length;
            if (!source.endsWith(SPAN_SEPARATOR)) {
                chipsCount--;
            }
            ChipsAdapter adapter = getAdapter();
            for (int i = chips.size(); i < chipsCount; i++) {
                Chips chips = adapter.getChipsByText(chipsText[i]);
                if (chips == null) {
                    chips = adapter.instantiateNewChips(chipsText[i]);
                } else {
                    chips = chips.copy();
                }
                this.chips.add(chips);
                shouldRedraw = true;
            }

            if (shouldRedraw) {
                SpannableStringBuilder text = prepareNewSpannableText(source, chipsText, chipsCount);
                setText(text);
                shouldRedraw = false;
                setSelection(this.length());

            }
        } else {
            shouldRedraw = false;
            setText("");
        }
    }

    private String prepareSource(String source) {

        while (source.contains("\n")) {
            source = source.replace("\n", " ");
        }
        while (source.contains("  ")) {
            source = source.replace("  ", " ");
        }
        source = source.replaceAll(separatorRegex, SPAN_SEPARATOR);
        String doubleSpanSeparator = SPAN_SEPARATOR + SPAN_SEPARATOR;

        while (source.contains(doubleSpanSeparator)) {
            source = source.replace(doubleSpanSeparator, SPAN_SEPARATOR);
        }
        if (source.startsWith(" ")) source = source.substring(1);
        if (source.startsWith(SPAN_SEPARATOR)) source = source.substring(SPAN_SEPARATOR.length());
        return source;
    }

    @Nullable
    private SpannableStringBuilder prepareNewSpannableText(String text, String[] chipsText,
                                                           int chipsCount) {
        ChipsAdapter adapter = getAdapter();

        int spanStart = 0;
        final SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        for (int i = 0; i < chipsCount; i++) {
            String chipsName = chipsText[i];
            Chips chips = getChips(i);
            boolean chipsValid = textValidator.isValid(chips) && adapter.isChipsValid(chips);
            Drawable chipsDrawable = adapter.getChipsDrawable(chips, chipsValid);
            OnSpanClickListener chipsClickListener = adapter.getChipsClickListener(i, chips);
            ClickableImageSpanWrapper.wrap(ssb, chipsDrawable, chipsClickListener,
                    spanStart, spanStart + chipsName.length() + 1);
            spanStart = spanStart + chipsName.length() + 2;
        }
        return ssb;
    }


    @Override
    public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
        if (adapter != null && !(adapter instanceof ChipsAdapter))
            throw new IllegalArgumentException("Adapter should be instance of ChipsAdapter");
        super.setAdapter(adapter);
    }

    @Override
    public ChipsAdapter getAdapter() {
        return (ChipsAdapter) super.getAdapter();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        int length = length();
        if (length != selStart || length != selEnd) {
            setSelection(this.length());
        }
    }

    public List<Chips> getChips() throws InvalidChipsException {
        List<Chips> result = new ArrayList<>();
        List<Pair<Integer, Chips>> invalidChips = new ArrayList<>();

        String s = prepareSource(getText().toString());
        String[] split = s.split(SPAN_SEPARATOR);
        int length = split.length;
        ChipsAdapter adapter = getAdapter();
        for (int i = 0; i < length; i++) {
            Chips chips = null;
            if (i < this.chips.size()) {
                chips = this.chips.get(i).copy();
            } else if (!split[i].isEmpty()) {
                chips = adapter.instantiateNewChips(split[i]);
            }
            if (chips != null) {
                if (!textValidator.isValid(chips) || !adapter.isChipsValid(chips)) {
                    invalidChips.add(new Pair<>(i, chips));
                } else {
                    result.add(chips);
                }
            }
        }
        if (!invalidChips.isEmpty()) {
            throw new InvalidChipsException("There are invalid chips", invalidChips);
        }
        return result;
    }

    @Override
    protected void onDetachedFromWindow() {
        removeClickableSpanListeners();
        super.onDetachedFromWindow();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE, super.onSaveInstanceState());
        bundle.putSerializable(CHIPS, chips);

        return bundle;
    }

    private void removeClickableSpanListeners() {
        Editable text = getText();
        ChipsAdapter adapter = getAdapter();
        if (text instanceof SpannableStringBuilder && adapter != null) {
            ClickableSpanImpl[] spans =
                    text.getSpans(0, text.length(), ClickableSpanImpl.class);
            for (ClickableSpanImpl span : spans) {
                span.setOnClickListener(null);
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            restoreChipsList(bundle);
            state = bundle.getParcelable(SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
        setClickableSpanListeners();
    }

    private void setClickableSpanListeners() {
        Editable text = getText();
        ChipsAdapter adapter = getAdapter();
        if (text instanceof SpannableStringBuilder && adapter != null) {
            ClickableSpanImpl[] spans = text.getSpans(0, text.length(), ClickableSpanImpl.class);
            int position = 0;
            for (ClickableSpanImpl span : spans) {
                Chips chips = getChips(position);
                span.setOnClickListener(adapter.getChipsClickListener(position, chips));
                position++;
            }
        }
    }

    private void restoreChipsList(Bundle bundle) {
        List<Serializable> chipsList = (List<Serializable>) bundle.getSerializable(CHIPS);
        this.chips.clear();
        if (chipsList != null) {
            for (Serializable chips : chipsList) {
                this.chips.add((Chips) chips);
            }
        }
    }

    public Chips getChips(int position) {
        return chips.get(position);
    }
}
