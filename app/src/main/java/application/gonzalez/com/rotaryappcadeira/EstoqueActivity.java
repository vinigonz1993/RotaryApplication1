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

public class EstoqueActivity extends AppCompatActivity {

    RelativeLayout leyout;
    TextView nomeDoClube, txtEstoqueZero;
    String nomeUsuario;
    EquipamentoAdapter equipamentoAdapter;
    List<Equipamento> equipamentoList;
    ListView listView;
    String HOST;
    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        leyout = findViewById(R.id.estoque);
        txtEstoqueZero = findViewById(R.id.txtEstoqueZero);
        listView = findViewById(R.id.listView);
        equipamentoList = new ArrayList<>();
        equipamentoAdapter = new EquipamentoAdapter(EstoqueActivity.this, equipamentoList);
        listView.setAdapter(equipamentoAdapter);

        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        nomeDoClube = findViewById(R.id.nomeDoClube);
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomeDoClube.setText(nomeUsuario);

        if (CheckNetwork.isInternetAvailable(EstoqueActivity.this)) {
            listaEquipamento();
        } else {
            Toast.makeText(this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
        }

    }

    private void listaEquipamento() {

        final ProgressDialog pDialog = ProgressDialog.show(EstoqueActivity.this,
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
                                                && obj.get("nome_paciente").getAsString().equals("Disponível")) {
                                            equipamentoList.add(c);
                                            estoqueTotal = estoqueTotal + 1;
                                        }
                                    }

                                    Collections.sort(equipamentoList, new Comparator<Equipamento>() {
                                        @Override
                                        public int compare(Equipamento equip1, Equipamento equip2) {
                                            return equip1.getNome_equipamento().compareToIgnoreCase(equip2.getNome_equipamento());
                                        }
                                    });

                                    if (estoqueTotal == 1) {
                                        Snackbar snackbar = Snackbar.make(leyout, estoqueTotal + " Equipamento em estoque", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                    } else {
                                        Snackbar snackbar = Snackbar.make(leyout, estoqueTotal + " Equipamentos em estoque", Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                    }


                                    if (equipamentoList.size() == 0) {

                                        txtEstoqueZero.setVisibility(View.VISIBLE);
                                    } else {
                                        txtEstoqueZero.setVisibility(View.GONE);
                                    }
                                    equipamentoAdapter.notifyDataSetChanged();

                                }
                            });
                    Thread.sleep(1000);
                } catch (Throwable err) {

                    Toast.makeText(EstoqueActivity.this, "Não poi possível buscar os equipamentos. Tente novamente", Toast.LENGTH_SHORT).show();
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
}
