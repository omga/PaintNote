package co.hatrus.andrew.paint;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;


import co.hatrus.andrew.paint.db.DataBaseHelper;
import co.hatrus.andrew.paint.model.Note;
import co.hatrus.andrew.paint.model.TextNote;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class NoteListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * Field to save state (position) of the ListView
     */
    Parcelable mListViewState=null;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */

//    private DataBaseHelper.NoteCursor mCursor;
//    private NoteCursorAdapter mCursorAdapter;
    private RealmResults<Note> mRealmResults;
    private NoteRealmAdapter mRealmAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mCursor = NoteLab.getInstance(getActivity().getApplicationContext()).getCursor();
//        mCursorAdapter = new NoteCursorAdapter(getActivity(),mCursor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        //(mListView).setAdapter(mCursorAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mCursor.close();
        //mRealmResults.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            Note note = mRealmResults.get(position);
            Log.d("OnClick", "pos+1: "+ position+ ", note: "+note.getTitle()+", instanceof: "+ note.getClass()+", getId "+note.getId());
            mListener.onNoteSelected(note, note.getId());

        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
    public void updateUI(){
//        mCursor = NoteLab.getInstance(getActivity().getApplicationContext()).getCursor();
//        mCursorAdapter = new NoteCursorAdapter(getActivity(),mCursor);
        mRealmResults = NoteLab.getInstance(getActivity().getApplicationContext()).getNotes();
        mRealmAdapter = new NoteRealmAdapter(getActivity(), mRealmResults, true);
        (mListView).setAdapter(mRealmAdapter);
        if(mListViewState!=null)
            mListView.onRestoreInstanceState(mListViewState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mListViewState = mListView.onSaveInstanceState();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        public void onNoteSelected(Note note, String id);
    }

    private class NoteCursorAdapter extends CursorAdapter {
        private DataBaseHelper.NoteCursor mCursor;

        public NoteCursorAdapter(Context context, DataBaseHelper.NoteCursor c) {
            super(context, c, 0);
            mCursor = c;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Note note = mCursor.getNote();
            TextView title = (TextView)view;
            title.setText(note.getTitle());

        }

    }

    /**
     * ListView adapter for RealmResults
     */
    private class NoteRealmAdapter extends RealmBaseAdapter<Note> {

        public NoteRealmAdapter(Context context, RealmResults<Note> realmResults, boolean automaticUpdate) {
            super(context, realmResults, automaticUpdate);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView title;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_notelist, parent, false);
            }
            title = (TextView) convertView.findViewById(R.id.title_listnote);
            title.setText(realmResults.get(position).getTitle());
            return convertView;
        }
    }

}
