package co.hatrus.andrew.paint.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by user on 11.02.15.
 */
@RealmClass
public class TextNote extends RealmObject {

    @PrimaryKey
    @Required
    private String id;
    @Required
    private String text;
    private Note note;

    public TextNote() {
        super();
        id = UUID.randomUUID().toString();
        note = new Note();
//        this.note.setType(Note.NOTE_TYPE_TEXT);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        id = id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        note = note;
    }
}
