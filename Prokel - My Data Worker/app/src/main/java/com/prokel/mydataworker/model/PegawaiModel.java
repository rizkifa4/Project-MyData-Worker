package com.prokel.mydataworker.model;

import java.util.List;

public class PegawaiModel {
    private String id;
    private String nama;
    private String jk;
    private String keahlian;
    private String agama;
    private String kontak;
    private String email;
    private String password;
    private String foto;
    private String level;
    private String aktivasi;
    private String token;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        this.keahlian = keahlian;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAktivasi() { return aktivasi; }

    public void setAktivasi(String aktivasi) { this.aktivasi = aktivasi; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public class PegawaiDataModel extends MessageModel {
        private List<PegawaiModel> results;

        public List<PegawaiModel> getResults() {
            return results;
        }

        public void setResults(List<PegawaiModel> results) {
            this.results = results;
        }
    }
}
