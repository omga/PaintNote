package co.hatrus.andrew.paint.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 11.02.15.
 */
public class ListNote extends Note{
    public static final String JSON_NOTES = "checknotes";
    List<String> notes = new LinkedList<>();

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_TITLE, getTitle());
            json.put(JSON_NOTES, new JSONArray(notes));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON LIST",json.toString());
        return json;
    }
}
