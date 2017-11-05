package com.example.vuphu.ordermonan;

/**
 * Created by vuphu on 8/24/2017.
 */

public class Ban {

    private int name;
    private Boolean trangThai;

    public Ban(int name, Boolean trangThai) {
        this.name = name;
        this.trangThai = trangThai;
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

}
