package com.example.chipsdemo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.humennyi.arkadii.chips.Chips;

/**
 * Created by arkadii on 1/1/17.
 */

public class ListDialogFragment extends DialogFragment {
    public static final String TAG = "list_dialog";
    private static final String CHIPS = "chips";
    private static final String TITLE = "title";

    public static ListDialogFragment newInstance(List<Chips> chips, String title) {
        ArrayList<Chips> list = new ArrayList<>();
        list.addAll(chips);

        Bundle args = new Bundle();
        args.putSerializable(CHIPS, list);
        args.putString(TITLE, title);

        ListDialogFragment fragment = new ListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<Chips> list = (ArrayList<Chips>) getArguments().getSerializable(CHIPS);
        String title = getArguments().getString(TITLE);
        return new AlertDialog.Builder(getActivity())
                .setAdapter(new ArrayAdapter<Chips>(getActivity(), android.R.layout.simple_list_item_1, list), null)
                .setTitle(title)
                .create();
    }
}
