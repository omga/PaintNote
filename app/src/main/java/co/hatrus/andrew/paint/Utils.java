package co.hatrus.andrew.paint;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;

/**
 * Created by user on 25.06.15.
 */
public class Utils {

    public static void setRobotoTypeface(Context context, TextView textView) {
        textView.setTypeface(Typeface
                .createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
    }

    public static boolean isAppRated(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("user_rated_app", false);
    }

    public static void setAppRated(Context context, boolean rated) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean("user_rated_app", rated)
                .apply();
    }

    public static long getLastTimeRateRequest(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong("rate_app_request_time", 0);
    }

    public static void setLastTimeRateRequest(Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putLong("rate_app_request_time", System.currentTimeMillis())
                .apply();
    }

    public static boolean shouldShowRateDialog(Context c) {

//        final long RATE_APP_REQUEST_INTERVAL = 2 * 60 * 1000;  //3 min test
//        final long RATE_APP_TIME_INSTALLED = 3 * 60 * 1000; //5 min test
        final long RATE_APP_REQUEST_INTERVAL = 5 * 24 * 60 * 60 * 1000;  //5 days
        final long RATE_APP_TIME_INSTALLED = 7 * 24 * 60 * 60 * 1000; //7 days
        // if app is not rated yet, request rate dialog every 5 days, only if app installed for at least 7 days
        long time = System.currentTimeMillis();
        return !isAppRated(c) &&
                (time - getLastTimeRateRequest(c) > RATE_APP_REQUEST_INTERVAL) &&
                (time - NoteLab.getInstance().getFirstEntryTime() > RATE_APP_TIME_INSTALLED);
    }

}
