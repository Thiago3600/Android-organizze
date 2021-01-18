package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {


    private EditText email, senha;
    private Button login;
    private Usuario user;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editTextText_login_email);
        senha = findViewById(R.id.editTextText_login_password);
        login = findViewById(R.id.button_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTexto = email.getText().toString(), senhaTexto = senha.getText().toString();
                if (!emailTexto.isEmpty() && !senhaTexto.isEmpty()){
                    user = new Usuario();
                    user.setEmail(emailTexto);
                    user.setSenha(senhaTexto);
                    validarLogin(user);
                }else{
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarLogin(Usuario user){
        auth = ConfiguracaoFirebase.getAutenticacao();
        auth.signInWithEmailAndPassword(user.getEmail(), user.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    abrirTelaPrincipal();
                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Senha incorreta";
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Email Incorreto.";
                    } catch (Exception e) {
                        excecao = e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Falha ao acessar o usuario: " + excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }
}