package com.example.thungan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private DatabaseReference mdatabase;
    private List<Ban> banList;
    private List<String> banListIds;
    private Typeface typeface;
    private BanAdapter adapter;

    private RecyclerView list_ban;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        banList = new ArrayList<>();
        banListIds = new ArrayList<>();
        list_ban = (RecyclerView) findViewById(R.id.list_ban);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list_ban.setHasFixedSize(true);
        list_ban.setLayoutManager(manager);

        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        Query query = mdatabase.child("Ban");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Ban item = dataSnapshot.getValue(Ban.class);
                String topicKey = dataSnapshot.getKey();
                if (!item.getTrangThai()) {
                    banList.add(item);
                    banListIds.add(topicKey);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Ban item = dataSnapshot.getValue(Ban.class);
                String topicKey = dataSnapshot.getKey();
                int topicIndex = banListIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    banList.set(topicIndex, item);
                    adapter.notifyItemChanged(topicIndex);

                } else if (!item.getTrangThai() && !(topicIndex > -1)) {
                    banList.add(item);
                    banListIds.add(topicKey);
                    adapter.notifyDataSetChanged();

                }
                if (item.getTrangThai() && topicIndex > -1) {
                    banList.remove(topicIndex);
                    banListIds.remove(topicIndex);
                    adapter.notifyItemRemoved(topicIndex);
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
        list_ban.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private static class ban_item extends RecyclerView.ViewHolder {
        TextView txt_trangThai;
        TextView soBan, txt_ThoiGian, txt_TongTien;

        public ban_item(View itemView) {
            super(itemView);
            soBan = (TextView) itemView.findViewById(R.id.txt_ban);
            txt_ThoiGian = (TextView) itemView.findViewById(R.id.txt_ThoiGian);
            txt_TongTien = (TextView) itemView.findViewById(R.id.txt_tongTien);
            txt_trangThai = (TextView) itemView.findViewById(R.id.txt_trangThai);
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_ban_thanh_toan, parent, false);
            return new ban_item(view);
        }

        @Override
        public void onBindViewHolder(ban_item holder, final int position) {

            holder.soBan.setText("" + banlist.get(position).getName());
            holder.soBan.setTypeface(typeface);
            holder.txt_TongTien.setText(" " + banlist.get(position).getTongTienHienTai() + ".000 VND");
            holder.txt_trangThai.setText("Đang dùng...");
            holder.txt_ThoiGian.setText(banlist.get(position).getThoiGianVao());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DetailHoaDonActivity.class);
                    intent.putExtra("keyOrder", banlistIds.get(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return banlist.size();
        }
    }


}
