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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_text_note, container, false);
        mNoteText = (EditText) v.findViewById(R.id.note_text);
        if(mNote!=null)
            mNoteText.setText(((TextNote)mNote).getText());
        else mNote = new TextNote();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((TextNote)mNote).setText(mNoteText.getText().toString());
    }

    @Override
    public void setNoteData() {
        ((TextNote)mNote).setText(mNoteText.getText().toString().trim());
    }
}
