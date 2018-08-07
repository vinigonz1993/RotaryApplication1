package application.gonzalez.com.rotaryappcadeira;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditaPaciente extends AppCompatActivity {

    Context context = EditaPaciente.this;
    ImageButton addEquipamento, addEquipamento_2, btnVoltar;
    TextView nomeDoClube, edtID, txtEquipSelecionado, txtEquipSelecionado_2;
    EditText txtIdEquipSelecionado, txtIdDoEquipamentoAntigo, txtIdEquipSelecionado_2, txtIdDoEquipamentoAntigo_2;
    String dev_1, dev_2, nomeUsuario, nomePaciente, mother, nascimento, email, telefone, endereco, id_paciente, equip, equip_id_antigo, equip_2, equip_id_antigo_2;
    Button btnSalvarAlteracoes, btnExcluir, btnRemover, btnRemover_2;
    EditText edtNome, edtNomeDaMae, edtDataDeNascimento, edtEmail, edtTelefone, edtEndereco;
    EquipamentoAdapter equipamentoAdapter;
    List<Equipamento> equipamentoList;

    Button diasDevo_1, diasDevo_2, retirada_1, retirada_2;
    String ret_1, ret_2;
    private AlertDialog alerta;

    private int itemClicado;

    private String HOST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_paciente);

        final ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        txtEquipSelecionado = findViewById(R.id.txtEquipamentoSelecionado);
        txtEquipSelecionado_2 = findViewById(R.id.txtEquipamentoSelecionado_2);
        txtIdDoEquipamentoAntigo = findViewById(R.id.idDoEquipamentoAntigo);
        txtIdDoEquipamentoAntigo_2 = findViewById(R.id.idDoEquipamentoAntigo_2);
        txtIdEquipSelecionado = findViewById(R.id.idDoEquipamentoSelecionado);
        txtIdEquipSelecionado_2 = findViewById(R.id.idDoEquipamentoSelecionado_2);
        edtNome = findViewById(R.id.edtNome);
        edtNomeDaMae = findViewById(R.id.edtNomeDaMae);
        edtDataDeNascimento = findViewById(R.id.edtDataDeNascimento);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtEndereco = findViewById(R.id.edtEndereco);
        edtID = findViewById(R.id.edtID);
        addEquipamento = findViewById(R.id.addEquipamento);
        addEquipamento_2 = findViewById(R.id.addEquipamento_2);
        equipamentoList = new ArrayList<>();
        equipamentoAdapter = new EquipamentoAdapter(EditaPaciente.this, equipamentoList);
        diasDevo_1 = findViewById(R.id.tempoRetorno_1);
        diasDevo_2 = findViewById(R.id.tempoRetorno_2);
        retirada_1 = findViewById(R.id.edtRetirada);
        retirada_2 = findViewById(R.id.edtRetirada2);

        Calendar calendar = Calendar.getInstance();
        final int Day = calendar.get(Calendar.DAY_OF_MONTH);
        final int Month = calendar.get(Calendar.MONTH);
        final int Year = calendar.get(Calendar.YEAR);

        diasDevo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog1 = new DatePickerDialog(EditaPaciente.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth1) {

                        month1 = month1 + 1;
                        year1 = year1 - 2000;
                        diasDevo_1.setText(dayOfMonth1 + "/" + month1 + "/" + year1);

                    }
                }, Year, Month, Day);
                dialog1.show();
            }
        });

        diasDevo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog2 = new DatePickerDialog(EditaPaciente.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view2, int year2, int month2, int dayOfMonth2) {

                        month2 = month2 + 1;
                        year2 = year2 - 2000;
                        diasDevo_2.setText(dayOfMonth2 + "/" + month2 + "/" + year2);

                    }
                }, Year, Month, Day);
                dialog2.show();
            }
        });

        retirada_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog1 = new DatePickerDialog(EditaPaciente.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth1) {

                        month1 = month1 + 1;
                        year1 = year1 - 2000;
                        retirada_1.setText(dayOfMonth1 + "/" + month1 + "/" + year1);

                    }
                }, Year, Month, Day);
                dialog1.show();
            }
        });

        retirada_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog1 = new DatePickerDialog(EditaPaciente.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth1) {

                        month1 = month1 + 1;
                        year1 = year1 - 2000;
                        retirada_2.setText(dayOfMonth1 + "/" + month1 + "/" + year1);

                    }
                }, Year, Month, Day);
                dialog1.show();
            }
        });

        nomeDoClube = findViewById(R.id.nomeDoClube);
        id_paciente = getIntent().getExtras().getString("id_paciente");
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomePaciente = getIntent().getExtras().getString("nome");
        mother = getIntent().getExtras().getString("mother");
        nascimento = getIntent().getExtras().getString("nascimento");
        email = getIntent().getExtras().getString("email");
        telefone = getIntent().getExtras().getString("telefone");
        endereco = getIntent().getExtras().getString("endereco");
        equip = getIntent().getExtras().getString("equip");
        equip_2 = getIntent().getExtras().getString("equip_2");
        dev_1 = getIntent().getExtras().getString("devolucao_1");
        dev_2 = getIntent().getExtras().getString("devolucao_2");
        equip_id_antigo = getIntent().getExtras().getString("equip_id");
        equip_id_antigo_2 = getIntent().getExtras().getString("equip_id_2");
        ret_1 = getIntent().getExtras().getString("timestamp");
        ret_2 = getIntent().getExtras().getString("timestamp_2");
        nomeDoClube.setText(nomeUsuario);
        edtNome.setText(nomePaciente);
        edtNomeDaMae.setText(mother);
        edtDataDeNascimento.setText(nascimento);
        edtEmail.setText(email);
        edtTelefone.setText(telefone);
        edtEndereco.setText(endereco);
        edtID.setText(id_paciente);
        txtEquipSelecionado.setText(equip);
        txtEquipSelecionado_2.setText(equip_2);
        txtIdDoEquipamentoAntigo.setText(equip_id_antigo);
        txtIdDoEquipamentoAntigo_2.setText(equip_id_antigo_2);


        btnVoltar = findViewById(R.id.btnVoltar);

        buscaData();
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRemover = findViewById(R.id.btnRemover);
        btnRemover_2 = findViewById(R.id.btnRemover_2);

        btnSalvarAlteracoes = findViewById(R.id.btnSalvarPaciente);
        btnSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(EditaPaciente.this)) {

                    final ProgressDialog progressDialog = new ProgressDialog(EditaPaciente.this);
                    progressDialog.setTitle("Aguarde");
                    progressDialog.setMessage("Salvando alterações...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    new Thread() {
                        public void run() {

                            final String id_paciente = edtID.getText().toString();
                            final String nome = edtNome.getText().toString();
                            final String mother = edtNomeDaMae.getText().toString();
                            final String telefone = edtTelefone.getText().toString();
                            final String email = edtEmail.getText().toString();
                            final String endereco = edtEndereco.getText().toString();
                            final String data_nascimento = edtDataDeNascimento.getText().toString();
                            final String clube = nomeDoClube.getText().toString();
                            final String equip = txtEquipSelecionado.getText().toString();
                            final String equip_2 = txtEquipSelecionado_2.getText().toString();
                            final String equip_id = txtIdEquipSelecionado.getText().toString();
                            final String equip_id_2 = txtIdEquipSelecionado_2.getText().toString();
                            final String equip_id_antigo = txtIdDoEquipamentoAntigo.getText().toString();
                            final String equip_id_antigo_2 = txtIdDoEquipamentoAntigo_2.getText().toString();
                            final String data_devolucao_1 = diasDevo_1.getText().toString();
                            final String data_devolucao_2 = diasDevo_2.getText().toString();


                            if (nome.isEmpty()) {
                                edtNome.setError("o Campo 'NOME' é obrigatório");

                            } else if (!id_paciente.isEmpty()) {

                                if (txtEquipSelecionado.getText().toString().equals("Nenhum equipamento selecionado")) {

                                } else {

                                    if (txtIdEquipSelecionado.getText().toString() != txtIdDoEquipamentoAntigo.getText().toString()
                                            && !txtIdEquipSelecionado.getText().toString().isEmpty()) {

                                        String time = HOST + "/timestamp.php";
                                        final String timestamp = retirada_1.getText().toString();
                                        Ion.with(EditaPaciente.this)
                                                .load(time)
                                                .setBodyParameter("id", id_paciente)
                                                .setBodyParameter("timestamp", timestamp)
                                                .setBodyParameter("devolucao_1", data_devolucao_1)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {
                                                        //RETORNO DO PHP

                                                        if (result.get("UPDATE").getAsString().equals("OK")) {

                                                            Paciente c = new Paciente();

                                                            c.setId(Integer.parseInt(id_paciente));
                                                            c.setTimestamp(timestamp);
                                                            c.setDevolucao_1(data_devolucao_1);


                                                            Toast.makeText(EditaPaciente.this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                                            voltaAoInicio();

                                                        } else {

                                                            Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
//////////////////////////                       UPDATE TIMESTAMP EQUIP_2

                                if (txtEquipSelecionado_2.getText().toString().equals("Nenhum equipamento selecionado")) {
                                    String devolucaoZerada = "";
                                    updateDevolucao_2(devolucaoZerada);

                                } else {

                                    if ((txtIdEquipSelecionado_2.getText().toString() != txtIdDoEquipamentoAntigo_2.getText().toString())
                                            && !txtIdEquipSelecionado_2.getText().toString().isEmpty()) {

                                        String time = HOST + "/timestamp_2.php";
                                        final String timestamp_2 = retirada_2.getText().toString();
                                        Ion.with(EditaPaciente.this)
                                                .load(time)
                                                .setBodyParameter("id", id_paciente)
                                                .setBodyParameter("timestamp_2", timestamp_2)
                                                .setBodyParameter("devolucao_2", data_devolucao_2)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {
                                                        //RETORNO DO PHP

                                                        if (result.get("UPDATE").getAsString().equals("OK")) {

                                                            Paciente c = new Paciente();

                                                            c.setId(Integer.parseInt(id_paciente));
                                                            c.setTimestamp(timestamp_2);
                                                            c.setDevolucao_2(data_devolucao_2);

                                                            Toast.makeText(EditaPaciente.this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                                            voltaAoInicio();

                                                        } else {

                                                            Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }

                                //////////////////////////////        UPDATE EQUIP_1
//1 vazio e 2 cheio
                                if (txtIdEquipSelecionado.getText().toString().isEmpty()
                                        && !txtIdEquipSelecionado_2.getText().toString().isEmpty()) {


                                    if (txtIdEquipSelecionado_2.getText().toString() != txtIdDoEquipamentoAntigo_2.getText().toString()) {
                                        zeraNome(equip_id_antigo_2);
                                    }

                                    String url = HOST + "/update.php";
                                    Ion.with(EditaPaciente.this)
                                            .load(url)
                                            .setBodyParameter("id", id_paciente)
                                            .setBodyParameter("nome", nome)
                                            .setBodyParameter("data_nascimento", data_nascimento)
                                            .setBodyParameter("mother", mother)
                                            .setBodyParameter("telefone", telefone)
                                            .setBodyParameter("email", email)
                                            .setBodyParameter("endereco", endereco)
                                            .setBodyParameter("clube", clube)
                                            .setBodyParameter("equip", equip)
                                            .setBodyParameter("equip_2", equip_2)
                                            .setBodyParameter("equip_id", equip_id_antigo)
                                            .setBodyParameter("equip_id_2", equip_id_2)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {
                                                    //RETORNO DO PHP

                                                    if (result.get("UPDATE").getAsString().equals("OK")) {

                                                        Paciente c = new Paciente();

                                                        c.setId(Integer.parseInt(id_paciente));
                                                        c.setNome(nome);
                                                        c.setMother(mother);
                                                        c.setNascimento(data_nascimento);
                                                        c.setEmail(email);
                                                        c.setTelefone(telefone);
                                                        c.setEndereco(endereco);
                                                        c.setEquip_2(equip_2);
                                                        c.setEquip(equip);
                                                        c.setEquip_id_2(equip_id_2);
                                                        c.setEquip_id(equip_id_antigo);
                                                        Toast.makeText(EditaPaciente.this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                                        voltaAoInicio();

                                                    } else {

                                                        Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    updateAll(equip_id_2);
                                    // zeraNome(equip_id_antigo_2);

                                } else if (!txtIdEquipSelecionado.getText().toString().isEmpty()
                                        && txtIdEquipSelecionado_2.getText().toString().isEmpty()) {

                                    String url = HOST + "/update.php";

                                    Ion.with(EditaPaciente.this)
                                            .load(url)
                                            .setBodyParameter("id", id_paciente)
                                            .setBodyParameter("nome", nome)
                                            .setBodyParameter("data_nascimento", data_nascimento)
                                            .setBodyParameter("mother", mother)
                                            .setBodyParameter("telefone", telefone)
                                            .setBodyParameter("email", email)
                                            .setBodyParameter("endereco", endereco)
                                            .setBodyParameter("clube", clube)
                                            .setBodyParameter("equip", equip)
                                            .setBodyParameter("equip_2", equip_2)
                                            .setBodyParameter("equip_id", equip_id)
                                            .setBodyParameter("equip_id_2", equip_id_antigo_2)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {
                                                    //RETORNO DO PHP

                                                    if (result.get("UPDATE").getAsString().equals("OK")) {

                                                        Paciente c = new Paciente();

                                                        c.setId(Integer.parseInt(id_paciente));
                                                        c.setNome(nome);
                                                        c.setMother(mother);
                                                        c.setNascimento(data_nascimento);
                                                        c.setEmail(email);
                                                        c.setTelefone(telefone);
                                                        c.setEndereco(endereco);
                                                        c.setEquip(equip);
                                                        c.setEquip_2(equip_2);
                                                        c.setEquip_id(equip_id);
                                                        c.setEquip_id_2(equip_id_antigo_2);
                                                        Toast.makeText(EditaPaciente.this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                                        voltaAoInicio();

                                                    } else {

                                                        Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    updateAll(equip_id);

                                } else if (txtIdEquipSelecionado_2.getText().toString().isEmpty()
                                        && txtIdEquipSelecionado.getText().toString().isEmpty()) {

                                    String url = HOST + "/update.php";

                                    Ion.with(EditaPaciente.this)
                                            .load(url)
                                            .setBodyParameter("id", id_paciente)
                                            .setBodyParameter("nome", nome)
                                            .setBodyParameter("data_nascimento", data_nascimento)
                                            .setBodyParameter("mother", mother)
                                            .setBodyParameter("telefone", telefone)
                                            .setBodyParameter("email", email)
                                            .setBodyParameter("endereco", endereco)
                                            .setBodyParameter("clube", clube)
                                            .setBodyParameter("equip", equip)
                                            .setBodyParameter("equip_2", equip_2)
                                            .setBodyParameter("equip_id", equip_id_antigo)
                                            .setBodyParameter("equip_id_2", equip_id_antigo_2)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {
                                                    //RETORNO DO PHP

                                                    if (result.get("UPDATE").getAsString().equals("OK")) {

                                                        Paciente c = new Paciente();

                                                        c.setId(Integer.parseInt(id_paciente));
                                                        c.setNome(nome);
                                                        c.setMother(mother);
                                                        c.setNascimento(data_nascimento);
                                                        c.setEmail(email);
                                                        c.setTelefone(telefone);
                                                        c.setEndereco(endereco);
                                                        c.setEquip(equip);
                                                        c.setEquip_2(equip_2);
                                                        c.setEquip_id(equip_id_antigo);
                                                        c.setEquip_id_2(equip_id_antigo_2);
                                                        Toast.makeText(EditaPaciente.this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                                        voltaAoInicio();

                                                    } else {

                                                        Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else if (!txtIdEquipSelecionado_2.getText().toString().isEmpty()
                                        && !txtIdEquipSelecionado.getText().toString().isEmpty()) {

                                    if (txtIdEquipSelecionado_2.getText().toString() != txtIdDoEquipamentoAntigo_2.getText().toString()) {

                                        if (txtIdDoEquipamentoAntigo_2.getText().toString().isEmpty()) {

                                        } else {
                                            zeraNome(equip_id_antigo_2);
                                        }
                                    }

                                    if (txtIdEquipSelecionado.getText().toString() != txtIdDoEquipamentoAntigo.getText().toString()) {

                                        if (txtIdDoEquipamentoAntigo.getText().toString().isEmpty()) {

                                        } else {
                                            zeraNome(equip_id_antigo);
                                        }

                                    }
////
                                    String url = HOST + "/update.php";

                                    Ion.with(EditaPaciente.this)
                                            .load(url)
                                            .setBodyParameter("id", id_paciente)
                                            .setBodyParameter("nome", nome)
                                            .setBodyParameter("data_nascimento", data_nascimento)
                                            .setBodyParameter("mother", mother)
                                            .setBodyParameter("telefone", telefone)
                                            .setBodyParameter("email", email)
                                            .setBodyParameter("endereco", endereco)
                                            .setBodyParameter("clube", clube)
                                            .setBodyParameter("equip", equip)
                                            .setBodyParameter("equip_2", equip_2)
                                            .setBodyParameter("equip_id", equip_id)
                                            .setBodyParameter("equip_id_2", equip_id_2)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {
                                                    //RETORNO DO PHP

                                                    if (result.get("UPDATE").getAsString().equals("OK")) {

                                                        Paciente c = new Paciente();

                                                        c.setId(Integer.parseInt(id_paciente));
                                                        c.setNome(nome);
                                                        c.setMother(mother);
                                                        c.setNascimento(data_nascimento);
                                                        c.setEmail(email);
                                                        c.setTelefone(telefone);
                                                        c.setEndereco(endereco);
                                                        c.setEquip(equip);
                                                        c.setEquip_2(equip_2);
                                                        c.setEquip_id(equip_id);
                                                        c.setEquip_id_2(equip_id_2);
                                                        Toast.makeText(EditaPaciente.this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                                        voltaAoInicio();

                                                        updateAll(equip_id);
                                                        updateAll(equip_id_2);
                                                        if (equip_id.toString().isEmpty() && !equip_id_antigo.toString().isEmpty()) {
                                                            updateAll(equip_id_antigo);
                                                        } else if (!equip_id.toString().isEmpty() && equip_id_antigo.toString().isEmpty()) {
                                                            updateAll(equip_id);
                                                        } else if (equip_id_2.toString().isEmpty() && !equip_id_antigo_2.toString().isEmpty()) {
                                                            updateAll(equip_id_antigo_2);
                                                        } else if (!equip_id_2.toString().isEmpty() && equip_id_antigo_2.toString().isEmpty()) {
                                                            updateAll(equip_id_2);
                                                        }


                                                    } else {

                                                        Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });

                                    ////
                                }

                                if (!diasDevo_1.getText().toString().equals(dev_1)) {
                                    String dataComparada1 = diasDevo_1.getText().toString();
                                    updateDevolucao_1(dataComparada1);
                                }
                                if (txtEquipSelecionado.getText().toString().equals("Nenhum equipamento selecionado")) {
                                    String dataComprada1 = "";
                                    updateDevolucao_1(dataComprada1);
                                }
                                if (!diasDevo_2.getText().toString().equals(dev_2)) {
                                    String dataComparada2 = diasDevo_2.getText().toString();
                                    updateDevolucao_2(dataComparada2);
                                }
                                if (txtEquipSelecionado_2.getText().toString().equals("Nenhum equipamento selecionado")) {
                                    String dataComprada2 = "";
                                    updateDevolucao_2(dataComprada2);
                                }

                                if (!retirada_1.getText().toString().equals(ret_1)) {
                                    String dataComparada1 = retirada_1.getText().toString();
                                    updateTimestamp_1(dataComparada1);
                                }
                                if (txtEquipSelecionado.getText().toString().equals("Nenhum equipamento selecionado")) {
                                    String dataComprada1 = "";
                                    updateTimestamp_1(dataComprada1);
                                }
                                if (!retirada_2.getText().toString().equals(ret_2)) {
                                    String dataComparada2 = retirada_2.getText().toString();
                                    updateTimestamp_2(dataComparada2);
                                }
                                if (txtEquipSelecionado_2.getText().toString().equals("Nenhum equipamento selecionado")) {
                                    String dataComprada2 = "";
                                    updateTimestamp_2(dataComprada2);
                                }
                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Dismiss the dialog
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }.start();
                } else {
                    Toast.makeText(context, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }


        });

        btnExcluir = findViewById(R.id.btnExcluir);

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(EditaPaciente.this)) {
                    final String id_paciente = edtID.getText().toString();

                    if (id_paciente.isEmpty()) {
                        Toast.makeText(EditaPaciente.this, "Nenhum contato selecionado", Toast.LENGTH_SHORT).show();

                    } else {
                        //apagar o paciente

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditaPaciente.this);
                        builder.setTitle("Excluir o paciente?");
                        builder.setMessage("Paciente: " + edtNome.getText().toString());
                        builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new Thread() {
                                    public void run() {

                                        String url = HOST + "/delete.php";

                                        Ion.with(EditaPaciente.this)
                                                .load(url)
                                                .setBodyParameter("id", id_paciente)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {

                                                        if (result.get("DELETE").getAsString().equals("OK")) {

                                                            voltaAoInicio();

                                                        } else {

                                                            Toast.makeText(EditaPaciente.this, "Ocorre um erro ao excluir", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }.start();

                                if (!txtIdDoEquipamentoAntigo.getText().toString().isEmpty()) {
                                    zeraNome(equip_id_antigo);
                                }
                                if (!txtIdDoEquipamentoAntigo_2.getText().toString().isEmpty()) {
                                    zeraNome(equip_id_antigo_2);
                                }

                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alerta = builder.create();
                        alerta.show();
                    }
                } else {
                    Toast.makeText(context, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addEquipamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaEquipamento();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditaPaciente.this);
                View mView = getLayoutInflater().inflate(R.layout.list_equip, null);
                mBuilder.setTitle("Selecione um equipamento");
                ListView listView = mView.findViewById(R.id.listaViewEq);
                equipamentoList = new ArrayList<>();
                equipamentoAdapter = new EquipamentoAdapter(EditaPaciente.this, equipamentoList);
                listView.setAdapter(equipamentoAdapter);

                mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        Equipamento c = (Equipamento) adapterView.getAdapter().getItem(position);

                        String id_equip = String.valueOf(c.getId());
                        String nomeEquip = c.getNome_equipamento().toString();
                        String clube = c.getNome_clube().toString();

                        itemClicado = position;

                        dialog.dismiss();
                        txtIdEquipSelecionado.setText(id_equip);
                        txtEquipSelecionado.setText(nomeEquip);
                    }
                });
            }
        });

        addEquipamento_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaEquipamento();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditaPaciente.this);
                View mView = getLayoutInflater().inflate(R.layout.list_equip, null);
                mBuilder.setTitle("Selecione um equipamento");
                ListView listView = mView.findViewById(R.id.listaViewEq);
                equipamentoList = new ArrayList<>();
                equipamentoAdapter = new EquipamentoAdapter(EditaPaciente.this, equipamentoList);
                listView.setAdapter(equipamentoAdapter);

                mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        Equipamento c = (Equipamento) adapterView.getAdapter().getItem(position);

                        String id_equip_2 = String.valueOf(c.getId());
                        String nomeEquip_2 = c.getNome_equipamento().toString();
                        String clube = c.getNome_clube().toString();

                        itemClicado = position;

                        dialog.dismiss();
                        txtIdEquipSelecionado_2.setText(id_equip_2);
                        txtEquipSelecionado_2.setText(nomeEquip_2);
                    }
                });
            }
        });

        btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(EditaPaciente.this)) {

                    if (txtEquipSelecionado.getText().toString().equals("Nenhum equipamento selecionado")) {
                        Toast.makeText(EditaPaciente.this, "Nenhum equipamento registrado para este paciente", Toast.LENGTH_LONG).show();

                    } else if (!txtIdEquipSelecionado.getText().toString().isEmpty()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditaPaciente.this);
                        builder.setTitle("Remover o equipamento do paciente?");
                        builder.setMessage("Equipamento: " + txtEquipSelecionado.getText().toString());
                        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtIdEquipSelecionado.setText("");
                                txtEquipSelecionado.setText("Nenhum equipamento selecionado");
                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                        alerta = builder.create();
                        alerta.show();


                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditaPaciente.this);
                        builder.setTitle("Remover o equipamento do paciente?");
                        builder.setMessage("Equipamento: " + txtEquipSelecionado.getText().toString());
                        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                final String equip_id = txtIdDoEquipamentoAntigo.getText().toString();
                                final String equip_id_apagar = "";
                                final String equip = "Nenhum equipamento selecionado";
                                final String timestamp = "";
                                final String devolucao_1 = "";
                                //final String zeraNome = "Disponível";

                                Calendar calendar_start = Calendar.getInstance();
                                String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar_start.getTime());
                                String devolucao1 = currentDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");

                                Calendar c = Calendar.getInstance(); // Get Calendar Instance
                                try {
                                    c.setTime(sdf.parse(devolucao1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                c.add(Calendar.DATE, 30);  // add 45 days
                                sdf = new SimpleDateFormat("dd/MM/yy");

                                Date resultdate = new Date(c.getTimeInMillis() + 86400000 * 5);   // Get new time
                                devolucao1 = sdf.format(resultdate);
                                final String dataDeDevolucao = devolucao1;
                                diasDevo_1.setText(devolucao1);

                                new Thread() {
                                    public void run() {

                                        String url = HOST + "/removeequip.php";

                                        Ion.with(EditaPaciente.this)
                                                .load(url)
                                                .setBodyParameter("id", id_paciente)
                                                .setBodyParameter("equip", equip)
                                                .setBodyParameter("equip_id", equip_id_apagar)
                                                .setBodyParameter("timestamp", timestamp)
                                                .setBodyParameter("devolucao_1", devolucao_1)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {
                                                        //RETORNO DO PHP

                                                        try {
                                                            if (result.get("UPDATE").getAsString().equals("OK")) {

                                                                Paciente c = new Paciente();

                                                                c.setId(Integer.parseInt(id_paciente));
                                                                c.setEquip(equip);
                                                                c.setEquip_id(equip_id_apagar);
                                                                c.setTimestamp(timestamp);
                                                                c.setDevolucao_1(devolucao_1);
                                                                Toast.makeText(EditaPaciente.this, "Equipamento removido com sucesso!", Toast.LENGTH_SHORT).show();

                                                                voltaAoInicio();

                                                            } else {

                                                                Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (Throwable err) {
                                                            Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }
                                }.start();
//////////////////////////////////////////////  ZERA O NOME DO PACIENTE NA TABELA DE EQUIPAMENTOS

                                new Thread() {
                                    public void run() {

                                        zeraNome(equip_id_antigo);
                                    }
                                }.start();
/////////////////////////////////////////
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        alerta = builder.create();
                        alerta.show();
                    }
                } else {
                    Toast.makeText(context, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnRemover_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(EditaPaciente.this)) {

                    if (txtEquipSelecionado_2.getText().toString().equals("Nenhum equipamento selecionado")) {
                        Toast.makeText(EditaPaciente.this, "Nenhum equipamento registrado para este paciente", Toast.LENGTH_LONG).show();
                    } else if (!txtIdEquipSelecionado_2.getText().toString().isEmpty()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditaPaciente.this);
                        builder.setTitle("Remover o equipamento do paciente?");
                        builder.setMessage("Equipamento: " + txtEquipSelecionado.getText().toString());
                        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtIdEquipSelecionado_2.setText("");
                                txtEquipSelecionado_2.setText("Nenhum equipamento selecionado");
                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                        alerta = builder.create();
                        alerta.show();


                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditaPaciente.this);
                        builder.setTitle("Remover o equipamento do paciente?");
                        builder.setMessage("Equipamento: " + txtEquipSelecionado.getText().toString());
                        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                final String equip_id_2 = txtIdDoEquipamentoAntigo_2.getText().toString();
                                final String equip_id_apagar_2 = "";
                                final String equip_2 = "Nenhum equipamento selecionado";
                                final String timestamp_2 = "";
                                final String devolucao_2 = "";

                                Calendar calendar_start = Calendar.getInstance();
                                String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar_start.getTime());
                                String devolucao1 = currentDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");

                                Calendar c = Calendar.getInstance(); // Get Calendar Instance
                                try {
                                    c.setTime(sdf.parse(devolucao1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                c.add(Calendar.DATE, 30);  // add 45 days
                                sdf = new SimpleDateFormat("dd/MM/yy");

                                Date resultdate = new Date(c.getTimeInMillis() + 86400000 * 5);   // Get new time
                                devolucao1 = sdf.format(resultdate);
                                final String dataDeDevolucao = devolucao1;
                                diasDevo_2.setText(devolucao1);

                                //final String zeraNome = "Disponível";

                                new Thread() {
                                    public void run() {

                                        String url = HOST + "/removeequip_2.php";

                                        Ion.with(EditaPaciente.this)
                                                .load(url)
                                                .setBodyParameter("id", id_paciente)
                                                .setBodyParameter("equip_2", equip_2)
                                                .setBodyParameter("equip_id_2", equip_id_apagar_2)
                                                .setBodyParameter("timestamp_2", timestamp_2)
                                                .setBodyParameter("devolucao_2", devolucao_2)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {
                                                        //RETORNO DO PHP

                                                        try {
                                                            if (result.get("UPDATE").getAsString().equals("OK")) {

                                                                Paciente c = new Paciente();

                                                                c.setId(Integer.parseInt(id_paciente));
                                                                c.setEquip_2(equip_2);
                                                                c.setEquip_id_2(equip_id_apagar_2);
                                                                c.setTimestamp_2(timestamp_2);
                                                                c.setDevolucao_2(devolucao_2);
                                                                Toast.makeText(EditaPaciente.this, "Equipamento removido com sucesso!", Toast.LENGTH_SHORT).show();

                                                                voltaAoInicio();

                                                            } else {

                                                                Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (Throwable err) {
                                                            Toast.makeText(EditaPaciente.this, "Erro", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }.start();
//////////////////////////////////////////////  ZERA O NOME DO PACIENTE NA TABELA DE EQUIPAMENTOS

                                new Thread() {
                                    public void run() {

                                        zeraNome(equip_id_antigo_2);
                                    }
                                }.start();
/////////////////////////////////////////
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        alerta = builder.create();
                        alerta.show();
                    }
                } else {
                    Toast.makeText(context, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDevolucao_2(final String devolucao_2) {
        if (!txtEquipSelecionado_2.getText().toString().equals("Nenhum equipamento selecionado")) {

            try {
                String url_equip = HOST + "/updateDevolucao2.php";

                Ion.with(EditaPaciente.this)
                        .load(url_equip)
                        .setBodyParameter("id", id_paciente)
                        .setBodyParameter("devolucao_2", devolucao_2)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                //RETORNO DO PHP

                                if (result.get("UPDATE").getAsString().equals("OK")) {

                                    Paciente c = new Paciente();

                                    c.setId(Integer.parseInt(id_paciente));
                                    c.setDevolucao_2(devolucao_2);

                                    voltaAoInicio();

                                } else {

                                    Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (Throwable err) {
                Toast.makeText(context, "Erro ao atualizar data de devolução do equipamento 2. Tente novamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateDevolucao_1(final String devolucao_1) {
        if (!txtEquipSelecionado.getText().toString().equals("Nenhum equipamento selecionado")) {
            try {
                String url_equip = HOST + "/updateDevolucao1.php";

                Ion.with(EditaPaciente.this)
                        .load(url_equip)
                        .setBodyParameter("id", id_paciente)
                        .setBodyParameter("devolucao_1", devolucao_1)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                //RETORNO DO PHP

                                if (result.get("UPDATE").getAsString().equals("OK")) {

                                    Paciente c = new Paciente();

                                    c.setId(Integer.parseInt(id_paciente));
                                    c.setDevolucao_1(devolucao_1);

                                    voltaAoInicio();

                                } else {

                                    Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (Throwable err) {
                Toast.makeText(context, "Erro ao atualizar data de devolução do equipamento 1. Tente novamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateTimestamp_1(final String timestamp) {

        try {
            if (!txtEquipSelecionado.getText().toString().equals("Nenhum equipamento selecionado")) {
                String url_equip = HOST + "/update_timestamp.php";

                Ion.with(EditaPaciente.this)
                        .load(url_equip)
                        .setBodyParameter("id", id_paciente)
                        .setBodyParameter("timestamp", timestamp)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                //RETORNO DO PHP

                                if (result.get("UPDATE").getAsString().equals("OK")) {

                                    Paciente c = new Paciente();

                                    c.setId(Integer.parseInt(id_paciente));
                                    c.setTimestamp(timestamp);

                                    voltaAoInicio();

                                } else {

                                    Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {

            }
        } catch (Throwable err) {
            Toast.makeText(context, "Erro ao atualizar data de empréstimo do equipamento 1. Tente novamente", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateTimestamp_2(final String timestamp_2) {

        try {
            if (!txtEquipSelecionado_2.getText().toString().equals("Nenhum equipamento selecionado")) {

                String url_equip = HOST + "/update_timestamp_2.php";

                Ion.with(EditaPaciente.this)
                        .load(url_equip)
                        .setBodyParameter("id", id_paciente)
                        .setBodyParameter("timestamp_2", timestamp_2)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                //RETORNO DO PHP

                                if (result.get("UPDATE").getAsString().equals("OK")) {

                                    Paciente c = new Paciente();

                                    c.setId(Integer.parseInt(id_paciente));
                                    c.setTimestamp_2(timestamp_2);

                                    voltaAoInicio();

                                } else {

                                    Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {

            }
        } catch (Throwable err) {
            Toast.makeText(context, "Erro ao atualizar data de empréstimo do equipamento 2. Tente novamente", Toast.LENGTH_SHORT).show();
        }

    }

    private void buscaData() {

        Calendar calendar_start = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar_start.getTime());
        String devolucao1 = currentDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");

        Calendar c = Calendar.getInstance(); // Get Calendar Instance
        try {
            c.setTime(sdf.parse(devolucao1));

            c.add(Calendar.DATE, 30);  // add 45 days
            sdf = new SimpleDateFormat("dd/MM/yy");

            Date resultdate = new Date(c.getTimeInMillis() + 86400000 * 5);   // Get new time
            devolucao1 = sdf.format(resultdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String dataDeDevolucao = devolucao1;

        if (!ret_1.toString().isEmpty()) {
            retirada_1.setText(ret_1);
        } else {
            retirada_1.setText(currentDate);
        }
        if (!ret_2.toString().isEmpty()) {
            retirada_2.setText(ret_2);
        } else {
            retirada_2.setText(currentDate);
        }

        diasDevo_2.setText(dataDeDevolucao);

        if (dev_1.toString().isEmpty()) {
            diasDevo_1.setText(dataDeDevolucao);
        } else {
            diasDevo_1.setText(dev_1);
        }

        if (dev_2.toString().isEmpty()) {
            diasDevo_2.setText(dataDeDevolucao);
        } else {
            diasDevo_2.setText(dev_2);
        }


    }

    public void voltaAoInicio() {
        Intent voltar = new Intent(EditaPaciente.this, MainActivity.class);
        voltar.putExtra("nome_usuario", nomeUsuario);
        startActivity(voltar);
        finish();
    }

    private void listaEquipamento() {

        try {

            String url = HOST + "/readequip.php";

            Ion.with(getBaseContext())
                    .load(url)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {

                            try {
                                for (int i = 0; i < result.size(); i++) {

                                    JsonObject obj = result.get(i).getAsJsonObject();

                                    Equipamento c = new Equipamento();

                                    c.setId(obj.get("id").getAsInt());
                                    c.setNome_equipamento(obj.get("nome_equip").getAsString());
                                    c.setNome_paciente(obj.get("nome_paciente").getAsString());
                                    c.setNome_clube(obj.get("nome_clube").getAsString());

                                    if (obj.get("nome_clube").getAsString().equals(nomeUsuario)
                                            && obj.get("nome_paciente").getAsString().equals("Disponível")
                                            && !obj.get("id").getAsString().equals(txtIdEquipSelecionado.getText().toString())
                                            && !obj.get("id").getAsString().equals(txtIdEquipSelecionado_2.getText().toString())) {

                                        equipamentoList.add(c);
                                    }

                                }

                                if (equipamentoList.size() == 0) {
                                    Toast.makeText(context, "Nenhum equipamento em estoque", Toast.LENGTH_SHORT).show();
                                }
                                equipamentoAdapter.notifyDataSetChanged();

                            } catch (Throwable err) {
                                Toast.makeText(context, "Um erro ocorreu", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        } catch (Throwable err) {
            Toast.makeText(context, "Não foi possível carregar os equipamentos. Tente novamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAll(final String id_do_equip) {

        final String nome_atualizado = edtNome.getText().toString();

        try {

            String url_equip = HOST + "/updateAll.php";

            Ion.with(EditaPaciente.this)
                    .load(url_equip)
                    .setBodyParameter("id", id_do_equip)
                    .setBodyParameter("nome_paciente", nome_atualizado)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            //RETORNO DO PHP

                            try {
                                if (result.get("UPDATE").getAsString().equals("OK")) {

                                    Equipamento c = new Equipamento();

                                    c.setId(Integer.parseInt(id_do_equip));
                                    c.setNome_paciente(nome_atualizado);
                                    Toast.makeText(EditaPaciente.this, "Equipamento atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                    voltaAoInicio();

                                } else {

                                    Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_LONG).show();
                                }
                            }catch (Throwable err){
                                Toast.makeText(context, "Não foi possível atualizar. Tente novamente", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        } catch (Throwable err) {
            updateAll(id_do_equip);

        }
    }

    private void zeraNome(final String id_do_equip) {


        try {
            String url_equip = HOST + "/updateAll.php";

            final String nomeZerado = "Disponível";
            Ion.with(EditaPaciente.this)
                    .load(url_equip)
                    .setBodyParameter("id", id_do_equip)
                    .setBodyParameter("nome_paciente", nomeZerado)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            //RETORNO DO PHP
                            try {
                                if (e != null) {
                                    Toast.makeText(context, "ERRO", Toast.LENGTH_SHORT).show();
                                }
                                if (result.get("UPDATE").getAsString().equals("OK")) {

                                    Equipamento c = new Equipamento();

                                    c.setId(Integer.parseInt(id_do_equip));
                                    c.setNome_paciente(nomeZerado);
                                    Toast.makeText(EditaPaciente.this, "Equipamento atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                    voltaAoInicio();

                                } else {

                                    Toast.makeText(EditaPaciente.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Throwable err) {
                            }
                        }
                    });

        } catch (Throwable err) {
            Toast.makeText(context, "Erro ao realizar atualização dos dados", Toast.LENGTH_SHORT).show();
        }
    }

}

