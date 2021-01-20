package com.example.organizze.Helper;

import android.util.Base64;

public class Base64Custom {


    public String codificar(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public String decodificar(String codificado){
        return new String(Base64.decode(codificado, Base64.DEFAULT));
    }
}
