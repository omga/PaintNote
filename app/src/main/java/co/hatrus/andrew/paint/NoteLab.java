package co.hatrus.andrew.paint;

import android.content.Context;
import android.util.Log;

import java.util.Iterator;

import co.hatrus.andrew.paint.model.ListNote;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.PaintNote;
import co.hatrus.andrew.paint.model.TextNote;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by user on 11.02.15.
 */
public class NoteLab {
    private static volatile NoteLab sNoteLab;
    private Realm mRealm;


    private NoteLab() {
        mRealm = Realm.getDefaultInstance();
    }

    public static NoteLab getInstance() {
        NoteLab noteLab = sNoteLab;
        if (noteLab == null) {
            synchronized (NoteLab.class) {
                noteLab = sNoteLab;
                if (noteLab == null) {
                    sNoteLab = new NoteLab();
                }
            }
        }
        return sNoteLab;
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

    public void updateTextNote(TextNote note) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(note);
        mRealm.commitTransaction();
    }

    public void updateListNote(ListNote note) {
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
        return mRealm.where(TextNote.class).equalTo("note.id", id).findFirst();

    }

    public ListNote getListNoteData(String id) {
        return mRealm.where(ListNote.class).equalTo("note.id", id).findFirst();

    }

    public PaintNote getPaintNoteData(String id) {
        return mRealm.where(PaintNote.class).equalTo("note.id",id).findFirst();
    }

    public RealmResults<Note> getNotes(){
        RealmQuery<Note> noteQuery = mRealm.where(Note.class);
        return noteQuery.findAllSorted("timeCreated", Sort.DESCENDING);
    }

    public void deleteObject(RealmObject ... robjects) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            for(RealmObject robject:robjects)
                if(robject!=null)
                    robject.deleteFromRealm();
            realm.commitTransaction();
            } catch (Exception ise){
                Log.e("Notelab","deleteObject "+ise.getMessage());
            } finally {
                if(realm!=null)
                    realm.close();
            }
    }
    public void deleteCheckListNote(RealmList<? extends RealmObject> robjects, ListNote listNote, Note note) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            for (RealmObject realmObject : robjects)
                if (realmObject != null)
                    realmObject.deleteFromRealm();
            listNote.deleteFromRealm();
            note.deleteFromRealm();
            realm.commitTransaction();
            } catch (Exception ise){
                Log.e("Notelab","deleteObjectList "+ise.getMessage());
            } finally {
                if(realm!=null)
                    realm.close();
            }
    }

    public Iterator<Note> getUpgoingNoteReminders() {
        return mRealm.where(Note.class)
                .equalTo("reminderEnabled", true)
                        //.greaterThan("timeRemind", Calendar.getInstance().getTimeInMillis())
                .findAllSorted("timeRemind")
                .iterator();
    }

    public void disableAlarm(String id) {
        mRealm.beginTransaction();
        mRealm.where(Note.class)
                .equalTo("id", id)
                .findFirst()
                .setReminderEnabled(false);
        mRealm.commitTransaction();
    }

}
