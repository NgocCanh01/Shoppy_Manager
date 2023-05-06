package com.example.manager.shoppy.ultils;

import com.example.manager.shoppy.model.GioHang;
import com.example.manager.shoppy.model.User;

import java.util.ArrayList;
import java.util.List;

public class Ultils {
    public static final String BASE_URL ="http://192.168.0.101:81/shoppee/";
    //chú ý ip có thể thay đổi, vào cmd -> ipconfig -> copy ipv4
    public static List<GioHang> mangGioHang;//thêm giỏ hàng nhưng chưa mua
    public static List<GioHang> mangMuaHang = new ArrayList<>();//đã mua
    public static final String KEY_GET_EMAIL = "email";
    public static User user_current = new User();
}
