package co.hatrus.andrew.paint.paint;

import android.support.v4.app.Fragment;

import co.hatrus.andrew.paint.BaseNoteActivity;
import co.hatrus.andrew.paint.MainFragmentActivity;


public class DragAndDrawActivity extends BaseNoteActivity {
    @Override
    protected co.hatrus.andrew.paint.BaseNoteFragment createFragment() {
        return new DragAndDrawFragment();
    }
}
