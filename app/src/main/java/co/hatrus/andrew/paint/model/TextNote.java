package co.hatrus.andrew.paint.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by user on 11.02.15.
 */
@RealmClass
public class TextNote extends RealmObject {

    @PrimaryKey
    private String id;
    private String text;
    private Note note;
    public TextNote() {
        super();
        id = UUID.randomUUID().toString();
        note = new Note();
        this.note.setType(1);
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
