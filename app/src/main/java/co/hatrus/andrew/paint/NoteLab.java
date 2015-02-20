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
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by user on 11.02.15.
 */
public class NoteLab {
    private List<Note> mNotes = new ArrayList<>(5);
    private Context mAppContext;
    private static NoteLab sNoteLab;
    //private DataBaseHelper mDBHelper;
    private Realm mRealm;

    public TextNote createTextNote() {
        mRealm.beginTransaction();
        Note n = mRealm.createObject(Note.class);
        TextNote tn = mRealm.createObject(TextNote.class);
        tn.setNote(n);
        mRealm.commitTransaction();
        return tn;
    }

    public Realm getRealm() {
        return mRealm;
    }

    public void setRealm(Realm realm) {
        mRealm = realm;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mRealm.close();
    }

    private NoteLab(Context appContext){
        mAppContext = appContext;
        mRealm = Realm.getInstance(appContext);
//        mRealm.close();
//        mRealm.deleteRealmFile(appContext);
        //mDBHelper = new DataBaseHelper(appContext);
    }

    public static NoteLab getInstance(Context appContext){
        if(sNoteLab==null)
            sNoteLab = new NoteLab(appContext);
        return sNoteLab;
    }

    public void addNote(Note note){
        mRealm.beginTransaction();
        mRealm.copyToRealm(note);
        //mDBHelper.insertNote(note);
        mRealm.commitTransaction();
    }

    public void addTextNote(TextNote textnote){
        mRealm.beginTransaction();
        mRealm.copyToRealm(textnote);
        mRealm.commitTransaction();
    }

    public void addListNote(ListNote listnote){
        mRealm.beginTransaction();
        mRealm.copyToRealm(listnote);
        mRealm.commitTransaction();
    }

    public void updateNote(Note note, int id){
        //mDBHelper.updateNote(id,note);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(note);
        mRealm.commitTransaction();
    }

    public void updateTextNote(TextNote note, int id){
        //mDBHelper.updateNote(id,note);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(note);
        mRealm.commitTransaction();
    }

    public void updateListNote(ListNote note, int id){
        //mDBHelper.updateNote(id,note);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(note);
        mRealm.commitTransaction();
    }

//    public Note getNote(int id){
//        DataBaseHelper.NoteCursor noteCursor = mDBHelper.queryNote(id);
//        Note note=null;
//        noteCursor.moveToFirst();
//        // if we got a row, get a run
//        if (!noteCursor.isAfterLast())
//            note = noteCursor.getNote();
//        noteCursor.close();
//        Log.d("NoteLab","crsr " + noteCursor+",note "+note+ " id= "+id);
//        return note;
//    }
//

    public TextNote getTextNoteData(int id){
        TextNote result = mRealm.where(TextNote.class).equalTo("note.id",id).findFirst();
        return result;
    }
    public ListNote getListNoteData(int id){
        ListNote result = mRealm.where(ListNote.class).equalTo("note.id",id).findFirst();
        return result;
    }


//    public DataBaseHelper.NoteCursor getCursor() {
//        return mDBHelper.queryNotes();
//    }
    public RealmResults<Note> getNotes(){
        RealmQuery<Note> noteQuery = mRealm.where(Note.class);
        RealmResults<Note> results = noteQuery.findAll();
        return results;
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
