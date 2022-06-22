package com.fabribh.churrascocalculator;

import static android.widget.Toast.makeText;
import static com.fabribh.churrascocalculator.ListaDeConvidadosActivity.MODO;
import static com.fabribh.churrascocalculator.R.string.boi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fabribh.churrascocalculator.entities.Convidado;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    public static final int NOVO    = 1;
    public static final int ALTERAR = 2;
    public static final String NOME = "NOME";
    public static final String PHONE = "PHONE";
    public static final String SEXO = "SEXO";
    public static final String ACOMPANHANTE = "ACOMPANHANTE";
    public static final String ITENS = "ITENS";
    public static final String ULTIMOS_ITENS = "ULTIMOS_ITENS";

    private Spinner spinnerItens;
    private EditText editTextNome, editTextPhone;
    private RadioGroup radioGroup;
    private CheckBox checkBoxSuco, checkBoxRefri, checkBoxCerveja,
            checkBoxBoi, checkBoxFrango, checkBoxPorco;

    private int modo;
    public static int posicaoNaLista = -1;

    private Set<String> ultimosItensSelecionados = new ArraySet<>();
    public static final String ARQUIVO = "com.fabribh.churrascocalculator.ULTIMOS_ITENS_SELECIONADO";

    public static void novoConvidado(ListaDeConvidadosActivity activity) {

        posicaoNaLista = -1;
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, NOVO);
    }

    public static void alterarConvidado(ListaDeConvidadosActivity activity,
                                        Convidado convidado,
                                        int posicao) {
        posicaoNaLista = posicao;
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(NOME, convidado.getNome());
        intent.putExtra(PHONE, convidado.getPhone());
        intent.putExtra(SEXO, convidado.getSexo());
        intent.putExtra(ACOMPANHANTE, convidado.getAcompanhante());
        intent.putExtra(ITENS, convidado.getItem().toString());

        activity.startActivityForResult(intent, ALTERAR);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        spinnerItens = findViewById(R.id.spinnerItens);
        editTextNome = findViewById(R.id.editTextNome);
        editTextPhone = findViewById(R.id.editTextPhone);
        radioGroup = findViewById(R.id.radioGroup);

        checkBoxSuco = findViewById(R.id.checkBoxSuco);
        checkBoxRefri = findViewById(R.id.checkBoxRefri);
        checkBoxCerveja = findViewById(R.id.checkBoxCerveja);
        checkBoxBoi = findViewById(R.id.checkBoxBoi);
        checkBoxFrango = findViewById(R.id.checkBoxFrango);
        checkBoxPorco = findViewById(R.id.checkBoxPorco);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        populateSpinner();

        if (bundle != null) {
            modo = bundle.getInt(MODO, NOVO);
            if (modo == NOVO) {
                setTitle(getString(R.string.novo_convidado));
            } else {
                editTextNome.setText(bundle.getString(NOME));
                editTextPhone.setText(bundle.getString(PHONE));
                spinnerItens.setSelection(bundle.getString(SEXO) == getString(R.string.masculino) ? 0 : 1);
                radioGroup.check(bundle.getString(ACOMPANHANTE).equals("Sim") ? R.id.radioButtonSim : R.id.radioButtonNao);

                String[] itensSelecionados = bundle.getString(ITENS).split("[^a-zA-Z\\t\\s]");
                for (int i = 0; i < itensSelecionados.length; i++){
                    if (itensSelecionados[i].trim().equals(getString(R.string.suco).trim())) {
                        checkBoxSuco.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().equals(getString(R.string.refrigerante).trim())) {
                        checkBoxRefri.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().equals(getString(R.string.cerveja).trim())) {
                        checkBoxCerveja.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().equals(getString(R.string.porco).trim())) {
                        checkBoxPorco.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().equals(getString(R.string.frango).trim())) {
                        checkBoxFrango.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().equals(getString(R.string.boi).trim())) {
                        checkBoxBoi.setChecked(true);
                    }
                }
                setTitle(getString(R.string.alterar_convidado));
            }
        }
        getPreferencia();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getPreferencia(){
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        ultimosItensSelecionados = sharedPreferences.getStringSet(ULTIMOS_ITENS, ultimosItensSelecionados);

        populaCheckBoxItensSharedPreferences();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPreferencia(List<String> itensSelecionados){
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putStringSet(ULTIMOS_ITENS, itensSelecionados
                .stream()
                .collect(Collectors.toSet()));

        editor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populaCheckBoxItensSharedPreferences(){
        if(!ultimosItensSelecionados.isEmpty()){
            ultimosItensSelecionados
                    .forEach(i -> {
                        if (i.equals(getString(R.string.suco))){
                          checkBoxSuco.setChecked(true);
                        }
                        if (i.equals(getString(R.string.refrigerante))){
                            checkBoxRefri.setChecked(true);
                        }
                        if (i.equals(getString(R.string.cerveja))){
                            checkBoxCerveja.setChecked(true);
                        }
                        if (i.equals(getString(R.string.frango))){
                            checkBoxFrango.setChecked(true);
                        }
                        if (i.equals(getString(R.string.porco))){
                            checkBoxPorco.setChecked(true);
                        }
                        if (i.equals(getString(boi))){
                            checkBoxBoi.setChecked(true);
                        }
                    });
        }
    }

    private void populateSpinner() {
        ArrayList<String> list = new ArrayList<>();

        list.add(getString(R.string.feminino));
        list.add(getString(R.string.masculino));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        spinnerItens.setAdapter(arrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void salvar() {
        String nome = editTextNome.getText().toString();
        String phone = editTextPhone.getText().toString();
        String acompanhante;
        ArrayList<String> itens = new ArrayList<>();

        if (nome == null || nome.trim().isEmpty()) {
            makeText(this, getString(R.string.erro_nome), Toast.LENGTH_SHORT)
                    .show();
            editTextNome.requestFocus();
        }
        if (phone == null || phone.trim().isEmpty()) {
            makeText(this, getString(R.string.erro_phone), Toast.LENGTH_SHORT)
                    .show();
            editTextPhone.requestFocus();
        }

        if (checkBoxBoi.isChecked()) {
            itens.add(getString(boi));
        }
        if (checkBoxFrango.isChecked()) {
            itens.add(getString(R.string.frango));
        }
        if (checkBoxPorco.isChecked()) {
            itens.add(getString(R.string.porco));
        }
        if (checkBoxCerveja.isChecked()) {
            itens.add(getString(R.string.cerveja));
        }
        if (checkBoxSuco.isChecked()) {
            itens.add(getString(R.string.suco));
        }
        if (checkBoxRefri.isChecked()) {
            itens.add(getString(R.string.refrigerante));
        }

        String sexo = (String) spinnerItens.getSelectedItem();
        if (sexo == null) {
            makeText(this, getString(R.string.erro_sexo), Toast.LENGTH_SHORT)
                    .show();
        }

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonSim:
                acompanhante = getString(R.string.sim);
                break;
            case R.id.radioButtonNao:
                acompanhante = getString(R.string.nao);
                break;
            default:
                makeText(this, getString(R.string.erro_acompanhante), Toast.LENGTH_SHORT)
                        .show();
                radioGroup.requestFocus();
                return;
        }

        Intent intent = new Intent();
        intent.putExtra(NOME, nome);
        intent.putExtra(PHONE, phone);
        intent.putExtra(SEXO, sexo);
        intent.putExtra(ACOMPANHANTE, acompanhante);
        intent.putExtra(ITENS, itens);

        setPreferencia(itens);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_opcoes, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemSalvar:
                salvar();
                return true;

            case android.R.id.home:
            case R.id.menuItemCancelar:
                cancelar();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cancelar() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}