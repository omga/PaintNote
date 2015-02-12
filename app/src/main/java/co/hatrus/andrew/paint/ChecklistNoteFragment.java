package co.hatrus.andrew.paint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import co.hatrus.andrew.paint.model.ListNote;


/**
 * Created by user on 12.02.15.
 */
public class ChecklistNoteFragment extends BaseNoteFragment {
    ListView checklist;
    ListNote note;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checklist_note,container,false);
        checklist = (ListView) v;
        note = (ListNote) NoteLab.getInstance(getActivity()).getNote(getArguments().getInt(BaseNoteFragment.EXTRA_NOTE_ID));
        note.toJSON();
        checklist.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,note.getNotes()));
        return v;
    }
}
