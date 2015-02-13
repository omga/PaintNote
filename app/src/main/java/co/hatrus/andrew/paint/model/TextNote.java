package co.hatrus.andrew.paint.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 11.02.15.
 */
public class TextNote extends Note {
    public static final String JSON_TEXT = "text";
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_TITLE, getTitle());
            json.put(JSON_TEXT, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
