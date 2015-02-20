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
    public static final String EXTRA_NOTE_TYPE = "NoteFragment.NoteType";

    protected NoteLab mNoteLab;
    protected int id;
    protected int type;


    public abstract void setNoteData();
    protected BaseNoteFragment putArgs(int id, int type){
        Bundle args = new Bundle();
        args.putInt(EXTRA_NOTE_ID,id);
        args.putInt(EXTRA_NOTE_TYPE,type);
        this.setArguments(args);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt(BaseNoteFragment.EXTRA_NOTE_ID,-1);
        type = getArguments().getInt(BaseNoteFragment.EXTRA_NOTE_ID,1);
        mNoteLab =  NoteLab.getInstance(getActivity());
        if(id!=-1)
            setNote();
        else
            newNote();
        setHasOptionsMenu(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setNoteData();
        if(id!=-1)
            updateNote(id);
        else
            saveNote();
        Log.d("BaseNoteFragment","onOptionsItemSelected");
        return true;
    }

    protected abstract void setNote();

    protected abstract void newNote();

    protected abstract void updateNote(int id);

    protected abstract void saveNote();

    public abstract void setNoteTitle(String title);
}
