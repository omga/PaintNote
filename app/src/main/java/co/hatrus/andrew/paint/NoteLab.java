package co.hatrus.andrew.paint;

import android.content.Context;
import android.util.Log;

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
    private static NoteLab sNoteLab;
    private Context mAppContext;
    private Realm mRealm;


    private NoteLab(Context appContext) {
        mAppContext = appContext;
        mRealm = Realm.getInstance(appContext);
    }

    public static NoteLab getInstance(Context appContext) {
        if (sNoteLab == null)
            sNoteLab = new NoteLab(appContext);
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
        return noteQuery.findAllSorted("timeCreated", false);
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
            for (RealmObject realmObject : robjects)
                if (realmObject != null)
                    realmObject.removeFromRealm();
        mRealm.commitTransaction();
        }catch (Exception ise){
            Log.e("Notelab","deleteObjectList "+ise.getMessage());
        }
    }

}
