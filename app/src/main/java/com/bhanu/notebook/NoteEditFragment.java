package com.bhanu.notebook;
import android.support.v4.app.Fragment;

import android.app.AlertDialog;
//import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteEditFragment extends Fragment{

    EditText title;
    EditText message;
    private long noteId = 0;

    private ImageButton noteCatButton;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialogObject, confirmDialogObject;
    private static final String MODIFIED_CATEGORY = "Modified Category";
    private boolean newNote = false;

    public NoteEditFragment(){
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceSpace){

        Bundle bundle = this.getArguments();
        if(bundle != null){
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA, false);
        }

        if(savedInstanceSpace != null){
            savedButtonCategory = (Note.Category) savedInstanceSpace.get(MODIFIED_CATEGORY);
        }

        //inflate fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container,false);


        //Get references to widget
        title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) fragmentLayout.findViewById(R.id.editNoteButton);
        Button savedButton = (Button) fragmentLayout.findViewById(R.id.saveNote);


        Intent intent = getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA, ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);

        if(savedButtonCategory != null){
            noteCatButton.setImageResource(Note.categoryToDrawable(savedButtonCategory));
        }else if(!newNote){
            Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
            noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
        }
        buildCategoryDialog();
        buildConfirmDialog();
        noteCatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                categoryDialogObject.show();
            }
        });


        savedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                confirmDialogObject.show();
            }
        });

        return  fragmentLayout;

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(MODIFIED_CATEGORY, savedButtonCategory);
    }

    private void buildCategoryDialog(){

        final String[] categories = new String[]{"Personal", "Technical", "Quote", "Finance"};
        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Choose Note type");
        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                categoryDialogObject.cancel();
                switch(item){
                    case 0:
                        savedButtonCategory = Note.Category.PERSONAL;
                        noteCatButton.setImageResource(R.drawable.p);
                        break;
                    case 1:
                        savedButtonCategory = Note.Category.TECHNICAL;
                        noteCatButton.setImageResource(R.drawable.t);
                        break;
                    case 2:
                        savedButtonCategory = Note.Category.QUOTE;
                        noteCatButton.setImageResource(R.drawable.q);
                        break;
                    case 3:
                        savedButtonCategory = Note.Category.FINANCE;
                        noteCatButton.setImageResource(R.drawable.f);
                        break;
                }

            }
        });
        categoryDialogObject = categoryBuilder.create();
    }











    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure want to save the note?");
        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                NotebookDBAdapter dbAdapter = new NotebookDBAdapter(getActivity().getBaseContext());
                dbAdapter.open();

                if(newNote){
                    // if it is a new note create in our database
                    dbAdapter.createNote(title.getText() + "", message.getText() + "",
                            (savedButtonCategory == null) ? Note.Category.PERSONAL : savedButtonCategory);
                }
                else{
                    // otherwise it's an old note so update the database
                    dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "",
                            savedButtonCategory);

                }
                dbAdapter.close();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        confirmDialogObject = confirmBuilder.create();

    }

}