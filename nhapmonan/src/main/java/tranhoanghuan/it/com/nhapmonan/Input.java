package tranhoanghuan.it.com.nhapmonan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Input extends AppCompatActivity {
    Button btnMOnAn, btnKhuyenMai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        addControls();
        addEvent();
    }

    private void addEvent() {
        btnMOnAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input.this, NhapMonAn.class);
                startActivity(intent);
            }
        });
        btnKhuyenMai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input.this, KhuyenMai.class);
                startActivity(intent);
            }
        });

    }

    private void addControls() {
        btnMOnAn = (Button) findViewById(R.id.btnNhapMonAn);
        btnKhuyenMai = (Button) findViewById(R.id.btnKhuyenMai);
    }
}
