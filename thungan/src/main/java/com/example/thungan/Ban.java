package com.example.thungan;

/**
 * Created by vuphu on 8/24/2017.
 */

public class Ban {

    private int name;
    private Boolean trangThai;
    private Boolean thanhToan;
    private int TongTienHienTai;
    private String thoiGianVao;

    public Ban(int name, Boolean trangThai, Boolean thanhToan, int tongTienHienTai, String thoiGianVao) {
        this.name = name;
        this.trangThai = trangThai;
        this.thanhToan = thanhToan;
        TongTienHienTai = tongTienHienTai;
        this.thoiGianVao = thoiGianVao;
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

    public Boolean getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(Boolean thanhToan) {
        this.thanhToan = thanhToan;
    }

    public int getTongTienHienTai() {
        return TongTienHienTai;
    }

    public void setTongTienHienTai(int tongTienHienTai) {
        TongTienHienTai = tongTienHienTai;
    }

    public String getThoiGianVao() {
        return thoiGianVao;
    }

    public void setThoiGianVao(String thoiGianVao) {
        this.thoiGianVao = thoiGianVao;
    }
}
