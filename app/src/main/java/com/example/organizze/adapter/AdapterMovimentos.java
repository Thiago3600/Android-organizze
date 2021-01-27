package com.example.organizze.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizze.Model.Movimentacao;
import com.example.organizze.R;

import java.util.List;

public class AdapterMovimentos extends RecyclerView.Adapter<AdapterMovimentos.MyViewHolder> {

    private List<Movimentacao> movimentacao;
    private Context context;

    public AdapterMovimentos(List<Movimentacao> movimentacao, Context context) {
        this.movimentacao = movimentacao;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentos, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movimentacao mov = movimentacao.get(position);
        String tipo = "";

        holder.descricao.setText(mov.getDescricao());
        holder.categoria.setText(mov.getCategoria());

        if (mov.getTipo().equals("d")){
            tipo = "-";
            holder.valor.setTextColor(Color.parseColor("#F44336"));
        }else{
            tipo = "";
            holder.valor.setTextColor(Color.parseColor("#FF03DAC5"));
        }
        holder.valor.setText(tipo + " R$ "+mov.getValor());
    }

    @Override
    public int getItemCount() {
        return movimentacao.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView descricao;
        public TextView categoria;
        public TextView valor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.descricao);
            categoria = itemView.findViewById(R.id.categoria);
            valor = itemView.findViewById(R.id.valor);
        }
    }


}
