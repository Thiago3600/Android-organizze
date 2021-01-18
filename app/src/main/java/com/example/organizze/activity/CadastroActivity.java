package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.Config.ConfiguracaoFirebase;
import com.example.organizze.Model.Usuario;
import com.example.organizze.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class CadastroActivity extends AppCompatActivity {

    private EditText editNovoCadNome, editNovoCadEmail, editNovoCadSenha;
    private Button cadastrarUsuario;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    //private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNovoCadNome = findViewById(R.id.editTextText_cadastro_nome);
        editNovoCadEmail = findViewById(R.id.editTextText_cadastro_email);
        editNovoCadSenha = findViewById(R.id.editTextText_cadastro_password);
        cadastrarUsuario = findViewById(R.id.button_cadastrar);
        usuario = new Usuario();

        cadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = editNovoCadNome.getText().toString();
                String textoEmail = editNovoCadEmail.getText().toString();
                String textoSenha = editNovoCadSenha.getText().toString();

                if (!textoNome.isEmpty()){
                    if (!textoEmail.isEmpty()){
                        if (!textoSenha.isEmpty()){
                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);
                            cadastrarUsuario();
                        }else{
                            Toast.makeText(CadastroActivity.this,"Digite uma senha",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,"Preencha o Email",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,"Preencha o Nome",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        Log.i("Auth", "EmailUser = "+usuario.getEmail());
        Log.i("Auth", "SenhaUser = "+usuario.getSenha());
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            abrirTelaPrincipal();
                        }else{
                            String excecao = "";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                excecao = "Digite uma senha mais forte e pelo menos 6 digitos.";
                            }catch (FirebaseAuthUserCollisionException e){
                                excecao = "Esse email ja existe.";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Digite um email valido";
                            }catch (Exception e){
                                excecao = e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroActivity.this,"Falha ao criar o usuario: "+ excecao,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }

}