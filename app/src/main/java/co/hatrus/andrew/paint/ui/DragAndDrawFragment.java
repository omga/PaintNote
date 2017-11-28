package co.hatrus.andrew.paint.ui;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import co.hatrus.andrew.paint.R;
import co.hatrus.andrew.paint.helper.ColorChooserDialog;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.PaintNote;
import co.hatrus.andrew.paint.view.BoxDrawingView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DragAndDrawFragment extends BaseNoteFragment {

    int selectedColorIndex = -1;
    private int mCurrentLineColor;
    private boolean isEraserOn = false;
    private PaintNote mPaintNote;
    private BoxDrawingView mBoxDrawingView;
    private ImageButton mColorButton, mEraserButton;

    public DragAndDrawFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container = (ViewGroup) inflater.inflate(R.layout.fragment_drag_and_draw, container, false);
        mBoxDrawingView = container.findViewById(R.id.box_drawing_view);
        mBoxDrawingView.setFileNameForSaving(mPaintNote.getId() + ".jpg");

        mColorButton = container.findViewById(R.id.colorButton);
        mEraserButton = container.findViewById(R.id.eraserButton);
        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.colorButton)
                showCustomColorChooser();
            else if (v.getId() == R.id.eraserButton)
                toggleEraser();
        };

        if (id != null)
            mBoxDrawingView.setEditable(false);

        mColorButton.setOnClickListener(onClickListener);
        mEraserButton.setOnClickListener(onClickListener);
        return container;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void toggleEditable() {
        //TODO enable/disable drawing on view. Or not?
        mBoxDrawingView.setEditable(!mBoxDrawingView.isEditable());
    }

    @Override
    public void deleteNote() {
        mBoxDrawingView.removePaintData();
        mNoteLab.deleteObject(mPaintNote, mPaintNote.getNote());
    }

    private void setNoteData() {
        Log.d("DragAndDrawFragment", "setNoteData, id : " + mPaintNote.getId());
        mBoxDrawingView.savePaintData();
    }

    @Override
    protected void getNote() {
        mPaintNote = mNoteLab.getPaintNoteData(id);
        mNote = mPaintNote.getNote();
    }

    @Override
    protected void newNote() {
        mPaintNote = new PaintNote();
        mNote = mPaintNote.getNote();
        mNote.setType(Note.NOTE_TYPE_PAINT);
        mPaintNote.setNote(mNote);
        Log.e("sSSSSSSSSSSssssss", mPaintNote.getId());
//        Log.e("sSSSSSSSSSSssssss",n.getId());
    }

    @Override
    protected void updateNote() {
        setNoteData();
    }

    @Override
    protected void saveNote() {
        mNoteLab.updatePaintNote(mPaintNote);
        Log.d("DragAndDrawFragment", "setNoteData, id : " + mPaintNote.getId());
        mBoxDrawingView.savePaintData();
    }

    @Override
    public void setNoteTitle(String title) {
        mNoteLab.getRealm().beginTransaction();
        mPaintNote.getNote().setTitle(title);
        //save paint data (?)
        mNoteLab.getRealm().commitTransaction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        GradientDrawable shapeDrawable = (GradientDrawable) mColorButton.getBackground();
        shapeDrawable.setColor(getResources().getColor(R.color.fab_main));
    }

    private void showCustomColorChooser() {
        new ColorChooserDialog().show(getActivity(), selectedColorIndex, (index, color, darker) -> {
            selectedColorIndex = index;
            mBoxDrawingView.setLineColor(color);
            GradientDrawable shapeDrawable = (GradientDrawable) mColorButton.getBackground();
            shapeDrawable.setColor(color);
//                ThemeSingleton.get().positiveColor = color;
//                ThemeSingleton.get().neutralColor = color;
//                ThemeSingleton.get().negativeColor = color;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    getActivity().getWindow().setStatusBarColor(darker);
        });
    }

    private void toggleEraser() {
        if (!isEraserOn) {
            mCurrentLineColor = mBoxDrawingView.getLineColor();
            int colorBG = getResources().getColor(R.color.paint_bg);
            mColorButton.setEnabled(false);
            mBoxDrawingView.setLineColor(colorBG);
            mBoxDrawingView.setLineWidth(BoxDrawingView.DEFAULT_LINE_WIDTH * 2);
            GradientDrawable shapeDrawable = (GradientDrawable) mEraserButton.getBackground();
            shapeDrawable.setColor(colorBG);
            isEraserOn = true;
        } else {
            mColorButton.setEnabled(true);
            mBoxDrawingView.setLineColor(mCurrentLineColor);
            mBoxDrawingView.setLineWidth(BoxDrawingView.DEFAULT_LINE_WIDTH);
            GradientDrawable shapeDrawable = (GradientDrawable) mEraserButton.getBackground();
            shapeDrawable.setColor(getResources().getColor(R.color.fab_main));
            isEraserOn = false;
        }

    }
}
