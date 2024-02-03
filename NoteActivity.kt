package com.example.uasandroid1sitimuslihah

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasandroid1sitimuslihah.db.NoteRoomDatabase
import com.example.uasandroid1sitimuslihah.model.Note
import com.example.uasandroid1sitimuslihah.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isDataInserted = sharedPreferences.getBoolean("isDataInserted", false)

        if (!isDataInserted) {
            addDummyData()
            sharedPreferences.edit().putBoolean("isDataInserted", true).apply()
        }

        getNotesDate()

        binding.floatingAdd.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
    }

    private fun addDummyData() {
        val database = NoteRoomDatabase.getDatabase(application)
        val dao = database.getNoteDao()

        val dummyDataList = listOf(
            Note(
                title = "2169700037",
                body = "Siti Muslihah",
                nilai = "100",
                keterangan = "Lulus",
                jumlahsks = "21",
                hargasks = "20000"
            )
        )

        for (dummyNote in dummyDataList) {
            dao.insert(dummyNote)
        }
    }


    private fun getNotesDate() {
        val database = NoteRoomDatabase.getDatabase(application)
        val dao = database.getNoteDao()
        val listItems = arrayListOf<Note>()
        listItems.addAll(dao.getAll())
        setupRecyclerView(listItems)
    }

    private fun setupRecyclerView(listItems: ArrayList<Note>) {
        binding.recycleViewMain.apply {
            adapter = NoteAdapter(listItems, object : NoteAdapter.NoteListener {
                override fun OnItemClicked(note: Note) {
                    val intent = Intent(this@NoteActivity, EditActivity::class.java)
                    intent.putExtra(EditActivity().EDIT_NOTE_EXTRA, note)
                    startActivity(intent)
                }
            })

            layoutManager = LinearLayoutManager(this@NoteActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        getNotesDate()
    }
}