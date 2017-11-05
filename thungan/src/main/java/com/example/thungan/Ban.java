package com.example.thungan;

/**
 * Created by vuphu on 8/24/2017.
 */

public class Ban {

    private int name;
    private Boolean trangThai;
    private Boolean yeuCauThanhToan;
    private long tongTienBan;
    private String thoiGianVao;
    private Boolean daThanhToan;

    public Ban(int name, Boolean trangThai, Boolean yeuCauThanhToan, int tongTienBan, String thoiGianVao, Boolean daThanhToan) {
        this.name = name;
        this.trangThai = trangThai;
        this.yeuCauThanhToan = yeuCauThanhToan;
        this.tongTienBan = tongTienBan;
        this.thoiGianVao = thoiGianVao;
        this.daThanhToan = daThanhToan;
    }

    public Ban() {
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public Boolean getYeuCauThanhToan() {
        return yeuCauThanhToan;
    }

    public void setYeuCauThanhToan(Boolean yeuCauThanhToan) {
        this.yeuCauThanhToan = yeuCauThanhToan;
    }

    public long getTongTienBan() {
        return tongTienBan;
    }

    public void setTongTienBan(long tongTienBan) {
        this.tongTienBan = tongTienBan;
    }

    public String getThoiGianVao() {
        return thoiGianVao;
    }

    public void setThoiGianVao(String thoiGianVao) {
        this.thoiGianVao = thoiGianVao;
    }

    public Boolean getDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(Boolean daThanhToan) {
        this.daThanhToan = daThanhToan;
    }
}
