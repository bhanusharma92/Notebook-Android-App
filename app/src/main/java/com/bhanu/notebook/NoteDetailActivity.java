package com.bhanu.notebook;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class NoteDetailActivity extends AppCompatActivity {

    public static final String NEW_NOTE_EXTRA = "NEW_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        createAndAddFragment();

    }

    public void createAndAddFragment(){

        //grab intent and fragment to launch from main activity list fragment
        Intent intent = getIntent();
        MainActivity.FragmentToLaunch fragmentToLaunch =
                (MainActivity.FragmentToLaunch) intent.getSerializableExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA);

        //grabbing our fragment manager and fragment transaction so that we can add our view or edit fragment dynamically
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //choose the correct fragment to load
        switch(fragmentToLaunch){
            case VIEW:
                //create and add note view fragment to note detail activity if that's what we want
                NoteViewFragment noteViewFragment = new NoteViewFragment();
                setTitle(R.string.view_fragment_title);
                fragmentTransaction.add(R.id.note_container, noteViewFragment, "NOTE_VIEW_FRAGMENT");
                break;
            case EDIT:
                //create and add note edit fragment to note detail activity if that's what we want
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                setTitle(R.string.edit_fragment_title);
                fragmentTransaction.add(R.id.note_container, noteEditFragment, "NOTE_VIEW_FRAGMENT");
                break;
            case CREATE:
                NoteEditFragment noteCreateFragment = new NoteEditFragment();
                setTitle(R.string.create_fragment_title);
                Bundle bundle = new Bundle();
                bundle.putBoolean(NEW_NOTE_EXTRA, true);
                noteCreateFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.note_container,noteCreateFragment, "NOTE_CREATE_FRAGMENT");
                break;
        }

        //commit our changes so that everything works
        fragmentTransaction.commit();
    }


}
