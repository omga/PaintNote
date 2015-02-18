package co.hatrus.andrew.paint;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        mDBHelper.insertNote(note);
    }

    public void updateNote(Note note, int id){
        mDBHelper.updateNote(id,note);
    }

    public Note getNote(int id){
        DataBaseHelper.NoteCursor noteCursor = mDBHelper.queryNote(id);
        Note note=null;
        noteCursor.moveToFirst();
        // if we got a row, get a run
        if (!noteCursor.isAfterLast())
            note = noteCursor.getNote();
        noteCursor.close();
        Log.d("NoteLab","crsr " + noteCursor+",note "+note+ " id= "+id);
        return note;
    }

    public DataBaseHelper.NoteCursor getCursor(){
        return mDBHelper.queryNotes();
    }

    public List<Note> getNoteList(){

//        TextNote a = new TextNote();
//        a.setTitle("textnote1");
//        a.setText("texty");
//        mDBHelper.insertNote(a);
//        TextNote b = new TextNote();
//        b.setTitle("textnote2");
//        b.setText("texty2");
//        mDBHelper.insertNote(b);
//        ListNote c = new ListNote();
//        c.setTitle("listnote3");
//        List<String > list = new LinkedList<>();
//        list.add("abc");
//        list.add("abc2");
//        list.add("abc3");
//        c.setItems(list);
//        mDBHelper.insertNote(c);
//        mNotes.add(a);
//        mNotes.add(b);
//        mNotes.add(c);
//        for(int i=0;i<10;i++){
//            mDBHelper.insertNote(new TextNote());
//        }
        return mNotes;
    }

}
