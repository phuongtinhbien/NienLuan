package com.example.thungan;

/**
 * Created by vuphu on 9/4/2017.
 */

public class HoaDonBase {

    private String maHoaDon;
    private long tongHoaDon;

    public HoaDonBase(String maHoaDon, long tongHoaDon) {

        this.maHoaDon = maHoaDon;
        this.tongHoaDon = tongHoaDon;
    }

    public HoaDonBase() {
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public long gettongHoaDon() {
        return tongHoaDon;
    }

    public void settongHoaDon(long tongHoaDon) {
        this.tongHoaDon = tongHoaDon;
    }


}
