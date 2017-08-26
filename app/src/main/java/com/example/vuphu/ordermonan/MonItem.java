package com.example.vuphu.ordermonan;

/**
 * Created by vuphu on 8/14/2017.
 */

public class MonItem {
    private String tenMon;
    private long giaBan;
    private String anhMon;

    public MonItem() {
    }

    public MonItem(String tenMon, long giaBan, String anhMon) {
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

}
