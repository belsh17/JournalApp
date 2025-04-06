package com.example.emptyactivity.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Models.Notes;
import com.example.emptyactivity.R;
import com.example.emptyactivity.MainActivity;
import com.example.emptyactivity.ViewNoteActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.util.Log;
import android.widget.Toast;

//Notes adapter class extends from recycler view
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    //variables
    private Context context;
    private List<Notes> notesList;
    private RoomDB roomDB;

    //constructor for adapter
    public NotesAdapter(Context context, List<Notes> notesList){
        //initializing variables
        this.context = context;
        this.notesList = notesList;
        this.roomDB = RoomDB.getInstance(context);
    }

    //note view holder class
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent, textViewDate;
        ImageButton btnDelete, btnEdit;

        //assigning variables to UI elements
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    //creating view holder
    @NonNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating indiv_note.xml
        View view = LayoutInflater.from(context).inflate(R.layout.indiv_note, parent, false);
        return new NoteViewHolder(view);
    }

    //binding data to view holder
    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {
       Notes note = notesList.get(position);
       Log.d("NotesAdapter", "Binding note at position: " + position + " with content: " + note.getContent());
      //setting content and date to that of the note stored
       holder.textViewContent.setText(note.getContent());
       holder.textViewDate.setText(note.getDate());

        // Delete Button
        holder.btnDelete.setOnClickListener(v -> deleteNote(position));

        // Edit Button
        holder.btnEdit.setOnClickListener(v -> showEditDialog(position, note));

        //viewing the note code
        //the 'v' represents the view which was clicked ie. recycler view item
        holder.itemView.setOnClickListener(v -> {
            //code in this block will be executed when user clicks indiv. row
            Log.d("NotesAdapter", "Navigating to ViewEntryActivity...");

            // Retrieve userId from SharedPreferences
            SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            int userId = preferences.getInt("userId", -1);  // Get the logged-in userId

            if(userId != -1) {
                //Creates intent to navigate to ViewNoteActivity
                Intent intent = new Intent(v.getContext(), ViewNoteActivity.class);
                //key value pair
                intent.putExtra("USER_ID", userId);  // Pass the userId
                intent.putExtra("JOURNAL_ID", note.getJournalID());
                intent.putExtra("NOTE_CONTENT", note.getContent());
                intent.putExtra("NOTE_DATE", note.getDate());
                intent.putExtra("NOTE_IMAGE", note.getImagePath());
                v.getContext().startActivity(intent);
            } else {
                Toast.makeText(context, "No user found, please log in again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    //method update notes list
    @SuppressLint("NotifyDataSetChanged")
    public void setNotesList(List<Notes> updatedNotes){
        this.notesList = updatedNotes;
        notifyDataSetChanged();
    }

    //delete note method
    private void deleteNote(int position) {
        //gets position of note in list
        Notes noteToDelete = notesList.get(position);
        //executor service, tasks execute sequentially
        Executor executor = Executors.newSingleThreadExecutor();
        //lambda expression
        executor.execute(() -> {
            //mainDAO accesses Data Access Object for database
            roomDB.mainDAO().delete(noteToDelete);
            //updating the UI after deletion
            ((MainActivity) context).runOnUiThread(() -> {
                notesList.remove(position); // Update the list
                notifyItemRemoved(position); // Notify the adapter
                ((MainActivity) context).loadNotes(); //reloads entire list of notes
                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }

    //edit note method
    private void showEditDialog(int position, Notes note) {
        //initializes alert dialog builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Note");

        //inflates dialog_edit_note.xml
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_edit_note, null);
        EditText input = viewInflated.findViewById(R.id.editTextNote);
        input.setText(note.getContent());

        builder.setView(viewInflated);

        //setting up positive and negative buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newContent = input.getText().toString().trim();
            if (!TextUtils.isEmpty(newContent)) {
                //checks if note is empty and setting to new content
                note.setContent(newContent);
                //initialize executor object
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    roomDB.mainDAO().update(note);
                    ((MainActivity) context).runOnUiThread(() -> {
                        notifyItemChanged(position);
                        Toast.makeText(context, "Note updated", Toast.LENGTH_SHORT).show();
                    });
                });
            } else {
                Toast.makeText(context, "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

}
