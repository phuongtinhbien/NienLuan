package tranhoanghuan.it.com.nhapmonan;


import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


import de.hdodenhof.circleimageview.CircleImageView;

public class KhuyenMai extends AppCompatActivity {
    ImageView imgHinhKM;
    Button btnSave;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://ordermonan.appspot.com");
    Bitmap bitmap =  null;

    int REQUEST_CODE_GALLERY = 0;
    int count = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyenmai);
        addControls();
        addEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savingPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoringPreferences();
    }

    private void restoringPreferences() {
        SharedPreferences pre=getSharedPreferences
                ("KhuyenMai",MODE_PRIVATE);
        count = pre.getInt("count", 4);
    }

    private void savingPreferences() {
        SharedPreferences pre=getSharedPreferences
                ("KhuyenMai", MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        editor.putInt("count", count);
        editor.commit();
    }

    private void addControls() {
        btnSave = (Button) findViewById(R.id.btnSave);
        imgHinhKM = (ImageView) findViewById(R.id.imgHinhKM);
    }

    private void addEvents() {
        imgHinhKM.setOnClickListener(new View.OnClickListener() {
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
                saveDataToFirebase();
            }
        });
    }

    private void saveDataToFirebase() {
        String name = "unknown";
        switch (count){
            case 1: name = "mot"; break;
            case 2: name = "hai"; break;
            case 3: name = "ba"; break;
            case 4: name = "bon"; break;
            default: Toast.makeText(KhuyenMai.this, Integer.toString(count), Toast.LENGTH_LONG).show();
        }
        count--;
        if(count < 1) count = 4;

        // delete image
        storageReference.child("khuyenmai/" + name + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(KhuyenMai.this, "Xóa hình thành công", Toast.LENGTH_LONG).show();
            }
        });
        // load image to storage
        StorageReference storageR = storageReference.child("khuyenmai/" + name + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageR.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(KhuyenMai.this, "Tải hình thất bại", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(KhuyenMai.this, "Tải hình thành công", Toast.LENGTH_LONG).show();
                imgHinhKM.setImageResource(R.drawable.ic_add);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri image = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(image, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(filePath);
            imgHinhKM.setImageBitmap(bitmap);

        }
    }
}
