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
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

public class ChipsView extends MultiAutoCompleteTextView implements OnItemClickListener {

    private final String TAG = "ChipsMultiAutoCompleteTextview";

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

    public void init(Context context){
        setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        setOnItemClickListener(this);
        addTextChangedListener(textWather);
    }

    private TextWatcher textWather = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(count >=1){
                if(s.charAt(start) == ',')
                    setChips(); // generate chips
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };
    /*This function has whole logic for chips generate*/
    public void setChips(){
        if(getText().toString().contains(",")) // check comman in string
        {

            SpannableStringBuilder ssb = new SpannableStringBuilder(getText());
            // split string wich comma
            String chips[] = getText().toString().trim().split(",");
            LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            int x =0;
            // loop will generate ImageSpan for every country name separated by comma
            for(String c : chips){
                ViewGroup chipsView = (ViewGroup) lf.inflate(R.layout.chips, null);
                ((TextView) chipsView.getChildAt(0)).setText(c);
                Bitmap bitmap = convertViewToBitmap(chipsView);
                SpannableString ss = new SpannableString(c);
                BitmapDrawable bmpDrawable = new BitmapDrawable(getResources(), bitmap);
                bmpDrawable.setBounds(0, 0,bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(bmpDrawable, ImageSpan.ALIGN_BASELINE);
                ssb.setSpan(new ImageSpan(bmpDrawable),x ,x + c.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                x = x+ c.length() +1;
            }
            setText(ssb);
            setSelection(getText().length());
        }
    }


    private Bitmap convertViewToBitmap(View v) {
        if (v.getMeasuredHeight() <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
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
//        ChipsItem ci = (ChipsItem) getAdapter().getItem(position);

        setChips(); // call generate chips when user select any item from auto complete
    }




}
