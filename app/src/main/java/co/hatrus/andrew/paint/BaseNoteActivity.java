package co.hatrus.andrew.paint;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.TextNote;
import co.hatrus.andrew.paint.paint.DragAndDrawFragment;
import co.hatrus.andrew.paint.widget.LoremViewsFactory;
import co.hatrus.andrew.paint.widget.WidgetProvider;


public class BaseNoteActivity extends MainFragmentActivity {
    EditText mTitle;
    NoteLab mNoteLab;
    int note_type;
    private Menu menu;
    private MenuItem homeMenuItem;
    String note_id;

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
        mTitle.setTypeface(Typeface
                .createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
        note_id = getIntent().getStringExtra(NoteListActivity.NOTE_ID_EXTRA);
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
                Toast.makeText(this, "saved", Toast.LENGTH_LONG).show();
                ((BaseNoteFragment)getSupportFragmentManager().findFragmentById(R.id.container)).setNoteTitle(mTitle.getText().toString().trim());
                updateWidgets();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
            onOptionsItemSelected(homeMenuItem);
        return super.onKeyDown(keyCode, event);
    }

    public void updateWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), WidgetProvider.class));
//        if(appWidgetIds.length > 0)
//            new WidgetProvider().onUpdate(getApplicationContext(),appWidgetManager,appWidgetIds);
        Intent intent = new Intent(this,WidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
        sendBroadcast(intent);

//
//        Context context = this;
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(thisWidget), R.id.widget_list);


//
//                remoteViews.removeAllViews(R.id.widget_checklist_container);
//                remoteViews.setTextViewText(R.id.title_widget_row, getTitle());
//                Log.e("WOWOWOWOWO","HRUHRU TEXT");
//                TextNote textNote = mNoteLab.getRealm().where(TextNote.class).equalTo("note.id", note_id).findFirst();
//                remoteViews.setViewVisibility(R.id.text_widget_row, View.VISIBLE);
//                remoteViews.setViewVisibility(R.id.widget_checklist_container, View.GONE);
//                remoteViews.setViewVisibility(R.id.paint_widget_row, View.GONE);
//                remoteViews.setTextViewText(R.id.text_widget_row, textNote.getText());
//        appWidgetManager.updateAppWidget(thisWidget, null);
//        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
}
