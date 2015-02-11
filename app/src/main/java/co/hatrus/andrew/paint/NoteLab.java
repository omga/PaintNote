package co.hatrus.andrew.paint;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.hatrus.andrew.paint.model.ListNote;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.TextNote;

/**
 * Created by user on 11.02.15.
 */
public class NoteLab {
    private List<Note> mNotes = new ArrayList<>(5);
    private Context mAppContext;
    private static NoteLab sNoteLab;

    private NoteLab(Context appContext){
        mAppContext = appContext;
    }

    public static NoteLab getInstance(Context appContext){
        if(sNoteLab==null)
            sNoteLab = new NoteLab(appContext);
        return sNoteLab;
    }
    public void addNote(Note note){
        mNotes.add(note);
    }
    public Note getNote(int id){
      return mNotes.get(id);
    }

    public List<Note> getNoteList(){
        TextNote a = new TextNote();
        a.setTitle("textnote1");
        a.setText("texty");
        TextNote b = new TextNote();
        b.setTitle("textnote2");
        b.setText("texty2");
        ListNote c = new ListNote();
        c.setTitle("listnote3");

        mNotes.add(a);
        mNotes.add(b);
        mNotes.add(c);

        return mNotes;
    }


}
