package com.bhanu.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bhanu on 6/6/16.
 */
public class NoteAdapter extends ArrayAdapter<Note> {


    public static class ViewHolder{
        TextView title;
        TextView note;
        ImageView noteIcon;
    }

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);

        // Create new ViewHolder
        ViewHolder viewHolder;


        // Check if existing view is being reused, otherwise inflate a new view from custom row layout
        if (convertView == null) {

            // if we don't have a view that is being used create one, and make sure you create a
            // view holder along with it to save our view references to.
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);

            //set our views to our view holder so that we no longer have to go back and find view
            //by id every time we have a new row
            viewHolder.title = (TextView) convertView.findViewById(R.id.listItemNoteTitle);
            viewHolder.note = (TextView) convertView.findViewById(R.id.listItemNoteBody);
            viewHolder.noteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteImg);

            //use set tag to remember our view holder
            convertView.setTag(viewHolder);
        }else{
            // we already have view so just go to view holder and grab widgets from it.
            viewHolder = (ViewHolder) convertView.getTag();
        }

//          commented due to optimisation
//        // Grab reference of views so we can populate them with specified note row data
//        TextView noteTitle = (TextView) convertView.findViewById(R.id.listItemNoteTitle);
//        TextView noteText = (TextView) convertView.findViewById(R.id.listItemNoteBody);
//        ImageView noteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteImg);
        // Fill each new referenced view with data associated with note it's referencing
        viewHolder.title.setText(note.getTitle());
        viewHolder.note.setText(note.getMessage());
        viewHolder.noteIcon.setImageResource(note.getAssociatedDrawable());

        return convertView;
    }

}