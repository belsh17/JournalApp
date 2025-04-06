package com.example.emptyactivity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyactivity.Adapters.NotesAdapter
import com.example.emptyactivity.Database.RoomDB
import com.example.emptyactivity.Models.Notes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

class MainActivity : FragmentActivity() {

    //private lateinit var executor: Executor
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var roomDB: RoomDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout) // Replace `activity_main` with your layout file name

        //get computer to say welcome [name]
        // Get reference to the TextView
        val welcomeText = findViewById<TextView>(R.id.textView)

        // Get the name from Intent
        val userName = intent.getStringExtra("USERNAME") ?: "Guest"  // Default to "Guest" if null

        // Set text to say "Hello [name]!"
        welcomeText.text = "Hello $userName!"

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTabs)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener { v: View? -> onBackPressedDispatcher.onBackPressed() }
        // Set the LayoutManager for RecyclerView (this controls the layout of items)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setLayoutManager(layoutManager);

        val textView3 = findViewById<TextView>(R.id.textView3)
        val button = findViewById<Button>(R.id.button)
        val dateFormat = SimpleDateFormat("MMM dd,yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val moodButton = findViewById<ImageButton>(R.id.moodButton)

        // Set the current date in the TextView
        textView3.text = currentDate

        // Sample code for database interaction
        roomDB = RoomDB.getInstance(applicationContext)
        notesAdapter = NotesAdapter(this, emptyList())  // Initialize with empty list
        recyclerView.adapter = notesAdapter
        //executor = Executors.newSingleThreadExecutor()
        loadNotes()

        // Check if the intent contains a new note
        val newNoteContent = intent.getStringExtra("NOTE_CONTENT")
        val newNoteDate = intent.getStringExtra("NOTE_DATE")

        if (!newNoteContent.isNullOrEmpty() && !newNoteDate.isNullOrEmpty()) {
            // Call the method to insert the new note
            addNewNote(newNoteContent, newNoteDate)
        }

        //code for mood button
        moodButton.setOnClickListener{
            val explicitIntent2 = Intent(this, mood_track::class.java)
            startActivity(explicitIntent2)
        }

        //use explicit intent when going from screen to screen
        button.setOnClickListener {
//          'this' shows the activity you are in now being mainActivity
            val explicitIntent = Intent(this, NoteScreen::class.java)
            startActivity(explicitIntent)
        }

        //enableEdgeToEdge() // Optional: Keep if your app requires edge-to-edge display

    }

    //testing for user linking
    fun loadNotes() {
        val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        //val userId = preferences.getString("userId", null)
        val userId = preferences.getInt("userId", -1)

        Log.d("MainActivity", "Loaded userId from SharedPreferences: $userId")

        //changed null to -1 for the int version
        if (userId != -1) {
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val notesList = roomDB.mainDAO().getNotesForUser(userId) // Fetch only this user's notes
                Log.d("MainActivity", "Notes retrieved for user: ${notesList.size}")  // Log the number of notes retrieved
                runOnUiThread {
                    notesAdapter.setNotesList(notesList) // Update UI with user's notes
                }
            }
        } else {
            Log.e("MainActivity", "No userId found in SharedPreferences")
            runOnUiThread {
                val intent = Intent(this@MainActivity, LoginScreen::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    //end of testing for user linking


    override fun onResume(){
        super.onResume()
        loadNotes()
    }

    //testing linking od user and entry
    fun addNewNote(noteContent: String, noteDate: String) {
        val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        //val userId = preferences.getString("userId", null)
        val userId = preferences.getInt("userId", -1)

        if (userId != -1) {
            val newNote = Notes().apply {
                content = noteContent
                date = noteDate
                this.userId = userId  // Associate note with the logged-in user
            }
            Log.d("MainActivity", "Loaded userId from SharedPreferences: $userId")

            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                roomDB.mainDAO().insert(newNote)
                runOnUiThread { loadNotes() }
            }
        } else {
            Log.e("MainActivity", "Error: UserId is null")
        }
    }

    //end of testing

}


