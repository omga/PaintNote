package co.hatrus.andrew.paint;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.paint.DragAndDrawFragment;


public class BaseNoteActivity extends MainFragmentActivity {
    EditText mTitle;
    NoteLab mNoteLab;
    int note_type;
    private Menu menu;

    @Override
    protected BaseNoteFragment createFragment() {
        switch (note_type) {
            case Note.NOTE_TYPE_TEXT:
                return new TextNoteFragment();
            case Note.NOTE_TYPE_LIST:
                return new ChecklistNoteFragment();
            case Note.NOTE_TYPE_PAINT:
                return new DragAndDrawFragment();
            default:
                return new TextNoteFragment();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_note);
        mTitle = (EditText) findViewById(R.id.note_title);
        String note_id = getIntent().getStringExtra(NoteListActivity.NOTE_ID_EXTRA);
        String noteTitle = getIntent().getStringExtra(NoteListActivity.NOTE_TITLE_EXTRA);
        note_type = getIntent().getIntExtra(NoteListActivity.NOTE_TYPE_EXTRA, 1);
        mNoteLab = NoteLab.getInstance(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(note_id!=null) {
            setNoteTitle(noteTitle);
            mTitle.setEnabled(false);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, createFragment().putArgs(note_id,note_type))
                    .commit();
        }
    }
    protected int getLayoutResId(){
        return R.layout.activity_base_note;
    }

    public void setNoteTitle(String title){
        this.mTitle.setText(title);
    }
    public String getNoteTitle(){
        return mTitle.getText().toString().trim();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base_note, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_save_note:
                Toast.makeText(this, "saved", Toast.LENGTH_LONG).show();
                ((BaseNoteFragment)getSupportFragmentManager().findFragmentById(R.id.container)).setNoteTitle(mTitle.getText().toString().trim());
                break;
            case R.id.action_edit_note:
                boolean isEnabled = mTitle.isEnabled();
                mTitle.setEnabled(!isEnabled);
                if(isEnabled)
                    item.setIcon(R.drawable.ic_edit_grey);
                else item.setIcon(R.drawable.ic_check_grey);
                break;
            case R.id.action_settings:
                Toast.makeText(this,"settings",Toast.LENGTH_LONG).show();
                break;
            case android.R.id.home:
                ((BaseNoteFragment)getSupportFragmentManager().findFragmentById(R.id.container)).setNoteTitle(mTitle.getText().toString().trim());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
