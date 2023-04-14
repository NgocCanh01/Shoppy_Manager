package com.example.shoppy.model;

public class GioHang {
    //Khi click nút thêm cần có biến lưu trữ thằng đã thêm
    // C1: Sử dụng biến toàn cục trong app để lưu sp => nếu đóng ứng dụng data k lưu
    //c1: sử dụng database lưu trữ  => TẠM DÙNG C1
    int idSp;
    String tenSp;
    long giaSp;
    String hinhSp;
    int soLuong;

    public GioHang() {
    }

    public int getIdSp() {
        return idSp;
    }

    public void setIdSp(int idSp) {
        this.idSp = idSp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public long getGiaSp() {
        return giaSp;
    }

    public void setGiaSp(long giaSp) {
        this.giaSp = giaSp;
    }

    public String getHinhSp() {
        return hinhSp;
    }

    public void setHinhSp(String hinhSp) {
        this.hinhSp = hinhSp;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
