package com.example.emptyactivity


//import com.google.android.gms.cast.framework.media.ImagePicker
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.emptyactivity.Database.RoomDB
import com.example.emptyactivity.Models.Notes
import com.example.emptyactivity.R.id.imageView
import com.example.emptyactivity.ui.theme.EmptyActivityTheme
import com.github.dhaval2404.imagepicker.ImagePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.Executors

//You must add permissions code for allowing media permission


class NoteScreen : ComponentActivity() {

    //declare variables
    private lateinit var executor: Executor
    private lateinit var roomDB: RoomDB
    private lateinit var mainActivity: MainActivity
    private var selectedImagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        links the design layout to activity
        setContentView(R.layout.note_screen)

        //start of saving text code
       //Initialize UI elements
        val editTContent = findViewById<EditText>(R.id.editTextTextMultiLine)
        val btnSave = findViewById<Button>(R.id.buttonSave)
        //        create variable for image view
        val imgGallery = findViewById<ImageView>(imageView)
        //        create variable for add image button
        val imgButton = findViewById<Button>(R.id.buttonImage)

        //Initialize database and executor
        roomDB = RoomDB.getInstance(applicationContext)
        val mainDAO = roomDB.mainDAO()
        executor = Executors.newSingleThreadExecutor()

        //end of saving text code

        //testing save button for linked users and entries
        btnSave.setOnClickListener {
            val noteContent = editTContent.text.toString().trim()
            if (noteContent.isNotEmpty()) {
                // Retrieve userId from SharedPreferences
                val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                //val userId = preferences.getString("userId", null)
                val userId = preferences.getInt("userId", -1)

                if (userId != -1) {
                    // Prepare the new note object
                    val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())

                    val newNote = Notes().apply {
                        content = noteContent
                        date = currentDate
                        this.userId = userId // Link note to the logged-in user
                        imagePath = selectedImagePath
                    }

                    // Insert the note into the database using executor
                    executor.execute {
                        roomDB.mainDAO().insert(newNote)
                    }

                    // Show confirmation and close activity
                    Toast.makeText(this@NoteScreen, "Your entry has been saved!", Toast.LENGTH_SHORT).show()
                    editTContent.text.clear()
                    finish()
                } else {
                    Toast.makeText(this@NoteScreen, "Error: No user found!", Toast.LENGTH_SHORT).show()
                }
            } else {
                editTContent.error = "Please enter text"
            }
        }

        //end of linking

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener { v: View? -> onBackPressedDispatcher.onBackPressed() }

//        code for the image button and to display image
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val uri = result.data?.data
                    imgGallery.setImageURI(uri)
                    // Save the image path
                    selectedImagePath = uri.toString()
                }
            }

        imgButton.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .galleryOnly()
                .galleryMimeTypes(arrayOf("image/png", "image/jpeg", "image/jpg")) // Allow only images
                .createIntent { intent ->
                    pickImageLauncher.launch(intent)
                }
        }

            enableEdgeToEdge()

    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EmptyActivityTheme {

    }
}

