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
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.adapter.ChipsAdapter;

public class ChipsView extends MultiAutoCompleteTextView implements OnItemClickListener {
    public static final String SUPER_STATE = "superState";
    public static final String CHIPS = "chips";
    public static final String SPAN_SEPARATOR = ", ";
    private String separatorRegex;
    private String patternRegex = ".+";
    private final ArrayList<Chips> chips = new ArrayList<>();
    private boolean shouldRedraw;
    private TextWatcher textWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String source = s.toString();
            if (count - before > 0) {
                setChips(source);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            String source = s.toString();
            if (count == 1 && after == 0 && source.endsWith(SPAN_SEPARATOR)) {
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
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    }

    private void fetchAttributes(TypedArray array) {
        final int id = array.getResourceId(R.styleable.ChipsView_separators, 0);

        if (id != 0) {
            final String[] values = getResources().getStringArray(id);
            List<String> separators = new ArrayList<>();
            for (String value : values) {
                if (!value.contains(",")) {
                    separators.add(value);
                }
            }
            separators.add(",");
            separatorRegex = "(" + TextUtils.join("|", separators) + ")+[ |\n]*";
        }

        String pattern = array.getString(R.styleable.ChipsView_pattern);
        if (pattern != null) {
            patternRegex = pattern;
        }
    }


    private String removeChips(String source, int position) {
        chips.remove(position);
        String[] split = source.split(SPAN_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i != position) {
                builder.append(split[i] + SPAN_SEPARATOR);
            }
        }
        return builder.toString();
    }

    private void setChips(String source) {
        source = prepareSource(source);
        if (!source.isEmpty()) {
            String[] chipsText = source.split(SPAN_SEPARATOR);
            int chipsCount = chipsText.length;
            if (!source.endsWith(SPAN_SEPARATOR)) {
                chipsCount--;
            }
            ChipsAdapter adapter = getAdapter();
            for (int i = chips.size(); i < chipsCount; i++) {
                Chips chips = adapter.getChipsByText(chipsText[i]);
                if (chips == null) {
                    chips = new Chips(chipsText[i], 0, isValidChipsText(chipsText[i]));
                } else {
                    chips = new Chips(chips.getText(), chips.getDrawableId(), isValidChipsText(chips.getText()));
                }
                this.chips.add(chips);
                shouldRedraw = true;
            }

            if (shouldRedraw) {
                SpannableStringBuilder text = this.prepareNewSpannableText(source, chipsText, chipsCount);
                setText(text);
                shouldRedraw = false;
            }
        } else {
            shouldRedraw = false;
            setText("");
        }
        setSelection(this.length());
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
        if (source.startsWith(SPAN_SEPARATOR)) source = source.substring(SPAN_SEPARATOR.length());
        return source;
    }

    @Nullable
    private SpannableStringBuilder prepareNewSpannableText(String text, String[] chipsText, int chipsCount) {
        ChipsAdapter adapter = getAdapter();

        int spanStart = 0;
        final SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        for (int i = 0; i < chipsCount; i++) {
            String chipsName = chipsText[i];
            Drawable chipsDrawable = adapter.getChipsDrawable(getChips(i));
            OnClickListener chipsClickListener = adapter.getChipsClickListener(i);
            ClickableImageSpanWrapper.wrap(ssb, chipsDrawable, chipsClickListener,
                    spanStart, spanStart + chipsName.length() + 1);
            spanStart = spanStart + chipsName.length() + 2;
        }
        return ssb;
    }

    private boolean isValidChipsText(String chipsName) {
        return chipsName.matches(patternRegex);
    }

    @Override
    public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
        if (adapter != null && !(adapter instanceof ChipsAdapter))
            throw new IllegalArgumentException("adapter should be instance of ChipsAdapter");
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
