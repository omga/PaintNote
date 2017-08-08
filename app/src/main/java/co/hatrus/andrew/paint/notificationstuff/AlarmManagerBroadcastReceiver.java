package co.hatrus.andrew.paint.notificationstuff;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Iterator;
import java.util.Random;

import co.hatrus.andrew.paint.BaseNoteActivity;
import co.hatrus.andrew.paint.NoteLab;
import co.hatrus.andrew.paint.NoteListActivity;
import co.hatrus.andrew.paint.R;
import co.hatrus.andrew.paint.model.Note;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "AlarmManagerReceiver";
    public static final String ONE_TIME = "onetime";
    Context mContext;


    @Override

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive ");
        if (intent == null || intent.getAction() == null)
            return;
        mContext = context;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");

        wl.acquire();

        //Set 3 first notification after device reboot
        if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "onReceive " +
                    intent.getStringExtra(NoteListActivity.NOTE_ID_EXTRA) +
                    " type " + intent.getIntExtra(NoteListActivity.NOTE_TYPE_EXTRA, -1) +
                    " title " + intent.getStringExtra(NoteListActivity.NOTE_TITLE_EXTRA));

            String id = intent.getStringExtra(NoteListActivity.NOTE_ID_EXTRA),
                    title = intent.getStringExtra(NoteListActivity.NOTE_TITLE_EXTRA);
            int type = intent.getIntExtra(NoteListActivity.NOTE_TYPE_EXTRA, -1);
            if(title == null || title.isEmpty())
                title = context.getString(R.string.app_name);
            sendNotif(id, type, title);
        }
        Iterator<Note> iterator =
                NoteLab.getInstance().
                        getUpgoingNoteReminders();
        for (int i = 0; i < 3; i++) {
            if (iterator.hasNext())
                setNotificationAlarm(mContext, iterator.next());
        }

        wl.release();
    }

    public static void setNotificationAlarm(Context context, Note note) {
        Log.d(TAG, "setNotificationAlarm ");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(NoteListActivity.NOTE_TYPE_EXTRA, note.getType());
        intent.putExtra(NoteListActivity.NOTE_ID_EXTRA, note.getId());
        intent.putExtra(NoteListActivity.NOTE_TITLE_EXTRA, note.getTitle());
        intent.setAction("setNotificationAlarm");

        if (PendingIntent.getBroadcast(context, note.getTimeCreated().hashCode(), intent, PendingIntent.FLAG_NO_CREATE) == null) {
            Log.d(TAG, "setNotificationAlarm create PI");
            PendingIntent pi = PendingIntent.getBroadcast(context, note.getTimeCreated().hashCode(), intent, 0);
            am.set(AlarmManager.RTC_WAKEUP, note.getTimeRemind(), pi);
        }
    }

    public static void cancelAlarm(Context context, Note note) {
        Log.d(TAG, "cancelAlarm");

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(NoteListActivity.NOTE_TYPE_EXTRA, note.getType());
        intent.putExtra(NoteListActivity.NOTE_ID_EXTRA, note.getId());
        intent.putExtra(NoteListActivity.NOTE_TITLE_EXTRA, note.getTitle());
        intent.setAction("setNotificationAlarm");

        PendingIntent sender = PendingIntent.getBroadcast(context, note.getTimeCreated().hashCode(), intent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        am.cancel(sender);

    }

    void sendNotif(String id, int type, String title) {
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_list_white)
                        .setContentText("Click to see your note")
                        .setContentTitle(title)
                        .setTicker("Note Reminder!")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true);


        Intent intent = new Intent(mContext, BaseNoteActivity.class);
        intent.putExtra(NoteListActivity.NOTE_ID_EXTRA, id);
        intent.putExtra(NoteListActivity.NOTE_TYPE_EXTRA, type);
        intent.putExtra(NoteListActivity.NOTE_TITLE_EXTRA, title);
//        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, 0);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
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

        nm.notify(new Random().nextInt(), builder.build());
        NoteLab.getInstance().disableAlarm(id);
    }


    public void setAlarm(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        intent.putExtra(ONE_TIME, Boolean.FALSE); // Задаем параметр интента

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

// Устанавливаем интервал срабатывания в 5 секунд.

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5, pi);

    }






    public void setOnetimeTimer(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        intent.putExtra(ONE_TIME, Boolean.TRUE); // Задаем параметр интента

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);

    }
}