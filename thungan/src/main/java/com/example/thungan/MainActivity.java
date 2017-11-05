package com.example.thungan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

import de.hdodenhof.circleimageview.CircleImageView;

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
        Query query = mdatabase.child("HoaDonDaTao").orderByChild("thoiGianVao");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Ban item = dataSnapshot.getValue(Ban.class);
                String topicKey = dataSnapshot.getKey();
                if (!item.getTrangThai()) {
                    banList.add(item);
                    banListIds.add(topicKey);
                    adapter.notifyDataSetChanged();
                    if (dataSnapshot.hasChild("HoaDon")) {
                        long tong = 0;
                        for (DataSnapshot data : dataSnapshot.child("HoaDon").getChildren()) {
                            long d = (long) data.child("tongHoaDon").getValue();
                            tong += d;

                        }
                        item.setTongTienBan(tong);
                    }

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
                    if (dataSnapshot.hasChild("HoaDon")) {
                        long tong = 0;
                        for (DataSnapshot data : dataSnapshot.child("HoaDon").getChildren()) {
                            long d = (long) data.child("tongHoaDon").getValue();
                            tong += d;

                        }
                        item.setTongTienBan(tong);
                    }

                } else if (!item.getTrangThai() && !(topicIndex > -1)) {
                    banList.add(item);
                    banListIds.add(topicKey);
                    adapter.notifyDataSetChanged();
                    if (dataSnapshot.hasChild("HoaDon")) {
                        long tong = 0;
                        for (DataSnapshot data : dataSnapshot.child("HoaDon").getChildren()) {
                            long d = (long) data.child("tongHoaDon").getValue();
                            tong += d;

                        }
                        item.setTongTienBan(tong);
                    }

                }
                if (item.getTrangThai() && topicIndex > -1) {
                    banList.remove(topicIndex);
                    banListIds.remove(topicIndex);
                    adapter.notifyItemRemoved(topicIndex);
                    adapter.notifyDataSetChanged();
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
        Button setLaiBanTrong, setDaThanhToan;
        CircleImageView yeucauTrue;

        public ban_item(View itemView) {
            super(itemView);
            soBan = (TextView) itemView.findViewById(R.id.txt_ban);
            txt_ThoiGian = (TextView) itemView.findViewById(R.id.txt_ThoiGian);
            txt_TongTien = (TextView) itemView.findViewById(R.id.txt_tongTien);
            txt_trangThai = (TextView) itemView.findViewById(R.id.txt_trangThai);
            setLaiBanTrong = (Button) itemView.findViewById(R.id.btn_setLaiBanTrong);
            setDaThanhToan = (Button) itemView.findViewById(R.id.btn_setDaThanhToan);
            yeucauTrue = (CircleImageView) itemView.findViewById(R.id.yeucauTrue);
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
        public void onBindViewHolder(final ban_item holder, final int position) {

            holder.soBan.setText("" + banlist.get(position).getName());
            if (banList.get(position).getYeuCauThanhToan()) {
                holder.soBan.setTextColor(Color.WHITE);
                holder.yeucauTrue.setImageResource(R.color.colorPrimaryDark);
            }
            holder.soBan.setTypeface(typeface);
            holder.txt_TongTien.setText(" " + banlist.get(position).getTongTienBan() + ".000 VND");
            holder.txt_trangThai.setText("Đang dùng...");
            holder.txt_ThoiGian.setText(banlist.get(position).getThoiGianVao());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ListHD.class);
                    intent.putExtra("keyOrder", banlistIds.get(position));
                    startActivity(intent);
                }
            });

            if (banList.get(position).getDaThanhToan()) {
                holder.setDaThanhToan.setBackgroundColor(Color.parseColor("#EF5350"));
                holder.setDaThanhToan.setText(R.string.daThanhToan);
            } else {
                holder.setDaThanhToan.setBackground(getResources().getDrawable(R.drawable.ripple));
                holder.setDaThanhToan.setText("Chưa thanh toán");
            }
            holder.setDaThanhToan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar c = Calendar.getInstance();
                    Date date = c.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateKey = dateFormat.format(date).substring(0, 11);
                    //co k cũng dc
                    HoaDonDaThanhToan hddtt = new HoaDonDaThanhToan(banList.get(position).getThoiGianVao(),
                            banlist.get(position).getTongTienBan());

                    mdatabase.child("DanhThuTheoNgay").child(dateKey).child(hddtt.getMaHD()).setValue(hddtt.getTongTienHoaDon())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mdatabase.child("HoaDonDaTao").child(banlistIds.get(position)).child("daThanhToan").setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Bàn đã thanh toán", Toast.LENGTH_SHORT).show();
                                                    holder.setDaThanhToan.setText(R.string.daThanhToan);
                                }
                            });

                                }
                            });
                }
            });
            holder.setLaiBanTrong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (banList.get(position).getDaThanhToan()) {
                        Ban reset = new Ban(banlist.get(position).getName(), true, false, 0, "", false);
                        mdatabase.child("HoaDonDaTao").child(banlistIds.get(position)).setValue(reset);
                        mdatabase.child("Ban").child(banlistIds.get(position)).setValue(reset);
                        Toast.makeText(context, "Đã reset lại bàn", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(context, "Chưa thanh toán\n Không thể reset", Toast.LENGTH_SHORT).show();


                }
            });

        }
        @Override
        public int getItemCount() {
            return banlist.size();
        }
    }


}
