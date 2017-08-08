package co.hatrus.andrew.paint;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.widget.WidgetProvider;


public class BaseNoteActivity extends MainFragmentActivity {
    EditText mTitle;
    TextView reminderTextView;
    TextView cancelReminderTextView;


    int note_type;
    String note_id;
    private Menu menu;
    private MenuItem homeMenuItem;

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
        reminderTextView = (TextView) findViewById(R.id.reminder_textView);
        cancelReminderTextView = (TextView) findViewById(R.id.reminder_cancel_textView);

        Utils.setRobotoTypeface(this, mTitle);
        Utils.setRobotoTypeface(this, reminderTextView);
        Utils.setRobotoTypeface(this, cancelReminderTextView);

        reminderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSetReminder();
            }
        });
        cancelReminderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReminder();
                setReminderTextView(getString(R.string.set_reminder));
                cancelReminderTextView.setVisibility(View.GONE);
            }
        });

        note_id = getIntent().getStringExtra(NoteListActivity.NOTE_ID_EXTRA);
        String noteTitle = getIntent().getStringExtra(NoteListActivity.NOTE_TITLE_EXTRA);
        note_type = getIntent().getIntExtra(NoteListActivity.NOTE_TYPE_EXTRA, 1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(note_id!=null) {
            setNoteTitle(noteTitle);
            mTitle.setEnabled(false);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, createFragment().putArgs(note_id, note_type))
                    .commit();
        }
    }
    protected int getLayoutResId(){
        return R.layout.activity_base_note;
    }

    public String getNoteTitle() {
        return mTitle.getText().toString().trim();
    }

    public void setNoteTitle(String title){
        this.mTitle.setText(title);
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
        homeMenuItem = menu.findItem(R.id.action_save_note);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_save_note:
                Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();
                ((BaseNoteFragment)getSupportFragmentManager().findFragmentById(R.id.container))
                        .setNoteTitle(mTitle.getText().toString().trim());
                updateWidgets();
                break;
            case R.id.action_edit_note:
                boolean isEnabled = mTitle.isEnabled();
                mTitle.setEnabled(!isEnabled);
                if(isEnabled)
                    item.setIcon(R.drawable.ic_edit_grey);
                else item.setIcon(R.drawable.ic_check_grey);
                break;
            case android.R.id.home:
                ((BaseNoteFragment)getSupportFragmentManager().findFragmentById(R.id.container))
                        .setNoteTitle(mTitle.getText().toString().trim());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BaseNoteFragment baseNoteFragment = ((BaseNoteFragment)
                    getSupportFragmentManager().findFragmentById(R.id.container));
            baseNoteFragment.setNoteTitle(mTitle.getText().toString().trim());
            baseNoteFragment.saveNoteAndNavigateBack();
            updateWidgets();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void updateWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.
                getAppWidgetIds(new ComponentName(getApplicationContext(), WidgetProvider.class));
        Intent intent = new Intent(this,WidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
        sendBroadcast(intent);
    }

    public void openDialogSetReminder () {
        ((BaseNoteFragment) getSupportFragmentManager().findFragmentById(R.id.container)).
                setReminder();
    }

    public void cancelReminder() {
        ((BaseNoteFragment) getSupportFragmentManager().findFragmentById(R.id.container)).
                cancelReminder();
    }

    public void setReminderTextView(String str) {
        reminderTextView.setText(str);
        cancelReminderTextView.setVisibility(View.VISIBLE);
    }
}
