package co.hatrus.andrew.paint.widget;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

import co.hatrus.andrew.paint.BaseNoteActivity;
import co.hatrus.andrew.paint.NoteListActivity;
import co.hatrus.andrew.paint.R;

public class WidgetProvider extends AppWidgetProvider {
    public static String EXTRA_WORD=
            "com.commonsware.android.appwidget.lorem.WORD";
    public static int sRandomNumberForUpdatingWidgetIdTrickDontDoItAtHome = 322;

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        sRandomNumberForUpdatingWidgetIdTrickDontDoItAtHome = new Random().nextInt(1000);
        Log.e("WidgetProvider", "randomnum: "+ sRandomNumberForUpdatingWidgetIdTrickDontDoItAtHome);
        for (int i=0; i<appWidgetIds.length; i++) {
            Log.e("WidgetProvider", "wid id: " + appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], null);
            Intent svcIntent=new Intent(ctxt, WidgetService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]+sRandomNumberForUpdatingWidgetIdTrickDontDoItAtHome);
            //svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            svcIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetIds[i]+sRandomNumberForUpdatingWidgetIdTrickDontDoItAtHome), null));
            RemoteViews widget=new RemoteViews(ctxt.getPackageName(),
                    R.layout.widget_layout);

            widget.setRemoteAdapter( appWidgetIds[i],R.id.widget_list,
                    svcIntent);

            Intent clickIntent=new Intent(ctxt, BaseNoteActivity.class);
            PendingIntent clickPI=PendingIntent
                    .getActivity(ctxt, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            widget.setPendingIntentTemplate(R.id.widget_list, clickPI);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }
        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }
}