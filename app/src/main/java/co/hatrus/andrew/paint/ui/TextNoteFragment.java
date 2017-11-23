package co.hatrus.andrew.paint.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import co.hatrus.andrew.paint.R;
import co.hatrus.andrew.paint.helper.Utils;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.TextNote;


/**
 * A simple {@link BaseNoteFragment} subclass. Note con
 */
public class TextNoteFragment extends BaseNoteFragment {

    private TextNote mTextNote;
    private EditText mNoteText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_text_note, container, false);
        mNoteText = v.findViewById(R.id.note_text);
        Utils.setRobotoTypeface(getActivity(), mNoteText);
        if(id!=null) {
            mNoteText.setText(mTextNote.getText());
            //mNoteText.setEnabled(false);

        }
        return v;
    }

    @Override
    public void toggleEditable() {
        //mNoteText.setEnabled(!mNoteText.isEnabled());
    }

    @Override
    public void deleteNote() {
        mNoteLab.deleteObject(mTextNote,mTextNote.getNote());
    }

    @Override
    protected void getNote() {
        mTextNote = mNoteLab.getTextNoteData(id);
        mNote = mTextNote.getNote();

    }

    @Override
    protected void newNote() {
        mTextNote = new TextNote();
        mNote = mTextNote.getNote();// new Note();
        mNote.setType(Note.NOTE_TYPE_TEXT);
        mTextNote.setNote(mNote);
    }

    @Override
    protected void updateNote() {
        //setNoteData();
    }

    @Override
    protected void saveNote() {
        mNoteLab.updateTextNote(mTextNote);
    }

    @Override
    public void setNoteTitle(String title) {
        mNoteLab.getRealm().beginTransaction();
        mTextNote.getNote().setTitle(title);
        mTextNote.setText(mNoteText.getText().toString().trim());
        mNoteLab.getRealm().commitTransaction();
    }
}
