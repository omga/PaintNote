package co.hatrus.andrew.paint.model;

/**
 * Created by user on 11.02.15.
 */
public abstract class Note {
    public static final String JSON_TITLE = "title";
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
