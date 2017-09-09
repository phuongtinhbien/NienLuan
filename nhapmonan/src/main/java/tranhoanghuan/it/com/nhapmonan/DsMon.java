package tranhoanghuan.it.com.nhapmonan;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DsMon extends AppCompatActivity {
    Boolean isDel, isEdit;
    int position;
    public static DatabaseReference mDatabase;
    private RecyclerView list_item_mon;
    public static String loai;
    public static List<MonAn> listMon;
    public static List<String> listMonIds;
    private AdapterMon adapter;
    private Typeface typeface;
    private int[] array;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_mon);
        addControls();

    }

    private void addControls() {
        Intent intent = getIntent();
        loai = intent.getStringExtra("loai");
        listMon = new ArrayList<>();
        listMonIds = new ArrayList<>();
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        list_item_mon = (RecyclerView) findViewById(R.id.list_item_mon);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        list_item_mon.setHasFixedSize(true);
        array = getResources().getIntArray(R.array.random_color);
        list_item_mon.setLayoutManager(gridLayoutManager);
        loadDataFromFB();
        adapter = new AdapterMon(this, listMon, typeface, array);
        list_item_mon.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadDataFromFB() {
        mDatabase.child("Menu").child(loai).orderByChild("tenMon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonAn item = dataSnapshot.getValue(MonAn.class);
                listMon.add(item);
                listMonIds.add(dataSnapshot.getKey().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                MonAn item = dataSnapshot.getValue(MonAn.class);
                String topicKey = dataSnapshot.getKey();
                int topicIndex = listMonIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    listMon.set(topicIndex, item);
                    adapter.notifyItemChanged(topicIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String topicKey = dataSnapshot.getKey();
                int topicIndex = listMonIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    listMonIds.remove(topicIndex);
                    listMon.remove(topicIndex);
                    adapter.notifyItemRemoved(topicIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
