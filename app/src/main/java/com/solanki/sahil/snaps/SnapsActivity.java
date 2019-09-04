package com.solanki.sahil.snaps;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class SnapsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ArrayList<String> emaails = new ArrayList<>();
    ListView listView;
    ArrayList<DataSnapshot> snaps = new ArrayList<>();
    ArrayAdapter arrayAdapter;



    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.snaps_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.snap) {
            Intent intent = new Intent(SnapsActivity.this, CreateSnapActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaps);


        mAuth = FirebaseAuth.getInstance();
        setTitle("MyFeed");

        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, emaails);
        listView.setAdapter(arrayAdapter);

        Utils.getDatabase().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                emaails.add(dataSnapshot.child("from").getValue().toString());
                arrayAdapter.notifyDataSetChanged();
                snaps.add(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index = 0;
                try {
                    for (DataSnapshot snapshot : snaps) {
                        if (snapshot.getKey().equals(dataSnapshot.getKey())) {
                            emaails.remove(index);
                            snaps.remove(index);
                        }
                        index++;
                    }
                } catch (Exception e) {
                }
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SnapsActivity.this, ViewSnapActivity.class);
                intent.putExtra("imageName", snaps.get(position).child("imageName").getValue().toString());
                try {
                    intent.putExtra("imageUrl", snaps.get(position).child("imageUrl").getValue().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Could not load image, try again later", Toast.LENGTH_LONG).show();
                }
                intent.putExtra("message", snaps.get(position).child("message").getValue().toString());
                intent.putExtra("snapKey", snaps.get(position).getKey());

                startActivity(intent);

            }
        });


    }
}
