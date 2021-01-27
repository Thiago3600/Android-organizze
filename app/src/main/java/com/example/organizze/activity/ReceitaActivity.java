package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaDrm;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.Config.ConfiguracaoFirebase;
import com.example.organizze.Helper.DateCustom;
import com.example.organizze.Model.Movimentacao;
import com.example.organizze.Model.Usuario;
import com.example.organizze.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class ReceitaActivity extends AppCompatActivity {

    private EditText campoValor;
    private TextInputEditText campoData, campodescricao, campocategoria;
    private String tipo = "r";
    private Movimentacao mov;
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        campoValor = findViewById(R.id.editTextTextReceitaValue);
        campoData = findViewById(R.id.textInputEditReceitaData);
        campodescricao = findViewById(R.id.textInputEditReceitaDescricao);
        campocategoria = findViewById(R.id.textInputEditReceitaCategoria);

        campoData.setText(DateCustom.dataAtual());
        recuperarReceita();

    }

    public void salvarReceita(View view){
        if (validarCampos()){
            mov = new Movimentacao();
            mov.setValor(Double.parseDouble(campoValor.getText().toString()));
            mov.setTipo(tipo);
            mov.setDescricao(campodescricao.getText().toString());
            mov.setData(campoData.getText().toString());
            mov.setCategoria(campocategoria.getText().toString());
            mov.salvar();
            atualizarReceitaTotal(Double.parseDouble(campoValor.getText().toString()));
        }

    }
    public void recuperarReceita(){
        DatabaseReference dataUsuario = ConfiguracaoFirebase.getCaminhoUsuario();
        dataUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario user = snapshot.getValue(Usuario.class);
                receitaTotal = user.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Boolean validarCampos(){

        if (!campoValor.getText().toString().isEmpty()){
            if (!campoData.getText().toString().isEmpty()){
                if (!campodescricao.getText().toString().isEmpty()){
                    if (!campocategoria.getText().toString().isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(ReceitaActivity.this, "Preencha o campo Categoria", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ReceitaActivity.this, "Preencha o campo Descricao", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ReceitaActivity.this, "Preencha o campo data", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(ReceitaActivity.this, "Preencha o campo valor", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    public void atualizarReceitaTotal(Double valor){
        DatabaseReference dataUsuario = ConfiguracaoFirebase.getCaminhoUsuario();
        dataUsuario.child("receitaTotal").setValue(valor);
        finish();
    }
}