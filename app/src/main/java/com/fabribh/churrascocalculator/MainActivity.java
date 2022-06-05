package com.fabribh.churrascocalculator;

import static android.widget.Toast.makeText;

import static com.fabribh.churrascocalculator.ListaDeConvidadosActivity.MODO;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int NOVO    = 1;
    public static final String NOME = "NOME";
    public static final String PHONE = "PHONE";
    public static final String SEXO = "SEXO";
    public static final String ACOMPANHANTE = "ACOMPANHANTE";
    public static final String ITENS = "ITENS";

    private Spinner spinnerItens;
    private EditText editTextNome, editTextPhone;
    private RadioGroup radioGroup;
    private CheckBox checkBoxSuco, checkBoxRefri, checkBoxCerveja,
            checkBoxBoi, checkBoxFrango, checkBoxPorco;

    public static void novoConvidado(AppCompatActivity activity) {

        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, NOVO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        populateSpinner();
    }

    private void populateSpinner() {
        ArrayList<String> list = new ArrayList<>();

        list.add(getString(R.string.feminino));
        list.add(getString(R.string.masculino));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        spinnerItens.setAdapter(arrayAdapter);
    }

    public void limparCampos(View view) {
        String mensagemLimpar = getString(R.string.campos_limpos_com_sucesso);
        editTextPhone.setText(null);
        editTextNome.setText(null);

        radioGroup.clearCheck();

        checkBoxPorco.setChecked(false);
        checkBoxBoi.setChecked(false);
        checkBoxFrango.setChecked(false);
        checkBoxSuco.setChecked(false);
        checkBoxCerveja.setChecked(false);
        checkBoxRefri.setChecked(false);

        spinnerItens.setSelected(false);

        makeText(this, mensagemLimpar, Toast.LENGTH_SHORT)
                .show();
    }

    public void salvar(View view) {
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
            itens.add(getString(R.string.boi));
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
}