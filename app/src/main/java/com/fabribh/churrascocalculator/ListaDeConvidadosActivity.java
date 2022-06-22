package com.fabribh.churrascocalculator;

import static com.fabribh.churrascocalculator.MainActivity.NOVO;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fabribh.churrascocalculator.adapter.ConvidadoAdapter;
import com.fabribh.churrascocalculator.entities.Convidado;
import com.fabribh.churrascocalculator.entities.Item;
import com.fabribh.churrascocalculator.utils.RecyclerItemClickListener;

import java.util.ArrayList;

public class ListaDeConvidadosActivity extends AppCompatActivity {

    public static final String MODO = "MODO";

    private RecyclerView recyclerViewConvidados;
    private RecyclerView.LayoutManager layoutManager;
    private ConvidadoAdapter convidadoAdapter;

    private ArrayList<Convidado> convidados;
    private int posicaoSelecionada = -1;
    private View viewSelecionada;
    private ActionMode actionMode;

    private int modo;

    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menuInflater.inflate(R.menu.lista_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menuItemAlterar:
                    alterarConvidado();
                    actionMode.finish();
                    return true;

                case R.id.menuItemExcluir:
                    excluirConvidado();
                    actionMode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

            if (viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode      = null;
            viewSelecionada = null;
            recyclerViewConvidados.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lista);

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
                setTitle(getString(R.string.novo_convidado));
            }
        }

        popularLista();

        recyclerViewConvidados.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        recyclerViewConvidados,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {
                                posicaoSelecionada = position;
                                alterarConvidado();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                posicaoSelecionada = position;
                                view.setBackgroundColor(Color.LTGRAY);
                                viewSelecionada = view;
                                recyclerViewConvidados.setEnabled(false);
                                actionMode = startSupportActionMode(callback);
                            }
                        })
        );
    }

    private void popularLista() {

        convidados = new ArrayList<>();

        convidadoAdapter = new ConvidadoAdapter(convidados, this);

        recyclerViewConvidados.setAdapter(convidadoAdapter);
    }

    private void excluirConvidado(){
        convidados.remove(posicaoSelecionada);
        convidadoAdapter.notifyDataSetChanged();
    }

    private void alterarConvidado() {
        Convidado convidado = convidados.get(posicaoSelecionada);
        MainActivity.alterarConvidado(this, convidado, posicaoSelecionada);
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

            if (MainActivity.posicaoNaLista != -1){
                convidados.remove(MainActivity.posicaoNaLista);
                convidados.add(posicaoSelecionada, convidado);
            }else {
                convidados.add(convidado);
            }

            convidadoAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_convidados_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemNovo:
                MainActivity.novoConvidado(this);
                return true;

            case R.id.menuItemSobre:
                SobreActivity.sobre(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}