package io.github.tgelacio.tictactoeleague;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;
import io.github.tgelacio.tictactoeleague.R;

public class EditDialog extends AppCompatDialogFragment {

    private EditText txtEditName;
    private EditDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditDialogListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement EditDialogListener.");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit, null);

        builder.setView(view)
                .setTitle("Edit player name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editedName = txtEditName.getText().toString();

                        listener.getEditedPlayerName(editedName);
                    }
                });

        txtEditName = (EditText) view.findViewById(R.id.txtEditedName);

        return builder.create();
    }

    public interface EditDialogListener {
        void getEditedPlayerName(String editedName);
    }
}
