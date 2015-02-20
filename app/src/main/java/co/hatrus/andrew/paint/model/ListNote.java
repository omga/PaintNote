package co.hatrus.andrew.paint.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by user on 11.02.15.
 */
public class ListNote extends RealmObject {
    @PrimaryKey
    private int id;
    private Note note;
    private RealmList<CheckList> noteItems;


    public ListNote() {
        note = new Note();
        this.note.setType(2);
    }

    public RealmList<CheckList> getNoteItems() {
        return noteItems;
    }

    public void setNoteItems(RealmList<CheckList> noteItems) {
        this.noteItems = noteItems;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
