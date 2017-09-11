package tranhoanghuan.it.com.nhapmonan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static tranhoanghuan.it.com.nhapmonan.DsMon.listMon;
import static tranhoanghuan.it.com.nhapmonan.DsMon.loai;
import static tranhoanghuan.it.com.nhapmonan.DsMon.mDatabase;

public class SuaMonAn extends AppCompatActivity {
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageReference = storage.getReferenceFromUrl("gs://ordermonan.appspot.com");
    CircleImageView imgHinh;
    Button btnCammera, btnBrowse, btnSave;
    EditText txtName, txtPrice;
    int position;
    MonAn monAn;
    String url;

    int REQUEST_CODE_CAMERA = 1;
    int REQUEST_CODE_GALLERY = 0;
    Bitmap bitmap = null;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_mon_an);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnCammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, REQUEST_CODE_CAMERA);
            }
        });
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, REQUEST_CODE_GALLERY);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Đang thay đổi dữ liệu...");
                progressDialog.show();
                deleteData();
                if (bitmap == null) {
                    addData();
                } else {
                    addData_Image();
                }
                finish();
            }
        });

    }

    private void addData() {
        final MonAn monAn = new MonAn();
        monAn.setTenMon(txtName.getText().toString());
        monAn.setGiaBan(Long.parseLong(txtPrice.getText().toString()));
        monAn.setAnhMon(url);

        String key = monAn.getTenMon();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, monAn);
        mDatabase.child("Menu").child(loai).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.hide();
                Toast.makeText(SuaMonAn.this, "Sửa món ăn thành công", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteData() {
        if (bitmap == null) {
            url = monAn.getAnhMon();
        } else {
            StorageReference storageR = storageReference.child(loai + "/" + monAn.getTenMon() + ".png");
            storageR.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(SuaMonAn.this, "Xóa hình ảnh thành công", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(SuaMonAn.this, exception.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        mDatabase.child("Menu").child(loai).child(monAn.getTenMon()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SuaMonAn.this, "Xóa dữ liệu thành công", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void addData_Image() {
        final MonAn monAn = new MonAn();
        monAn.setTenMon(txtName.getText().toString());
        monAn.setGiaBan(Long.parseLong(txtPrice.getText().toString()));
        // Get the data from an ImageView as bytes
        StorageReference storageR = storageReference.child(loai + "/" + monAn.getTenMon() + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageR.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(SuaMonAn.this, "Tải hình thất bại", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                monAn.setAnhMon(downloadUrl.toString());

                String key = monAn.getTenMon();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, monAn);
                mDatabase.child("Menu").child(loai).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.hide();
                        Toast.makeText(SuaMonAn.this, "Sửa món ăn thành công", Toast.LENGTH_LONG).show();
                        imgHinh.setImageBitmap(null);
                        imgHinh.setImageResource(R.drawable.ic_add);
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri image = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(image, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(filePath);
            imgHinh.setImageBitmap(bitmap);
        }

    }

    private void addControls() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        monAn = listMon.get(position);
        progressDialog = new ProgressDialog(this);
        imgHinh = (CircleImageView) findViewById(R.id.imgHinh);
        Picasso.with(SuaMonAn.this).load(monAn.getAnhMon()).into(imgHinh);
        btnBrowse = (Button) findViewById(R.id.btnBrowse);
        btnCammera = (Button) findViewById(R.id.btnCamera);
        btnSave = (Button) findViewById(R.id.btnSave);
        txtName = (EditText) findViewById(R.id.txtName);
        txtName.setText(monAn.getTenMon());
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        txtPrice.setText(Long.toString(monAn.getGiaBan()));
    }
}
