package com.bhanu.notebook;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityListFragment extends ListFragment {


//    public MainActivityListFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }

    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    /*
        String[] values = new String[]{"Android", "Iphone", "Ubuntu", "Arch linux",
                "Windows", "Cromebook"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);

        setListAdapter(adapter);
    */

        /* We no longer needs them as we are going to read notes from our database
        notes= new ArrayList<Note>();
        notes.add(new Note("This is a new note title", "This is body of the note", Note.Category.PERSONAL));
        notes.add(new Note("Another title", "goooooooooooooo out and  playe", Note.Category.TECHNICAL));
        notes.add(new Note("yet another title", "This is body of the note", Note.Category.PERSONAL));
        notes.add(new Note("one more title", "This is body of the note", Note.Category.FINANCE));
        notes.add(new Note("lorem ipsum title", "This is body of the note", Note.Category.QUOTE));
        notes.add(new Note("second This is a new note title", "This is body of the note", Note.Category.PERSONAL));
        notes.add(new Note("second Another title", "goooooooooooooo out and  playe", Note.Category.TECHNICAL));
        notes.add(new Note("second yet another title", "This is body of the note", Note.Category.PERSONAL));
        notes.add(new Note("second one more title", "This is body of the note", Note.Category.FINANCE));
        notes.add(new Note("second lorem ipsum title", "This is body of the note", Note.Category.QUOTE));
        */

        NotebookDBAdapter dbAdapter = new NotebookDBAdapter(getActivity().getBaseContext());
        dbAdapter.open();
        notes = dbAdapter.getAllNotes();
        dbAdapter.close();
        noteAdapter =  new NoteAdapter(getActivity(), notes);
        setListAdapter(noteAdapter);

        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v,position, id);

        launchNoteDetailActivity(MainActivity.FragmentToLaunch.VIEW,  position);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        //give me the position of whatever item i longed pressed on
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowPosition = info.position;
        Note note = (Note) getListAdapter().getItem(rowPosition);


        //return to us id of whatever item is selected
        switch(item.getItemId()){
            //if we press edit
            case R.id.edit:
                launchNoteDetailActivity(MainActivity.FragmentToLaunch.EDIT, rowPosition);
                return true;

            case R.id.delete:
                NotebookDBAdapter dbAdapter = new NotebookDBAdapter(getActivity().getBaseContext());
                dbAdapter.open();
                dbAdapter.deleteNote(note.getId());
                notes.clear();
                notes.addAll(dbAdapter.getAllNotes());
                noteAdapter.notifyDataSetChanged();
                dbAdapter.close();

        }
        return super.onContextItemSelected(item);
    }



    public void launchNoteDetailActivity(MainActivity.FragmentToLaunch ftl,int position){

        // Grab note information associated with whatever note item we clicked on
        Note note =(Note) getListAdapter().getItem(position);

        // Create new intent that launches our noteDetailActivity
        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);

        // Pass along the information of the note we clicked on to our noteDetailActivity
        intent.putExtra(MainActivity.NOTE_TITLE_EXTRA, note.getTitle());
        intent.putExtra(MainActivity.NOTE_MESSAGE_EXTRA, note.getMessage());
        intent.putExtra(MainActivity.NOTE_ID_EXTRA, note.getId());
        intent.putExtra(MainActivity.NOTE_CATEGORY_EXTRA, note.getCategory());

        switch(ftl){
            case VIEW:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLaunch.VIEW);
                break;
            case EDIT:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLaunch.EDIT);
                break;
        }

        startActivity(intent);

    }


}
