package co.hatrus.andrew.paint.model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;


/**
 * Created by user on 11.02.15.
 */
@RealmClass
public class ListNote extends RealmObject {
    @PrimaryKey
    @Required
    private String id;
    private Note note;
    private RealmList<CheckListItem> noteItems;


    public ListNote() {
        id = UUID.randomUUID().toString();
        noteItems = new RealmList<>();
        note = new Note();
    }

    public RealmList<CheckListItem> getNoteItems() {
        return noteItems;
    }

    public void setNoteItems(RealmList<CheckListItem> noteItems) {
        this.noteItems = noteItems;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
