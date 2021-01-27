package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.Config.ConfiguracaoFirebase;
import com.example.organizze.Helper.Base64Custom;
import com.example.organizze.Helper.DateCustom;
import com.example.organizze.Model.Movimentacao;
import com.example.organizze.Model.Usuario;
import com.example.organizze.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DespesaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoDescricao, campoCategoria;
    private EditText campoValor;
    private Movimentacao mov;
    private String tipo = "d";
    private Double despesaTotal;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        campoValor = findViewById(R.id.editTextDespesaValue);
        campoData = findViewById(R.id.textInputEditDespesaData);
        campoDescricao = findViewById(R.id.textInputEditDespesaDescricao);
        campoCategoria = findViewById(R.id.textInputEditDespesaCategoria);
        campoData.setText(DateCustom.dataAtual());
        recuperarDespesa();

    }

    public void salvarDespesa(View view){

        if(validarCampos()){
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            mov = new Movimentacao();
            mov.setCategoria(Objects.requireNonNull(campoCategoria.getText()).toString());
            mov.setData(Objects.requireNonNull(campoData.getText()).toString());
            mov.setDescricao(Objects.requireNonNull(campoDescricao.getText()).toString());
            mov.setTipo(tipo);
            mov.setValor(valorRecuperado);
            mov.salvar();
            despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesa(despesaAtualizada);
            Toast.makeText(DespesaActivity.this, "Despesa incluida com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public Boolean validarCampos(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoCAtegoria = campoCategoria.getText().toString();

        if (!textoValor.isEmpty()){
            if (!textoData.isEmpty()){
                if (!textoDescricao.isEmpty()){
                    if (!textoCAtegoria.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(DespesaActivity.this, "Preencha o campo Categoria", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DespesaActivity.this, "Preencha o campo Descricao", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(DespesaActivity.this, "Preencha o campo Data", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(DespesaActivity.this, "Preencha o campo Valor", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public void recuperarDespesa(){


        DatabaseReference dataUsuario = ConfiguracaoFirebase.getCaminhoUsuario();


        dataUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario user = snapshot.getValue(Usuario.class);
                despesaTotal = user.getDespesaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void atualizarDespesa(Double despesa){
        DatabaseReference dataUsuario = ConfiguracaoFirebase.getCaminhoUsuario();
        dataUsuario.child("despesaTotal").setValue(despesa);
    }
}