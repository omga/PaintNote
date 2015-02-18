package co.hatrus.andrew.paint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import co.hatrus.andrew.paint.model.Note;

/**
 * Created by user on 12.02.15.
 */
public abstract class BaseNoteFragment extends Fragment {

    public static final String EXTRA_NOTE_ID = "NoteFragment.NoteId";
    protected Note mNote;
    protected NoteLab mNoteLab;
    private int id;

    public void setNoteTitle(String title){
        mNote.setTitle(title);
    }
    public abstract void setNoteData();
    protected BaseNoteFragment putArgs( int id){
        Bundle args = new Bundle();
        args.putInt(EXTRA_NOTE_ID,id);
        this.setArguments(args);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt(BaseNoteFragment.EXTRA_NOTE_ID,-1);
        mNoteLab =  NoteLab.getInstance(getActivity());
        if(id!=-1)
            mNote = mNoteLab.getNote(id);
        setHasOptionsMenu(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setNoteData();
        if(id!=-1)
            mNoteLab.updateNote(mNote,id);
        else
            mNoteLab.addNote(mNote);
        Log.d("BaseNoteFragment","onOptionsItemSelected");
        return true;
    }
}
