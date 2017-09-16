package tranhoanghuan.it.com.nhapmonan;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tranh on 23/08/2017.
 */

public class MonAn implements Serializable {
    private String tenMon;
    private long giaBan;
    private String anhMon;
    private boolean hetHang;


    public MonAn() {
    }

    public MonAn(String tenMon, long giaBan, String anhMon, boolean hetHang) {
        this.tenMon = tenMon;
        this.giaBan = giaBan;
        this.anhMon = anhMon;
        this.hetHang = hetHang;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public long getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(long giaBan) {
        this.giaBan = giaBan;
    }

    public String getAnhMon() {
        return anhMon;
    }

    public void setAnhMon(String anhMon) {
        this.anhMon = anhMon;
    }

    public boolean isHetHang() {
        return hetHang;
    }

    public void setHetHang(boolean hetHang) {
        this.hetHang = hetHang;
    }
}
