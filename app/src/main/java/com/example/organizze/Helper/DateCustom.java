package com.example.organizze.Helper;

import java.text.SimpleDateFormat;

public class DateCustom {


    public static String dataAtual(){

        long data = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(data);
    }

    public static String mesAno(String data){
        String mesAnoArray[] = data.split("/");
        return mesAnoArray[1]+mesAnoArray[2];
    }




}
