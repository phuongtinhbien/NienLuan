package tranhoanghuan.it.com.nhapmonan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class Input extends AppCompatActivity {
    CardView cvDish, cvKM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
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

    }

}
