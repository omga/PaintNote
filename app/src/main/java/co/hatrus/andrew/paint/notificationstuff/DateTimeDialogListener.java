package co.hatrus.andrew.paint.notificationstuff;


import android.app.FragmentManager;
import android.content.Context;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.hatrus.andrew.paint.BaseNoteActivity;
import co.hatrus.andrew.paint.model.Note;
import io.realm.Realm;

/**
 * Created by user on 11.05.15.
 */
public class DateTimeDialogListener implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    int year, month, day, hour, minute;

    Note mNote;
    private Context mContext;
    Calendar now = Calendar.getInstance();
    FragmentManager fm;

    public DateTimeDialogListener(Note note, Context context, FragmentManager fm) {
        mNote = note;
        mContext = context;
        this.fm = fm;

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;
        showTimeDialog();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        hour = hourOfDay;
        this.minute = minute;
        Calendar c = getTimeInMillis();
        Realm r = Realm.getInstance(mContext);
        r.beginTransaction();
        mNote.setReminderEnabled(true);
        mNote.setTimeRemind(c.getTimeInMillis());
        r.commitTransaction();

        setAlarm(c.getTimeInMillis());

        ((BaseNoteActivity) mContext).setReminderTextView(
                new SimpleDateFormat("EE MMM dd, HH:mm").format(c.getTime()));
    }

    public Calendar getTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        return calendar;
    }

    private void setAlarm(long millis) {
        AlarmManagerBroadcastReceiver.setNotificationAlarm(mContext, mNote);
    }

    public void showDialog() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(fm, "DateTimeDialog");

    }

    public void showTimeDialog() {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(false);
        tpd.show(fm, "DateTimeDialog");
    }


}
