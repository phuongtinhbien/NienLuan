package tranhoanghuan.it.com.nhapmonan;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tranh on 23/08/2017.
 */

public class MonAn {
    private String tenMon;
    private long giaBan;
    private String anhMon;

    public Map<String, Boolean> stars = new HashMap<>();

    public MonAn() {
    }

    public MonAn(String tenMon, long giaBan, String anhMon) {
        this.tenMon = tenMon;
        this.giaBan = giaBan;
        this.anhMon = anhMon;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("anhMon", this.anhMon);
        result.put("giaBan", this.giaBan);
        result.put("tenMon", this.tenMon);

        return result;
    }
}
