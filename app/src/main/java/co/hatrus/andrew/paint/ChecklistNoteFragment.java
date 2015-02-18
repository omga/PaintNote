package co.hatrus.andrew.paint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import co.hatrus.andrew.paint.model.ListNote;


/**
 * Created by user on 12.02.15.
 */
public class ChecklistNoteFragment extends BaseNoteFragment {
    ListView checklist;
    Button addBtn;
    ArrayAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checklist_note,container,false);
        checklist = (ListView) v.findViewById(R.id.checklist_view);
        addBtn = (Button) v.findViewById(R.id.checklist_add_btn);
        if(mNote==null)
            mNote = new ListNote();
        mAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,((ListNote)mNote).getItems());
        checklist.setAdapter(mAdapter);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListNote)mNote).getItems().add("new item");
                mAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    @Override
    public void setNoteData() {

    }
}
