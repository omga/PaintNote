package co.hatrus.andrew.paint;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;
import java.util.UUID;

import co.hatrus.andrew.paint.model.CheckList;
import co.hatrus.andrew.paint.model.ListNote;



/**
 * Created by user on 12.02.15.
 */
public class ChecklistNoteFragment extends BaseNoteFragment {
    ListView checklist;
    ImageButton addBtn;
    CheckListAdaprer mCheckListAdaprer;
    ListNote mListNote;
    private EditText mDialogItemInput;
    private View positiveAction, neutralAction;
    private FrameLayout.LayoutParams btnLayoutParams;
    private boolean isListEmpty = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checklist_note,container,false);
        checklist = (ListView) v.findViewById(R.id.checklist_view);
        addBtn = (ImageButton) v.findViewById(R.id.checklist_add_btn);
        mCheckListAdaprer = new CheckListAdaprer(getActivity(), mListNote.getNoteItems());
        checklist.setAdapter(mCheckListAdaprer);
        btnLayoutParams = (FrameLayout.LayoutParams)addBtn.getLayoutParams();
        if(isListEmpty) {
            btnLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            addBtn.setLayoutParams(btnLayoutParams);
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomView();


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
    public void deleteNote() {
        mNoteLab.deleteObjectList(mListNote.getNoteItems());
        mNoteLab.deleteObject(mListNote, mListNote.getNote());
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
        isListEmpty = true;
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

    public void addChecklistItem(String text){
        mNoteLab.getRealm().beginTransaction();
        CheckList checkListItem = mNoteLab.getRealm().createObject(CheckList.class);
        checkListItem.setId(UUID.randomUUID().toString());
        checkListItem.setItem(text);
        checkListItem.setChecked(false);
        mListNote.getNoteItems().add(checkListItem);
        mNoteLab.getRealm().commitTransaction();
        mCheckListAdaprer.notifyDataSetChanged();
    }

    private void fabBtnToCorner(){
        btnLayoutParams.gravity = Gravity.BOTTOM|Gravity.RIGHT;
        addBtn.setLayoutParams(btnLayoutParams);
    }
    private void showCustomView() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Add Item")
                .customView(R.layout.dialog_customview, true)
                .positiveText("Add")
                .neutralText("Add More")
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        addChecklistItem(mDialogItemInput.getText().toString());
                            if(isListEmpty) {

                                fabBtnToCorner();
                                isListEmpty=false;
                            }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        addChecklistItem(mDialogItemInput.getText().toString());
                        if(isListEmpty) {

                            fabBtnToCorner();
                            isListEmpty=false;
                        }
                        showCustomView();
                    }
                }).build();
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        neutralAction = dialog.getActionButton(DialogAction.NEUTRAL);
        mDialogItemInput = (EditText) dialog.getCustomView().findViewById(R.id.new_item);
        mDialogItemInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0) {
                    positiveAction.setEnabled(true);
                    neutralAction.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
        neutralAction.setEnabled(false); // disabled by default
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        mDialogItemInput.requestFocus();
//        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
//                showSoftInput(mDialogItemInput, InputMethodManager.SHOW_IMPLICIT);
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
            if(checkList.isChecked())
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else
                textView.setPaintFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            return convertView;
        }
    }
}
