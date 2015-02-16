package co.hatrus.andrew.paint;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import co.hatrus.andrew.paint.db.DataBaseHelper;
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
    private DataBaseHelper mDBHelper;

    private NoteLab(Context appContext){
        mAppContext = appContext;
        mDBHelper = new DataBaseHelper(appContext);
    }

    public static NoteLab getInstance(Context appContext){
        if(sNoteLab==null)
            sNoteLab = new NoteLab(appContext);
        return sNoteLab;
    }
    public void addNote(Note note){
        mDBHelper.insertTextNote(note);
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
        List<String > list = new LinkedList<>();
        list.add("abc");
        list.add("abc2");
        list.add("abc3");
        c.setNotes(list);
        mNotes.add(a);
        mNotes.add(b);
        mNotes.add(c);
        for(int i=0;i<20;i++){
            mNotes.add(new TextNote());
        }

        return mNotes;
    }

    public DataBaseHelper.NoteCursor getCursor(){
        return mDBHelper.queryNotes();
    }

}
