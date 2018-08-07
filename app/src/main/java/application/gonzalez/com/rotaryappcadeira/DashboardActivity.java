package application.gonzalez.com.rotaryappcadeira;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class DashboardActivity extends AppCompatActivity {

    CardView card1, card2, card3;
    ImageButton btnVoltar;
    TextView nomeDoClube;
    TextView txtCadastrados, txtDisponiveis, txtEmUso;
    String nomeUsuario;
    String HOST;

    TextView txtcadeiras, txtbanho, txtoutros, txtNcadeiras, txtNbanho, txtNoutros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        nomeDoClube = findViewById(R.id.nomeDoClube);
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomeDoClube.setText(nomeUsuario);

        txtCadastrados = findViewById(R.id.txtEquipCadastrados);
        txtDisponiveis = findViewById(R.id.txtEquipDisponiveis);
        txtEmUso = findViewById(R.id.txtEquipIndisponiveis);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);

        txtcadeiras = findViewById(R.id.txtcadeiras);
        txtbanho = findViewById(R.id.txtbanho);
        txtoutros = findViewById(R.id.txtoutros);

        txtNbanho = findViewById(R.id.txtNbanho);
        txtNcadeiras = findViewById(R.id.txtNcadeiras);
        txtNoutros = findViewById(R.id.txtNoutros);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(DashboardActivity.this, TelaInicial.class);
                voltar.putExtra("nome_usuario", nomeUsuario);
                startActivity(voltar);
                finish();
            }
        });

        if (CheckNetwork.isInternetAvailable(DashboardActivity.this)) {
            listaEquipamentosCadastrados();
        } else {
            Toast.makeText(this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
        }

        //CADASTRADOS (TODOS)
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DashboardActivity.this, TelaEquipamento.class);
                intent1.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent1);

            }
        });

        //ESTOQUE
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DashboardActivity.this, EstoqueActivity.class);
                intent1.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent1);
            }
        });

        //EM USO
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DashboardActivity.this, EmUso.class);
                intent1.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent1);
            }
        });
    }

    private void listaEquipamentosCadastrados() {

        final ProgressDialog pDialog = ProgressDialog.show(DashboardActivity.this,
                "Aguarde",
                "Carregando dados..",
                true);

        new Thread() {
            public void run() {

                try {
                    String url_read_equip_cadastrados = HOST + "/readequip.php";

                    Ion.with(getBaseContext())
                            .load(url_read_equip_cadastrados)
                            .asJsonArray()
                            .setCallback(new FutureCallback<JsonArray>() {
                                @Override
                                public void onCompleted(Exception e, JsonArray result) {

                                    int iC = 0;
                                    int iD = 0;
                                    int iU = 0;
                                    ///
                                    int rodas = 0;
                                    int banho = 0;
                                    int outros = 0;
                                    String Crodas = "";
                                    String Cbanho = "";
                                    String mOutros = "";
                                    ///
                                    String iCadastrados = "0";
                                    String iDispon = "0";
                                    String iEmUso = "0";
                                    try {
                                        for (int i = 0; i < result.size(); i++) {

                                            JsonObject obj = result.get(i).getAsJsonObject();

                                            Equipamento c = new Equipamento();

                                            c.setId(obj.get("id").getAsInt());
                                            c.setNome_equipamento(obj.get("nome_equip").getAsString());
                                            c.setNome_paciente(obj.get("nome_paciente").getAsString());
                                            c.setNome_clube(obj.get("nome_clube").getAsString());

                                            if (obj.get("nome_clube").getAsString().equals(nomeUsuario)) {

                                                if (obj.get("nome_paciente").getAsString().equals("Disponível")) {
                                                    iD = iD + 1;
                                                } else {
                                                    iU = iU + 1;
                                                }
                                                iC = iC + 1;

                                                if (obj.get("nome_equip").getAsString().contains("Cadeira de rodas")) {
                                                    rodas = rodas + 1;
                                                } else if (obj.get("nome_equip").getAsString().contains("Cadeira de banho")) {
                                                    banho = banho + 1;
                                                } else {
                                                    outros = outros + 1;
                                                }
                                            }

                                            Crodas = String.valueOf(rodas);
                                            Cbanho = String.valueOf(banho);
                                            mOutros = String.valueOf(outros);

                                            iCadastrados = String.valueOf(iC);
                                            iDispon = String.valueOf(iD);
                                            iEmUso = String.valueOf(iU);

                                        }
                                    } catch (Throwable err) {
                                        Toast.makeText(DashboardActivity.this, "mensagem de erro", Toast.LENGTH_SHORT).show();
                                    }

                                    txtCadastrados.setText(iCadastrados);
                                    txtDisponiveis.setText(iDispon);
                                    txtEmUso.setText(iEmUso);

                                    txtcadeiras.setText(Crodas);
                                    txtbanho.setText(Cbanho);
                                    txtoutros.setText(mOutros);

                                    if (rodas > 0){
                                        txtcadeiras.setVisibility(View.VISIBLE);
                                        txtNcadeiras.setVisibility(View.VISIBLE);
                                    }
                                    if (banho > 0){
                                        txtbanho.setVisibility(View.VISIBLE);
                                        txtNbanho.setVisibility(View.VISIBLE);
                                    }
                                    if (outros > 0){
                                        txtoutros.setVisibility(View.VISIBLE);
                                        txtNoutros.setVisibility(View.VISIBLE);
                                    }


                                }
                            });
                    Thread.sleep(1000);
                } catch (Throwable err) {
                    Toast.makeText(DashboardActivity.this, "Não foi possível buscar os equipamentos. Tente novamente", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        Intent voltar = new Intent(DashboardActivity.this, TelaInicial.class);
        voltar.putExtra("nome_usuario", nomeUsuario);
        startActivity(voltar);
        finish();

    }
}
