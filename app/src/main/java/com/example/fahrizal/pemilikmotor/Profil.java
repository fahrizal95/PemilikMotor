package com.example.fahrizal.pemilikmotor;

public class Profil {
    private String key;
    private String nama;
    private String namaTambal;
    private String nomorHp;
    private String email;
    private String password;
    private String ulangPassword;
    private Double latitude;
    private Double longitude;
    private String urlFotoProfil;
    private String urlFotoTambal;
    private String txtbuka;
    private String txttutup;
    private String status;

    public Profil(){

    }


    public Profil(String nama,String namaTambal,String nomorHp,String email,String password,String ulangPassword,String urlFotoProfil, String urlFotoTambal){
        this.nama = nama;
        this.namaTambal = namaTambal;
        this.nomorHp = nomorHp;
        this.email = email;
        this.password = password;
        this.ulangPassword = ulangPassword;
        this.urlFotoProfil = urlFotoProfil;
        this.urlFotoTambal = urlFotoTambal;

    }
    public Profil(String nama,String namaTambal,String nomorHp,String email,String password,String ulangPassword,String urlFotoProfil, String urlFotoTambal,Double latitude, Double longitude, String txtbuka, String txttutup){
        this.nama = nama;
        this.namaTambal = namaTambal;
        this.nomorHp = nomorHp;
        this.email = email;
        this.password = password;
        this.ulangPassword = ulangPassword;
        this.latitude = latitude;
        this.longitude = longitude;
        this.urlFotoProfil = urlFotoProfil;
        this.urlFotoTambal = urlFotoTambal;
        this.txtbuka = txtbuka;
        this.txttutup = txttutup;

    }

    public Profil(String key, String nama, String namaTambal, String nomorHp, String email, String password, String ulangPassword, Double latitude, Double longitude, String urlFotoProfil, String urlFotoTambal, String txtbuka, String txttutup, String status) {
        this.key = key;
        this.nama = nama;
        this.namaTambal = namaTambal;
        this.nomorHp = nomorHp;
        this.email = email;
        this.password = password;
        this.ulangPassword = ulangPassword;
        this.latitude = latitude;
        this.longitude = longitude;
        this.urlFotoProfil = urlFotoProfil;
        this.urlFotoTambal = urlFotoTambal;
        this.txtbuka = txtbuka;
        this.txttutup = txttutup;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaTambal() {
        return namaTambal;
    }

    public void setNamaTambal(String namaTambal) {
        this.namaTambal = namaTambal;
    }

    public String getNomorHp() {
        return nomorHp;
    }

    public void setNomorHp(String nomorHp) {
        this.nomorHp = nomorHp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUlangPassword() {
        return ulangPassword;
    }

    public void setUlangPassword(String ulangPassword) {
        this.ulangPassword = ulangPassword;
    }

    public String getUrlFotoProfil() {
        return urlFotoProfil;
    }

    public void setUrlFotoProfil(String urlFotoProfil) {
        this.urlFotoProfil = urlFotoProfil;
    }

    public String getUrlFotoTambal() {
        return urlFotoTambal;
    }

    public void setUrlFotoTambal(String urlFotoTambal) {
        this.urlFotoTambal = urlFotoTambal;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTxtbuka() {
        return txtbuka;
    }

    public void setTxtbuka(String txtbuka) {
        this.txtbuka = txtbuka;
    }

    public String getTxttutup() {
        return txttutup;
    }

    public void setTxttutup(String txttutup) {
        this.txttutup = txttutup;
    }
}
