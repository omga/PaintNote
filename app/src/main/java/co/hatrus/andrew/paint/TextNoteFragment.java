package co.hatrus.andrew.paint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mTextNote.setText(mNoteText.getText().toString());
//    }

    @Override
    public void setNoteData() {
        mTextNote.setText(mNoteText.getText().toString().trim());
    }

    @Override
    protected void setNote() {
        mTextNote = mNoteLab.getTextNoteData(id);
    }

    @Override
    protected void newNote() {
       // mTextNote = mNoteLab.createTextNote();
        mRealm = mNoteLab.getRealm();
        mRealm.beginTransaction();
        mTextNote = mRealm.createObject(TextNote.class);

    }

    @Override
    protected void updateNote(int id) {
        setNoteData();
        mNoteLab.updateTextNote(mTextNote,id);
    }

    @Override
    protected void saveNote() {
//        setNoteData();
//        mNoteLab.addTextNote(mTextNote);
        mRealm.commitTransaction();
    }

    @Override
    public void setNoteTitle(String title) {
//        mTextNote.getNote().setTitle(title);
    }
}
