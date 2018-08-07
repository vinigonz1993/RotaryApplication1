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

public class AdicionaPaciente extends AppCompatActivity {

    TextView nomeDoClube, edtID;
    String nomeUsuario;
    ImageButton btnVoltar;
    Button btnSalvarPaciente;
    EditText edtNome, edtNomeDaMae, edtDataDeNascimento, edtEmail, edtTelefone, edtEndereco;

    private String HOST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_paciente);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        //Edit Texts
        edtNome = findViewById(R.id.edtNome);
        edtNomeDaMae = findViewById(R.id.edtNomeDaMae);
        edtDataDeNascimento = findViewById(R.id.edtDataDeNascimento);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtEndereco = findViewById(R.id.edtEndereco);
        edtID = findViewById(R.id.edtID);
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

        btnSalvarPaciente = findViewById(R.id.btnSalvarPaciente);
        btnSalvarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(AdicionaPaciente.this)) {
                    final String id = edtID.getText().toString();
                    final String nome = edtNome.getText().toString();
                    final String mother = edtNomeDaMae.getText().toString();
                    final String telefone = edtTelefone.getText().toString();
                    final String email = edtEmail.getText().toString();
                    final String endereco = edtEndereco.getText().toString();
                    final String data_nascimento = edtDataDeNascimento.getText().toString();
                    final String clube = nomeDoClube.getText().toString();
                    final String equip = "Nenhum equipamento selecionado";
                    final String equip_id = "";
                    final String timestamp = "";
                    final String equip_2 = "Nenhum equipamento selecionado";
                    final String equip_id_2 = "";
                    final String timestamp_2 = "";
                    final String devolucao_1 = "";
                    final String devolucao_2 = "";


                    if (nome.isEmpty()) {


                        Toast.makeText(AdicionaPaciente.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                        // edtNome.setError("o Campo 'NOME' é obrigatório");

                    } else if (id.isEmpty()) {

                        Toast.makeText(AdicionaPaciente.this, "Salvando paciente", Toast.LENGTH_SHORT).show();

                        String url = HOST + "/create.php";

                        Ion.with(AdicionaPaciente.this)
                                .load(url)
                                .setBodyParameter("nome", nome)
                                .setBodyParameter("data_nascimento", data_nascimento)
                                .setBodyParameter("mother", mother)
                                .setBodyParameter("telefone", telefone)
                                .setBodyParameter("email", email)
                                .setBodyParameter("endereco", endereco)
                                .setBodyParameter("clube", clube)
                                .setBodyParameter("equip", equip)
                                .setBodyParameter("equip_2", equip_2)
                                .setBodyParameter("equip_id", equip_id)
                                .setBodyParameter("equip_id_2", equip_id_2)
                                .setBodyParameter("timestamp", timestamp)
                                .setBodyParameter("timestamp_2", timestamp_2)
                                .setBodyParameter("devolucao_1", devolucao_1)
                                .setBodyParameter("devolucao_2", devolucao_2)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        //RETORNO DO PHP

                                        try {
                                            if (result.get("CREATE").getAsString().equals("OK")) {

                                                int idRetornada = Integer.parseInt(result.get("ID").getAsString());

                                                Paciente c = new Paciente();

                                                c.setId(Integer.parseInt(String.valueOf(idRetornada)));
                                                c.setNome(nome);
                                                c.setMother(mother);
                                                c.setNascimento(data_nascimento);
                                                c.setEmail(email);
                                                c.setTelefone(telefone);
                                                c.setEndereco(endereco);
                                                c.setEquip(equip);
                                                c.setEquip(equip_2);
                                                c.setEquip_id(equip_id);
                                                c.setEquip_id(equip_id_2);
                                                c.setTimestamp(timestamp);
                                                c.setTimestamp(timestamp_2);
                                                c.setDevolucao_1(devolucao_1);
                                                c.setDevolucao_2(devolucao_2);

                                                Toast.makeText(AdicionaPaciente.this, "Paciente registrado com sucesso!", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(AdicionaPaciente.this, MainActivity.class);
                                                intent.putExtra("nome_usuario", nomeUsuario);
                                                startActivity(intent);
                                                finish();

                                            } else {

                                                Toast.makeText(AdicionaPaciente.this, "Ocorre um erro ao salvar", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Throwable err) {
                                            Toast.makeText(AdicionaPaciente.this, "Não foi possível adicionar o paciente. Tente novamente.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {

                    }
                } else {
                    Toast.makeText(AdicionaPaciente.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
