package com.fabribh.churrascocalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fabribh.churrascocalculator.entities.Convidado;
import com.fabribh.churrascocalculator.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class ListaDeConvidadosActivity extends AppCompatActivity {

    private ListView listaDeConvidados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_convidados);

        listaDeConvidados = findViewById(R.id.listaDeConvidados);

        listaDeConvidados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Convidado convidado = (Convidado) listaDeConvidados.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        convidado + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();
            }
        });

        popularLista();
    }

    private void popularLista() {
        String[] nomes = getResources().getStringArray(R.array.nomes);
        String[] phones = getResources().getStringArray(R.array.phones);
        String[] itensChurrasco = getResources().getStringArray(R.array.itens);

        ArrayList<Convidado> convidados = new ArrayList<>();
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < itensChurrasco.length; i++) {
            items.add(new Item(itensChurrasco[i]));
        }

        for (int cont = 0; cont < nomes.length; cont++) {

            convidados.add(new Convidado(nomes[cont], phones[cont], items));
        }

        ArrayAdapter<Convidado> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                convidados
        );

        listaDeConvidados.setAdapter(adapter);
    }
}