package co.hatrus.andrew.paint;

/**
 * Created by user on 11.02.15.
 */
public class ChecklistNoteActivity extends BaseNoteActivity {
    @Override
    protected BaseNoteFragment createFragment() {
        return new ChecklistNoteFragment();
    }
}
