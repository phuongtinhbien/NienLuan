package tranhoanghuan.it.com.nhapmonan;

/**
 * Created by tranh on 23/08/2017.
 */

public class MonAn {
    private String ten;
    private long gia;
    private String loai;

    public MonAn() {
    }

    public MonAn(String ten, long gia, String loai) {
        this.ten = ten;
        this.gia = gia;
        this.loai = loai;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
}
