package com.fabribh.churrascocalculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fabribh.churrascocalculator.R;
import com.fabribh.churrascocalculator.entities.Convidado;

import java.util.List;
import java.util.Objects;


public class ConvidadoAdapter extends RecyclerView.Adapter<ConvidadoAdapter.ConvidadoViewHolder> {

    private final List<Convidado> convidados;
    private final Context context;

    public ConvidadoAdapter(List<Convidado> convidados, Context context) {
        this.convidados = convidados;
        this.context = context;
    }

    @NonNull
    @Override
    public ConvidadoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemListaConvidados = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_lista_de_convidados,
                        viewGroup, false);
        return new ConvidadoViewHolder(itemListaConvidados);
    }

    @Override
    public void onBindViewHolder(@NonNull ConvidadoViewHolder holder, int position) {
        Convidado convidado = convidados.get(position);

        holder.nome.setText(convidado.getNome());
        holder.phone.setText(convidado.getPhone());
        holder.itens.setText(convidado.getItem().toString());
        String sexo;
        sexo = context.getString(R.string.sexoExibicao).concat(Objects.isNull(convidado.getSexo()) ? "Feminino" : convidado.getSexo());
        holder.sexo.setText(sexo);
        String acompanhante;
        acompanhante = context.getString(R.string.acompanhanteExibir).concat(Objects.isNull(convidado.getAcompanhante()) ? "Não" : convidado.getAcompanhante());
        holder.acompanhante.setText(acompanhante);
    }

    @Override
    public int getItemCount() {
        return convidados.size();
    }

    class ConvidadoViewHolder extends RecyclerView.ViewHolder {

        public ConvidadoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView nome = itemView.findViewById(R.id.nomeConvidado);
        TextView phone = itemView.findViewById(R.id.celConvidado);
        TextView itens = itemView.findViewById(R.id.itensSelecionados);
        TextView sexo = itemView.findViewById(R.id.sexoConvidado);
        TextView acompanhante = itemView.findViewById(R.id.acompanhanteConvidado);
    }
}
