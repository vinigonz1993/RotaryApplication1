package application.gonzalez.com.rotaryappcadeira;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //      Adicionar Timestamp entre activities        //

    TextView zeroPacientes;
    RelativeLayout searchBar, leyout;
    ImageButton btnAddPaciente, btnVoltar, btnSearch, btnCancel, btnShowSearch;
    EditText searchField;
    TextView nomeDoClube;
    String nomeUsuario;
    PacienteAdapter pacienteAdapter;
    List<Paciente> pacienteLista;
    ListView listView;
    String resultado_da_pesquisa;

    private int itemClicado;

    private String HOST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        listView = findViewById(R.id.listView);
        pacienteLista = new ArrayList<>();
        pacienteAdapter = new PacienteAdapter(MainActivity.this, pacienteLista);
        listView.setAdapter(pacienteAdapter);

        leyout = findViewById(R.id.layout);
        zeroPacientes = findViewById(R.id.txtZeroPacientes);

        nomeDoClube = findViewById(R.id.nomeDoClube);
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomeDoClube.setText(nomeUsuario);
        if (CheckNetwork.isInternetAvailable(MainActivity.this)) {
            listaPacientes();
            clicaItemDaLista();
        } else {
            Toast.makeText(this, "Nenhuma conexão foi detectada", Toast.LENGTH_SHORT).show();
        }
        //Botões
        btnAddPaciente = findViewById(R.id.addPaciente);

        btnAddPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdicionaPaciente.class);
                intent.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent);
            }
        });

        searchBar = findViewById(R.id.searchbar);

        btnVoltar = findViewById(R.id.btnVoltar);

        btnShowSearch = findViewById(R.id.search_button_show);
        btnShowSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.VISIBLE);
                btnShowSearch.setVisibility(View.GONE);
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(MainActivity.this, TelaInicial.class);
                voltar.putExtra("nome_usuario", nomeUsuario);
                startActivity(voltar);
                finish();
            }
        });


        btnSearch = findViewById(R.id.searchIcon);
        searchField = findViewById(R.id.searchField);

        btnCancel = findViewById(R.id.cancelIcon);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pacienteLista.clear();
                pacienteAdapter.notifyDataSetChanged();

                searchField.setText("");

                pacienteLista = new ArrayList<>();
                pacienteAdapter = new PacienteAdapter(MainActivity.this, pacienteLista);
                listView.setAdapter(pacienteAdapter);
                if (CheckNetwork.isInternetAvailable(MainActivity.this)) {
                    listaPacientes();
                } else {
                    Toast.makeText(MainActivity.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }

                searchBar.setVisibility(View.GONE);
                btnShowSearch.setVisibility(View.VISIBLE);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(MainActivity.this)) {
                    resultado_da_pesquisa = searchField.getText().toString().trim();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    pacienteLista.clear();
                    pacienteAdapter.notifyDataSetChanged();

                    pacienteLista = new ArrayList<>();
                    pacienteAdapter = new PacienteAdapter(MainActivity.this, pacienteLista);
                    listView.setAdapter(pacienteAdapter);

                    if (resultado_da_pesquisa.toString().isEmpty()) {
                        listaPacientes();
                    } else {
                        listaPacientesDaPesquisa();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent voltar = new Intent(MainActivity.this, TelaInicial.class);
        voltar.putExtra("nome_usuario", nomeUsuario);
        startActivity(voltar);
        finish();

    }

    private void clicaItemDaLista() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Paciente c = (Paciente) adapterView.getAdapter().getItem(position);

                String id_paciente = String.valueOf(c.getId());
                String nome = c.getNome().toString();
                String mother = c.getMother().toString();
                String email = c.getEmail().toString();
                String clube = c.getClube().toString();
                String telefone = c.getTelefone().toString();
                String nascimento = c.getNascimento();
                String endereco = c.getEndereco();
                String equip = c.getEquip();
                String equip_2 = c.getEquip_2();
                String equip_id = c.getEquip_id();
                String equip_id_2 = c.getEquip_id_2();
                String devolucao_1 = c.getDevolucao_1();
                String devolucao_2 = c.getDevolucao_2();
                String timestamp = c.getTimestamp();
                String timestamp_2 = c.getTimestamp_2();

                Intent intent = new Intent(MainActivity.this, EditaPaciente.class);
                intent.putExtra("id", id);
                intent.putExtra("nome", nome);
                intent.putExtra("mother", mother);
                intent.putExtra("email", email);
                intent.putExtra("clube", clube);
                intent.putExtra("telefone", telefone);
                intent.putExtra("nascimento", nascimento);
                intent.putExtra("endereco", endereco);
                intent.putExtra("id_paciente", id_paciente);
                intent.putExtra("nome_usuario", nomeUsuario);
                intent.putExtra("equip", equip);
                intent.putExtra("equip_2", equip_2);
                intent.putExtra("equip_id", equip_id);
                intent.putExtra("equip_id_2", equip_id_2);
                intent.putExtra("devolucao_1", devolucao_1);
                intent.putExtra("devolucao_2", devolucao_2);
                intent.putExtra("timestamp", timestamp);
                intent.putExtra("timestamp_2", timestamp_2);
                startActivity(intent);

                itemClicado = position;

                Toast.makeText(MainActivity.this, "Editar o paciente " + c.getNome(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void listaPacientes() {

        final ProgressDialog pDialog = ProgressDialog.show(MainActivity.this, "Aguarde", "Carregando pacientes..", true);

        new Thread() {
            public void run() {


                String url = HOST + "/read.php";

                Ion.with(getBaseContext())
                        .load(url)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {

                                try {

                                    int totalPacientes = 0;
                                    for (int i = 0; i < result.size(); i++) {

                                        JsonObject obj = result.get(i).getAsJsonObject();

                                        Paciente c = new Paciente();
                                        c.setId(obj.get("id").getAsInt());
                                        c.setNome(obj.get("nome").getAsString());
                                        c.setTelefone(obj.get("telefone").getAsString());
                                        c.setMother(obj.get("mother").getAsString());
                                        c.setEmail(obj.get("email").getAsString());
                                        c.setEndereco(obj.get("endereco").getAsString());
                                        c.setNascimento(obj.get("data_nascimento").getAsString());
                                        c.setClube(obj.get("clube").getAsString());
                                        c.setEquip(obj.get("equip").getAsString());
                                        c.setEquip_2(obj.get("equip_2").getAsString());
                                        c.setEquip_id(obj.get("equip_id").getAsString());
                                        c.setEquip_id_2(obj.get("equip_id_2").getAsString());
                                        c.setTimestamp(obj.get("timestamp").getAsString());
                                        c.setDevolucao_1(obj.get("devolucao_1").getAsString());
                                        c.setDevolucao_2(obj.get("devolucao_2").getAsString());
                                        c.setTimestamp_2(obj.get("timestamp_2").getAsString());


                                        if (obj.get("clube").getAsString().equals(nomeUsuario)) {
                                            pacienteLista.add(c);
                                            totalPacientes = totalPacientes + 1;
                                        }
                                    }

                                    if (totalPacientes == 1) {
                                        Snackbar snackbar = Snackbar.make(leyout, totalPacientes + " Paciente encontrado", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                    } else {
                                        Snackbar snackbar = Snackbar.make(leyout, totalPacientes + " Pacientes encontrados", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                    }

                                    if (pacienteLista.size() == 0) {
                                        zeroPacientes.setVisibility(View.VISIBLE);
                                    } else {
                                        zeroPacientes.setVisibility(View.GONE);
                                    }
                                    Thread.sleep(1000);
                                } catch (Throwable err) {
                                    Toast.makeText(MainActivity.this, "Tente novamente", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                Collections.sort(pacienteLista, new Comparator<Paciente>() {
                                    @Override
                                    public int compare(Paciente paciente1, Paciente paciente2) {
                                        return paciente1.getNome().compareToIgnoreCase(paciente2.getNome());
                                    }
                                });
                                pacienteAdapter.notifyDataSetChanged();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();
                                    }
                                });
                            }
                        });
            }
        }.start();
    }

    private void listaPacientesDaPesquisa() {

        final ProgressDialog pDialog = ProgressDialog.show(MainActivity.this, "Aguarde", "Carregando pacientes..", true);

        new Thread() {
            public void run() {

                String url = HOST + "/read.php";

                Ion.with(getBaseContext())
                        .load(url)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {

                                try {
                                    int totalPacientes = 0;
                                    ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                                            "Buscando dados. Aguarde...", true);
                                    for (int i = 0; i < result.size(); i++) {

                                        JsonObject obj = result.get(i).getAsJsonObject();

                                        Paciente c = new Paciente();
                                        c.setId(obj.get("id").getAsInt());
                                        c.setNome(obj.get("nome").getAsString());
                                        c.setTelefone(obj.get("telefone").getAsString());
                                        c.setMother(obj.get("mother").getAsString());
                                        c.setEmail(obj.get("email").getAsString());
                                        c.setEndereco(obj.get("endereco").getAsString());
                                        c.setNascimento(obj.get("data_nascimento").getAsString());
                                        c.setClube(obj.get("clube").getAsString());
                                        c.setEquip(obj.get("equip").getAsString());
                                        c.setEquip_2(obj.get("equip_2").getAsString());
                                        c.setEquip_id(obj.get("equip_id").getAsString());
                                        c.setEquip_id_2(obj.get("equip_id_2").getAsString());
                                        c.setTimestamp(obj.get("timestamp").getAsString());
                                        c.setDevolucao_1(obj.get("devolucao_1").getAsString());
                                        c.setDevolucao_2(obj.get("devolucao_2").getAsString());
                                        c.setTimestamp_2(obj.get("timestamp_2").getAsString());


                                        if (obj.get("clube").getAsString().equals(nomeUsuario) && obj.get("nome").getAsString().contains(resultado_da_pesquisa)) {
                                            pacienteLista.add(c);
                                            totalPacientes = totalPacientes + 1;
                                        }
                                    }
                                    if (totalPacientes == 1) {
                                        Snackbar snackbar = Snackbar.make(leyout, totalPacientes + " Paciente encontrado", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                    } else {
                                        Snackbar snackbar = Snackbar.make(leyout, totalPacientes + " Pacientes encontrados", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                    }

                                    dialog.dismiss();
                                    Thread.sleep(1000);
                                } catch (Throwable err) {
                                    Toast.makeText(MainActivity.this, "Tente novamente", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                Collections.sort(pacienteLista, new Comparator<Paciente>() {
                                    @Override
                                    public int compare(Paciente paciente1, Paciente paciente2) {
                                        return paciente1.getNome().compareToIgnoreCase(paciente2.getNome());
                                    }
                                });
                                pacienteAdapter.notifyDataSetChanged();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();
                                    }
                                });
                            }
                        });
            }
        }.start();
    }
}
