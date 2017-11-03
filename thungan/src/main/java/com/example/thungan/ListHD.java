package com.example.thungan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ListHD extends AppCompatActivity {

    private List<HoaDonBase> list;
    private RecyclerView listHoaDon;
    private HoaDonAdapter adapter;
    private DatabaseReference mdatabase;
    private String keyOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hd);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        Intent intent = getIntent();
        keyOrder = intent.getStringExtra("keyOrder");
        list = new ArrayList<>();
        listHoaDon = (RecyclerView) findViewById(R.id.list_hoa_don_frag);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listHoaDon.setHasFixedSize(true);
        listHoaDon.setLayoutManager(manager);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mdatabase.child("HoaDonDaTao").child(keyOrder).child("HoaDon");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                HoaDonBase item = dataSnapshot.getValue(HoaDonBase.class);
                item.setMaHoaDon(dataSnapshot.getKey());
                list.add(item);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

        adapter = new HoaDonAdapter(this, list);
        listHoaDon.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private static class item_HoaDon extends RecyclerView.ViewHolder {


        TextView thoiGian, tongTien;

        public item_HoaDon(View itemView) {
            super(itemView);
            thoiGian = (TextView) itemView.findViewById(R.id.txt_ThoiGian_HoaDon_frag);
            tongTien = (TextView) itemView.findViewById(R.id.txt_tongTienHoaDon_frag);
        }
    }

    public class HoaDonAdapter extends RecyclerView.Adapter<item_HoaDon> {

        private Activity context;
        private List<HoaDonBase> listHD;

        public HoaDonAdapter(Activity context, List<HoaDonBase> list) {
            this.context = context;
            this.listHD = list;
        }

        @Override
        public item_HoaDon onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_don, parent, false);

            return new item_HoaDon(view);
        }

        @Override
        public void onBindViewHolder(item_HoaDon holder, final int position) {

            holder.thoiGian.setText(listHD.get(position).getMaHoaDon());
            holder.tongTien.setText(listHD.get(position).gettongHoaDon() + ".000 VND");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailHoaDonActivity.class);
                    intent.putExtra("keyOrder", keyOrder);
                    intent.putExtra("maHoaDon", listHD.get(position).getMaHoaDon());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return listHD.size();
        }
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
}
