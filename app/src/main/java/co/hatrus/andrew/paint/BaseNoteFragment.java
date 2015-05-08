package co.hatrus.andrew.paint;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.notificationstuff.AlarmManagerBroadcastReceiver;
import co.hatrus.andrew.paint.notificationstuff.NotificationService;

/**
 * Created by user on 12.02.15.
 */
public abstract class BaseNoteFragment extends Fragment {

    public static final String EXTRA_NOTE_ID = "NoteFragment.NoteId";
    public static final String EXTRA_NOTE_TYPE = "NoteFragment.NoteType";
    protected NoteLab mNoteLab;
    protected String id;
    protected int type;
    protected Note mNote;

    protected BaseNoteFragment putArgs(String id, int type) {
        Bundle args = new Bundle();
        args.putString(EXTRA_NOTE_ID, id);
        args.putInt(EXTRA_NOTE_TYPE, type);
        this.setArguments(args);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString(BaseNoteFragment.EXTRA_NOTE_ID);
        type = getArguments().getInt(BaseNoteFragment.EXTRA_NOTE_TYPE, 1);
        mNoteLab = NoteLab.getInstance(getActivity());
        if (id != null)
            getNote();
        else
            newNote();
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.action_save_note:
//                setNoteData();
                if (id != null)
                    updateNote();
                else
                    saveNote();
                getActivity().finish();
                break;
            case R.id.action_delete_note:
                deleteNote();
                ((BaseNoteActivity) getActivity()).updateWidgets();
                NavUtils.navigateUpFromSameTask(getActivity());
                break;
            case R.id.action_edit_note:
                toggleEditable();
                break;
            case android.R.id.home:
//                setNoteData();
                if (id != null)
                    updateNote();
                else
                    saveNote();

                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return true;
    }
    public void setReminder(long timeInMillis) {
//        mNote.setTimeRemind(timeInMillis);
//        mNote.setReminderEnabled(true);


        Log.d("BaseNoteFragment","setReminder " + mNote.getId() + "type " + mNote.getType());
//        AlarmManager alarmMgr;
//        PendingIntent alarmIntent;
//        alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getActivity(), NotificationService.class);
//        intent.putExtra(NoteListActivity.NOTE_TYPE_EXTRA,mNote.getType());
//        intent.putExtra(NoteListActivity.NOTE_ID_EXTRA,mNote.getId());
//        alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
//
//        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() +
//                        60 * 1000, alarmIntent);

        //new AlarmManagerBroadcastReceiver().setOnetimeTimer(getActivity());
//        Intent intent = new Intent(getActivity(), AlarmManagerBroadcastReceiver.class);
//        intent.putExtra(NoteListActivity.NOTE_TYPE_EXTRA,mNote.getType());
//        intent.putExtra(NoteListActivity.NOTE_ID_EXTRA,mNote.getId());
        new AlarmManagerBroadcastReceiver().setNotificationAlarm(getActivity(), mNote);
    }
    public abstract void toggleEditable();

    public abstract void deleteNote();

    protected abstract void getNote();

    protected abstract void newNote();

    protected abstract void updateNote();

    protected abstract void saveNote();

    public abstract void setNoteTitle(String title);
}
