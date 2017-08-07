package co.hatrus.andrew.paint;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Andrew on 07.08.2017.
 */
public class PaintNoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
