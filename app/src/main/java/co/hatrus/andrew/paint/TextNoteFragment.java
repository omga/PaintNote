package co.hatrus.andrew.paint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.TextNote;
import io.realm.Realm;


/**
 * A simple {@link BaseNoteFragment} subclass. Note con
 */
public class TextNoteFragment extends BaseNoteFragment {

    private TextNote mTextNote;
    private EditText mNoteText;
    Realm mRealm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_text_note, container, false);
        mNoteText = (EditText) v.findViewById(R.id.note_text);
        if(mTextNote!=null)
            mNoteText.setText(mTextNote.getText());
        return v;
    }

    @Override
    public void deleteNote() {
        mNoteLab.deleteObject(mTextNote,mTextNote.getNote());
    }

    @Override
    public void setNoteData() {
        mNoteLab.getRealm().beginTransaction();
        mTextNote.setText(mNoteText.getText().toString().trim());
        mNoteLab.getRealm().commitTransaction();
    }

    @Override
    protected void getNote() {
        mTextNote = mNoteLab.getTextNoteData(id);

    }

    @Override
    protected void newNote() {
        mTextNote = new TextNote();
        Note n = new Note();
        n.setType(Note.NOTE_TYPE_TEXT);
        mTextNote.setNote(n);

    }

    @Override
    protected void updateNote() {
        setNoteData();
    }

    @Override
    protected void saveNote() {
        mNoteLab.updateTextNote(mTextNote,1);
    }

    @Override
    public void setNoteTitle(String title) {
        mNoteLab.getRealm().beginTransaction();
        mTextNote.getNote().setTitle(title);
        mTextNote.setText(mNoteText.getText().toString().trim());
        mNoteLab.getRealm().commitTransaction();
    }
}
