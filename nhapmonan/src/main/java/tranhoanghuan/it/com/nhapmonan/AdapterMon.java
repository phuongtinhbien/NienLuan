package tranhoanghuan.it.com.nhapmonan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.fillViewport;
import static android.R.attr.key;
import static android.R.attr.theme;
import static android.R.attr.typeface;
import static tranhoanghuan.it.com.nhapmonan.DsMon.adapter;
import static tranhoanghuan.it.com.nhapmonan.DsMon.listMonIds;
import static tranhoanghuan.it.com.nhapmonan.DsMon.loai;
import static tranhoanghuan.it.com.nhapmonan.DsMon.mDatabase;
import static tranhoanghuan.it.com.nhapmonan.SuaMonAn.storageReference;

/**
 * Created by tranh on 03/09/2017.
 */

public class AdapterMon extends RecyclerView.Adapter<item_mon> {
    Context context;
    List<MonAn> monItemList;
    private Typeface typeface;

    public AdapterMon() {
    }

    public AdapterMon(Context context, List<MonAn> monItemList, Typeface typeface) {
        this.context = context;
        this.monItemList = monItemList;
        this.typeface = typeface;
    }

    @Override
    public item_mon onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new item_mon(view);
    }

    @Override
    public void onBindViewHolder(item_mon holder, final int position) {
        holder.txtTenMon.setText(monItemList.get(position).getTenMon());
        holder.txtTenMon.setTypeface(typeface);
        holder.txtGia.setText("VND " + monItemList.get(position).getGiaBan() + ".000");
        Picasso.with(context).load(monItemList.get(position).getAnhMon()).into(holder.imgHinh);
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StorageReference storageR = storageReference.child(loai + "/" + monItemList.get(position).getTenMon() + ".png");
                storageR.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Xóa hình ảnh thành công", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                mDatabase.child("Menu").child(loai).child(listMonIds.get(position)).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(v.getContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        if (monItemList.get(position).isHetHang()) {
            holder.img_huyHetHang.setVisibility(View.VISIBLE);
            holder.img_hetHang.setVisibility(View.INVISIBLE);
        }
        if (monItemList.get(position).isHetHang() == false) {
            holder.img_hetHang.setVisibility(View.VISIBLE);
            holder.img_huyHetHang.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SuaMonAn.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
        holder.img_hetHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monItemList.get(position).setHetHang(true);
                mDatabase.child("Menu").child(loai).child(listMonIds.get(position)).child("hetHang").setValue(monItemList.get(position).isHetHang());
            }
        });

        holder.img_huyHetHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monItemList.get(position).setHetHang(false);
                mDatabase.child("Menu").child(loai).child(listMonIds.get(position)).child("hetHang").setValue(monItemList.get(position).isHetHang());
            }
        });
    }

    @Override
    public int getItemCount() {
        return monItemList.size();
    }
}

class item_mon extends RecyclerView.ViewHolder {
    TextView txtGia, txtTenMon;
    ImageView imgHinh;
    CardView itemCard;
    ImageView img_delete, img_hetHang, img_huyHetHang;

    public item_mon(View itemView) {
        super(itemView);
        txtGia = (TextView) itemView.findViewById(R.id.txtGia);
        txtTenMon = (TextView) itemView.findViewById(R.id.txtTenMon);
        imgHinh = (ImageView) itemView.findViewById(R.id.imgHinh);
        img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
        img_hetHang = (ImageView) itemView.findViewById(R.id.img_hetHang);
        img_huyHetHang = (ImageView) itemView.findViewById(R.id.img_huyHetHang);
        itemCard = (CardView) itemView.findViewById(R.id.card_list_item);
    }

}




