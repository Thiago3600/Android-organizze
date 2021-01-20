package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.Config.ConfiguracaoFirebase;
import com.example.organizze.R;
import com.example.organizze.activity.CadastroActivity;
import com.example.organizze.activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

   private FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();

        verificaUsuarioLogado();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boasVindas();
    }



    public void btEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
    public void btCadastrese(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void boasVindas(){
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()

        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()

        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()

        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()

        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .canGoBackward(true)
                .build()

        );
    }
    public void verificaUsuarioLogado(){
        auth = ConfiguracaoFirebase.getAutenticacao();
        auth.signOut();
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(this, PrincipalActivity.class));
        }
    }
}