package co.hatrus.andrew.paint;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private boolean isListEmpty = false, isEditable = false;
    private AdapterView.OnItemClickListener mOnItemClickListenerStrikeThru, mOnItemClickListenerEditable;

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
                showCustomView(getDialogAddtem());


            }
        });
        if(Build.VERSION.SDK_INT>= 21)
            addBtn.setOnTouchListener(new View.OnTouchListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            addBtn.setElevation(2.0f);
                            return false; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            addBtn.setElevation(10.0f);
                            return false; // if you want to handle the touch event
                    }
                    return false;
                }
            });
        mOnItemClickListenerStrikeThru = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) (view.findViewById(R.id.checlist_item_text));
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
        };
        checklist.setOnItemClickListener(mOnItemClickListenerStrikeThru);
        mOnItemClickListenerEditable = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) (view.findViewById(R.id.checlist_item_text));
                CheckList checkList = mListNote.getNoteItems().get(position);
                showCustomView(getDialogEdittem(checkList));
                if ((item.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0)
                    item.setPaintFlags( item.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                Log.d("Checklist","edit: " + checkList.getItem());
            }
        };
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return v;
    }

    @Override
    public void toggleEditable() {
        isEditable=!isEditable;
        if(isEditable)
            checklist.setOnItemClickListener(mOnItemClickListenerEditable);
        else checklist.setOnItemClickListener(mOnItemClickListenerStrikeThru);
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
    private void showCustomView(MaterialDialog dialog) {
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        neutralAction = dialog.getActionButton(DialogAction.NEUTRAL);
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
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        mDialogItemInput.requestFocus();
    }
    private MaterialDialog getDialogAddtem() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Add Item")
                .customView(R.layout.dialog_customview, true)
                .positiveText("Add")
                .neutralText("Add More")
                .negativeText(android.R.string.cancel)
                .callback(new MaterialButtonAddItemCallback()).build();
        mDialogItemInput = (EditText) dialog.getCustomView().findViewById(R.id.new_item);
        return dialog;
    }

    private MaterialDialog getDialogEdittem(CheckList checkList) {
        MaterialDialog dialog =  new MaterialDialog.Builder(getActivity())
                .title("Edit Item")
                .customView(R.layout.dialog_customview, true)
                .positiveText("Edit")
                .negativeText(android.R.string.cancel)
                .callback(new MaterialButtonEditItemCallback(checkList)).build();
        mDialogItemInput = (EditText) dialog.getCustomView().findViewById(R.id.new_item);
        mDialogItemInput.setText(checkList.getItem());
        return dialog;
    }

    private class MaterialButtonAddItemCallback extends MaterialDialog.ButtonCallback {
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
            showCustomView(getDialogAddtem());
        }
    }

    private class MaterialButtonEditItemCallback extends MaterialDialog.ButtonCallback {
        CheckList checkList;
        public MaterialButtonEditItemCallback(CheckList checkList) {
            super();
            this.checkList = checkList;
        }
        @Override
        public void onPositive(MaterialDialog dialog) {
            mNoteLab.getRealm().beginTransaction();
            checkList.setChecked(false);
            checkList.setItem(mDialogItemInput.getText().toString());
            mNoteLab.getRealm().commitTransaction();
            mCheckListAdaprer.notifyDataSetChanged();
        }

        @Override
        public void onNegative(MaterialDialog dialog) {
        }

    }


    private class CheckListAdaprer<CheckList> extends ArrayAdapter<CheckList> {

        public CheckListAdaprer(Context context, List<CheckList> objects) {
            super(context, R.layout.checklist_item, R.id.checlist_item_text, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.checklist_item, null);
            }
            co.hatrus.andrew.paint.model.CheckList checkList = mListNote.getNoteItems().get(position);
            TextView textView = (TextView)convertView.findViewById(R.id.checlist_item_text);
            textView.setText(checkList.getItem());
            if(checkList.isChecked())
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else
                textView.setPaintFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            return convertView;
        }
    }
}
