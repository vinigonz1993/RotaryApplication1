package application.gonzalez.com.rotaryappcadeira;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Historico_devolvido extends AppCompatActivity {

    String resultado_da_pesquisa;
    EditText searchField;
    RelativeLayout searchBar, leyout;
    String nomeUsuario;
    TextView nomeDoClube, txtRegistroZero;
    List<Devolvido> listaDevolvido;
    HistoricoAdapter historicoAdapter;
    ListView listView;
    String HOST;
    private AlertDialog alerta;
    ImageButton btnVoltar, btnShowHideSearch, btnSearch, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_devolvido);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        nomeDoClube = findViewById(R.id.nomeDoClube);
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomeDoClube.setText(nomeUsuario);

        searchBar = findViewById(R.id.searchbar);

        listView = findViewById(R.id.listaHist);
        listaDevolvido = new ArrayList<>();
        historicoAdapter = new HistoricoAdapter(Historico_devolvido.this, listaDevolvido);
        listView.setAdapter(historicoAdapter);
        txtRegistroZero = findViewById(R.id.txtRegistroZero);

        leyout = findViewById(R.id.layoutH);

        if (CheckNetwork.isInternetAvailable(Historico_devolvido.this)) {
            listaHistoricoDevolvidos();
            clicaItem();
        } else {
            Toast.makeText(this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
        }

        btnShowHideSearch = findViewById(R.id.search_button_show);
        btnShowHideSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.VISIBLE);
                btnShowHideSearch.setVisibility(View.GONE);
            }
        });

        btnSearch = findViewById(R.id.searchIcon);
        searchField = findViewById(R.id.searchField);

        btnCancel = findViewById(R.id.cancelIcon);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listaDevolvido.clear();
                historicoAdapter.notifyDataSetChanged();

                searchField.setText("");

                listaDevolvido = new ArrayList<>();
                historicoAdapter = new HistoricoAdapter(Historico_devolvido.this, listaDevolvido);
                listView.setAdapter(historicoAdapter);
                if (CheckNetwork.isInternetAvailable(Historico_devolvido.this)) {
                    listaHistoricoDevolvidos();
                } else {
                    Toast.makeText(Historico_devolvido.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
                searchBar.setVisibility(View.GONE);
                btnShowHideSearch.setVisibility(View.VISIBLE);

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resultado_da_pesquisa = searchField.getText().toString().trim();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                listaDevolvido.clear();
                historicoAdapter.notifyDataSetChanged();

                listaDevolvido = new ArrayList<>();
                historicoAdapter = new HistoricoAdapter(Historico_devolvido.this, listaDevolvido);
                listView.setAdapter(historicoAdapter);

                if (CheckNetwork.isInternetAvailable(Historico_devolvido.this)) {
                    if (resultado_da_pesquisa.toString().isEmpty()) {
                        listaHistoricoDevolvidos();
                    } else {
                        listaPesquisa();
                    }
                } else {
                    Toast.makeText(Historico_devolvido.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(Historico_devolvido.this, TelaInicial.class);
                voltar.putExtra("nome_usuario", nomeUsuario);
                startActivity(voltar);
                finish();
            }
        });


    }

    private void clicaItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Devolvido d = (Devolvido) adapterView.getAdapter().getItem(position);

                String nome = d.getNome();
                String telefone = d.getTelefone();
                String id_paciente = String.valueOf(d.getId());
                String email = d.getEmail();
                String mother = d.getMother();
                String endereco = d.getEndereco();
                String clube = d.getClube();
                String data_nascimento = d.getData_nascimento();
                String equip = d.getEquip();
                String equip_id = d.getEquip_id();
                String data_added = d.getData_added();
                String data_devolucao = d.getData_devolucao();

                Intent intent = new Intent(Historico_devolvido.this, HistoricoDetails.class);
                intent.putExtra("nome", nome);
                intent.putExtra("mother", mother);
                intent.putExtra("email", email);
                intent.putExtra("clube", clube);
                intent.putExtra("telefone", telefone);
                intent.putExtra("data_nascimento", data_nascimento);
                intent.putExtra("endereco", endereco);
                intent.putExtra("id_paciente", id_paciente);
                intent.putExtra("equip", equip);
                intent.putExtra("equip_id", equip_id);
                intent.putExtra("data_added", data_added);
                intent.putExtra("data_devolucao", data_devolucao);
                startActivity(intent);

            }
        });
    }

    private void listaHistoricoDevolvidos() {

        final ProgressDialog pDialog = ProgressDialog.show(Historico_devolvido.this,
                "Aguarde", "Buscando pacientes..", true);

        new Thread() {
            public void run() {

                String url = HOST + "/read_devolvidos.php";

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

                                        Devolvido c = new Devolvido();
                                        c.setId(obj.get("id").getAsInt());
                                        c.setNome(obj.get("nome").getAsString());
                                        c.setTelefone(obj.get("telefone").getAsString());
                                        c.setMother(obj.get("mother").getAsString());
                                        c.setEmail(obj.get("email").getAsString());
                                        c.setEndereco(obj.get("endereco").getAsString());
                                        c.setData_nascimento(obj.get("data_nascimento").getAsString());
                                        c.setClube(obj.get("clube").getAsString());
                                        c.setEquip(obj.get("equip").getAsString());
                                        c.setEquip_id(obj.get("equip_id").getAsString());
                                        c.setData_added(obj.get("data_added").getAsString());
                                        c.setData_devolucao(obj.get("data_devolucao").getAsString());

                                        if (obj.get("clube").getAsString().equals(nomeUsuario)) {
                                            listaDevolvido.add(c);
                                            totalPacientes = totalPacientes + 1;
                                        }
                                    }
                                    if (totalPacientes == 1) {
                                        Snackbar snackbar = Snackbar.make(leyout, totalPacientes + " Registro encontrado", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                    } else {
                                        Snackbar snackbar = Snackbar.make(leyout, totalPacientes + " Registros encontrados", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                    }

                                    if (totalPacientes == 0) {
                                        txtRegistroZero.setVisibility(View.VISIBLE);
                                    } else {
                                        txtRegistroZero.setVisibility(View.GONE);
                                    }

                                    Thread.sleep(1000);
                                } catch (Throwable err) {
                                    Toast.makeText(Historico_devolvido.this, "Tente novamente", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();
                                    }
                                });

                                Collections.sort(listaDevolvido, new Comparator<Devolvido>() {
                                    @Override
                                    public int compare(Devolvido paciente1, Devolvido paciente2) {
                                        return paciente1.getNome().compareToIgnoreCase(paciente2.getNome());
                                    }
                                });

                                historicoAdapter.notifyDataSetChanged();
                            }
                        });
            }
        }.start();
    }

    private void listaPesquisa() {
        final ProgressDialog pDialog = ProgressDialog.show(Historico_devolvido.this, "Aguarde", "Buscando dados..", true);

        new Thread() {
            public void run() {

                String url = HOST + "/read_devolvidos.php";

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

                                        Devolvido c = new Devolvido();
                                        c.setId(obj.get("id").getAsInt());
                                        c.setNome(obj.get("nome").getAsString());
                                        c.setTelefone(obj.get("telefone").getAsString());
                                        c.setMother(obj.get("mother").getAsString());
                                        c.setEmail(obj.get("email").getAsString());
                                        c.setEndereco(obj.get("endereco").getAsString());
                                        c.setData_nascimento(obj.get("data_nascimento").getAsString());
                                        c.setClube(obj.get("clube").getAsString());
                                        c.setEquip(obj.get("equip").getAsString());
                                        c.setEquip_id(obj.get("equip_id").getAsString());
                                        c.setData_added(obj.get("data_added").getAsString());
                                        c.setData_devolucao(obj.get("data_devolucao").getAsString());

                                        if (obj.get("clube").getAsString().equals(nomeUsuario)
                                                && obj.get("nome").getAsString().contains(resultado_da_pesquisa)) {
                                            listaDevolvido.add(c);
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

                                    Thread.sleep(1000);
                                } catch (Throwable err) {
                                    Toast.makeText(Historico_devolvido.this, "Tente novamente", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                Collections.sort(listaDevolvido, new Comparator<Devolvido>() {
                                    @Override
                                    public int compare(Devolvido paciente1, Devolvido paciente2) {
                                        return paciente1.getNome().compareToIgnoreCase(paciente2.getNome());
                                    }
                                });
                                historicoAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        Intent voltar = new Intent(Historico_devolvido.this, TelaInicial.class);
        voltar.putExtra("nome_usuario", nomeUsuario);
        startActivity(voltar);
        finish();

    }
}
