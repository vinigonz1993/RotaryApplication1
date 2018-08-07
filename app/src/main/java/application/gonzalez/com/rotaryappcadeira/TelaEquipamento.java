package application.gonzalez.com.rotaryappcadeira;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class TelaEquipamento extends AppCompatActivity {

    ImageButton btnAddCadeira, btnVoltar;
    TextView nomeDoClube, equipZerado, totalDeEquipamentos;
    String nomeUsuario;
    EquipamentoAdapter equipamentoAdapter;
    List<Equipamento> equipamentoList;
    ListView listView;

    private int itemClicado;

    private String HOST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_equipamento);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        listView = findViewById(R.id.listView);
        equipamentoList = new ArrayList<>();
        equipamentoAdapter = new EquipamentoAdapter(TelaEquipamento.this, equipamentoList);
        listView.setAdapter(equipamentoAdapter);
        equipZerado = findViewById(R.id.txtEquipamentosZerado);
        totalDeEquipamentos = findViewById(R.id.totaldeEquipamentos);

        if (CheckNetwork.isInternetAvailable(TelaEquipamento.this)) {
            listaEquipamento();
            clicaItemDaLista();
        } else {
            Toast.makeText(this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
        }

        nomeDoClube = findViewById(R.id.nomeDoClube);
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomeDoClube.setText(nomeUsuario);

        btnAddCadeira = findViewById(R.id.addCadeira);
        btnAddCadeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaEquipamento.this, AdicionaEquipamento.class);
                intent.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent);
            }
        });

        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(TelaEquipamento.this, DashboardActivity.class);
                voltar.putExtra("nome_usuario", nomeUsuario);
                startActivity(voltar);
            }
        });
    }

    private void clicaItemDaLista() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Equipamento c = (Equipamento) adapterView.getAdapter().getItem(position);

                String id_paciente = String.valueOf(c.getId());
                String id_equip = String.valueOf(c.getId());
                String nomeEquip = c.getNome_equipamento().toString();
                String clube = c.getNome_clube().toString();
                String nome_paciente = c.getNome_paciente().toString();

                Intent intent = new Intent(TelaEquipamento.this, EditaEquipamento.class);
                intent.putExtra("id", id);
                intent.putExtra("nome", nomeEquip);
                intent.putExtra("clube", clube);
                intent.putExtra("nome_paciente", nome_paciente);
                intent.putExtra("id_paciente", id_paciente);
                intent.putExtra("nome_usuario", nomeUsuario);
                intent.putExtra("id_equip", id_equip);
                startActivity(intent);

                itemClicado = position;

                Toast.makeText(TelaEquipamento.this, "Editar o paciente " + c.getNome_equipamento(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void listaEquipamento() {

        final ProgressDialog pDialog = ProgressDialog.show(TelaEquipamento.this,
                "Aguarde",
                "Carregando equipamentos..",
                true);

        new Thread() {
            public void run() {


                String url = HOST + "/readequip.php";

                Ion.with(getBaseContext())
                        .load(url)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {

                                try {
                                    int ttlEquip = 0;
                                    for (int i = 0; i < result.size(); i++) {

                                        JsonObject obj = result.get(i).getAsJsonObject();

                                        Equipamento c = new Equipamento();

                                        c.setId(obj.get("id").getAsInt());
                                        c.setNome_equipamento(obj.get("nome_equip").getAsString());
                                        c.setNome_paciente(obj.get("nome_paciente").getAsString());
                                        c.setNome_clube(obj.get("nome_clube").getAsString());

                                        if (obj.get("nome_clube").getAsString().equals(nomeUsuario)) {
                                            equipamentoList.add(c);

                                            ttlEquip = ttlEquip + 1;
                                        }


                                        equipamentoAdapter.notifyDataSetChanged();
                                    }

                                    //Toast.makeText(TelaEquipamento.this, "Total: " + ttlEquip, Toast.LENGTH_SHORT).show();
                                    if (ttlEquip == 0) {
                                        totalDeEquipamentos.setVisibility(View.GONE);
                                    } else {
                                        totalDeEquipamentos.setText("TOTAL DE EQUIPAMENTOS: " + ttlEquip);
                                    }

                                    Collections.sort(equipamentoList, new Comparator<Equipamento>() {
                                        @Override
                                        public int compare(Equipamento equip1, Equipamento equip2) {
                                            return equip1.getNome_paciente().compareToIgnoreCase(equip2.getNome_paciente());
                                        }
                                    });

                                    if (equipamentoList.size() == 0) {

                                        equipZerado.setVisibility(View.VISIBLE);
                                    } else {
                                        equipZerado.setVisibility(View.GONE);
                                    }
                                    Thread.sleep(1000);
                                } catch (Throwable err) {
                                    Toast.makeText(TelaEquipamento.this, "Tente novamente", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }


                        });


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                    }
                });

            }
        }.start();
    }


    @Override
    public void onBackPressed() {
        Intent voltar = new Intent(TelaEquipamento.this, DashboardActivity.class);
        voltar.putExtra("nome_usuario", nomeUsuario);
        startActivity(voltar);
        finish();

    }
}
