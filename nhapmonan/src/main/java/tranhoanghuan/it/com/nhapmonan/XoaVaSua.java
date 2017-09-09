package tranhoanghuan.it.com.nhapmonan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class XoaVaSua extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xoa_va_sua);

    }

    public void monAn(View view) {
        Intent intent = new Intent(this, DsMon.class);
        intent.putExtra("loai", "monAn");
        startActivity(intent);
    }

    public void thucUong(View view) {
        Intent intent = new Intent(this, DsMon.class);
        intent.putExtra("loai", "thucUong");
        startActivity(intent);
    }

    public void trangMieng(View view) {
        Intent intent = new Intent(this, DsMon.class);
        intent.putExtra("loai", "trangMieng");
        startActivity(intent);
    }
}
