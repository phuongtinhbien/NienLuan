package tranhoanghuan.it.com.nhapmonan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NhapMonAn extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://ordermonan.appspot.com");
    private static final int REQUEST_WRITE_PERMISSION = 786;

    Spinner spLoai;
    ArrayList<String> dsLoai;
    ArrayAdapter<String> adapterLoai;
    int lastedSelected=-1;
    String loai = "";

    CircleImageView imgHinh;
    Button btnCammera, btnBrowse, btnSave;
    EditText txtName, txtPrice;

    int REQUEST_CODE_CAMERA = 1;
    int REQUEST_CODE_GALLERY = 0;
    Bitmap bitmap =  null;

    private ProgressDialog progressDialog;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addEvents();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_mon_an);
        requestPermission();
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

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            addEvents();
        }
    }

    private void saveDataToFirebase() {

        final MonAn monAn = new MonAn();
        monAn.setTenMon(txtName.getText().toString());
        txtName.setText("");
        monAn.setGiaBan(Long.parseLong(txtPrice.getText().toString()));
        txtPrice.setText("");
        switch (dsLoai.get(lastedSelected)) {
            case "Món ăn":
                loai = "monAn";
                break;
            case "Thức uống":
                loai = "thucUong";
                break;
            case "Tráng miệng":
                loai = "trangMieng";
                break;
        }
        // Get the data from an ImageView as bytes
        StorageReference storageR = storageReference.child(loai + "/" + monAn.getTenMon() + ".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageR.putBytes(data);
        final String finalLoai = loai;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(NhapMonAn.this, "Tải hình thất bại", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                monAn.setAnhMon(downloadUrl.toString());
                DatabaseReference mDatabase2 = mDatabase.child("Menu").child(loai);
                String key = monAn.getTenMon();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, monAn);
                mDatabase2.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.hide();
                        Toast.makeText(NhapMonAn.this, "Thêm món ăn thành công", Toast.LENGTH_LONG).show();
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
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
        }
        else if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
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
        spLoai = (Spinner) findViewById(R.id.spLoai);
        dsLoai = new ArrayList<>();
        dsLoai.add("Món ăn");
        dsLoai.add("Thức uống");
        dsLoai.add("Tráng miệng");
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
