package com.example.vuphu.ordermonan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentAll extends Fragment {

    private RecyclerView list_item;
    private Context context;
    private List<MonItem> listMon;
    private DatabaseReference mDatabase;
    private List<String> listMonIds;
    private MonAdapter adapter;
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
        listMon = new ArrayList<>();
        listMonIds = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        list_item = (RecyclerView) view.findViewById(R.id.list_mon_moi);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_item.setLayoutManager(manager);
        list_item.setHasFixedSize(true);
        Query query = mDatabase.child("Menu").child("monAn");
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
        Activity a = getActivity();
        adapter = new MonAdapter(a, listMon);
        adapter.notifyDataSetChanged();
        list_item.setAdapter(adapter);
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

        public item_mon_moi(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.txt_item_mon_gia);
            name = (TextView) itemView.findViewById(R.id.txt_item_mon);
            img = (CircleImageView) itemView.findViewById(R.id.img_item_mon);
        }
    }

    private class MonAdapter extends RecyclerView.Adapter<item_mon_moi> {

        Activity a;
        List<MonItem> monItemList;

        public MonAdapter(Activity a, List<MonItem> monItemList) {
            this.a = a;
            this.monItemList = monItemList;
        }

        @Override
        public item_mon_moi onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_mon, parent, false);
            return new item_mon_moi(view);
        }

        @Override
        public void onBindViewHolder(item_mon_moi holder, final int position) {
            holder.name.setVisibility(View.GONE);
            holder.price.setVisibility(View.GONE);
            Picasso.with(context).load(monItemList.get(position).getAnhMon()).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(a, Order.class);
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
