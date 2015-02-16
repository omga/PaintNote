package co.hatrus.andrew.paint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import co.hatrus.andrew.paint.model.Note;


public abstract class BaseNoteActivity extends MainFragmentActivity {
    EditText mTitle;
    Note mNote;

    @Override
    protected abstract BaseNoteFragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_note);
        mTitle = (EditText) findViewById(R.id.node_title);
        int note_id = getIntent().getIntExtra(NoteListActivity.NOTE_ID_EXTRA, 0);
        mNote = NoteLab.getInstance(getApplicationContext()).getNoteList().
                get(note_id);
        setNoteTitle(mNote.getTitle());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, createFragment().putArgs(note_id))
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
        return mTitle.getText().toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNote.setTitle(mTitle.getText().toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_save_note:
                Toast.makeText(this, "save: " + mNote.toString(), Toast.LENGTH_LONG).show();
                NoteLab.getInstance(getApplicationContext()).addNote(mNote);
                return true;
            case R.id.action_settings:
                Toast.makeText(this,"settings",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
