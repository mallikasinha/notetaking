package com.mallikasinha.notetaking;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import javax.sql.StatementEvent;

public class NewNoteActivity extends AppCompatActivity {

    private Button btnCreate;
    private EditText etTitle,etContent;
    private Toolbar mToolbar;

    private FirebaseAuth fAuth;
    private DatabaseReference fNotesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        btnCreate =(Button) findViewById(R.id.new_note_btn);
        etTitle = (EditText) findViewById(R.id.new_note_title);
        etContent =(EditText) findViewById(R.id.new_note_content);
        mToolbar =(Toolbar) findViewById(R.id.new_note_toolbar);

        setSupportActionBar(mToolbar);

        fAuth = FirebaseAuth.getInstance();
        fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title =etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                    createNote(title,content);
                }
                else{
                    Snackbar.make(v,"Fill empty fields", Snackbar.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void createNote(String title , final String content){

        if(fAuth.getCurrentUser() !=null){
            DatabaseReference newNoteRef = fNotesDatabase.push();

            Map noteMap = new HashMap();
            noteMap.put("title",title);
            noteMap.put("content",content);
            noteMap.put("timestamp", ServerValue.TIMESTAMP);

            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });

            mainThread.start();
                newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(NewNoteActivity.this,"Note added to database", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(NewNoteActivity.this,"Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        }

    }

}
