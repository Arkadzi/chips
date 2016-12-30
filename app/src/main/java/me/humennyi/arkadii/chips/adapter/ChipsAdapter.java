package me.humennyi.arkadii.chips.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;
import me.humennyi.arkadii.chips.ChipsView;
import me.humennyi.arkadii.chips.OnSpanClickListener;

/**
 * Created by arkadii on 12/21/16.
 */

public class ChipsAdapter extends BaseAdapter implements ChipsHandler, Filterable {
    private final List<Chips> suggestionData = new ArrayList<>();
    private final List<Chips> currentSuggestions = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final Context context;
    private ChipsIdHolder suggestionIdHolder;
    private ChipsIdHolder chipsIdHolder;
    private ChipsIdHolder invalidChipsIdHolder;
    @Nullable
    private PopupAdapter popupAdapter;
    @Nullable
    private ListPopupWindow listPopupWindow;
    @Nullable
    private OnChipsClickListener chipsClickListener;

    public ChipsAdapter(Context context, @Nullable PopupCreator popupCreator,
                        ChipsIdHolder suggestionIdHolder, ChipsIdHolder chipsIdHolder,
                        ChipsIdHolder invalidChipsIdHolder,
                        @Nullable List<Chips> suggestions, ChipsView chipsView) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.suggestionIdHolder = suggestionIdHolder;
        this.chipsIdHolder = chipsIdHolder;
        this.invalidChipsIdHolder = invalidChipsIdHolder;
        if (popupCreator != null) {
            listPopupWindow = new ListPopupWindow(context);
            popupAdapter = new PopupAdapter(popupCreator, layoutInflater, listPopupWindow);
            listPopupWindow.setAdapter(popupAdapter);
            listPopupWindow.setModal(false);
            listPopupWindow.setAnchorView(chipsView);

        }
        setSuggestionData(suggestions);
    }

    public void setSuggestionData(@Nullable List<Chips> chips) {
        this.suggestionData.clear();
        if (chips != null) {
            this.suggestionData.addAll(chips);
        }
    }

    public boolean hidePopup() {
        if (listPopupWindow != null && listPopupWindow.isShowing()) {
            listPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return currentSuggestions.size();
    }

    @Override
    public Chips getItem(int position) {
        return currentSuggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ChipsViewHolder viewHolder;
        if (view != null) {
            viewHolder = ((ChipsViewHolder) view.getTag());
        } else {
            viewHolder = ChipsViewHolder.inflate(layoutInflater, suggestionIdHolder, parent);
            view = viewHolder.getView();
        }

        viewHolder.bind(getItem(position));

        return view;
    }

    @Override
    public Drawable getChipsDrawable(Chips chips) {
        ChipsIdHolder idHolder = chips.isValid() ? chipsIdHolder : invalidChipsIdHolder;
        return ChipsUtils.generateDrawable(context, layoutInflater, chips, idHolder);
    }


    @Override
    public OnSpanClickListener getChipsClickListener(final int chipsPosition, final Chips chips) {
        return new OnSpanClickListener() {
            @Override
            public void onClick(ChipsView widget, int xOffset, int yOffset) {
                showPopup(chips, xOffset, yOffset);
                if (chipsClickListener != null) {
                    chipsClickListener.onChipsClick(chipsPosition, chips);
                }
            }
        };
    }

    @Nullable
    @Override
    public Chips getChipsByText(String text) {
        for (Chips chips : suggestionData) {
            if (chips.getText().equalsIgnoreCase(text)) {
                return chips;
            }
        }
        return null;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Chips) resultValue).getText();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                currentSuggestions.clear();
                try {
                    for (Chips chips : suggestionData) {
                        if (chips.getText().toLowerCase().startsWith(
                                constraint.toString().toLowerCase())) {
                            currentSuggestions.add(chips);
                        }
                    }
                } catch (Exception ignored) {
                }
                filterResults.values = currentSuggestions;
                filterResults.count = currentSuggestions.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    private void showPopup(Chips chips, int xOffset, int yOffset) {
        if (popupAdapter != null && listPopupWindow != null) {
            popupAdapter.setChips(chips);
            listPopupWindow.setHorizontalOffset(xOffset);
            listPopupWindow.setVerticalOffset(yOffset);
            listPopupWindow.show();
        }
    }

    public void setChipsClickListener(@Nullable OnChipsClickListener chipsClickListener) {
        this.chipsClickListener = chipsClickListener;
    }

    public interface OnChipsClickListener {
        void onChipsClick(int position, Chips chips);
    }

    public interface PopupCreator {
        View getPopupView(LayoutInflater inflater, ViewGroup parent, Chips chips);
    }

    private static class PopupAdapter extends BaseAdapter {
        private final List<Chips> singleChips = new ArrayList<>();
        private final PopupCreator popupCreator;
        private final LayoutInflater inflater;
        private ListPopupWindow listPopupWindow;

        PopupAdapter(PopupCreator popupCreator, LayoutInflater inflater, ListPopupWindow listPopupWindow) {
            this.popupCreator = popupCreator;
            this.inflater = inflater;
            this.listPopupWindow = listPopupWindow;
        }

        @Override
        public int getCount() {
            return singleChips.size();
        }

        @Override
        public Chips getItem(int position) {
            return singleChips.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View popupView = popupCreator.getPopupView(inflater, parent, this.getItem(position));
            listPopupWindow.setContentWidth(ChipsUtils.measureContentWidth(popupView));
            return popupView;
        }

        public final void setChips(Chips chips) {
            this.singleChips.clear();
            this.singleChips.add(chips);
        }
    }
}
