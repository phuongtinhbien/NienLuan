package com.example.vuphu.ordermonan;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListItemMon extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView list_item_mon;
    private String key;
    private DatabaseReference mDatabase;
    private List<MonItem> listMon;
    private List<String> listMonIds;
    private  MonAdapter adapter;
    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_mon);
        toolbar = (Toolbar) findViewById(R.id.list_item_toolbar);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listMon = new ArrayList<>();
        listMonIds = new ArrayList<>();
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        list_item_mon = (RecyclerView) findViewById(R.id.list_item_mon);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        list_item_mon.setHasFixedSize(true);
        list_item_mon.setLayoutManager(gridLayoutManager);
        Query query = mDatabase.child("Menu").child(key).orderByChild("tenMon");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonItem item = dataSnapshot.getValue(MonItem.class);
                listMon.add(item);
                listMonIds.add(dataSnapshot.getKey().toString());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                MonItem item = dataSnapshot.getValue(MonItem.class);
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
        adapter = new MonAdapter(this, listMon);
        list_item_mon.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    private static class item_mon extends RecyclerView.ViewHolder{

        TextView price, name;
        CircleImageView img;
        ImageView thanhtoan;
        public item_mon(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.txt_item_mon_gia);
            name = (TextView) itemView.findViewById(R.id.txt_item_mon);
            img = (CircleImageView) itemView.findViewById(R.id.img_item_mon);
            thanhtoan = (ImageView) itemView.findViewById(R.id.img_shoping);
        }
    }

    private class MonAdapter extends RecyclerView.Adapter<item_mon>{

        Context context;
        List<MonItem> monItemList;

        public MonAdapter(Context context, List<MonItem> monItemList) {
            this.context = context;
            this.monItemList = monItemList;
        }

        @Override
        public item_mon onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_mon,parent,false);
            return new item_mon(view) ;
        }

        @Override
        public void onBindViewHolder(item_mon holder, final int position) {
            holder.name.setText(monItemList.get(position).getTenMon());
            holder.name.setTypeface(typeface);
            holder.price.setText("VND "+monItemList.get(position).getGiaBan()+".000");
            Picasso.with(context).load(monItemList.get(position).getAnhMon()).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListItemMon.this, Order.class);
                    intent.putExtra("parent", key);
                    intent.putExtra("key", listMonIds.get(position));
                    startActivity(intent);
                }
            });

            holder.thanhtoan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference mdata = mDatabase.child("HoaDon");
                    Calendar c = Calendar.getInstance();
                    Intent intent = new Intent(ListItemMon.this, ThanhToanActivity.class);
                    intent.putExtra("parent", key);
                    intent.putExtra("key", listMonIds.get(position));
                    startActivity(intent);
                }
            });

        }
        @Override
        public int getItemCount() {
            return monItemList.size();
        }
    }
}
