package com.fabribh.churrascocalculator;

import static com.fabribh.churrascocalculator.MainActivity.NOVO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fabribh.churrascocalculator.adapter.ConvidadoAdapter;
import com.fabribh.churrascocalculator.entities.Convidado;
import com.fabribh.churrascocalculator.entities.Item;
import com.fabribh.churrascocalculator.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListaDeConvidadosActivity extends AppCompatActivity {

    public static final String MODO = "MODO";

    private RecyclerView recyclerViewConvidados;
    private RecyclerView.LayoutManager layoutManager;
    private ConvidadoAdapter convidadoAdapter;

    private ArrayList<Convidado> convidados;

    private int modo;

    public void adicionarConvidado(View view) {
        MainActivity.novoConvidado(this);
    }

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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            modo = bundle.getInt(MODO, NOVO);

            if (modo == NOVO) {
                setTitle("Novo Convidado");
            }
        }

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

        convidados = new ArrayList<>();

        convidadoAdapter = new ConvidadoAdapter(convidados, this);

        recyclerViewConvidados.setAdapter(convidadoAdapter);
    }

    public void abrirSobre(View view) {
        SobreActivity.sobre(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getExtras();

            ArrayList<Item> items = new ArrayList<>();

            String nome = bundle.getString(MainActivity.NOME);
            String phone = bundle.getString(MainActivity.PHONE);
            String sexo = bundle.getString(MainActivity.SEXO);
            String acompanhante = bundle.getString(MainActivity.ACOMPANHANTE);
            ArrayList<String> itensSelecionados = bundle.getStringArrayList(MainActivity.ITENS);

            itensSelecionados.stream()
                    .forEach(i -> items.add(new Item(i)));

            Convidado convidado = new Convidado(nome, phone, items);

            convidado.setSexo(sexo);
            convidado.setAcompanhante(acompanhante);

            convidados.add(convidado);

            convidadoAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}