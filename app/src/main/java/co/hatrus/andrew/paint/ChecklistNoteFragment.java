package co.hatrus.andrew.paint;

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
    ArrayAdapter<CheckList> mAdapter;
    ListNote mListNote;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checklist_note,container,false);
        checklist = (ListView) v.findViewById(R.id.checklist_view);
        addBtn = (Button) v.findViewById(R.id.checklist_add_btn);
        mAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,mListNote.getNoteItems());
        checklist.setAdapter(mAdapter);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoteLab.getRealm().beginTransaction();
                CheckList checkListItem = mNoteLab.getRealm().createObject(CheckList.class);
                checkListItem.setId(UUID.randomUUID().toString());
                checkListItem.setItem("new shit item");
                mListNote.getNoteItems().add(checkListItem);
                mNoteLab.getRealm().commitTransaction();
                mAdapter.notifyDataSetChanged();

            }
        });
        checklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) view;
                item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
        Log.e("sSSSSSSSSSSssssss", mListNote.getId());
        Log.e("sSSSSSSSSSSaaa","txt: "+mListNote.getNoteItems().size());
        Log.e("sSSSSSSSSSSuuu","ttl: "+mListNote.getNote().getTitle());
    }

    @Override
    protected void newNote() {
        mListNote = new ListNote();
//        Note n = new Note();
//        n.setType(Note.NOTE_TYPE_LIST);
//        mListNote.getNote(n);
        Log.e("sSSSSSSSSSSssssss",mListNote.getId());
        Log.e("sSSSSSSSSSSssssss",mListNote.getNote().getId());
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
        Log.e("sSSSSSSSSSSssssss", mListNote.getId());
        Log.e("sSSSSSSSSSSaaa","txt: "+mListNote.getNoteItems());
        Log.e("sSSSSSSSSSSuuu","ttl: "+mListNote.getNote().getTitle());
        mNoteLab.getRealm().commitTransaction();
    }
}
