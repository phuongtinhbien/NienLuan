package tranhoanghuan.it.com.nhapmonan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NhapMonAn extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://ordermonan.appspot.com");


    Spinner spLoai;
    ArrayList<String> dsLoai;
    ArrayAdapter<String> adapterLoai;
    int lastedSelected=-1;

    CircleImageView imgHinh;
    Button btnCammera, btnBrowse, btnSave;
    EditText txtName, txtPrice;

    int REQUEST_CODE_CAMERA = 1;
    int REQUEST_CODE_GALLERY = 0;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_mon_an);
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
                progressDialog.setMessage("Đang upload dữ liệu...");
                progressDialog.show();
                saveDataToFirebase();
            }
        });

        spLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lastedSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void saveDataToFirebase() {

        final MonAn monAn = new MonAn();
        monAn.setTenMon(txtName.getText().toString());
        txtName.setText("");
        monAn.setGiaBan(Long.parseLong(txtPrice.getText().toString()));
        txtPrice.setText("");
        // Get the data from an ImageView as bytes
        StorageReference storageR = storageReference.child(dsLoai.get(lastedSelected) + "/" + monAn.getTenMon() + ".png");
        imgHinh.setDrawingCacheEnabled(true);
        imgHinh.buildDrawingCache();
        Bitmap bitmap = imgHinh.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageR.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(NhapMonAn.this, "Lỗi tải hình", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                monAn.setAnhMon(downloadUrl.toString());
                Calendar c = Calendar.getInstance();
                DatabaseReference mDatabase2 = mDatabase.child("Menu").child(dsLoai.get(lastedSelected));
                String key = c.getTime().toString();
                Map<String, Object> postValues = monAn.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, postValues);
                mDatabase2.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.hide();
                        Toast.makeText(NhapMonAn.this, "Thêm món ăn thành công", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Save data to Database
//        mDatabase.child("Menu").child(dsLoai.get(lastedSelected)).push().setValue(monAn, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if(databaseError == null){
//                    Toast.makeText(NhapMonAn.this, "Thêm món ăn thành công", Toast.LENGTH_LONG).show();
//                    Toast.makeText(NhapMonAn.this, "key = " + databaseReference.getKey(), Toast.LENGTH_LONG).show();
//                    // get key
//                    //databaseReference.getKey();
//
//                }
//                else {
//                    Toast.makeText(NhapMonAn.this, "Thêm món ăn thất bại", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
        }
        else if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri image = data.getData();
            imgHinh.setImageURI(image);
        }

    }

    private void addControls() {
        spLoai = (Spinner) findViewById(R.id.spLoai);
        dsLoai = new ArrayList<>();
        dsLoai.add("monAn");
        dsLoai.add("thucUong");
        dsLoai.add("trangMieng");
        adapterLoai = new ArrayAdapter<String>(NhapMonAn.this, android.R.layout.simple_spinner_item, dsLoai);
        adapterLoai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoai.setAdapter(adapterLoai);

        progressDialog = new ProgressDialog(this);

        imgHinh = (CircleImageView) findViewById(R.id.imgHinh);
        btnBrowse = (Button) findViewById(R.id.btnBrowse);
        btnCammera = (Button) findViewById(R.id.btnCamera);
        btnSave = (Button) findViewById(R.id.btnSave);
        txtName = (EditText) findViewById(R.id.txtName);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
    }
}
