package co.hatrus.andrew.paint.model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by user on 11.02.15.
 */
@RealmClass
public class Note extends RealmObject {
    @Ignore
    public static final int NOTE_TYPE_TEXT = 1;
    @Ignore
    public static final int  NOTE_TYPE_LIST = 2;
    @Ignore
    public static final int NOTE_TYPE_PAINT = 3;
    @PrimaryKey
    private String id;
    private String title;
    private Date timeCreated;
    private long timeRemind;
    private boolean reminderEnabled;
    private int type;

    public Note() {
        id = UUID.randomUUID().toString();
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

    public String getId() {
        return id;
    }

    public void setId(String UUID) {
        id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimeRemind() {
        return timeRemind;
    }

    public void setTimeRemind(long timeRemind) {
        this.timeRemind = timeRemind;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }
}
