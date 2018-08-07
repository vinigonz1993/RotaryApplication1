package application.gonzalez.com.rotaryappcadeira;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;

public class CalendarDetailsActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    Button btnEquipDevolvido_1, btnEquipDevolvido_2;
    CardView rel1, rel2;
    ImageButton btnVoltar;
    TextView nomeClube;
    private AlertDialog dialog;
    String id_paciente, timestamp, timestamp_2, devolucao, devolucao_2,
            equip, equip_2, nomeDoClube, nome, HOST, mother, telefone, data_nascimento,
            email, endereco, equip_id, equip_id_2, nomeUsuario;

    TextView nome_paciente, equipamento_1, adicionado, devolver, equipamento_2, adicionado_2, devolver_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_details);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        rel1 = findViewById(R.id.rel1);
        rel2 = findViewById(R.id.rel2);
        nomeClube = findViewById(R.id.nomeDoClube);

        btnEquipDevolvido_1 = findViewById(R.id.btnEquip_foi_devolvido_1);
        btnEquipDevolvido_2 = findViewById(R.id.btnEquip_foi_devolvido_2);
        id_paciente = getIntent().getExtras().getString("id_paciente");
        nome = getIntent().getExtras().getString("nome");
        timestamp = getIntent().getExtras().getString("timestamp");
        mother = getIntent().getExtras().getString("mother");
        timestamp_2 = getIntent().getExtras().getString("timestamp_2");
        devolucao = getIntent().getExtras().getString("devolucao");
        devolucao_2 = getIntent().getExtras().getString("devolucao_2");
        equip = getIntent().getExtras().getString("equip");
        telefone = getIntent().getExtras().getString("telefone");
        email = getIntent().getExtras().getString("email");
        endereco = getIntent().getExtras().getString("endereco");
        data_nascimento = getIntent().getExtras().getString("nascimento");
        equip_2 = getIntent().getExtras().getString("equip_2");
        equip_id = getIntent().getExtras().getString("equip_id");
        equip_id_2 = getIntent().getExtras().getString("equip_id_2");
        nomeDoClube = getIntent().getExtras().getString("nome_usuario");
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");

        nomeClube.setText(nomeDoClube);
        nome_paciente = findViewById(R.id.txtInfoNome);
        equipamento_1 = findViewById(R.id.txtInfoEquipamento1);
        adicionado = findViewById(R.id.txtInfoAdicionado1);
        devolver = findViewById(R.id.txtInfoDevolucao1);
        equipamento_2 = findViewById(R.id.txtInfoEquipamento2);
        adicionado_2 = findViewById(R.id.txtInfoAdicionado2);
        devolver_2 = findViewById(R.id.txtInfoDevolucao2);
        btnVoltar = findViewById(R.id.btnVoltar);

        nome_paciente.setText(nome);
        equipamento_1.setText(equip);
        adicionado.setText(timestamp);
        devolver.setText(devolucao);
        equipamento_2.setText(equip_2);
        adicionado_2.setText(timestamp_2);
        devolver_2.setText(devolucao_2);

        if (equipamento_1.getText().toString().equals("Nenhum equipamento selecionado")) {
            rel1.setVisibility(View.GONE);
        }

        if (equipamento_2.getText().toString().equals("Nenhum equipamento selecionado")) {
            rel2.setVisibility(View.GONE);
        }

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnEquipDevolvido_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.isInternetAvailable(CalendarDetailsActivity.this)) {
                    createpopupdialog();
                } else {
                    Toast.makeText(CalendarDetailsActivity.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEquipDevolvido_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.isInternetAvailable(CalendarDetailsActivity.this)) {
                    createpopupdialog2();
                } else {
                    Toast.makeText(CalendarDetailsActivity.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createpopupdialog2() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_devolucao, null);
        Button cancel = view.findViewById(R.id.cancel);
        Button confirmarDevolucao = view.findViewById(R.id.confirmarDevolucao);
        final Button calendario = view.findViewById(R.id.btnCalendarioDevolucao);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int Day = calendar.get(Calendar.DAY_OF_MONTH);
                final int Month = calendar.get(Calendar.MONTH);
                final int Year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog1 = new DatePickerDialog(CalendarDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth1) {

                        month1 = month1 + 1;
                        year1 = year1 - 2000;
                        calendario.setText(dayOfMonth1 + "/" + month1 + "/" + year1);

                    }
                }, Year, Month, Day);
                dialog1.show();
            }
        });

        confirmarDevolucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.isInternetAvailable(CalendarDetailsActivity.this)) {
                    final String data_devolvido2 = calendario.getText().toString();

                    String url = HOST + "/devolvido_create.php";

                    Ion.with(CalendarDetailsActivity.this)
                            .load(url)
                            .setBodyParameter("nome", nome)
                            .setBodyParameter("data_nascimento", data_nascimento)
                            .setBodyParameter("mother", mother)
                            .setBodyParameter("telefone", telefone)
                            .setBodyParameter("email", email)
                            .setBodyParameter("endereco", endereco)
                            .setBodyParameter("clube", nomeDoClube)
                            .setBodyParameter("equip", equip_2)
                            .setBodyParameter("equip_id", equip_id_2)
                            .setBodyParameter("data_added", timestamp_2)
                            .setBodyParameter("data_devolucao", data_devolvido2)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    try {

                                        if (result.get("CREATE").getAsString().equals("OK")) {

                                            int idRetornada = Integer.parseInt(result.get("ID").getAsString());

                                            Devolvido d = new Devolvido();

                                            d.setId(idRetornada);
                                            d.setNome(nome);
                                            d.setTelefone(telefone);
                                            d.setEmail(email);
                                            d.setEndereco(endereco);
                                            d.setEquip(equip_2);
                                            d.setData_added(timestamp_2);
                                            d.setData_devolucao(data_devolvido2);
                                            d.setMother(mother);
                                            d.setData_nascimento(data_nascimento);
                                            d.setClube(nomeDoClube);
                                            d.setEquip_id(equip_id_2);

                                            Toast.makeText(CalendarDetailsActivity.this, "Equipamento devolvido!", Toast.LENGTH_SHORT).show();
                                            Intent voltar = new Intent(CalendarDetailsActivity.this, TelaInicial.class);
                                            voltar.putExtra("nome_usuario", nomeUsuario);
                                            startActivity(voltar);
                                            finish();
                                        } else {

                                        }
                                    } catch (Throwable err) {
                                        Toast.makeText(CalendarDetailsActivity.this, "Erro ao confirmar devolução. Tente novamente", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    String remove_equip = HOST + "/removeequip_2.php";

                    final String zera = "";
                    final String zera_equipamento = "Nenhum equipamento selecionado";
                    Ion.with(CalendarDetailsActivity.this)
                            .load(remove_equip)
                            .setBodyParameter("id", id_paciente)
                            .setBodyParameter("equip_2", zera_equipamento)
                            .setBodyParameter("equip_id_2", zera)
                            .setBodyParameter("timestamp_2", zera)
                            .setBodyParameter("devolucao_2", zera)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    //RETORNO DO PHP

                                    if (result.get("UPDATE").getAsString().equals("OK")) {

                                        Paciente c = new Paciente();

                                        c.setId(Integer.parseInt(id_paciente));
                                        c.setEquip_2(zera_equipamento);
                                        c.setEquip_id_2(zera);
                                        c.setTimestamp_2(zera);
                                        c.setDevolucao_2(zera);

                                    } else {

                                    }
                                }
                            });

                    zeraNome(equip_id_2);
                    dialog.dismiss();
                } else {
                    Toast.makeText(CalendarDetailsActivity.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void createpopupdialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_devolucao, null);
        Button cancel = view.findViewById(R.id.cancel);
        Button confirmarDevolucao = view.findViewById(R.id.confirmarDevolucao);
        final Button calendario = view.findViewById(R.id.btnCalendarioDevolucao);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int Day = calendar.get(Calendar.DAY_OF_MONTH);
                final int Month = calendar.get(Calendar.MONTH);
                final int Year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog1 = new DatePickerDialog(CalendarDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth1) {

                        month1 = month1 + 1;
                        year1 = year1 - 2000;
                        calendario.setText(dayOfMonth1 + "/" + month1 + "/" + year1);

                    }
                }, Year, Month, Day);
                dialog1.show();
            }
        });

        confirmarDevolucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.isInternetAvailable(CalendarDetailsActivity.this)) {

                    final String data_devolvido = calendario.getText().toString();
                    String url = HOST + "/devolvido_create.php";

                    Ion.with(CalendarDetailsActivity.this)
                            .load(url)
                            .setBodyParameter("nome", nome)
                            .setBodyParameter("data_nascimento", data_nascimento)
                            .setBodyParameter("mother", mother)
                            .setBodyParameter("telefone", telefone)
                            .setBodyParameter("email", email)
                            .setBodyParameter("endereco", endereco)
                            .setBodyParameter("clube", nomeDoClube)
                            .setBodyParameter("equip", equip)
                            .setBodyParameter("equip_id", equip_id)
                            .setBodyParameter("data_added", timestamp)
                            .setBodyParameter("data_devolucao", data_devolvido)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    try {
                                        if (result.get("CREATE").getAsString().equals("OK")) {

                                            int idRetornada = Integer.parseInt(result.get("ID").getAsString());

                                            Devolvido d = new Devolvido();

                                            d.setId(idRetornada);
                                            d.setNome(nome);
                                            d.setTelefone(telefone);
                                            d.setEmail(email);
                                            d.setEndereco(endereco);
                                            d.setEquip(equip);
                                            d.setData_added(timestamp);
                                            d.setData_devolucao(data_devolvido);
                                            d.setMother(mother);
                                            d.setData_nascimento(data_nascimento);
                                            d.setClube(nomeDoClube);
                                            d.setEquip_id(equip_id);

                                            Toast.makeText(CalendarDetailsActivity.this, "Equipamento devolvido!", Toast.LENGTH_SHORT).show();
                                            Intent voltar = new Intent(CalendarDetailsActivity.this, TelaInicial.class);
                                            voltar.putExtra("nome_usuario", nomeUsuario);
                                            startActivity(voltar);
                                            finish();

                                        } else {

                                            Toast.makeText(CalendarDetailsActivity.this, "Ocorre um erro ao salvar", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Throwable err) {
                                    }
                                }
                            });

                    String remove_equip = HOST + "/removeequip.php";

                    final String zera = "";
                    final String zera_equipamento = "Nenhum equipamento selecionado";
                    Ion.with(CalendarDetailsActivity.this)
                            .load(remove_equip)
                            .setBodyParameter("id", id_paciente)
                            .setBodyParameter("equip", zera_equipamento)
                            .setBodyParameter("equip_id", zera)
                            .setBodyParameter("timestamp", zera)
                            .setBodyParameter("devolucao_1", zera)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    //RETORNO DO PHP

                                    if (result.get("UPDATE").getAsString().equals("OK")) {

                                        Paciente c = new Paciente();

                                        c.setId(Integer.parseInt(id_paciente));
                                        c.setEquip(zera_equipamento);
                                        c.setEquip_id(zera);
                                        c.setTimestamp(zera);
                                        c.setDevolucao_1(zera);


                                    } else {

                                    }
                                }
                            });

                    zeraNome(equip_id);
                    dialog.dismiss();
                } else {
                    Toast.makeText(CalendarDetailsActivity.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void zeraNome(final String id_do_equip) {

        try {

            String url_equip = HOST + "/updateAll.php";

            final String nomeZerado = "Disponível";
            Ion.with(CalendarDetailsActivity.this)
                    .load(url_equip)
                    .setBodyParameter("id", id_do_equip)
                    .setBodyParameter("nome_paciente", nomeZerado)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            //RETORNO DO PHP

                            if (e != null) {
                                Toast.makeText(CalendarDetailsActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                            }
                            if (result.get("UPDATE").getAsString().equals("OK")) {

                                Equipamento c = new Equipamento();

                                c.setId(Integer.parseInt(id_do_equip));
                                c.setNome_paciente(nomeZerado);

                            } else {

                            }
                        }
                    });

        } catch (Throwable err) {
            zeraNome(id_do_equip);
        }
    }

}
