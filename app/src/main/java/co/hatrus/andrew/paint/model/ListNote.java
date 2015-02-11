package co.hatrus.andrew.paint.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11.02.15.
 */
public class ListNote extends Note{
    List<String> notes = new ArrayList<>(5);

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}
