package application.gonzalez.com.rotaryappcadeira;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TelaInicial extends AppCompatActivity implements View.OnClickListener {

    Button btnTxt;
    //TextView txt_info;
    RelativeLayout relativeLayout;
    Context mContext = TelaInicial.this;
    String nomeUsuario;
    TextView nomeDoClube;
    Button btnDashboard, btnPacientes, btnCadeiras, btnCalendario, btnSair, btnHistorico;
    //Button btnEstoque, btnCadeiras;
    String HOST;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        nomeDoClube = findViewById(R.id.nomeDoClube);
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomeDoClube.setText(nomeUsuario);

        btnTxt = findViewById(R.id.btnTxt);
        // txt_info = findViewById(R.id.txt_info);
        btnCadeiras = findViewById(R.id.btnCadeiras);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnPacientes = findViewById(R.id.btnPacientes);
        //btnEstoque = findViewById(R.id.btnEstoque);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnHistorico = findViewById(R.id.btnHistorico);
        btnSair = findViewById(R.id.btnLogout);

        //txt_info.setPaintFlags(txt_info.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnCadeiras.setOnClickListener(this);
        btnCalendario.setOnClickListener(this);
        btnPacientes.setOnClickListener(this);
        //btnEstoque.setOnClickListener(this);
        btnDashboard.setOnClickListener(this);
        btnSair.setOnClickListener(this);
        btnHistorico.setOnClickListener(this);
        btnTxt.setOnClickListener(this);
        //txt_info.setOnClickListener(this);

        if (CheckNetwork.isInternetAvailable(TelaInicial.this)) {
            listaPacientes();
        } else {
            Toast.makeText(mContext, "Nenhuma conexão conectada", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCadeiras:
                Intent intent = new Intent(TelaInicial.this, AdicionaEquipamento.class);
                intent.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent);
                break;

            case R.id.btnPacientes:
                Intent intent2 = new Intent(mContext, MainActivity.class);
                intent2.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent2);

                break;

           /* case R.id.btnEstoque:
                Intent intent3 = new Intent(mContext, EstoqueActivity.class);
                intent3.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent3);

                break;*/

            case R.id.btnDashboard:
                Intent intent4 = new Intent(mContext, DashboardActivity.class);
                intent4.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent4);

                break;

            case R.id.btnCalendario:
                Intent intent5 = new Intent(mContext, CalendarActivity.class);
                intent5.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent5);

                break;

            case R.id.btnTxt:
                Intent intent9 = new Intent(mContext, CalendarActivity.class);
                intent9.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent9);

                break;

            case R.id.btnHistorico:
                Intent intent6 = new Intent(mContext, Historico_devolvido.class);
                intent6.putExtra("nome_usuario", nomeUsuario);
                startActivity(intent6);

                break;

            case R.id.btnLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(TelaInicial.this);
                builder.setTitle("Terminar sessão");
                builder.setMessage("Deseja realmente sair?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent6 = new Intent(mContext, LoginScreen.class);
                        startActivity(intent6);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alerta = builder.create();
                alerta.show();

                break;

        }
    }

    private void listaPacientes() {
        // final ProgressDialog pDialog = ProgressDialog.show(TelaInicial.this, "Aguarde", "Buscando dados..", true);

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

                                    int totalExpirado = 0;

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

                                        Calendar calendar = Calendar.getInstance();
                                        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
                                        DateFormat df = new SimpleDateFormat("dd/MM/yy");
                                        String atual = currentDate;
                                        Date startDate = df.parse(atual);
                                        Calendar c1 = Calendar.getInstance();
                                        c1.setTime(startDate);
                                        long ms1 = c1.getTimeInMillis();

                                        if (obj.get("clube").getAsString().equals(nomeUsuario)
                                                && !obj.get("timestamp").getAsString().isEmpty()
                                                && !obj.get("timestamp_2").getAsString().isEmpty()) {

                                            String fimDate = c.getDevolucao_1().toString();
                                            long diff = 0;

                                            Date endDate = df.parse(fimDate);
                                            Calendar c2 = Calendar.getInstance();
                                            c2.setTime(endDate);

                                            long ms2 = c2.getTimeInMillis();

                                            diff = ms2 - ms1;
                                            int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));

                                            if (diffInDays <= 0) {
                                                totalExpirado = totalExpirado + 1;
                                            }

                                            String fimDate2 = c.getDevolucao_2().toString();
                                            long diff2 = 0;

                                            Date endDate2 = df.parse(fimDate2);
                                            Calendar c3 = Calendar.getInstance();
                                            c3.setTime(endDate2);

                                            long ms3 = c3.getTimeInMillis();

                                            diff2 = ms3 - ms1;
                                            int diffInDays2 = (int) (diff2 / (24 * 60 * 60 * 1000));

                                            if (diffInDays2 <= 0) {
                                                totalExpirado = totalExpirado + 1;
                                            }

                                        } else if (obj.get("clube").getAsString().equals(nomeUsuario)
                                                && !obj.get("timestamp_2").getAsString().isEmpty()) {

                                            String fimDate2 = c.getDevolucao_2().toString();
                                            long diff2 = 0;

                                            Date endDate2 = df.parse(fimDate2);
                                            Calendar c3 = Calendar.getInstance();
                                            c3.setTime(endDate2);

                                            long ms3 = c3.getTimeInMillis();

                                            diff2 = ms3 - ms1;
                                            int diffInDays2 = (int) (diff2 / (24 * 60 * 60 * 1000));
                                            //Toast.makeText(CalendarActivity.this, diffInDays2, Toast.LENGTH_SHORT).show();

                                            if (diffInDays2 <= 0) {
                                                totalExpirado = totalExpirado + 1;
                                            }

                                        } else if (obj.get("clube").getAsString().equals(nomeUsuario)
                                                && !obj.get("timestamp").getAsString().isEmpty()) {
                                            String fimDate = c.getDevolucao_1().toString();
                                            long diff = 0;

                                            Date endDate = df.parse(fimDate);
                                            Calendar c2 = Calendar.getInstance();
                                            c2.setTime(endDate);

                                            long ms2 = c2.getTimeInMillis();

                                            diff = ms2 - ms1;
                                            int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));

                                            if (diffInDays <= 0) {
                                                totalExpirado = totalExpirado + 1;
                                            }

                                        }
                                    }

                                    if (totalExpirado == 1) {
                                        btnTxt.setText(String.valueOf(totalExpirado + " expirado"));
                                    }
                                    if (totalExpirado > 1) {
                                        btnTxt.setText(String.valueOf(totalExpirado + " expirados"));
                                    }

                                    btnTxt.setText(String.valueOf(totalExpirado + " expirados"));
                                    if (totalExpirado == 0) {
                                        btnTxt.setVisibility(View.GONE);
                                    } else {
                                        btnTxt.setVisibility(View.VISIBLE);
                                    }

                                } catch (Throwable err) {
                                    Toast.makeText(TelaInicial.this, "Tente novamente", Toast.LENGTH_SHORT).show();

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //pDialog.dismiss();
                                    }
                                });

                            }
                        });
            }
        }.

                start();
    }
}
