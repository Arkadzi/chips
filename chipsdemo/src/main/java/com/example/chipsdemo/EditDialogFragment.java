package com.example.chipsdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by arkadii on 1/2/17.
 */

public class EditDialogFragment extends DialogFragment {

    public static final String TAG = "edit_dialog";
    private static final String ACTOR = "actor";
    private static final String POSITION = "position";
    private EditText textName;
    private EditText textSurname;
    private Actor actor;
    private int position;

    public static EditDialogFragment newInstance(int position, Actor actor) {
        Bundle args = new Bundle();
        args.putSerializable(ACTOR, actor);
        args.putInt(POSITION, position);
        EditDialogFragment fragment = new EditDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        actor = (Actor) getArguments().getSerializable(ACTOR);
        position = getArguments().getInt(POSITION);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit, null);
        textName = ((EditText) view.findViewById(R.id.text_name));
        textSurname = ((EditText) view.findViewById(R.id.text_surname));
        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = textName.getText().toString();
                        String surname = textSurname.getText().toString();
                        actor.setData(name, surname);
                        ((MainActivity) getActivity()).onDataChanged(position, actor);
                    }
                }).setNegativeButton("Cancel", null)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        textName.setText(actor.getName());
        textSurname.setText(actor.getSurname());
    }
}
