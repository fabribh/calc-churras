package com.fabribh.churrascocalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fabribh.churrascocalculator.adapter.ConvidadoAdapter;
import com.fabribh.churrascocalculator.entities.Convidado;
import com.fabribh.churrascocalculator.entities.Item;
import com.fabribh.churrascocalculator.utils.RecyclerItemClickListener;

import java.util.ArrayList;

public class ListaDeConvidadosActivity extends AppCompatActivity {

    private RecyclerView                recyclerViewConvidados;
    private RecyclerView.LayoutManager  layoutManager;
    private ConvidadoAdapter            convidadoAdapter;

    private ArrayList<Convidado> convidados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lista);

        getSupportActionBar().hide();

        recyclerViewConvidados = findViewById(R.id.recycleView_convidados);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewConvidados.setLayoutManager(layoutManager);
        recyclerViewConvidados.setHasFixedSize(true);
        recyclerViewConvidados.addItemDecoration(new DividerItemDecoration(this,
                LinearLayout.VERTICAL));

        popularLista();

        recyclerViewConvidados.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        recyclerViewConvidados,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {
                                Convidado convidado = convidados.get(position);

                                Toast.makeText(getApplicationContext(),
                                        convidado.getNome() + " Recebeu um click!",
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Convidado convidado = convidados.get(position);

                                Toast.makeText(getApplicationContext(),
                                        convidado.getNome() + " Recebeu um click longo!",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
        );
    }

    private void popularLista() {
        String[] nomes = getResources().getStringArray(R.array.nomes);
        String[] phones = getResources().getStringArray(R.array.phones);
        String[] itensChurrasco = getResources().getStringArray(R.array.itens);

        convidados = new ArrayList<>();
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < itensChurrasco.length; i++) {
            items.add(new Item(itensChurrasco[i]));
        }

        for (int cont = 0; cont < nomes.length; cont++) {

            convidados.add(new Convidado(nomes[cont], phones[cont], items));
        }

        convidadoAdapter = new ConvidadoAdapter(convidados, this);

        recyclerViewConvidados.setAdapter(convidadoAdapter);
    }
}