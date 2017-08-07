package co.hatrus.andrew.paint;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * Created by user on 25.06.15.
 */
public class Utils {

    private static long RATE_APP_REQUEST_INTERVAL = 5 * 24 * 60 * 60 * 1000; // 5 days

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
                .putLong("user_rated_app", System.currentTimeMillis())
                .apply();
    }

    public static boolean shouldShowRateDialog(Context c) {
        return !isAppRated(c) &&
                (System.currentTimeMillis() - getLastTimeRateRequest(c) > RATE_APP_REQUEST_INTERVAL);
    }
}
