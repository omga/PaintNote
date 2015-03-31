package co.hatrus.andrew.paint.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by user on 23.02.15.
 */
@RealmClass
public class PaintNote extends RealmObject {
    @PrimaryKey
    private String id;
    private Note note;
    private int backgroundColor;

    public PaintNote() {
        id = UUID.randomUUID().toString();
        note = new Note();
        this.note.setType(Note.NOTE_TYPE_PAINT);
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
