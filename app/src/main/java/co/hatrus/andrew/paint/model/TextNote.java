package co.hatrus.andrew.paint.model;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 11.02.15.
 */
public class TextNote extends RealmObject {

    @PrimaryKey
    private int id;
    private String text;
    private Note note;
    public TextNote() {
        super();
        note = new Note();
        this.note.setType(1);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        note = note;
    }
}
