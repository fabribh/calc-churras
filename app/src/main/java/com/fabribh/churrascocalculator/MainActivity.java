package com.fabribh.churrascocalculator;

import static android.widget.Toast.makeText;
import static com.fabribh.churrascocalculator.ListaDeConvidadosActivity.MODO;
import static com.fabribh.churrascocalculator.R.string.boi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fabribh.churrascocalculator.entities.Convidado;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int NOVO    = 1;
    public static final int ALTERAR = 2;
    public static final String NOME = "NOME";
    public static final String PHONE = "PHONE";
    public static final String SEXO = "SEXO";
    public static final String ACOMPANHANTE = "ACOMPANHANTE";
    public static final String ITENS = "ITENS";
    public static final String SUCO = "Suco";
    public static final String REFRI = "Refrigerante";
    public static final String CERVEJA = "Cerveja";
    public static final String FRANGO = "Frango";
    public static final String PORCO = "Porco";
    public static final String BOI = "Boi";

    private Spinner spinnerItens;
    private EditText editTextNome, editTextPhone;
    private RadioGroup radioGroup;
    private CheckBox checkBoxSuco, checkBoxRefri, checkBoxCerveja,
            checkBoxBoi, checkBoxFrango, checkBoxPorco;

    private int modo;
    public static int posicaoNaLista = -1;

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
                setTitle("Novo Convidado");
            } else {
                editTextNome.setText(bundle.getString(NOME));
                editTextPhone.setText(bundle.getString(PHONE));
                spinnerItens.setSelection(bundle.getString(SEXO) == getString(R.string.masculino) ? 0 : 1);
                radioGroup.check(bundle.getString(ACOMPANHANTE).equals("Sim") ? R.id.radioButtonSim : R.id.radioButtonNao);

                String[] itensSelecionados = bundle.getString(ITENS).split("[^a-zA-Z\\t\\s]");
                for (int i = 0; i < itensSelecionados.length; i++){
                    switch (itensSelecionados[i].trim()){
                        case SUCO:
                            checkBoxSuco.setChecked(true);
                            break;

                        case REFRI:
                            checkBoxRefri.setChecked(true);
                            break;

                        case CERVEJA:
                            checkBoxCerveja.setChecked(true);
                            break;

                        case PORCO:
                            checkBoxPorco.setChecked(true);
                            break;

                        case FRANGO:
                            checkBoxFrango.setChecked(true);
                            break;

                        case BOI:
                            checkBoxBoi.setChecked(true);
                            break;
                    }
                }
                setTitle("Alterar Convidado");
            }
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