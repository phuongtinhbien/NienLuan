package com.example.thungan;

/**
 * Created by vuphu on 8/27/2017.
 */

public class HoaDon {

    private String tenMon;
    private int soLuong;
    private int giaBan;

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(int giaBan) {
        this.giaBan = giaBan;
    }

    public HoaDon(String tenMon, int soLuong, int giaBan) {

        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    public HoaDon() {

    }
}
