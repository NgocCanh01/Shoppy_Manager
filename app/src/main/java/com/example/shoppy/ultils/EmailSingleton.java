package com.example.shoppy.ultils;

public class EmailSingleton {
    private static EmailSingleton instance;
    private String email;
    private String mobile;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private EmailSingleton() {
    }

    public static synchronized EmailSingleton getInstance() {
        if (instance == null) {
            instance = new EmailSingleton();
        }
        return instance;
    }


}
