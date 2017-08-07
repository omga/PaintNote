package co.hatrus.andrew.paint.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.File;
import java.util.ArrayList;

import co.hatrus.andrew.paint.BaseNoteActivity;
import co.hatrus.andrew.paint.NoteListActivity;
import co.hatrus.andrew.paint.R;
import co.hatrus.andrew.paint.model.CheckListItem;
import co.hatrus.andrew.paint.model.ListNote;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.PaintNote;
import co.hatrus.andrew.paint.model.TextNote;
import io.realm.Realm;
import io.realm.RealmResults;

public class NoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final int DEFAULT_MAX_ITEMS_COUNT = 5;
    private static int ITEMS_COUNT = 5;
    ArrayList<RemoteViews> rowList = new ArrayList<>(ITEMS_COUNT);
    private Realm mRealm;
    private RealmResults<Note> mRealmResults;
    private Context ctxt = null;
    private int appWidgetId;

    public NoteViewsFactory(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        onDataSetChanged();
    }

    @Override
    public void onDestroy() {
// no-op
    }

    @Override
    public int getCount() {
        return (ITEMS_COUNT);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return rowList.get(position);
    }

    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    @Override
    public void onDataSetChanged() {
        Log.d("NoteViewsFactory", "onDataSetChanged IC " + ITEMS_COUNT);
        mRealm = Realm.getDefaultInstance();
        mRealmResults = mRealm.where(Note.class).findAllSorted("timeCreated");
        long count = mRealm.where(Note.class).count();
        if (count < DEFAULT_MAX_ITEMS_COUNT) {
            ITEMS_COUNT = (int) count;
            if (ITEMS_COUNT == 0) {
                mRealm.close();
                return;
            }
        } else
            ITEMS_COUNT = DEFAULT_MAX_ITEMS_COUNT;
        rowList.clear();
        for (int i = 0; i < ITEMS_COUNT; i++)
            rowList.add(getRow(mRealmResults.get(i)));

        mRealm.close();

    }

    //returns widget list row depending on note type awful code i know sry
    private RemoteViews getRow(Note note) {
        RemoteViews row = new RemoteViews(ctxt.getPackageName(),
                R.layout.widget_text_row);
        row.removeAllViews(R.id.widget_checklist_container);
        row.setTextViewText(R.id.title_widget_row, note.getTitle());

        switch (note.getType()) {
            case Note.NOTE_TYPE_TEXT:
                TextNote textNote = mRealm.where(TextNote.class).equalTo("note.id", note.getId()).findFirst();
                row.setViewVisibility(R.id.text_widget_row, View.VISIBLE);
                row.setViewVisibility(R.id.widget_checklist_container, View.GONE);
                row.setViewVisibility(R.id.paint_widget_row, View.GONE);
                row.setTextViewText(R.id.text_widget_row, textNote.getText());

                break;
            case Note.NOTE_TYPE_LIST:
                ListNote listNote = mRealm.where(ListNote.class).equalTo("note.id", note.getId()).findFirst();
                row.setViewVisibility(R.id.widget_checklist_container, View.VISIBLE);
                row.setViewVisibility(R.id.text_widget_row, View.GONE);
                row.setViewVisibility(R.id.paint_widget_row, View.GONE);
                RemoteViews rmChild;
                for (CheckListItem checkListItem : listNote.getNoteItems()) {
                    rmChild = new RemoteViews(ctxt.getPackageName(), R.layout.widget_checklist_item);
                    rmChild.setTextViewText(R.id.widget_checklist_item, checkListItem.getItem());
                    if (checkListItem.isChecked()) {
                        rmChild.setInt(R.id.widget_checklist_item, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        rmChild.setImageViewResource(R.id.widget_checklist_icon,R.drawable.btn_check_on_disabled_holo_light);
                    }
                    row.addView(R.id.widget_checklist_container, rmChild);
                }
                break;
            case Note.NOTE_TYPE_PAINT:
                PaintNote paintNote = mRealm.where(PaintNote.class).equalTo("note.id", note.getId()).findFirst();
                row.setViewVisibility(R.id.widget_checklist_container, View.GONE);
                row.setViewVisibility(R.id.text_widget_row, View.GONE);
                row.setViewVisibility(R.id.paint_widget_row, View.VISIBLE);
                File file = new File(ctxt.getExternalFilesDir("painta") + "/" + paintNote.getId() + ".jpg");
                if (!file.exists())
                    break;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                row.setViewVisibility(R.id.paint_widget_row, View.VISIBLE);
                row.setImageViewBitmap(R.id.paint_widget_row, bitmap);
                break;
            default:
                return null;
        }
        Intent i = new Intent(ctxt, BaseNoteActivity.class);
        i.setAction("OnListItemClickTag");
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        i.putExtra(NoteListActivity.NOTE_ID_EXTRA, note.getId());
        i.putExtra(NoteListActivity.NOTE_TITLE_EXTRA, note.getTitle());
        i.putExtra(NoteListActivity.NOTE_TYPE_EXTRA, note.getType());
        row.setOnClickFillInIntent(R.id.row_container, i);
        return row;
    }
}