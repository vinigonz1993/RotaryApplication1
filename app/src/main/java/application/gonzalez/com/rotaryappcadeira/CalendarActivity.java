package application.gonzalez.com.rotaryappcadeira;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    String resultado_da_pesquisa;
    EditText searchField;
    TextView restante1, restante2;
    TextView nomeDoClube;
    String nomeUsuario;
    String HOST;
    ImageButton btnVoltar, btnShowHideSearch, btnSearch, btnCancel;
    RelativeLayout RLayout, searchBar;

    CalendarAdapter calendarAdapter;
    List<Paciente> calendarList;
    ListView listView;

    private int itemClicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        searchBar = findViewById(R.id.searchbar);
        btnSearch = findViewById(R.id.searchIcon);
        searchField = findViewById(R.id.searchField);
        btnCancel = findViewById(R.id.cancelIcon);
        RLayout = findViewById(R.id.layoutRRR);
        nomeDoClube = findViewById(R.id.nomeDoClube);
        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        nomeDoClube.setText(nomeUsuario);

        listView = findViewById(R.id.listCalendar);
        calendarList = new ArrayList<>();
        calendarAdapter = new CalendarAdapter(CalendarActivity.this, calendarList);
        listView.setAdapter(calendarAdapter);

        if (CheckNetwork.isInternetAvailable(CalendarActivity.this)) {
            listaPacientes();
            clicaLista();
        } else {
            Toast.makeText(this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
        }

        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(CalendarActivity.this, TelaInicial.class);
                voltar.putExtra("nome_usuario", nomeUsuario);
                startActivity(voltar);
                finish();
            }
        });

        btnShowHideSearch = findViewById(R.id.search_button_show);
        btnShowHideSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.VISIBLE);
                btnShowHideSearch.setVisibility(View.GONE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendarList.clear();
                calendarAdapter.notifyDataSetChanged();

                searchField.setText("");

                calendarList = new ArrayList<>();
                calendarAdapter = new CalendarAdapter(CalendarActivity.this, calendarList);
                listView.setAdapter(calendarAdapter);
                if (CheckNetwork.isInternetAvailable(CalendarActivity.this)) {
                    listaPacientes();
                } else {
                    Toast.makeText(CalendarActivity.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
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

                calendarList.clear();
                calendarAdapter.notifyDataSetChanged();

                calendarList = new ArrayList<>();
                calendarAdapter = new CalendarAdapter(CalendarActivity.this, calendarList);
                listView.setAdapter(calendarAdapter);

                if (CheckNetwork.isInternetAvailable(CalendarActivity.this)) {
                    if (resultado_da_pesquisa.toString().isEmpty()) {
                        listaPacientes();
                    } else {
                        listaPesquisa();
                    }
                } else {
                    Toast.makeText(CalendarActivity.this, "Nenhuma conexão detectada", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void listaPesquisa() {

        final ProgressDialog pDialog = ProgressDialog.show(CalendarActivity.this, "Aguarde", "Buscando dados..", true);

        try {
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
                                            && !obj.get("devolucao_1").getAsString().isEmpty()
                                            && !obj.get("devolucao_2").getAsString().isEmpty()
                                            && obj.get("nome").getAsString().contains(resultado_da_pesquisa)) {

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
                                        calendarList.add(c);
                                    } else if (obj.get("clube").getAsString().equals(nomeUsuario)
                                            && !obj.get("devolucao_2").getAsString().isEmpty()
                                            && obj.get("nome").getAsString().contains(resultado_da_pesquisa)) {

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
                                        calendarList.add(c);
                                    } else if (obj.get("clube").getAsString().equals(nomeUsuario)
                                            && !obj.get("devolucao_1").getAsString().isEmpty()
                                            && obj.get("nome").getAsString().contains(resultado_da_pesquisa)) {
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
                                        calendarList.add(c);

                                        Collections.sort(calendarList, new Comparator<Paciente>() {
                                            @Override
                                            public int compare(Paciente paciente1, Paciente paciente2) {
                                                return paciente1.getNome().compareToIgnoreCase(paciente2.getNome());
                                            }
                                        });
                                    }
                                }

                                Snackbar snackbar = Snackbar
                                        .make(RLayout, totalExpirado + " Equipamentos expirados", Snackbar.LENGTH_INDEFINITE)
                                        .setActionTextColor(Color.YELLOW)
                                        .setAction("Ok", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });

                                snackbar.show();

                                Thread.sleep(1000);
                            } catch (Throwable err) {
                                Toast.makeText(CalendarActivity.this, "Tente novamente", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                }
                            });


                            calendarAdapter.notifyDataSetChanged();
                        }
                    });
        } catch (Throwable err) {
            Toast.makeText(this, "Erro ao listar os pacientes. Tente novamente", Toast.LENGTH_SHORT).show();
        }

    }

    private void listaPacientes() {

        final ProgressDialog pDialog = ProgressDialog.show(CalendarActivity.this, "Aguarde", "Buscando dados..", true);

        try {

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
                                            && !obj.get("devolucao_1").getAsString().isEmpty()
                                            && !obj.get("devolucao_2").getAsString().isEmpty()) {

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
                                        calendarList.add(c);
                                    } else if (obj.get("clube").getAsString().equals(nomeUsuario)
                                            && !obj.get("devolucao_2").getAsString().isEmpty()
                                            && !obj.get("equip_2").getAsString().equals("Nenhum equipamento selecionado")) {

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
                                        calendarList.add(c);
                                    } else if (obj.get("clube").getAsString().equals(nomeUsuario)
                                            && !obj.get("devolucao_1").getAsString().isEmpty()
                                            && !obj.get("equip").getAsString().equals("Nenhum equipamento selecionado")) {
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
                                        calendarList.add(c);

                                        Collections.sort(calendarList, new Comparator<Paciente>() {
                                            @Override
                                            public int compare(Paciente paciente1, Paciente paciente2) {
                                                return paciente1.getNome().compareToIgnoreCase(paciente2.getNome());
                                            }
                                        });
                                    }
                                }

                                Snackbar snackbar = Snackbar
                                        .make(RLayout, totalExpirado + " Equipamentos expirados", Snackbar.LENGTH_INDEFINITE)
                                        .setActionTextColor(Color.YELLOW)
                                        .setAction("Ok", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });

                                snackbar.show();

                                Thread.sleep(1000);
                            } catch (Throwable err) {
                                Toast.makeText(CalendarActivity.this, "Tente novamente", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                }
                            });


                            calendarAdapter.notifyDataSetChanged();
                        }
                    });
        } catch (Throwable err) {
            Toast.makeText(this, "Não foi possível acessar o banco de dados. Tente novamente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void clicaLista() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Paciente c = (Paciente) adapterView.getAdapter().getItem(position);

                String id_paciente = String.valueOf(c.getId());
                String nome = c.getNome().toString();
                String mother = c.getMother().toString();
                String email = c.getEmail().toString();
                String clube = c.getClube().toString();
                String telefone = c.getTelefone().toString();
                String nascimento = c.getNascimento();
                String endereco = c.getEndereco();
                String equip = c.getEquip();
                String equip_2 = c.getEquip_2();
                String equip_id = c.getEquip_id();
                String equip_id_2 = c.getEquip_id_2();
                String timestamp = c.getTimestamp();
                String timestamp_2 = c.getTimestamp_2();
                String devolucao = c.getDevolucao_1();
                String devolucao_2 = c.getDevolucao_2();

                Intent intent = new Intent(CalendarActivity.this, CalendarDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("nome", nome);
                intent.putExtra("mother", mother);
                intent.putExtra("email", email);
                intent.putExtra("clube", clube);
                intent.putExtra("telefone", telefone);
                intent.putExtra("nascimento", nascimento);
                intent.putExtra("endereco", endereco);
                intent.putExtra("id_paciente", id_paciente);
                intent.putExtra("nome_usuario", nomeUsuario);
                intent.putExtra("equip", equip);
                intent.putExtra("equip_2", equip_2);
                intent.putExtra("equip_id", equip_id);
                intent.putExtra("equip_id_2", equip_id_2);
                intent.putExtra("timestamp", timestamp);
                intent.putExtra("timestamp_2", timestamp_2);
                intent.putExtra("devolucao", devolucao);
                intent.putExtra("devolucao_2", devolucao_2);
                startActivity(intent);

                itemClicado = position;

                Toast.makeText(CalendarActivity.this, "Paciente " + c.getNome(), Toast.LENGTH_SHORT).show();


            }
        });
    }


}
