package com.example.organizze.Model;

import com.example.organizze.Config.ConfiguracaoFirebase;
import com.example.organizze.Helper.Base64Custom;
import com.example.organizze.Helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;
    private String key;


    public Movimentacao() {
    }

    public void salvar(){

        FirebaseAuth auth = ConfiguracaoFirebase.getAutenticacao();
        String idUsuarioCodificado = new Base64Custom().codificar(auth.getCurrentUser().getEmail());
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        String mesAno = DateCustom.mesAno(this.getData());

        databaseReference
                .child("Movimentacao")
                .child(idUsuarioCodificado)
                .child(mesAno)
                .push()
                .setValue(this);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
