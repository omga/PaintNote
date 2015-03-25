package co.hatrus.andrew.paint;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import co.hatrus.andrew.paint.db.DataBaseHelper;
import co.hatrus.andrew.paint.model.ListNote;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.PaintNote;
import co.hatrus.andrew.paint.model.TextNote;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
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

    public void updatePaintNote(PaintNote note) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(note);
        mRealm.commitTransaction();
    }



    public TextNote getTextNoteData(String id) {
        TextNote result = mRealm.where(TextNote.class).equalTo("note.id",id).findFirst();
        return result;
    }

    public ListNote getListNoteData(String id) {
        ListNote result = mRealm.where(ListNote.class).equalTo("note.id",id).findFirst();
        return result;
    }

    public PaintNote getPaintNoteData(String id) {
        return mRealm.where(PaintNote.class).equalTo("note.id",id).findFirst();
    }

    public RealmResults<Note> getNotes(){
        RealmQuery<Note> noteQuery = mRealm.where(Note.class);
        RealmResults<Note> results = noteQuery.findAllSorted("timeCreated",false);
        return results;
    }


    public void deleteObject(RealmObject ... robjects) {
        try {

        mRealm.beginTransaction();
        for(RealmObject robject:robjects)
            if(robject!=null)
                robject.removeFromRealm();
        mRealm.commitTransaction();
        }catch (Exception ise){
            Log.e("Notelab","deleteObject "+ise.getMessage());
        }
    }
    public void deleteObjectList(RealmList<? extends RealmObject> robjects) {
        try{
        mRealm.beginTransaction();
        for(RealmObject robject:robjects)
            if(robject!=null)
                robject.removeFromRealm();
        mRealm.commitTransaction();
        }catch (Exception ise){
            Log.e("Notelab","deleteObjectList "+ise.getMessage());
        }
    }

}
