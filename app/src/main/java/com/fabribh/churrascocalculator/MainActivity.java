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
import android.os.Parcelable;
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
import com.fabribh.churrascocalculator.entities.Item;
import com.fabribh.churrascocalculator.persistencia.ConvidadoDatabase;
import com.fabribh.churrascocalculator.utils.UtilsGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static final int KILO = 1;
    public static final int LITRO = 2;
    public static final int LATA = 3;
    private static final String ID = "ID";

    private Spinner spinnerItens;
    private EditText editTextNome, editTextPhone;
    private RadioGroup radioGroup;
    private CheckBox checkBoxSuco, checkBoxRefri, checkBoxCerveja,
            checkBoxBoi, checkBoxFrango, checkBoxPorco;

    private int modo;
    public static int posicaoNaLista = -1;

    private Set<String> ultimosItensSelecionados = new ArraySet<>();
    public static final String ARQUIVO = "com.fabribh.churrascocalculator.ULTIMOS_ITENS_SELECIONADO";

    private Convidado convidado;

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
        intent.putExtra(ID, convidado.getId());
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
                convidado = new Convidado();
                getPreferencia();
            } else {
                int id = bundle.getInt(ID);

                ConvidadoDatabase database = ConvidadoDatabase.getDatabase(this);
                convidado = database.convidadoDao().queryForId(id);

                editTextNome.setText(bundle.getString(NOME));
                editTextPhone.setText(bundle.getString(PHONE));
                spinnerItens.setSelection(bundle.getString(SEXO) == getString(R.string.masculino) ? 0 : 1);
                radioGroup.check(bundle.getString(ACOMPANHANTE).equals("Sim") ? R.id.radioButtonSim : R.id.radioButtonNao);

                String[] itensSelecionados = bundle.getString(ITENS).split("[^a-zA-Z\\t\\s]");
                for (int i = 0; i < itensSelecionados.length; i++){
                    if (itensSelecionados[i].trim().contains(getString(R.string.suco).trim())) {
                        checkBoxSuco.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().contains(getString(R.string.refrigerante).trim())) {
                        checkBoxRefri.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().contains(getString(R.string.cerveja).trim())) {
                        checkBoxCerveja.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().contains(getString(R.string.porco).trim())) {
                        checkBoxPorco.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().contains(getString(R.string.frango).trim())) {
                        checkBoxFrango.setChecked(true);
                    }
                    if (itensSelecionados[i].trim().contains(getString(R.string.boi).trim())) {
                        checkBoxBoi.setChecked(true);
                    }
                }
                setTitle(getString(R.string.alterar_convidado));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getPreferencia(){
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        ultimosItensSelecionados = sharedPreferences.getStringSet(ULTIMOS_ITENS, ultimosItensSelecionados);

        populaCheckBoxItensSharedPreferences();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPreferencia(Set<String> itensSelecionados){
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putStringSet(ULTIMOS_ITENS, itensSelecionados);

        editor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populaCheckBoxItensSharedPreferences(){
        if(!ultimosItensSelecionados.isEmpty()){
            ultimosItensSelecionados
                    .forEach(i -> {
                        if (i.contains(getString(R.string.suco))){
                          checkBoxSuco.setChecked(true);
                        }
                        if (i.contains(getString(R.string.refrigerante))){
                            checkBoxRefri.setChecked(true);
                        }
                        if (i.contains(getString(R.string.cerveja))){
                            checkBoxCerveja.setChecked(true);
                        }
                        if (i.contains(getString(R.string.frango))){
                            checkBoxFrango.setChecked(true);
                        }
                        if (i.contains(getString(R.string.porco))){
                            checkBoxPorco.setChecked(true);
                        }
                        if (i.contains(getString(boi))){
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
        String nome = UtilsGUI.validaCampoTexto(this,
                                                editTextNome.getText().toString(), // Pode ser null
                                                R.string.erro_nome);
        if (nome == null) {
            editTextNome.setText(null);
            editTextNome.requestFocus();
            return;
        }

        String phone = UtilsGUI.validaCampoTexto(this,
                                                editTextPhone.getText().toString(), // Pode ser null
                                                R.string.erro_phone);
        if (phone == null) {
            editTextPhone.setText(null);
            editTextPhone.requestFocus();
            return;
        }

        String sexo = UtilsGUI.validaCampoTexto(this,
                (String) spinnerItens.getSelectedItem(),
                R.string.erro_sexo);
        if (sexo == null) {
            spinnerItens.requestFocus();
            return;
        }

        String acompanhante;

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonSim:
                acompanhante = getString(R.string.sim);
                break;
            case R.id.radioButtonNao:
                acompanhante = getString(R.string.nao);
                break;
            default:
                UtilsGUI.validaCampoTexto(this,
                        null,
                        R.string.erro_acompanhante);
                radioGroup.requestFocus();
                return;
        }
        List<Item> itens = new ArrayList<>();

        if (checkBoxBoi.isChecked()) {
            String s = getQuantidadeDoItemSelecionado(acompanhante, KILO);
            itens.add(new Item(getString(R.string.boi),s));
        }
        if (checkBoxFrango.isChecked()) {
            String s = getQuantidadeDoItemSelecionado(acompanhante, KILO);
            itens.add(new Item(getString(R.string.frango),s));
        }
        if (checkBoxPorco.isChecked()) {
            String s = getQuantidadeDoItemSelecionado(acompanhante, KILO);
            itens.add(new Item(getString(R.string.porco),s));
        }
        if (checkBoxCerveja.isChecked()) {
            String s = getQuantidadeDoItemSelecionado(acompanhante, LATA);
            itens.add(new Item(getString(R.string.cerveja),s));
        }
        if (checkBoxSuco.isChecked()) {
            String s = getQuantidadeDoItemSelecionado(acompanhante, LITRO);
            itens.add(new Item(getString(R.string.suco),s));
        }
        if (checkBoxRefri.isChecked()) {
            String s = getQuantidadeDoItemSelecionado(acompanhante, LITRO);
            itens.add(new Item(getString(R.string.refrigerante),s));
        }

        String itensComoString = itens.stream().map(Objects::toString).collect(Collectors.joining(", "));

        Intent intent = new Intent();
        intent.putExtra(NOME, nome);
        intent.putExtra(PHONE, phone);
        intent.putExtra(SEXO, sexo);
        intent.putExtra(ACOMPANHANTE, acompanhante);
        intent.putExtra(ITENS, itensComoString);

        setPreferencia(itens.stream().map(Objects::toString).collect(Collectors.toSet()));
        ConvidadoDatabase database = ConvidadoDatabase.getDatabase(this);
        convidado.setNome(nome);
        convidado.setPhone(phone);
        convidado.setSexo(sexo);
        convidado.setAcompanhante(acompanhante);
        convidado.setItem(itensComoString);
        if (modo == NOVO) {
            database.convidadoDao().insert(convidado);
        } else {
            database.convidadoDao().update(convidado);
        }

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @NonNull
    private String getQuantidadeDoItemSelecionado(String acompanhante, int n) {
        String retorno = null;
        switch (n){
            case 1:
                retorno = acompanhante.equals(getString(R.string.sim)) ?
                    getString(R.string.kg_08) :
                    getString(R.string.kg_04);
                break;
            case 2:
                retorno = acompanhante.equals(getString(R.string.sim)) ?
                    getString(R.string.l_2) :
                    getString(R.string.l_1);
                break;
            case 3:
                retorno = acompanhante.equals(getString(R.string.sim)) ?
                        getString(R.string.lt_12) :
                        getString(R.string.lt_6);
                break;
        }
        return retorno;
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