package co.hatrus.andrew.paint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import co.hatrus.andrew.paint.model.TextNote;


/**
 * A simple {@link BaseNoteFragment} subclass. Note con
 */
public class TextNoteFragment extends BaseNoteFragment {

    private EditText mNoteText;
    TextNote mNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_text_note, container, false);
        mNoteText = (EditText) v.findViewById(R.id.note_text);
        mNote = (TextNote) NoteLab.getInstance(getActivity()).getNote(getArguments().getInt(BaseNoteFragment.EXTRA_NOTE_ID));
        mNoteText.setText(mNote.getText());
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        mNote.setText(mNoteText.getText().toString());
    }

}
