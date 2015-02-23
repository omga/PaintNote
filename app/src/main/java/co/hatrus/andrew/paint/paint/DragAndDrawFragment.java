package co.hatrus.andrew.paint.paint;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.hatrus.andrew.paint.BaseNoteFragment;
import co.hatrus.andrew.paint.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DragAndDrawFragment extends BaseNoteFragment {


    public DragAndDrawFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drag_and_draw, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setNoteData() {

    }

    @Override
    protected void setNote() {

    }

    @Override
    protected void newNote() {

    }

    @Override
    protected void updateNote() {

    }

    @Override
    protected void saveNote() {

    }

    @Override
    public void setNoteTitle(String title) {

    }
}
