package com.example.thungan;

/**
 * Created by vuphu on 8/29/2017.
 */

public class HoaDonDaThanhToan {
    private String maHD;
    private long tongTienHoaDon;

    public HoaDonDaThanhToan() {
    }

    public HoaDonDaThanhToan(String maHD, long tongTienHoaDon) {
        this.maHD = maHD;
        this.tongTienHoaDon = tongTienHoaDon;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public long getTongTienHoaDon() {
        return tongTienHoaDon;
    }

    public void setTongTienHoaDon(int tongTienHoaDon) {
        this.tongTienHoaDon = tongTienHoaDon;
    }
}
