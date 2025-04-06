package com.example.emptyactivity.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emptyactivity.Database.RoomDB;
import com.example.emptyactivity.Models.Mood;
import com.example.emptyactivity.R;

import java.util.List;

//MoodAdapter class used with recycler view to display moods in a list
public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {

    //initializing variables
     private List<Mood> moodList;
     private Context context;
     private RoomDB roomDB;

     //constructor for adapter
     public MoodAdapter(List<Mood> moodList) {
         //passing the list of moods to the adapter
         this.moodList = moodList;
         this.roomDB = RoomDB.getInstance(context);
     }

     //holds the view/UI elements for each mood item in recycler
     public static class MoodViewHolder extends RecyclerView.ViewHolder {
         TextView moodText;
         public MoodViewHolder(@NonNull View itemView) {
             super(itemView);
             moodText = itemView.findViewById(R.id.moodText);
         }

     }

     //called when new view must be created
     @NonNull
     @Override
    public MoodAdapter.MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         //Inflater turns XML layout into actual view objects, inflating item_mood.xml
         //parent is ViewGroup - contain inflated view
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mood, parent, false);
         return new MoodViewHolder(view);
     }

     //binds data to each item in list
     @Override
     //void method has no return
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
         //retrieves mood object at given position from mood list
         Mood mood = moodList.get(position);
         Log.d("MoodAdapter", "Binding mood at position: " + position + " with content: " + mood.getUserMood());
         holder.moodText.setText(mood.getUserMood());
     }

     //gets the list size for positions of moods
     @Override
    public int getItemCount(){
         return moodList.size();
     }

     //updates the data in the adapter
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Mood> newMoods) {
        this.moodList = newMoods;
        notifyDataSetChanged(); // Notify adapter to refresh the list
    }

}
