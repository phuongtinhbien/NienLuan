package com.example.vuphu.ordermonan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Order extends AppCompatActivity {

    private MonItem item = new MonItem();
    private DatabaseReference mDatabase;
    private Toolbar toolbar;
    private String key, parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        parent = intent.getStringExtra("parent");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getValue(parent, key);
        toolbar = (Toolbar) findViewById(R.id.toolbar_order);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(key);
        Log.i("Tag", String.valueOf(item));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getValue(String parent, String key) {
        DatabaseReference d = mDatabase.child("Menu").child(parent).child(key);
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item = dataSnapshot.getValue(MonItem.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
