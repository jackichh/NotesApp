package com.example.notesapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.example.notesapp.R;
import com.example.notesapp.adapters.NotesAdapter;
import com.example.notesapp.database.NotesDatabase;
import com.example.notesapp.entities.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(view -> {



            Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            startActivity(intent);
        });

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList);
        notesRecyclerView.setAdapter(notesAdapter);

//        getNotes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getNotes();
    }

    private void getNotes() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here

            List<Note> notes = NotesDatabase
                        .getDatabase(getApplicationContext())
                        .noteDao().getAllNotes();



            handler.post(() -> {
                // pass to main thread
                if(noteList.size() == 0){
                    noteList.addAll(notes);

                } else {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyDataSetChanged();
                    //notesAdapter.notifyItemInserted(0);
                }
//                notesRecyclerView.setAdapter(new NotesAdapter(noteList));
//                notesAdapter.notifyDataSetChanged();
                notesRecyclerView.smoothScrollToPosition(0);
            });
        });

//        @SuppressLint("StaticFieldLeak")
//        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
//
//            @Override
//            protected List<Note> doInBackground(Void... voids) {
//                return NotesDatabase
//                        .getDatabase(getApplicationContext())
//                        .noteDao().getAllNotes();
//            }
//
//            @Override
//            protected void onPostExecute(List<Note> notes) {
//                super.onPostExecute(notes);
//                if(noteList.size() == 0){
//                    noteList.addAll(notes);
//                    notesAdapter.notifyDataSetChanged();
//                } else {
//                    noteList.add(0, notes.get(0));
//                    notesAdapter.notifyItemInserted(0);
//                }
//                notesRecyclerView.smoothScrollToPosition(0);
//            }
//        }
//
//        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes();
        }
    }


}