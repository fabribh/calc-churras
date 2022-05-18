package com.fabribh.churrascocalculator;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;

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

    private Spinner spinnerItens;
    private EditText editTextNome, editTextPhone;
    private RadioGroup radioGroup;
    private CheckBox checkBoxSuco, checkBoxRefri, checkBoxCerveja,
            checkBoxBoi, checkBoxFrango, checkBoxPorco;

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
        String mensagem = "";
        String nome = editTextNome.getText().toString();
        String phone = editTextPhone.getText().toString();

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
        mensagem = nome + "\n" + phone + "\n";

        if (checkBoxBoi.isChecked()) {
            mensagem += getString(R.string.boi) + "\n";
        }
        if (checkBoxFrango.isChecked()) {
            mensagem += getString(R.string.frango) + "\n";
        }
        if (checkBoxPorco.isChecked()) {
            mensagem += getString(R.string.porco) + "\n";
        }
        if (checkBoxCerveja.isChecked()) {
            mensagem += getString(R.string.cerveja) + "\n";
        }
        if (checkBoxSuco.isChecked()) {
            mensagem += getString(R.string.suco) + "\n";
        }
        if (checkBoxRefri.isChecked()) {
            mensagem += getString(R.string.refrigerante) + "\n";
        }

        String sexo = (String) spinnerItens.getSelectedItem();
        if (sexo != null) {
            mensagem += sexo + "\n";
        }

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonSim:
                mensagem += "Acompanhante: " + getString(R.string.sim) + "\n";
                break;
            case R.id.radioButtonNao:
                mensagem += "Acompanhante: " + getString(R.string.nao) + "\n";
                break;
            default:
                makeText(this, getString(R.string.erro_acompanhante), Toast.LENGTH_SHORT)
                        .show();
                radioGroup.requestFocus();
                return;
        }

        if (mensagem.isEmpty()) {
            mensagem = getString(R.string.nenhuma_opcao_selecionada);
        } else {
            mensagem = getString(R.string.foram_selecionados) + "\n" + mensagem;
        }

        makeText(this,
                mensagem,
                Toast.LENGTH_SHORT
        ).show();
    }
}