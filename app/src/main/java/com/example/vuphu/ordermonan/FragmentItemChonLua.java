package com.example.vuphu.ordermonan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class FragmentItemChonLua extends Fragment {

    private Context context;
    private int[] img_item = {R.drawable.food_outline, R.drawable.drink_icon, R.drawable.cake_icons};
    private String[] item = {"Món ăn", "Thức uống", "Tráng miệng"};

    private RecyclerView list_item;
    private Typeface typeface;


    public FragmentItemChonLua() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_item_chon_lua, container, false);

        list_item = (RecyclerView) view.findViewById(R.id.list_chon_lua);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        list_item.setLayoutManager(manager);
        list_item.setHasFixedSize(true);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/vnf-quicksand-bold.ttf");
        Activity a = getActivity();
        ListAdapter adapter = new ListAdapter(a);
        list_item.setAdapter(adapter);

        return view;
    }

    private static class ChonLua extends RecyclerView.ViewHolder {

        private ImageView img_item;
        private TextView tv_ten_chon_lua;
        private CardView card_item;
        String name;

        public ChonLua(View itemView) {
            super(itemView);
            card_item = (CardView) itemView.findViewById(R.id.card_luachon);
            img_item = (ImageView) itemView.findViewById(R.id.img_item_chon_lua);
            tv_ten_chon_lua = (TextView) itemView.findViewById(R.id.item_chon_lua);
        }

    }

    public class ListAdapter extends RecyclerView.Adapter<ChonLua> {

        Activity a;

        public ListAdapter(Activity a) {
            this.a = a;
        }

        @Override
        public ChonLua onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(a).inflate(R.layout.item_chon_lua, parent, false);
            return new ChonLua(view);
        }

        @Override
        public void onBindViewHolder(final ChonLua holder, int position) {

            if (position == 0) {
                holder.card_item.setCardBackgroundColor(getResources().getColor(R.color.monAn));
                holder.name = "monAn";
            }
            if (position == 1) {
                holder.card_item.setCardBackgroundColor(getResources().getColor(R.color.thucUong));
                holder.name = "thucUong";
            }
            if (position == 2) {
                holder.card_item.setCardBackgroundColor(getResources().getColor(R.color.trangMieng));
                holder.name = "trangMieng";
            }
            holder.img_item.setImageResource(img_item[position]);

            holder.tv_ten_chon_lua.setText(item[position]);
            holder.tv_ten_chon_lua.setTypeface(typeface);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(a, ListItemMon.class);
                    intent.putExtra("key", holder.name);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return item.length;
        }

    }


}
