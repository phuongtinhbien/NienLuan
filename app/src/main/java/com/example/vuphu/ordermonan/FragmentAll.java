package com.example.vuphu.ordermonan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentAll extends Fragment {

    private RecyclerView list_item_mon;
    private RecyclerView list_item_do_uong;
    private Context context;
    private List<MonItem> listMon;
    private List<String> listMonIds;
    private List<MonItem> listDoUong;
    private List<String> listDoUongIds;
    private DatabaseReference mDatabase;
    private ImageView mot, hai, ba, bon;

    private StorageReference mStorageRef;
    private MonAdapter adapter_mon, adapter_do_uong;
    private ViewFlipper khuyenMai;
    private int[] array;
    public FragmentAll() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_all, container, false);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        listMon = new ArrayList<>();
        listMonIds = new ArrayList<>();
        listDoUong = new ArrayList<>();
        listDoUongIds = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list_item_mon = (RecyclerView) view.findViewById(R.id.list_mon_moi);
        list_item_do_uong = (RecyclerView) view.findViewById(R.id.list_do_uong_moi);


        khuyenMai = (ViewFlipper) view.findViewById(R.id.fliper);
        mot = new ImageView(context);
        hai = new ImageView(context);
        ba = new ImageView(context);
        bon = new ImageView(context);


        StorageReference islandRef = mStorageRef.child("khuyenmai/mot.png");
        StorageReference islandRef1 = mStorageRef.child("khuyenmai/hai.jpg");
        StorageReference islandRef2 = mStorageRef.child("khuyenmai/ba.jpg");
        StorageReference islandRef3 = mStorageRef.child("khuyenmai/bon.jpg");
        array = context.getResources().getIntArray(R.array.random_color);

        khuyenMai.addView(mot);
        khuyenMai.addView(hai);
        khuyenMai.addView(ba);
        khuyenMai.addView(bon);
        khuyenMai.startFlipping();
        khuyenMai.setFlipInterval(4000);
        Glide.with(this).using(new FirebaseImageLoader()).load(islandRef).into(mot);
        Glide.with(this).using(new FirebaseImageLoader()).load(islandRef1).into(hai);
        Glide.with(this).using(new FirebaseImageLoader()).load(islandRef2).into(ba);
        Glide.with(this).using(new FirebaseImageLoader()).load(islandRef3).into(bon);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager manager1 = new LinearLayoutManager(context);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        list_item_mon.setLayoutManager(manager);
        list_item_mon.setHasFixedSize(true);

        list_item_do_uong.setLayoutManager(manager1);
        list_item_do_uong.setHasFixedSize(true);

        Query queryMon = mDatabase.child("Menu").child("monAn");
        queryMon.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonItem item = dataSnapshot.getValue(MonItem.class);
                listMon.add(item);
                listMonIds.add(dataSnapshot.getKey().toString());
                adapter_mon.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                MonItem item = dataSnapshot.getValue(MonItem.class);
                String topicKey = dataSnapshot.getKey();
                int topicIndex = listMonIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    listMon.set(topicIndex, item);
                    adapter_mon.notifyItemChanged(topicIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String topicKey = dataSnapshot.getKey();
                int topicIndex = listMonIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    listMonIds.remove(topicIndex);
                    listMon.remove(topicIndex);
                    adapter_mon.notifyItemRemoved(topicIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Query queryDoUong = mDatabase.child("Menu").child("thucUong");
        queryDoUong.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonItem item = dataSnapshot.getValue(MonItem.class);
                listDoUong.add(item);
                listDoUongIds.add(dataSnapshot.getKey().toString());
                adapter_do_uong.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                MonItem item = dataSnapshot.getValue(MonItem.class);
                String topicKey = dataSnapshot.getKey();
                int topicIndex = listDoUongIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    listDoUong.set(topicIndex, item);
                    adapter_do_uong.notifyItemChanged(topicIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String topicKey = dataSnapshot.getKey();
                int topicIndex = listDoUongIds.indexOf(topicKey);
                if (topicIndex > -1) {
                    listDoUongIds.remove(topicIndex);
                    listDoUong.remove(topicIndex);
                    adapter_do_uong.notifyItemRemoved(topicIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Activity a = getActivity();
        adapter_mon = new MonAdapter(a, listMon, listMonIds, "monAn");
        list_item_mon.setAdapter(adapter_mon);
        adapter_mon.notifyDataSetChanged();

        adapter_do_uong = new MonAdapter(a, listDoUong, listDoUongIds, "thucUong");
        list_item_do_uong.setAdapter(adapter_do_uong);
        adapter_mon.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private static class item_mon_moi extends RecyclerView.ViewHolder {

        TextView price, name;
        CircleImageView img;
        CardView itemCard;

        public item_mon_moi(View itemView) {
            super(itemView);
            itemCard = (CardView) itemView.findViewById(R.id.card_item);
            price = (TextView) itemView.findViewById(R.id.txt_item_mon_gia);
            name = (TextView) itemView.findViewById(R.id.txt_item_mon);
            img = (CircleImageView) itemView.findViewById(R.id.img_item_mon);
        }
    }

    private class MonAdapter extends RecyclerView.Adapter<item_mon_moi> {

        Activity a;
        List<MonItem> monItemList;
        List<String> monItemIds;
        String parent;

        public MonAdapter(Activity a, List<MonItem> monItemList, List<String> monItemIds, String p) {
            this.a = a;
            this.monItemList = monItemList;
            this.monItemIds = monItemIds;
            this.parent = p;
        }

        @Override
        public item_mon_moi onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_mon2, parent, false);
            return new item_mon_moi(view);
        }

        @Override
        public void onBindViewHolder(item_mon_moi holder, final int position) {
            holder.name.setVisibility(View.GONE);
            holder.price.setVisibility(View.GONE);
            int randomStr = array[new Random().nextInt(array.length)];
            holder.itemCard.setCardBackgroundColor(randomStr);
            Picasso.with(context).load(monItemList.get(position).getAnhMon()).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(a, Order.class);
                    intent.putExtra("parent", parent);
                    intent.putExtra("key", monItemIds.get(position));
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
