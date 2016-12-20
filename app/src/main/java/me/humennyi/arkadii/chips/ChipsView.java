package me.humennyi.arkadii.chips;

/**
 * Created by arkadii on 12/20/16.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

public class ChipsView extends MultiAutoCompleteTextView implements OnItemClickListener {

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
                        setChips(firstPart + lastPart);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        Log.e("ChipsView", "append '" + text + "'");
    }

    public void setChips(CharSequence source) {
        String text = source.toString();
        while (text.contains("  ")) {
            text = text.replace("  ", " ");
        }
        Log.e("ChipsView", "'" + text + "'");
        if (text.contains(",")) {

            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            String chips[] = text.split(", ");
            LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            int x = 0;
            for (String c : chips) {

                ViewGroup chipsView = (ViewGroup) lf.inflate(R.layout.chips, null);
                ((TextView) chipsView.getChildAt(0)).setText(c);
                Bitmap bitmap = convertViewToBitmap(chipsView);
                BitmapDrawable bmpDrawable = new BitmapDrawable(getResources(), bitmap);
                bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
                ssb.setSpan(new ImageSpan(bmpDrawable), x, x + c.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                x = x + c.length() + 2;
            }
            setText(ssb);
            setSelection(getText().length());
        } else {
            setText("");
        }
    }


    private Bitmap convertViewToBitmap(View v) {
        if (v.getMeasuredHeight() <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v.measure(specWidth, specWidth);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setChips(getText());
    }


    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelection(this.length());
    }
}
