package co.hatrus.andrew.paint;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import co.hatrus.andrew.paint.model.CheckListItem;
import co.hatrus.andrew.paint.model.ListNote;
import co.hatrus.andrew.paint.model.Note;


/**
 * Created by user on 12.02.15.
 */
public class ChecklistNoteFragment extends BaseNoteFragment {

    private ListView checklist;
    private ImageButton addBtn;
    private CheckListAdapter mCheckListAdapter;
    private ListNote mListNote;
    private EditText mDialogItemInput;
    private View positiveAction, neutralAction;
    private FrameLayout.LayoutParams btnLayoutParams;
    private boolean isListEmpty = false, isEditable = false;
    private AdapterView.OnItemClickListener mOnItemClickListenerStrikeThru, mOnItemClickListenerEditable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checklist_note, container, false);
        checklist = (ListView) v.findViewById(R.id.checklist_view);
        addBtn = (ImageButton) v.findViewById(R.id.checklist_add_btn);
        mCheckListAdapter = new CheckListAdapter(getActivity(), mListNote.getNoteItems());
        checklist.setAdapter(mCheckListAdapter);
        btnLayoutParams = (FrameLayout.LayoutParams) addBtn.getLayoutParams();
        if (isListEmpty) {
            btnLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            addBtn.setLayoutParams(btnLayoutParams);
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomView(getDialogAddtem());

            }
        });
        if (Build.VERSION.SDK_INT >= 21)
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
                CheckListItem checkListItem = mListNote.getNoteItems().get(position);
                mNoteLab.getRealm().beginTransaction();
                checkListItem.setChecked(!checkListItem.isChecked());
                if ((item.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0)
                    item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                else
                    item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mNoteLab.getRealm().commitTransaction();
            }
        };
        checklist.setOnItemClickListener(mOnItemClickListenerStrikeThru);
        mOnItemClickListenerEditable = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) (view.findViewById(R.id.checlist_item_text));
                CheckListItem checkListItem = mListNote.getNoteItems().get(position);
                showCustomView(getDialogEdittem(checkListItem));
                if ((item.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0)
                    item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        };
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return v;
    }

    @Override
    public void toggleEditable() {
        isEditable = !isEditable;
        if (isEditable)
            checklist.setOnItemClickListener(mOnItemClickListenerEditable);
        else checklist.setOnItemClickListener(mOnItemClickListenerStrikeThru);
    }

    @Override
    public void deleteNote() {
        mNoteLab.deleteObjectList(mListNote.getNoteItems());
        mNoteLab.deleteObject(mListNote, mListNote.getNote());
    }

    @Override
    protected void getNote() {
        mListNote = mNoteLab.getListNoteData(id);
        mNote = mListNote.getNote();

    }

    @Override
    protected void newNote() {
        mListNote = new ListNote();
        mNote = mListNote.getNote();
        mNote.setType(Note.NOTE_TYPE_LIST);
        mListNote.setNote(mNote);
        isListEmpty = true;
    }

    @Override
    protected void updateNote() {
    }

    @Override
    protected void saveNote() {
        mNoteLab.updateListNote(mListNote);

    }

    @Override
    public void setNoteTitle(String title) {
        mNoteLab.getRealm().beginTransaction();
        mListNote.getNote().setTitle(title);
        mNoteLab.getRealm().commitTransaction();
    }

    public void addChecklistItem(String text) {
        mNoteLab.getRealm().beginTransaction();
        CheckListItem checkListItem = mNoteLab
                .getRealm()
                .createObject(CheckListItem.class,
                        UUID.randomUUID().toString());
        checkListItem.setItem(text);
        checkListItem.setChecked(false);
        mListNote.getNoteItems().add(checkListItem);
        mNoteLab.getRealm().commitTransaction();
        mCheckListAdapter.notifyDataSetChanged();
    }

    private void fabBtnToCorner() {
        btnLayoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
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
                if (s.toString().trim().length() > 0) {
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
                .title(R.string.add_item)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.add_text)
                .neutralText(R.string.add_more)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialButtonAddItemCallback()).build();
        mDialogItemInput = (EditText) dialog.getCustomView().findViewById(R.id.new_item);
        return dialog;
    }

    private MaterialDialog getDialogEdittem(CheckListItem checkListItem) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.edit_item)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.edit)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialButtonEditItemCallback(checkListItem)).build();
        mDialogItemInput = (EditText) dialog.getCustomView().findViewById(R.id.new_item);
        mDialogItemInput.setText(checkListItem.getItem());
        return dialog;
    }

    private class MaterialButtonAddItemCallback extends MaterialDialog.ButtonCallback {
        @Override
        public void onPositive(MaterialDialog dialog) {
            addChecklistItem(mDialogItemInput.getText().toString());
            if (isListEmpty) {
                fabBtnToCorner();
                isListEmpty = false;
            }
        }

        @Override
        public void onNegative(MaterialDialog dialog) {
        }

        @Override
        public void onNeutral(MaterialDialog dialog) {
            addChecklistItem(mDialogItemInput.getText().toString());
            if (isListEmpty) {
                fabBtnToCorner();
                isListEmpty = false;
            }
            showCustomView(getDialogAddtem());
        }
    }

    private class MaterialButtonEditItemCallback extends MaterialDialog.ButtonCallback {
        CheckListItem mCheckListItem;

        public MaterialButtonEditItemCallback(CheckListItem checkListItem) {
            super();
            this.mCheckListItem = checkListItem;
        }

        @Override
        public void onPositive(MaterialDialog dialog) {
            mNoteLab.getRealm().beginTransaction();
            mCheckListItem.setChecked(false);
            mCheckListItem.setItem(mDialogItemInput.getText().toString());
            mNoteLab.getRealm().commitTransaction();
            mCheckListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNegative(MaterialDialog dialog) {
        }

    }


    private class CheckListAdapter<CheckList> extends ArrayAdapter<CheckList> {

        public CheckListAdapter(Context context, List<CheckList> objects) {
            super(context, R.layout.item_checklist, R.id.checlist_item_text, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_checklist, null);
            }
            CheckListItem checkListItem = mListNote.getNoteItems().get(position);
            TextView textView = (TextView) convertView.findViewById(R.id.checlist_item_text);
            textView.setText(checkListItem.getItem());
            if (checkListItem.isChecked())
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            return convertView;
        }
    }
}
