package co.hatrus.andrew.paint.paint;


import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.ThemeSingleton;

import co.hatrus.andrew.paint.BaseNoteFragment;
import co.hatrus.andrew.paint.ColorChooserDialog;
import co.hatrus.andrew.paint.R;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.PaintNote;


/**
 * A simple {@link Fragment} subclass.
 */
public class DragAndDrawFragment extends BaseNoteFragment {

    private PaintNote mPaintNote;
    private BoxDrawingView mBoxDrawingView;
    private ImageButton colorButton;
    int selectedColorIndex = -1;
    public DragAndDrawFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container = (ViewGroup) inflater.inflate(R.layout.fragment_drag_and_draw, container, false);
        mBoxDrawingView = (BoxDrawingView) container.findViewById(R.id.box_drawing_view);
        mBoxDrawingView.setFileNameForSaving(mPaintNote.getId()+".jpg");

        colorButton = (ImageButton) container.findViewById(R.id.colorButton);

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomColorChooser();
            }
        });
        return container;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setNoteData() {
        Log.d("DragAndDrawFragment","setNoteData, id : "+ mPaintNote.getId());
        mBoxDrawingView.savePaintData();
    }

    @Override
    protected void getNote() {
        mPaintNote = mNoteLab.getPaintNoteData(id);
    }

    @Override
    protected void newNote() {
        mPaintNote = new PaintNote();
//        Note n = new Note();
//        n.setType(Note.NOTE_TYPE_TEXT);
//        mPaintNote.setNote(n);
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
    }

    @Override
    public void setNoteTitle(String title) {
        mNoteLab.getRealm().beginTransaction();
        mPaintNote.getNote().setTitle(title);
        //save paint data (?)
        mNoteLab.getRealm().commitTransaction();
    }

    private void showCustomColorChooser() {
        new ColorChooserDialog().show(getActivity(), selectedColorIndex, new ColorChooserDialog.Callback() {
            @Override
            public void onColorSelection(int index, int color, int darker) {
                selectedColorIndex = index;
                mBoxDrawingView.setLineColor(color);
                GradientDrawable shapeDrawable = (GradientDrawable)colorButton.getBackground();
                shapeDrawable.setColor(color);
//                ThemeSingleton.get().positiveColor = color;
//                ThemeSingleton.get().neutralColor = color;
//                ThemeSingleton.get().negativeColor = color;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    getActivity().getWindow().setStatusBarColor(darker);
            }
        });
    }
}
