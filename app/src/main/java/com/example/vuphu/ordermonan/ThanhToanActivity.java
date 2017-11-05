package com.example.vuphu.ordermonan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThanhToanActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SharedPreferences preferences;
    private Typeface typeface;
    private RecyclerView list_order;
    private DatabaseReference mDatabase;
    private String keyOrder;
    private List<MonOrder> listMonOrder;
    private List<String> listMonOrderIds;
    private MonAdapter adapter;
    private Button thanhToan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        toolbar = (Toolbar) findViewById(R.id.toolbar_thanh_toan);
        preferences = getSharedPreferences("keyOrder", MODE_PRIVATE); // luu tru tam thoi bien Ban
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listMonOrder = new ArrayList<>();
        listMonOrderIds = new ArrayList<>();
        thanhToan = (Button) findViewById(R.id.thanhToan_button);
        thanhToan.setTextSize(20);
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        thanhToan.setTypeface(typeface);
        list_order = (RecyclerView) findViewById(R.id.list_mon_item_order);
        list_order.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list_order.setLayoutManager(manager);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        keyOrder = preferences.getString("keyBan", "").toString(); //lay key Ban hien tai

        Query query = mDatabase.child("Ban").child(keyOrder).child("HoaDon");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonOrder item = dataSnapshot.getValue(MonOrder.class);
                if (!dataSnapshot.getKey().equals("timeYCXuatHD")) {
                    listMonOrder.add(item);
                    listMonOrderIds.add(dataSnapshot.getKey().toString());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                MonOrder item = dataSnapshot.getValue(MonOrder.class);
                String topicKey = dataSnapshot.getKey();
                int topicIndex = listMonOrderIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    listMonOrder.set(topicIndex, item);
                    adapter.notifyItemChanged(topicIndex);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String topicKey = dataSnapshot.getKey();
                int topicIndex = listMonOrderIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    listMonOrderIds.remove(topicIndex);
                    listMonOrder.remove(topicIndex);
                    adapter.notifyItemRemoved(topicIndex);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new MonAdapter(this, listMonOrder, listMonOrderIds);
        list_order.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void thanhToan(View view) {

        mDatabase.child("Ban").child(keyOrder).child("trangThai").setValue(true);
        mDatabase.child("Ban").child(keyOrder).child("thanhToan").setValue(true);


    }

    private static class item_mon extends RecyclerView.ViewHolder {

        TextView price, name, soluong;
        CircleImageView img;
        ImageView remove, add, sub;

        public item_mon(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.txt_item_mon_gia_order);
            name = (TextView) itemView.findViewById(R.id.txt_item_mon_order);
            img = (CircleImageView) itemView.findViewById(R.id.img_item_mon_order);
            remove = (ImageView) itemView.findViewById(R.id.img_remove);
            soluong = (TextView) itemView.findViewById(R.id.txt_so_luong);
            sub = (ImageView) itemView.findViewById(R.id.icon_sub);
            add = (ImageView) itemView.findViewById(R.id.icon_add);
        }
    }

    private class MonAdapter extends RecyclerView.Adapter<item_mon> {

        Context context;
        List<MonOrder> monItemList;
        List<String> monItemListIds;

        public MonAdapter(Context context, List<MonOrder> monItemList, List<String> monItemListIds) {
            this.context = context;
            this.monItemList = monItemList;
            this.monItemListIds = monItemListIds;
        }

        @Override
        public item_mon onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
            return new item_mon(view);
        }

        @Override
        public void onBindViewHolder(item_mon holder, final int position) {
            holder.name.setText(monItemList.get(position).getTenMon());
            holder.name.setTypeface(typeface);
            holder.price.setText("VND " + monItemList.get(position).getGiaBan() + ".000");
            holder.soluong.setText("" + monItemList.get(position).getSoLuong());
            Picasso.with(context).load(monItemList.get(position).getAnhMon()).into(holder.img);

            thanhToan.setText("Tổng Tiền ( " + TongTien() + ".000" + " )");
            mDatabase.child("Ban").child(keyOrder).child("TongTienHienTai").setValue(TongTien());
            //Tăng số lượng thêm 1
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference mdata = mDatabase.child("Ban").child(keyOrder).child("HoaDon")
                            .child(monItemListIds.get(position)).child("soLuong");
                    mdata.setValue(monItemList.get(position).getSoLuong() + 1);

                }
            });
            //Giảm số lượng đi 1
            holder.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference m = mDatabase.child("Ban").child(keyOrder).child("HoaDon")
                            .child(monItemListIds.get(position));
                    DatabaseReference mdata = m.child("soLuong");
                    mdata.setValue(monItemList.get(position).getSoLuong() - 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (monItemList.get(position).getSoLuong() == 0) {
                                m.setValue(null);
                            }
                        }
                    });


                }
            });

            //Xóa ra khỏi giỏ
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItemCount() == 1) {
                        thanhToan.setText("Thanh Toán");
                    }
                    DatabaseReference mdata = mDatabase.child("Ban").child(keyOrder).child("HoaDon").child(monItemListIds.get(position));
                    mdata.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Toast.makeText(context, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }

        public long TongTien() {
            long tong = 0;
            for (int i = 0; i < monItemList.size(); i++) {
                tong += monItemList.get(i).getGiaBan() * monItemList.get(i).getSoLuong();
            }
            if (monItemList.size() == 0)
                return 0;
            return tong;
        }

        @Override
        public int getItemCount() {
            return monItemList.size();
        }

    }

    //set quay lại
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
