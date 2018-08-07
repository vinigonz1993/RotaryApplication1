package application.gonzalez.com.rotaryappcadeira;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class EditaEquipamento extends AppCompatActivity {

    TextView nomeDoClube, IDdoEquip;
    EditText nome_equip;
    private String HOST;
    String IdEquipamento, nomeClube, nomeEquipamento;
    Button btnSalvarAlteracoes, btnExcluir;
    String equip_id;
    String equip_id_2;
    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_equipamento);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        nomeDoClube = findViewById(R.id.nomeDoClube);
        IDdoEquip = findViewById(R.id.edtIDdoEquipamento);
        nome_equip = findViewById(R.id.edtNomeDoEquipamento);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnSalvarAlteracoes = findViewById(R.id.btnSalvarAteracao);
        btnExcluir = findViewById(R.id.btnExcluir);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String IdEquipamento = getIntent().getExtras().getString("id_equip");
        nomeClube = getIntent().getExtras().getString("nome_usuario");
        nomeEquipamento = getIntent().getExtras().getString("nome");
        nome_equip.setText(nomeEquipamento);
        nomeDoClube.setText(nomeClube);
        IDdoEquip.setText(IdEquipamento);

        btnSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(EditaEquipamento.this)) {
                    final String idDoEquipamento = IDdoEquip.getText().toString();
                    final String nomeEquip = nome_equip.getText().toString();
                    final String clube = nomeDoClube.getText().toString();

                    if (nomeEquip.isEmpty()) {
                        nome_equip.setError("o Campo 'NOME' é obrigatório");

                    } else if (!idDoEquipamento.isEmpty()) {

                        String url1 = HOST + "/up_equip_1.php";

                        try {
                            Ion.with(EditaEquipamento.this)
                                    .load(url1)
                                    .setBodyParameter("equip_id", idDoEquipamento)
                                    .setBodyParameter("equip", nomeEquip)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {

                                            Paciente c = new Paciente();

                                            c.setEquip_id(idDoEquipamento);
                                            c.setEquip(nomeEquip);

                                        }
                                    });

                            String url = HOST + "/updateequip.php";

                            Ion.with(EditaEquipamento.this)
                                    .load(url)
                                    .setBodyParameter("id", idDoEquipamento)
                                    .setBodyParameter("nome_equip", nomeEquip)
                                    .setBodyParameter("nome_clube", clube)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            //RETORNO DO PHP

                                            if (result.get("UPDATE").getAsString().equals("OK")) {

                                                Equipamento c = new Equipamento();

                                                c.setId(Integer.parseInt(idDoEquipamento));
                                                c.setNome_clube(clube);
                                                c.setNome_equipamento(nomeEquip);
                                                Toast.makeText(EditaEquipamento.this, "Equipamento atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                                voltaAoInicio();

                                            } else {

                                                Toast.makeText(EditaEquipamento.this, "Ocorre um erro ao atualizar", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } catch (Throwable err) {
                            Toast.makeText(EditaEquipamento.this, "Não foi possível salvar. Tente novamente", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(EditaEquipamento.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(EditaEquipamento.this)) {

                    final String id_equipamento = IDdoEquip.getText().toString();

                    if (id_equipamento.isEmpty()) {
                        Toast.makeText(EditaEquipamento.this, "Nenhum contato selecionado", Toast.LENGTH_LONG).show();

                    } else {
                        //apagar o paciente
                        String url = HOST + "/deleteequip.php";

                        Ion.with(EditaEquipamento.this)
                                .load(url)
                                .setBodyParameter("id", id_equipamento)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        //RETORNO DO PHP

                                        try {
                                            if (result.get("DELETE").getAsString().equals("OK")) {

                                                voltaAoInicio();
                                                Toast.makeText(EditaEquipamento.this, "Excluído com sucesso", Toast.LENGTH_SHORT).show();

                                            } else {

                                                Toast.makeText(EditaEquipamento.this, "Ocorre um erro ao excluir", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Throwable err) {
                                            Toast.makeText(EditaEquipamento.this, "Erro. Tente novamente", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(EditaEquipamento.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void voltaAoInicio() {
        Intent voltar = new Intent(EditaEquipamento.this, TelaEquipamento.class);
        voltar.putExtra("nome_usuario", nomeClube);
        startActivity(voltar);
        finish();
    }
}
