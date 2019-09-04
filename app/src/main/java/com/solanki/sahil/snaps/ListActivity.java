package com.solanki.sahil.snaps;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    ArrayList<String> arrayListEmails = new ArrayList<>();
    ArrayList<String> arrayListKeys = new ArrayList<>();
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Share with....");
        final Intent intent = getIntent();

        listView = findViewById(R.id.listView);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListEmails);
        listView.setAdapter(arrayAdapter);

        Utils.getDatabase().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String email = dataSnapshot.child("email").getValue().toString();
                arrayListEmails.add(email);
                arrayListKeys.add(dataSnapshot.getKey());
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
                Map<String, String> map = new HashMap<>();
                map.put("from", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                map.put("imageName", intent.getStringExtra("imageName"));
                map.put("imageUrl", intent.getStringExtra("imageUrl"));
                map.put("message", intent.getStringExtra("message"));

                Utils.getDatabase().getReference().child("users").child(arrayListKeys.get(position)).child("snaps").push().setValue(map);

                Intent intent = new Intent(ListActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }
}
