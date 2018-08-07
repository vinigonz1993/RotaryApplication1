package application.gonzalez.com.rotaryappcadeira;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class EmUso extends AppCompatActivity {

    RelativeLayout leyout;
    ImageButton btnVoltar;
    TextView nomeDoClube, equipZerado, totalDeEquipamentos;
    String nomeUsuario;
    EquipamentoAdapter equipamentoAdapter;
    List<Equipamento> equipamentoList;
    ListView listView;
    TextView txtEquipamentoZero;

    private int itemClicado;

    private String HOST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_uso);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        listView = findViewById(R.id.listView);
        equipamentoList = new ArrayList<>();
        equipamentoAdapter = new EquipamentoAdapter(EmUso.this, equipamentoList);
        listView.setAdapter(equipamentoAdapter);
        equipZerado = findViewById(R.id.txtEquipamentosZerado);
        totalDeEquipamentos = findViewById(R.id.totaldeEquipamentos);
        txtEquipamentoZero = findViewById(R.id.txtEquipamentosZerado);

        if (CheckNetwork.isInternetAvailable(EmUso.this)) {
            listaEquipamento();
            clicaItemDaLista();
        } else {
            Toast.makeText(this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
        }

        nomeDoClube = findViewById(R.id.nomeDoClube);
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomeDoClube.setText(nomeUsuario);

        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void listaEquipamento() {

        final ProgressDialog pDialog = ProgressDialog.show(EmUso.this,
                "Aguarde",
                "Carregando estoque..",
                true);

        new Thread() {
            public void run() {

                try {

                    String url = HOST + "/readequip.php";

                    Ion.with(getBaseContext())
                            .load(url)
                            .asJsonArray()
                            .setCallback(new FutureCallback<JsonArray>() {
                                @Override
                                public void onCompleted(Exception e, JsonArray result) {

                                    int estoqueTotal = 0;
                                    for (int i = 0; i < result.size(); i++) {

                                        JsonObject obj = result.get(i).getAsJsonObject();

                                        Equipamento c = new Equipamento();

                                        c.setId(obj.get("id").getAsInt());
                                        c.setNome_equipamento(obj.get("nome_equip").getAsString());
                                        c.setNome_paciente(obj.get("nome_paciente").getAsString());
                                        c.setNome_clube(obj.get("nome_clube").getAsString());

                                        if (obj.get("nome_clube").getAsString().equals(nomeUsuario)
                                                && !obj.get("nome_paciente").getAsString().equals("Disponível")) {
                                            equipamentoList.add(c);
                                            estoqueTotal = estoqueTotal + 1;
                                        }
                                    }

                                    totalDeEquipamentos.setText("Total de equipamentos: " + String.valueOf(estoqueTotal));

                                    Collections.sort(equipamentoList, new Comparator<Equipamento>() {
                                        @Override
                                        public int compare(Equipamento equip1, Equipamento equip2) {
                                            return equip1.getNome_paciente().compareToIgnoreCase(equip2.getNome_paciente());
                                        }
                                    });

                                    if (equipamentoList.size() == 0) {

                                        txtEquipamentoZero.setVisibility(View.VISIBLE);
                                    } else {
                                        txtEquipamentoZero.setVisibility(View.GONE);
                                    }
                                    equipamentoAdapter.notifyDataSetChanged();

                                }
                            });
                    Thread.sleep(1000);
                } catch (Throwable err) {

                    finish();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                    }
                });
            }
        }.start();

    }

    private void clicaItemDaLista() {

    }

    @Override
    public void onBackPressed() {
        Intent voltar = new Intent(EmUso.this, DashboardActivity.class);
        voltar.putExtra("nome_usuario", nomeUsuario);
        startActivity(voltar);
        finish();

    }
}
