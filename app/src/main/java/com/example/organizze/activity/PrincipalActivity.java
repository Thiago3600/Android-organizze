package com.example.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaDrm;
import android.os.Bundle;

import com.example.organizze.Config.ConfiguracaoFirebase;
import com.example.organizze.Model.Movimentacao;
import com.example.organizze.Model.Usuario;
import com.example.organizze.adapter.AdapterMovimentos;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizze.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.organizze.R.menu.menu;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView saudacao, saldo;
    private FirebaseAuth auth;
    private DatabaseReference usuarioRef;
    private DatabaseReference movimentacoesRef = ConfiguracaoFirebase.getDatabase();
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMov;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoTotal = 0.0;
    private RecyclerView recyclerViewMovimentos;
    private List<Movimentacao> movimentacao = new ArrayList<>();
    private AdapterMovimentos adapterMovimentos;
    private String mesAnoSelecionado;
    private Movimentacao movimento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        calendarView = findViewById(R.id.calendarView);
        saudacao = findViewById(R.id.textViewPrincipalNome);
        saldo = findViewById(R.id.textViewPrincipalSaldoValor);
        recyclerViewMovimentos = findViewById(R.id.reciclerViewMovimentos);

        adapterMovimentos = new AdapterMovimentos(movimentacao, this);
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerViewMovimentos.setLayoutManager(layoutmanager);
        recyclerViewMovimentos.setHasFixedSize(true);
        recyclerViewMovimentos.setAdapter(adapterMovimentos);
        swipe();
        configuraCalendarView();
    }


    public void swipe(){
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.i("swipe", "Item arrastado"+direction);
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerViewMovimentos);
    }

    private void excluirMovimentacao(RecyclerView.ViewHolder itemViewHolder) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Excluir movimento");
        dialog.setMessage("Deseja excluir essa movimentacao, depois da exclusao voce nao tera como recuperar essa movimentacao");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = itemViewHolder.getAdapterPosition();
                movimento = movimentacao.get(position);
                usuarioRef = ConfiguracaoFirebase.getCaminhoMovimentacaoData(mesAnoSelecionado);
                usuarioRef.child(movimento.getKey()).removeValue();
                adapterMovimentos.notifyItemRemoved(position);
                atualizarSaldoGeral();
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapterMovimentos.notifyDataSetChanged();
                Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.create().show();
    }

    public void atualizarSaldoGeral(){
        DatabaseReference usuarioCaminho = ConfiguracaoFirebase.getCaminhoUsuario();
        if (movimento.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimento.getValor();
            usuarioCaminho.child("receitaTotal").setValue(receitaTotal);
        }
    }
    public void recuperarMovimentacoes(){
        String idUsuario = ConfiguracaoFirebase.getEmailBase64Custom();
       usuarioRef = movimentacoesRef.child("Movimentacao").child(idUsuario).child(mesAnoSelecionado);
        Log.i("Dados", "Iduser: "+ idUsuario +"retorno: "+mesAnoSelecionado);
        valueEventListenerMov = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movimentacao.clear();
                for (DataSnapshot dados: snapshot.getChildren()){

                    Movimentacao mov = dados.getValue(Movimentacao.class);
                    mov.setKey(dados.getKey());
                    movimentacao.add(mov);
                }
                adapterMovimentos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Log.i("DataMov", mesAnoSelecionado);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                auth = ConfiguracaoFirebase.getAutenticacao();
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void despesaNovo(View view){
        Intent intentDespesaActivity = new Intent(this, DespesaActivity.class);
        startActivity(intentDespesaActivity);

    }
    public void receitaNovo(View view){
        Intent intentReceitaActivity = new Intent(this, ReceitaActivity.class);
        startActivity(intentReceitaActivity);
    }
    public void configuraCalendarView(){
        CharSequence meses[] = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        CharSequence diaSemana[] = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"};
        calendarView.setTitleMonths(meses);
        CalendarDay date = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (date.getMonth()+1));
        mesAnoSelecionado = mesSelecionado +""+ date.getYear();
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                mesAnoSelecionado = String.format("%02d", (date.getMonth()+1))+""+date.getYear();
                usuarioRef.removeEventListener(valueEventListenerMov);
                recuperarMovimentacoes();

                Log.i("", "MesAno = "+mesAnoSelecionado);
            }
        });
    }

    public void recuperarResumo(){
        auth = ConfiguracaoFirebase.getAutenticacao();
        usuarioRef = ConfiguracaoFirebase.getCaminhoUsuario();
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuarioDados = snapshot.getValue(Usuario.class);
                despesaTotal = usuarioDados.getDespesaTotal();
                receitaTotal = usuarioDados.getReceitaTotal();
                resumoTotal = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                saudacao.setText("Ola, "+usuarioDados.getNome());
                saldo.setText("R$ "+ decimalFormat.format(resumoTotal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapterMovimentos.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();


        recuperarResumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        usuarioRef.removeEventListener(valueEventListenerMov);
    }
}