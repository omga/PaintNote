package co.hatrus.andrew.paint.notificationstuff;

import java.text.Format;

import java.text.SimpleDateFormat;

import java.util.Date;

import android.app.AlarmManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;

import android.os.Bundle;

import android.os.PowerManager;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import co.hatrus.andrew.paint.BaseNoteActivity;
import co.hatrus.andrew.paint.BaseNoteFragment;
import co.hatrus.andrew.paint.NoteListActivity;
import co.hatrus.andrew.paint.R;
import co.hatrus.andrew.paint.model.Note;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    Context mContext;


    @Override

    public void onReceive(Context context, Intent intent) {
        mContext = context;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");

// Осуществляем блокировку

        wl.acquire();



// Здесь можно делать обработку.

        Bundle extras = intent.getExtras();
        Log.d("BaseNoteFragment", "setReminder " +
                intent.getStringExtra(NoteListActivity.NOTE_ID_EXTRA) +
                " type " + intent.getIntExtra(NoteListActivity.NOTE_TYPE_EXTRA, -1)+
                " title " + intent.getStringExtra(NoteListActivity.NOTE_TITLE_EXTRA));
        StringBuilder msgStr = new StringBuilder();



        if (extras != null) {

            // проверяем параметр ONE_TIME, если это одиночный будильник,

            // выводим соответствующее сообщение.

            msgStr.append("Одноразовый "+ extras.getString(NoteListActivity.NOTE_ID_EXTRA));

        }

        Format formatter = new SimpleDateFormat("hh:mm:ss a");

        msgStr.append(formatter.format(new Date()));



        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
        String id =intent.getStringExtra(NoteListActivity.NOTE_ID_EXTRA),
            title =intent.getStringExtra(NoteListActivity.NOTE_TITLE_EXTRA);
        int type = intent.getIntExtra(NoteListActivity.NOTE_TYPE_EXTRA, -1);
        sendNotif(id,type,title);

// Разблокируем поток.

        wl.release();

    }



    public void setAlarm(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        intent.putExtra(ONE_TIME, Boolean.FALSE); // Задаем параметр интента

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

// Устанавливаем интервал срабатывания в 5 секунд.

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5, pi);

    }



    public void cancelAlarm(Context context) {

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(sender); // Отменяем будильник, связанный с интентом данного класса

    }



    public void setOnetimeTimer(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        intent.putExtra(ONE_TIME, Boolean.TRUE); // Задаем параметр интента

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);

    }
    public void setNotificationAlarm(Context context, Note note) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(NoteListActivity.NOTE_TYPE_EXTRA, note.getType());
        intent.putExtra(NoteListActivity.NOTE_ID_EXTRA, note.getId());
        intent.putExtra(NoteListActivity.NOTE_TITLE_EXTRA, note.getTitle());

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +10000, pi);
    }

    void sendNotif(String id, int type, String title) {
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext).
                        setSmallIcon(R.drawable.ic_launcher)
                        .setContentText("Click to see your note")
                        .setContentTitle(title)
                        .setTicker("Note Reminder!").setDefaults(Notification.DEFAULT_LIGHTS);

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

        nm.notify(1, builder.build());
    }
}