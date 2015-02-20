package co.hatrus.andrew.paint.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 11.02.15.
 */
public class Note extends RealmObject {
    @Ignore
    public static final int NOTE_TYPE_TEXT = 1;
    @Ignore
    public static final int  NOTE_TYPE_LIST = 2;
    @Ignore
    public static final int NOTE_TYPE_PAINT = 3;
    @PrimaryKey
    private int id;
    private String title;
    private Date timeCreated;
    private int type;

    public Note() {
        timeCreated = new Date();
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date date){
        this.timeCreated = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
