package com.example.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.organizze.Helper.DateCustom;
import com.example.organizze.Model.Movimentacao;
import com.example.organizze.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class DespesaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoDescricao, campoCategoria;
    private EditText campoValor;
    private Movimentacao mov;
    private String tipo = "d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        campoValor = findViewById(R.id.editTextDespesaValue);
        campoData = findViewById(R.id.textInputEditDespesaData);
        campoDescricao = findViewById(R.id.textInputEditDespesaDescricao);
        campoCategoria = findViewById(R.id.textInputEditDespesaCategoria);

        campoData.setText(DateCustom.dataAtual());
        campoValor.setText("0");
    }

    public void salvarDespesa(View view){
        mov = new Movimentacao();

        mov.setCategoria(Objects.requireNonNull(campoCategoria.getText()).toString());
        mov.setData(Objects.requireNonNull(campoData.getText()).toString());
        mov.setDescricao(Objects.requireNonNull(campoDescricao.getText()).toString());
        mov.setTipo(tipo);
        mov.setValor(Double.parseDouble(campoValor.getText().toString()));
        mov.salvar();
    }

}