package com.example.thungan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DetailHoaDonActivity extends AppCompatActivity {


    private String keyOrder, maHoaDon;
    private Typeface typeface;
    private DatabaseReference mDatabase;
    private RecyclerView listHoaDon;
    private TextView tongTien, tenBan, txt_thoiGianHD;
    private List<HoaDon> listhd;
    private List<String> listitemIds;
    private item_HoaDonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hoa_don);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        listhd = new ArrayList<>();
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        listitemIds = new ArrayList<>();
        keyOrder = intent.getStringExtra("keyOrder");
        maHoaDon = intent.getStringExtra("maHoaDon");
        setTitle(tenBan());
        tongTien = (TextView) findViewById(R.id.txt_tongTienHoaDon);
        tenBan = (TextView) findViewById(R.id.txt_TenBan);
        tongTien.setTypeface(typeface);
        tenBan.setText(tenBan());
        tenBan.setTypeface(typeface);
        txt_thoiGianHD = (TextView) findViewById(R.id.txt_ThoiGian_HoaDon);
        txt_thoiGianHD.setTypeface(typeface);
        //lay thoi gian bat dau tao hoa don
        mDatabase.child("HoaDonDaTao").child(keyOrder).child("HoaDon").child(maHoaDon)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        txt_thoiGianHD.setText(dataSnapshot.getKey().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        listHoaDon = (RecyclerView) findViewById(R.id.list_gia);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listHoaDon.setLayoutManager(manager);
        listHoaDon.setHasFixedSize(true);
        Query query = mDatabase.child("HoaDonDaTao").child(keyOrder).child("HoaDon").child(maHoaDon).child("DanhSachMon");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String tenMon = dataSnapshot.getKey();
                HoaDon item = dataSnapshot.getValue(HoaDon.class);
                listhd.add(item);
                listitemIds.add(tenMon);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String tenMon = dataSnapshot.getKey();
                int topicIndex = listitemIds.indexOf(tenMon);
                if (topicIndex > -1) {
                    HoaDon item = dataSnapshot.getValue(HoaDon.class);
                    listhd.set(topicIndex, item);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String tenMon = dataSnapshot.getKey();
                int topicIndex = listitemIds.indexOf(tenMon);
                if (topicIndex > -1) {
                    listhd.remove(topicIndex);
                    listitemIds.remove(topicIndex);
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
        adapter = new item_HoaDonAdapter(this, listhd, listitemIds);
        listHoaDon.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public String tenBan() {
        String ten = null;

        ten = "Bàn " + keyOrder.substring(keyOrder.length() - 1);
        return ten;
    }

    private static class item_HoaDon extends RecyclerView.ViewHolder {

        private TextView tenMon, soLuong, giaMon, giaTong;

        public item_HoaDon(View itemView) {
            super(itemView);

            tenMon = (TextView) itemView.findViewById(R.id.txt_ten);
            soLuong = (TextView) itemView.findViewById(R.id.txt_sl);
            giaMon = (TextView) itemView.findViewById(R.id.txt_giaMon);
            giaTong = (TextView) itemView.findViewById(R.id.txt_giaTong);

        }
    }

    public class item_HoaDonAdapter extends RecyclerView.Adapter<item_HoaDon> {

        private Context context;
        private List<HoaDon> listhd;
        private List<String> listitemIds;

        public item_HoaDonAdapter(Context context, List<HoaDon> listhd, List<String> listitemIds) {
            this.context = context;
            this.listhd = listhd;
            this.listitemIds = listitemIds;
        }

        @Override
        public item_HoaDon onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            return new item_HoaDon(view);
        }

        @Override
        public void onBindViewHolder(item_HoaDon holder, int position) {
            holder.giaTong.setText(listhd.get(position).getGiaBan() * listhd.get(position).getSoLuong() + ".000");
            holder.soLuong.setText(listhd.get(position).getSoLuong() + "");
            holder.giaMon.setText("x " + listhd.get(position).getGiaBan() + ".000");
            holder.tenMon.setText(listhd.get(position).getTenMon());

            tongTien.setText(TongTien() + ".000 VND");
        }

        public long TongTien() {
            long tong = 0;
            for (int i = 0; i < listhd.size(); i++) {
                tong += listhd.get(i).getGiaBan() * listhd.get(i).getSoLuong();
            }
            if (listhd.size() == 0)
                return 0;
            return tong;
        }

        @Override
        public int getItemCount() {
            return listhd.size();
        }
    }
}
