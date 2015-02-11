package co.hatrus.andrew.paint;

import android.support.v4.app.Fragment;

/**
 * Created by user on 11.02.15.
 */
public class TextNoteActivity extends BaseNoteActivity {
    @Override
    protected Fragment createFragment() {
        return new TextNoteFragment();
    }
}
