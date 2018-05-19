package com.mallikasinha.notetaking;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private RecyclerView mNoteList;
    private GridLayoutManager gridLayoutManager;


    private DatabaseReference fNoteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNoteList = findViewById(R.id.main_notes_list);

        gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);

        mNoteList.setHasFixedSize(true);
        mNoteList.setLayoutManager(gridLayoutManager);

        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() !=null) {
            fNoteDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }

        updateUI();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_note_toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public void onStart(){
        super.onStart();




            final FirebaseRecyclerAdapter<NoteModel,NoteViewHolder> firebaseRecyclerAdapter = null;
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(

            NoteModel.class,
            R.layout.single_note_layout,
            NoteViewHolder.class,
            fNoteDatabase
    ) {
        @Override
        protected void onBindViewHolder(@NonNull final NoteViewHolder holder, int position, @NonNull NoteModel model) {

            String noteId = getRef(position).getKey();

            fNoteDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String title = dataSnapshot.child("title").getValue().toString();
                    String timeStamp = dataSnapshot.child("timestamp").getValue().toString();

                    holder.setNoteTitle(title);
                    holder.setNoteTime(timeStamp);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            mNoteList.setAdapter(firebaseRecyclerAdapter);
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }
    };
    }

    private void updateUI(){

        if(fAuth.getCurrentUser() != null){
            Log.i("MainActivity", "fAuth != null");
        }
        else{
            Intent startIntent = new Intent( MainActivity.this, NewNoteActivity.class);
            startActivity(startIntent);
            finish();
            Log.i( "MainActivity", "fAuth == null");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){

            case R.id.main_new_note_btn:
                Intent newIntent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(newIntent);
                break;
        }



        //noinspection SimplifiableIfStatement
           return true;


    }
}
