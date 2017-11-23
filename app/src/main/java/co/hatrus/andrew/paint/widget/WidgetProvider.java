package co.hatrus.andrew.paint.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import co.hatrus.andrew.paint.R;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.ui.BaseNoteActivity;
import co.hatrus.andrew.paint.ui.NoteListActivity;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            Log.e("WidgetProvider", "wid id: " + appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], null);
            Intent svcIntent = new Intent(ctxt, WidgetService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetIds[i]), null));
            RemoteViews widget = new RemoteViews(ctxt.getPackageName(),
                    R.layout.widget_layout);
            widget.setRemoteAdapter(R.id.widget_list,
                    svcIntent);

            //intent for onListItemClick
            widget.setPendingIntentTemplate(R.id.widget_list, getBaseNotePendingIntent(ctxt, -1, "ListItemClickTag"));

            // 3 PendingIntents and for buttons click to create new textnote/listnote/paintnote
            widget.setOnClickPendingIntent(R.id.btn_new_textnote, getBaseNotePendingIntent(ctxt, Note.NOTE_TYPE_TEXT, "TextTag"));
            widget.setOnClickPendingIntent(R.id.btn_new_paintnote, getBaseNotePendingIntent(ctxt, Note.NOTE_TYPE_PAINT, "PaintTag"));
            widget.setOnClickPendingIntent(R.id.btn_new_listnote, getBaseNotePendingIntent(ctxt, Note.NOTE_TYPE_LIST, "ListTag"));

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widget_list);
        }
        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }

    private PendingIntent getBaseNotePendingIntent(Context ctxt, int noteType, String Tag) {
        Intent clickIntent = new Intent(ctxt, BaseNoteActivity.class);
        if (noteType != -1)
            clickIntent.putExtra(NoteListActivity.NOTE_TYPE_EXTRA, noteType);
        clickIntent.setAction(Tag);
        return PendingIntent
                .getActivity(ctxt, 0,
                        clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
    }
}