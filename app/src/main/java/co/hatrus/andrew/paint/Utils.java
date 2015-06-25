package co.hatrus.andrew.paint;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by user on 25.06.15.
 */
public class Utils {
    public static void setRobotoTypeface(Context context, TextView textView) {
        textView.setTypeface(Typeface
                .createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
    }
}
