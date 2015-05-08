package co.hatrus.andrew.paint.notificationstuff;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import co.hatrus.andrew.paint.BaseNoteActivity;
import co.hatrus.andrew.paint.NoteListActivity;
import co.hatrus.andrew.paint.R;

public class NotificationService extends Service {

    NotificationManager nm;
    int type;
    String id;

    @Override
    public void onCreate() {
        Log.d("NotificationService", "onCreate " );
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        id = intent.getStringExtra(NoteListActivity.NOTE_ID_EXTRA);
        type = intent.getIntExtra(NoteListActivity.NOTE_TYPE_EXTRA,-1);
        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this).
                        setSmallIcon(R.drawable.abc_ic_go_search_api_mtrl_alpha)
                        .setContentText("blablatext")
                        .setContentTitle("bla title")
                        .setTicker("ticker???").setDefaults(Notification.DEFAULT_LIGHTS);

        Intent intent = new Intent(this, BaseNoteActivity.class);
        intent.putExtra(NoteListActivity.NOTE_ID_EXTRA, id);
        intent.putExtra(NoteListActivity.NOTE_TYPE_EXTRA, type);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(BaseNoteActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);

        nm.notify(1, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
