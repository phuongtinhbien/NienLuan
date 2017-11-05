package tranhoanghuan.it.com.nhapmonan;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Input extends AppCompatActivity {
    CardView cvDish, cvKM, cvDeleteEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        if (!isNetworkConnected()) {
            Toast.makeText(this, "Không có kết nối mạng! Vui lòng kết nối Internet", Toast.LENGTH_LONG).show();
        }
        cvDish = (CardView) findViewById(R.id.cvDish);
        cvDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input.this, NhapMonAn.class);
                startActivity(intent);
            }
        });
        cvKM = (CardView) findViewById(R.id.cvKM);
        cvKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input.this, KhuyenMai.class);
                startActivity(intent);
            }
        });
        cvDeleteEdit = (CardView) findViewById(R.id.cvDeleteEdit);
        cvDeleteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input.this, XoaVaSua.class);
                startActivity(intent);
            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
