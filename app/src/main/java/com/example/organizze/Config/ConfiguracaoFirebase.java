package com.example.organizze.Config;

import com.example.organizze.Helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference databaseReference;

    public static DatabaseReference getDatabase(){
        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
    public static FirebaseAuth getAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static DatabaseReference getCaminhoUsuario(){
        String email = autenticacao.getCurrentUser().getEmail();
        String idUsuario = new Base64Custom().codificar(email);
        DatabaseReference dataUsuario = ConfiguracaoFirebase.getDatabase().child("Usuario").child(idUsuario);

        return dataUsuario;
    }
    public static String getEmailBase64Custom(){
        autenticacao = getAutenticacao();
        String email = autenticacao.getCurrentUser().getEmail();
        return new Base64Custom().codificar(email);
    }

    public static DatabaseReference getCaminhoMovimentacaoData(String data){
        DatabaseReference databaseReference = getDatabase();
        return databaseReference.child("Movimentacao").child(getEmailBase64Custom()).child(data);

    }

}
