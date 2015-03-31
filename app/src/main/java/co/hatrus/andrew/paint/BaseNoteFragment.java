package co.hatrus.andrew.paint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by user on 12.02.15.
 */
public abstract class BaseNoteFragment extends Fragment {

    public static final String EXTRA_NOTE_ID = "NoteFragment.NoteId";
    public static final String EXTRA_NOTE_TYPE = "NoteFragment.NoteType";

    protected NoteLab mNoteLab;
    protected String id;
    protected int type;



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
            getNote();
        else
            newNote();
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.action_save_note:
//                setNoteData();
                if(id!=null)
                    updateNote();
                else
                    saveNote();
                getActivity().finish();
                break;
            case R.id.action_delete_note:
                deleteNote();
                ((BaseNoteActivity)getActivity()).updateWidgets();
                NavUtils.navigateUpFromSameTask(getActivity());
                break;
            case R.id.action_edit_note:
                toggleEditable();
                break;
            case android.R.id.home:
//                setNoteData();
                if(id!=null)
                    updateNote();
                else
                    saveNote();

                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return true;
    }

    public abstract void toggleEditable();

    public abstract void deleteNote();

    public abstract void setNoteData();

    protected abstract void getNote();

    protected abstract void newNote();

    protected abstract void updateNote();

    protected abstract void saveNote();

    public abstract void setNoteTitle(String title);
}
