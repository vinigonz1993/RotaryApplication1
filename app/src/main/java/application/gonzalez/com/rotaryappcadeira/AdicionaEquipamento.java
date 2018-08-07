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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class AdicionaEquipamento extends AppCompatActivity {

    TextView select1, select2;
    Button btnAdicionaEquipamento;
    ImageButton btnVoltar;
    EditText nomeEquipamento, IDdoEquipamento;
    String nomeUsuario;
    TextView edtIDdoEquipamento, nomeDoClube;
    String HOST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_equipamento);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

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

        nomeEquipamento = findViewById(R.id.edtNomeDoEquipamento);
        edtIDdoEquipamento = findViewById(R.id.edtIDdoEquipamento);

        select1 = findViewById(R.id.txtSelect1);
        select2 = findViewById(R.id.txtSelect2);

        select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equip_nome = select1.getText().toString();
                nomeEquipamento.setText(equip_nome);
            }
        });

        select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equip_nome = select2.getText().toString();
                nomeEquipamento.setText(equip_nome);
            }
        });

        btnAdicionaEquipamento = findViewById(R.id.btnSalvarCadeira);
        btnAdicionaEquipamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String idDoEquipamento = edtIDdoEquipamento.getText().toString();
                final String nomeEquip = nomeEquipamento.getText().toString();
                final String nome_clube = nomeDoClube.getText().toString();
                final String nome_paciente = "Disponível";

                if (nomeEquip.isEmpty())    {
                    nomeEquipamento.setError("o Campo 'NOME' é obrigatório");

                }   else if (idDoEquipamento.isEmpty())  {

                    String url = HOST + "/createequip.php";

                    Ion.with(AdicionaEquipamento.this)
                            .load(url)
                            .setBodyParameter("nome_equip", nomeEquip)
                            .setBodyParameter("nome_clube", nome_clube)
                            .setBodyParameter("nome_paciente", nome_paciente)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result.get("CREATE_EQUIP").getAsString().equals("OK")) {

                                        int idRetornada = Integer.parseInt(result.get("ID").getAsString());

                                        Equipamento c = new Equipamento();

                                        c.setId(Integer.parseInt(String.valueOf(idRetornada)));
                                        c.setNome_equipamento(nomeEquip);
                                        c.setNome_clube(nome_clube);
                                        c.setNome_paciente(nome_paciente);

                                        Toast.makeText(AdicionaEquipamento.this, "Equipamento registrado com sucesso!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(AdicionaEquipamento.this, TelaInicial.class);
                                        intent.putExtra("nome_usuario", nomeUsuario);
                                        startActivity(intent);
                                        finish();

                                    } else {

                                        Toast.makeText(AdicionaEquipamento.this, "Ocorreu um erro ao salvar", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });

                }   else    {

                }
            }
        });
    }
}
