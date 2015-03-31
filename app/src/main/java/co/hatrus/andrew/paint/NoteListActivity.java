package co.hatrus.andrew.paint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import co.hatrus.andrew.paint.model.Note;



public class NoteListActivity extends MainFragmentActivity
        implements NoteListFragment.OnFragmentInteractionListener{

    public static final String NOTE_ID_EXTRA = "note_id";
    public static final String NOTE_TYPE_EXTRA = "note_type";
    public static final String NOTE_TITLE_EXTRA = "note_title";
    NoteLab mNoteLab;

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mNoteLab = NoteLab.getInstance(getApplicationContext());
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
    public void onNoteSelected(Note note,String id) {
        Toast.makeText(this,"clicked: "+id,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NoteListActivity.this,BaseNoteActivity.class);
        intent.putExtra(NOTE_ID_EXTRA, id);
        intent.putExtra(NOTE_TITLE_EXTRA, note.getTitle());
        intent.putExtra(NOTE_TYPE_EXTRA, note.getType());
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
            showMaterialDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMaterialDialog() {
        CharSequence[] list={getString(R.string.dialog_textnote_item),
                getString(R.string.dialog_listnote_item),
                getString(R.string.dialog_paintnote_item)};
        new MaterialDialog.Builder(this)
                .title("Chose attachment please")
                .items(list)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = new Intent(NoteListActivity.this,BaseNoteActivity.class);
                        switch(which){
                            case 0:
                                intent.putExtra(NOTE_TYPE_EXTRA, Note.NOTE_TYPE_TEXT);
                                break;
                            case 1:
                                intent.putExtra(NOTE_TYPE_EXTRA, Note.NOTE_TYPE_LIST);
                                break;
                            case 2:
                                intent.putExtra(NOTE_TYPE_EXTRA, Note.NOTE_TYPE_PAINT);
                                break;
                            default:
                                intent.putExtra(NOTE_TYPE_EXTRA, Note.NOTE_TYPE_TEXT);
                        }
                        startActivity(intent);
                    }
                })
                .show();
    }
}
