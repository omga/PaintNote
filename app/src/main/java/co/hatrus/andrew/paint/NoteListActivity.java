package co.hatrus.andrew.paint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.hatrus.andrew.paint.model.ListNote;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.TextNote;
import co.hatrus.andrew.paint.paint.DragAndDrawActivity;


public class NoteListActivity extends MainFragmentActivity
        implements NoteListFragment.OnFragmentInteractionListener{


    public static final String NOTE_ID_EXTRA = "note_id";
    List<Note> mNotes;

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mNotes=NoteLab.getInstance(getApplicationContext()).getNoteList();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, createFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onNoteUpdated();
    }

    @Override
    public void onNoteSelected(int id) {
        Toast.makeText(this,"clicked: "+id,Toast.LENGTH_SHORT).show();
        Intent intent;// = new Intent(NoteListActivity.this,TextNoteActivity.class);
        Note note = mNotes.get(id);
        if(note instanceof ListNote)
            intent = new Intent(NoteListActivity.this,ChecklistNoteActivity.class);
//        else if (note instanceof PaintNote)
//            intent = new Intent(NoteListActivity.this,TextNoteActivity.class);
        else
            intent = new Intent(NoteListActivity.this,TextNoteActivity.class);
        intent.putExtra(NOTE_ID_EXTRA, id);
        startActivity(intent);
    }

    public void onNoteUpdated(){
        FragmentManager fm  = getSupportFragmentManager();
        NoteListFragment listFragment = (NoteListFragment)fm.findFragmentById(R.id.container);
        listFragment.updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_add_note) {
            Toast.makeText(this,"ADD",Toast.LENGTH_SHORT).show();
            createAttachmentDialog().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public Dialog createAttachmentDialog() {
        CharSequence[] list={getString(R.string.dialog_textnote_item),
                getString(R.string.dialog_listnote_item),
                getString(R.string.dialog_paintnote_item)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chose attachment please")
                .setItems(list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Intent intent;
                        Note note;
                        switch(which){
                            case 0:
                                intent = new Intent(NoteListActivity.this,TextNoteActivity.class);
                                note = new TextNote();
                                mNotes.add(note);
                                intent.putExtra("note_id",mNotes.size()-1);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(NoteListActivity.this,ChecklistNoteActivity.class);
                                note = new ListNote();
                                mNotes.add(note);
                                intent.putExtra("note_id",mNotes.size()-1);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(NoteListActivity.this,DragAndDrawActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
        return builder.create();
    }
}