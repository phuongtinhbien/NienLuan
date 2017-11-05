package com.example.vuphu.ordermonan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChonBanActivity extends AppCompatActivity {

    private DatabaseReference mdatabase;
    private RecyclerView listban;
    private List<Ban> banList;
    private List<String> banListIds;
    private BanAdapter adapter;
    private TextView trangThaiBan;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_ban);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        banList = new ArrayList<>();
        banListIds = new ArrayList<>();
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        trangThaiBan = (TextView) findViewById(R.id.trangThai);
        listban = (RecyclerView) findViewById(R.id.list_ban);
        GridLayoutManager grid = new GridLayoutManager(this, 3);
        listban.setHasFixedSize(true);
        listban.setLayoutManager(grid);
        trangThaiBan.setTypeface(typeface);

        Query query = mdatabase.child("Ban");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Ban ban = dataSnapshot.getValue(Ban.class);
                if (ban.getTrangThai()) {
                    banList.add(ban);
                    banListIds.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Ban item = dataSnapshot.getValue(Ban.class);
                String topicKey = dataSnapshot.getKey();
                int topicIndex = banListIds.indexOf(topicKey);
                if (!item.getTrangThai() && topicIndex > -1) {
                    banList.remove(topicIndex);
                    banListIds.remove(topicIndex);
                    adapter.notifyDataSetChanged();
                }
                if (item.getTrangThai() && !(topicIndex > -1)) {
                    if (item.getName() <= banList.size()) {
                        banList.add(item.getName() - 1, item);
                        banListIds.add(item.getName() - 1, topicKey);
                        adapter.notifyDataSetChanged();
                    } else {
                        banList.add(item);
                        banListIds.add(topicKey);
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new BanAdapter(this, banList, banListIds);
        listban.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private static class ban_item extends RecyclerView.ViewHolder {

        TextView soBan;

        public ban_item(View itemView) {
            super(itemView);
            soBan = (TextView) itemView.findViewById(R.id.txt_ban);
        }
    }

    public class BanAdapter extends RecyclerView.Adapter<ban_item> {

        private Context context;
        private List<Ban> banlist;
        private List<String> banlistIds;

        public BanAdapter(Context context, List<Ban> banlist, List<String> banlistIds) {
            this.context = context;
            this.banlist = banlist;
            this.banlistIds = banlistIds;
        }

        @Override
        public ban_item onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_ban_trong, parent, false);
            if (getItemCount() != 0) {
                trangThaiBan.setText("Chọn bàn trống bạn muốn");
            }
            return new ban_item(view);
        }

        @Override
        public void onBindViewHolder(ban_item holder, final int position) {

            holder.soBan.setText("" + banlist.get(position).getName());
            holder.soBan.setTypeface(typeface);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChonBanActivity.this, MainActivity.class);
                    intent.putExtra("keyOrder", banlistIds.get(position));
                    startActivity(intent);
                    mdatabase.child("Ban").child(banlistIds.get(position)).child("trangThai").setValue(false);
                    Calendar c = Calendar.getInstance();
                    Date date = c.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String key = dateFormat.format(date);
                    mdatabase.child("Ban").child(banlistIds.get(position)).child("thoiGianVao").setValue(key);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return banlist.size();
        }
    }
}
