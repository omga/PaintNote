package co.hatrus.andrew.paint.model;

import java.util.Date;

/**
 * Created by user on 11.02.15.
 */
public abstract class Note {
    public static final String JSON_TITLE = "mTitle";

    private int mId;
    private String mTitle;
    private Date mTimeCreated;

    public Note() {
        mId = -1;
        mTimeCreated = new Date();
    }
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Date getTimeCreated() {
        return mTimeCreated;
    }

    public void setTimeCreated(Date date){
        this.mTimeCreated = date;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
