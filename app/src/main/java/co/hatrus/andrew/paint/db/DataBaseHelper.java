package co.hatrus.andrew.paint.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import co.hatrus.andrew.paint.model.Note;

/**
 * Created by user on 13.02.15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "paintnote.sqlite";
    private static final String TABLE_NOTE = "note";
    private static final String COLUMN_NOTE_TITTLE = "title";
    private static final String COLUMN_NOTE_TIME = "timestamp";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table note(" +
                "_id integer primary key autoincrement, " +
                "title varchar(50), type integer, timestamp integer)");
        db.execSQL("create table textnote(" +
                "text varchar(500), note_id integer references note(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long insertTextNote(Note note){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE_TITTLE, note.getTitle());
        cv.put(COLUMN_NOTE_TIME, note.getTimeCreated().getTime());
        cv.put("type",1);
        return getWritableDatabase().insert(TABLE_NOTE,null,cv);
    }
    public NoteCursor queryNotes(){
        Cursor c = getReadableDatabase().query(TABLE_NOTE,null,null,null,null,null,null);
        return new NoteCursor(c);
    }
    public static class NoteCursor extends CursorWrapper{
        public NoteCursor (Cursor c) {
            super(c);
        }
        public Note getNote(){
            if (isBeforeFirst() || isAfterLast())
                return null;
            Note note = new Note();
            note.setId(getPosition());
            note.setTitle(getString(getColumnIndex(COLUMN_NOTE_TITTLE)));
            note.setTimeCreated(new Date(getLong(getColumnIndex(COLUMN_NOTE_TIME))));
            return note;
        }
    }
}
