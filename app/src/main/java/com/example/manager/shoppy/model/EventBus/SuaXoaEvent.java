package com.example.manager.shoppy.model.EventBus;

import com.example.manager.shoppy.model.SanPhamMoi;

public class SuaXoaEvent {
    SanPhamMoi sanPhamMoi;

    public SuaXoaEvent(SanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }

    public SanPhamMoi getSanPhamMoi() {
        return sanPhamMoi;
    }

    public void setSanPhamMoi(SanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }
}
