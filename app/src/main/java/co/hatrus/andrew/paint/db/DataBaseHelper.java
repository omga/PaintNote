package co.hatrus.andrew.paint.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.hatrus.andrew.paint.model.ListNote;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.TextNote;

/**
 * Created by user on 13.02.15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "paintnote.sqlite";
    private static final String TABLE_NOTE = "note";
    private static final String TABLE_NOTE_ID = "_id";
    private static final String COLUMN_NOTE_TITTLE = "title";
    private static final String COLUMN_NOTE_DATA = "note_data";
    private static final String COLUMN_NOTE_TIME = "time_created";
    private static final String COLUMN_NOTE_TYPE = "type";
    private static final int TEXTNOTE_TYPE_CODE = 1;
    private static final int LISTNOTE_TYPE_CODE = 2;
    private static final int PAINTNOTE_TYPE_CODE = 3;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table note(" +
                "_id integer primary key autoincrement, " +
                "title varchar(50), type integer, note_data varchar(500), time_created integer, time_modified integer)");
//        db.execSQL("create table textnote(" +
//                "text varchar(500), note_id integer references note(_id))");
//        db.execSQL("create table listnote(" +
//                "text varchar(500), note_id integer references note(_id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!=newVersion) {
            Log.w("DataBaseHelper","table dropped. verisons: "+oldVersion+" --> "+newVersion);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTE);
            onCreate(db);
        }
    }

    private ContentValues setContentValues(Note note){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE_TITTLE, note.getTitle());
        cv.put(COLUMN_NOTE_TIME, note.getTimeCreated().getTime());
        if(note instanceof TextNote) {
            cv.put(COLUMN_NOTE_TYPE, TEXTNOTE_TYPE_CODE);
            cv.put(COLUMN_NOTE_DATA,((TextNote) note).getText());
        } else if (note instanceof ListNote){
            String listStr=((ListNote) note).getItems().toString();
            listStr=listStr.substring(1,listStr.length()-1);
            cv.put(COLUMN_NOTE_TYPE, LISTNOTE_TYPE_CODE);
            cv.put(COLUMN_NOTE_DATA,listStr);
        } else return null;
        return cv;
    }
    public long insertNote(Note note){
        ContentValues cv = setContentValues(note);
        return getWritableDatabase().insert(TABLE_NOTE,null,cv);
    }

    public long updateNote(int id, Note note){
        ContentValues cv = setContentValues(note);
        return getWritableDatabase().update(TABLE_NOTE,cv,TABLE_NOTE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public NoteCursor queryNote(int id){
        Cursor wrapped = getReadableDatabase().query(TABLE_NOTE,
                null, // all columns
                TABLE_NOTE_ID + " = ?", // look for a run ID
                new String[]{ String.valueOf(id) }, // with this value
                null, // group by
                null, // order by
                null, // having
                "1"); // limit 1 row
        return new NoteCursor(wrapped);
    }

    public NoteCursor queryNotes(){
        Cursor c = getReadableDatabase().query(TABLE_NOTE,null,null,null,null,null,null);//COLUMN_NOTE_TIME + " DESC");
        return new NoteCursor(c);
    }

    public static class NoteCursor extends CursorWrapper{
        public NoteCursor (Cursor c) {
            super(c);
        }
        public Note getNote(){
            if (isBeforeFirst() || isAfterLast())
                return null;
            Note note;
            switch (getInt(getColumnIndex(COLUMN_NOTE_TYPE))){
                case 2:
                    note = new ListNote();
                    String data = getString(getColumnIndex(COLUMN_NOTE_DATA));
                    LinkedList<String> listNotes= new LinkedList<>(Arrays.asList(data.split(",\\s*")));
                        ((ListNote) note).setItems(listNotes);
                    break;
                case 3:
                    note = new TextNote();
                    break;
                default:
                    note = new TextNote();
                    ((TextNote)note).setText(getString(getColumnIndex(COLUMN_NOTE_DATA)));
            }
            note.setId(getPosition());
            note.setTitle(getString(getColumnIndex(COLUMN_NOTE_TITTLE)));
            note.setTimeCreated(new Date(getLong(getColumnIndex(COLUMN_NOTE_TIME))));
            return note;
        }
    }
}
