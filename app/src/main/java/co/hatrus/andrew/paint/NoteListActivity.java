package co.hatrus.andrew.paint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import co.hatrus.andrew.paint.paint.DragAndDrawActivity;


public class NoteListActivity extends MainFragmentActivity implements NoteListFragment.OnFragmentInteractionListener {


    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }

    @Override
    public void onNoteSelected(String id) {
        Toast.makeText(this,"clicked: "+id,Toast.LENGTH_SHORT).show();

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
        CharSequence[] list={"Text","List","Paint"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chose attachment please")
                .setItems(list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Intent intent;
                        switch(which){
                            case 0:
                                intent = new Intent(NoteListActivity.this,TextNoteActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(NoteListActivity.this,TextNoteActivity.class);
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
