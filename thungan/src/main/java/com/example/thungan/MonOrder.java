package com.example.thungan;

/**
 * Created by vuphu on 8/24/2017.
 */

public class MonOrder {
    private String tenMon;
    private long giaBan;
    private String anhMon;
    private int soLuong;

    public MonOrder() {
    }

    public MonOrder(String tenMon, long giaBan, String anhMon, int soLuong) {
        this.tenMon = tenMon;
        this.giaBan = giaBan;
        this.anhMon = anhMon;
        this.soLuong = soLuong;
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
