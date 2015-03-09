package co.hatrus.andrew.paint;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

import co.hatrus.andrew.paint.model.CheckList;
import co.hatrus.andrew.paint.model.ListNote;
import co.hatrus.andrew.paint.model.Note;


/**
 * Created by user on 12.02.15.
 */
public class ChecklistNoteFragment extends BaseNoteFragment {
    ListView checklist;
    Button addBtn;
    CheckListAdaprer mCheckListAdaprer;
    ListNote mListNote;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checklist_note,container,false);
        checklist = (ListView) v.findViewById(R.id.checklist_view);
        addBtn = (Button) v.findViewById(R.id.checklist_add_btn);
        mCheckListAdaprer = new CheckListAdaprer(getActivity(), mListNote.getNoteItems());
        checklist.setAdapter(mCheckListAdaprer);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoteLab.getRealm().beginTransaction();
                CheckList checkListItem = mNoteLab.getRealm().createObject(CheckList.class);
                checkListItem.setId(UUID.randomUUID().toString());
                checkListItem.setItem("new shit item");
                checkListItem.setChecked(false);
                mListNote.getNoteItems().add(checkListItem);
                mNoteLab.getRealm().commitTransaction();
                mCheckListAdaprer.notifyDataSetChanged();

            }
        });
        checklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) view;
                CheckList checkList = mListNote.getNoteItems().get(position);
                Log.d("Checklist","bool1: " + checkList.isChecked());
                mNoteLab.getRealm().beginTransaction();
                checkList.setChecked(!checkList.isChecked());
                if ((item.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0)
                    item.setPaintFlags( item.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                else
                    item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mNoteLab.getRealm().commitTransaction();
                Log.d("Checklist","bool2: " + checkList.isChecked());
            }
        });
        return v;
    }

    @Override
    public void setNoteData() {

    }

    @Override
    protected void getNote() {
        mListNote = mNoteLab.getListNoteData(id);

    }

    @Override
    protected void newNote() {
        mListNote = new ListNote();
    }

    @Override
    protected void updateNote() {
    }

    @Override
    protected void saveNote() {
        mNoteLab.updateListNote(mListNote,2);

    }

    @Override
    public void setNoteTitle(String title) {
        mNoteLab.getRealm().beginTransaction();
        mListNote.getNote().setTitle(title);
        mNoteLab.getRealm().commitTransaction();
    }

    private class CheckListAdaprer<CheckList> extends ArrayAdapter<CheckList> {

        public CheckListAdaprer(Context context, List<CheckList> objects) {
            super(context, android.R.layout.simple_list_item_1, android.R.id.text1, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
            }
            co.hatrus.andrew.paint.model.CheckList checkList = mListNote.getNoteItems().get(position);
            TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
            textView.setText(checkList.getItem());
            Log.d("Checklist","bool3: " + checkList.isChecked());
            if(checkList.isChecked())
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else
                textView.setPaintFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            return convertView;
        }
    }
}
