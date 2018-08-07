package application.gonzalez.com.rotaryappcadeira;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class HistoricoDetails extends AppCompatActivity {

    ImageButton btnVoltar;
    Button btnExcluirDados;
    TextView nomeDoClube, txtID, txtInfoNome, txtEquipamento, txtInfoAdd, txtInfoDev, txtTelefone, txtEmail, txtEndereco;
    String nome, id, mother, data_nascimento, endereco, clube, data_added, data_devolucao, equip, equip_id, telefone, email;
    String id_final;
    private AlertDialog alerta;
    String HOST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_details);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        nome = getIntent().getExtras().getString("nome");
        id = getIntent().getExtras().getString("id_paciente");
        mother = getIntent().getExtras().getString("mother");
        data_nascimento = getIntent().getExtras().getString("data_nascimento");
        endereco = getIntent().getExtras().getString("endereco");
        clube = getIntent().getExtras().getString("clube");
        data_added = getIntent().getExtras().getString("data_added");
        data_devolucao = getIntent().getExtras().getString("data_devolucao");
        equip = getIntent().getExtras().getString("equip");
        telefone = getIntent().getExtras().getString("telefone");
        equip_id = getIntent().getExtras().getString("equip_id");
        email = getIntent().getExtras().getString("email");
        id_final = id.toString();

        nomeDoClube = findViewById(R.id.nomeDoClube);
        txtInfoNome = findViewById(R.id.txtInfoNome);
        txtEquipamento = findViewById(R.id.txtInfoEquipamento1);
        txtInfoAdd = findViewById(R.id.txtInfoAdicionado1);
        txtInfoDev = findViewById(R.id.txtInfoDevolucao1);
        txtTelefone = findViewById(R.id.txtInfoTelefone);
        txtEmail = findViewById(R.id.txtInfoEmail);
        txtID = findViewById(R.id.txtInfoID);
        txtEndereco = findViewById(R.id.txtInfoEndereco);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnExcluirDados = findViewById(R.id.btnExcluirDados);

        nomeDoClube.setText(clube);
        txtInfoNome.setText(nome);
        txtEquipamento.setText(equip);
        txtInfoAdd.setText(data_added);
        txtInfoDev.setText(data_devolucao);
        txtTelefone.setText(telefone);
        txtEmail.setText(email);
        txtID.setText(id_final);
        txtEndereco.setText(endereco);

        checaDados();

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(HistoricoDetails.this, Historico_devolvido.class);
                voltar.putExtra("nome_usuario", clube);
                startActivity(voltar);
                finish();
            }
        });

        btnExcluirDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(HistoricoDetails.this)) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(HistoricoDetails.this);
                    builder.setTitle("Excluír o paciente " + nome);
                    builder.setMessage("Deseja excluír permanentemente o histórico do paciente " + nome);
                    builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new Thread() {
                                public void run() {

                                    final String id_paciente = txtID.getText().toString();
                                    String url = HOST + "/delete_devolvidos.php";

                                    Ion.with(HistoricoDetails.this)
                                            .load(url)
                                            .setBodyParameter("id", id_paciente)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {

                                                    try {
                                                        if (result.get("DELETE").getAsString().equals("OK")) {

                                                            Intent voltar = new Intent(HistoricoDetails.this, Historico_devolvido.class);
                                                            voltar.putExtra("nome_usuario", clube);
                                                            startActivity(voltar);
                                                            finish();

                                                        } else {

                                                            Toast.makeText(HistoricoDetails.this, "Ocorre um erro ao excluir", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Throwable err) {
                                                        Toast.makeText(HistoricoDetails.this, "Um erro ocorreu", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }.start();
                        }
                    });

                    builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alerta = builder.create();
                    alerta.show();
                } else {
                    Toast.makeText(HistoricoDetails.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checaDados() {

        if (telefone.toString().equals("")) {
            txtTelefone.setText("Sem dados");
        }

        if (email.toString().equals("")) {
            txtEmail.setText("Sem dados");
        }

        if (endereco.toString().equals("")) {
            txtEndereco.setText("Sem dados");
        }
    }
}
