package com.example.notesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.R;
import com.example.notesapp.database.NotesDatabase;
import com.example.notesapp.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteSubTitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;

    private String selectedNoteColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(view -> onBackPressed());

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubTitle = findViewById(R.id.inputNoteSubTitle);
        inputNoteText = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(view -> saveNote());

        selectedNoteColor="#333333";

        initMiscellaneous();
        setSubtitleIndicatorColor();

    }

    public void saveNote() {
        if(inputNoteTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if(inputNoteSubTitle.getText().toString().trim().isEmpty() &&
                inputNoteText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubTitle.getText().toString());
        note.setNoteText(inputNoteText.getText().toString());
        note.setDateTime(textDateTime.getText().toString());
        note.setColor(selectedNoteColor);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            //Background work here
            NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);


            handler.post(() -> {
                // pass to main thread
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            });
        });

//        @SuppressLint("StaticFieldLeak")
//        class saveNoteTask extends AsyncTask<Void, Void, Void> {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void unused) {
//                super.onPostExecute(unused);
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        }
//
//        new saveNoteTask().execute();
    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        bottomSheetBehavior.setPeekHeight(135);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(view -> {
            if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(view -> {
            selectedNoteColor ="#333333";
            imageColor1.setImageResource(R.drawable.ic_done);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(view -> {
            selectedNoteColor ="#FDBE3B";
            imageColor2.setImageResource(R.drawable.ic_done);
            imageColor1.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(view -> {
            selectedNoteColor ="#FF4842";
            imageColor3.setImageResource(R.drawable.ic_done);
            imageColor2.setImageResource(0);
            imageColor1.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(view -> {
            selectedNoteColor ="#3A52Fc";
            imageColor4.setImageResource(R.drawable.ic_done);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor1.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(view -> {
            selectedNoteColor ="#000000";
            imageColor5.setImageResource(R.drawable.ic_done);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor1.setImageResource(0);
            setSubtitleIndicatorColor();
        });

    }

    void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

}