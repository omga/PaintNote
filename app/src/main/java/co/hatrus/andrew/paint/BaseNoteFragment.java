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
    protected String id;
    protected int type;


    public abstract void setNoteData();
    protected BaseNoteFragment putArgs(String id, int type){
        Bundle args = new Bundle();
        args.putString(EXTRA_NOTE_ID,id);
        args.putInt(EXTRA_NOTE_TYPE,type);
        this.setArguments(args);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString(BaseNoteFragment.EXTRA_NOTE_ID);
        type = getArguments().getInt(BaseNoteFragment.EXTRA_NOTE_TYPE,1);
        mNoteLab =  NoteLab.getInstance(getActivity());
        if(id!=null)
            setNote();
        else
            newNote();
        setHasOptionsMenu(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setNoteData();
        if(id!=null)
            updateNote();
        else
            saveNote();
        Log.d("BaseNoteFragment","onOptionsItemSelected do nothing but just finish");
        getActivity().finish();
        return true;
    }

    protected abstract void setNote();

    protected abstract void newNote();

    protected abstract void updateNote();

    protected abstract void saveNote();

    public abstract void setNoteTitle(String title);
}
