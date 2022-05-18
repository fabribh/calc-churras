package com.fabribh.churrascocalculator.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fabribh.churrascocalculator.R;
import com.fabribh.churrascocalculator.entities.Convidado;

import java.util.List;


public class ConvidadoAdapter<T> extends BaseAdapter {

    private final List<Convidado> convidados;
    private final Activity activity;

    public ConvidadoAdapter(List<Convidado> convidados, Activity activity) {
        this.convidados = convidados;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return convidados.size();
    }

    @Override
    public Object getItem(int i) {
        return convidados.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater()
                .inflate(R.layout.activity_lista_de_convidados, parent, false);
        Convidado convidado = convidados.get(position);

        TextView nome = (TextView) view.findViewById(R.id.editTextNome);
        TextView phone = (TextView) view.findViewById(R.id.editTextPhone);

        nome.setText(convidado.getNome());
        phone.setText(convidado.getPhone());

        return view;
    }
}
