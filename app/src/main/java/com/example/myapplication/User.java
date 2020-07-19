package com.example.myapplication;



//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {

    private int id;
    private String username, email, alamat,nama;

    public User(String id_user, String username, String nama, String alamat, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.alamat = alamat;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAlamat() {
        return alamat;
    }

    public  String getNama(){
        return  nama;
    }
}