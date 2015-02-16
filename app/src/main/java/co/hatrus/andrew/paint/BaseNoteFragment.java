package co.hatrus.andrew.paint;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by user on 12.02.15.
 */
public abstract class BaseNoteFragment extends Fragment {

    public static final String EXTRA_NOTE_ID = "NoteFragment.NoteId";

    protected BaseNoteFragment putArgs( int id){
        Bundle args = new Bundle();
        args.putInt(EXTRA_NOTE_ID,id);
        this.setArguments(args);
        return this;
    }


}
