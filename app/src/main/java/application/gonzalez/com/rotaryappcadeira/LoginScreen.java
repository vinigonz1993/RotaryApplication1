package application.gonzalez.com.rotaryappcadeira;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    RelativeLayout relProgress;
    private Context mContext = LoginScreen.this;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnLogar, btnRegistrar;
    private ImageView imageView;
    String url = "";
    String parametros = "";
    private SharedPreferences myPrefs, senhaPreff;
    private static final String PREFS_NAME = "";
    String HOST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        ConnectionToHOST host = new ConnectionToHOST();
        HOST = host.HOST_URL.toString();

        relProgress = findViewById(R.id.relprogress);
        imageView = findViewById(R.id.imageView);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogar = findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    relProgress.setVisibility(View.VISIBLE);


                    String email = edtEmail.getText().toString();
                    String senha = edtSenha.getText().toString();

                    if (email.isEmpty() || senha.isEmpty()) {
                        relProgress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Todos os campos devem ser preenchidos", Toast.LENGTH_LONG).show();
                    } else {


                        url = HOST + "/logar.php";

                        parametros = "email=" + email + "&senha=" + senha;

                        myPrefs = getSharedPreferences(PREFS_NAME, 0);
                        senhaPreff = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        SharedPreferences.Editor editorSenha = senhaPreff.edit();

                        editor.putString("user", edtEmail.getText().toString());
                        editor.commit();

                        editorSenha.putString("senha", edtSenha.getText().toString());
                        editorSenha.commit();

                        new SolicitaDados().execute(url);
                    }


                } else {
                    relProgress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences prefssenha = getSharedPreferences(PREFS_NAME, 0);

        if (prefs.contains("user")) {
            String user = prefs.getString("user", "");
            edtEmail.setText(user);
        }

        if (prefssenha.contains("senha")) {
            String senha = prefssenha.getString("senha", "");
            edtSenha.setText(senha);
        }
    }

    class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return Conexao.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(final String resultado) {

            try {
                if (resultado.contains("login_ok")) {

                    String[] dados = resultado.split(",");

                    //edtEmail.setText(dados[0] + " - " + dados[1] + " + " + dados[2]);

                    Intent abreTelaInicial = new Intent(mContext, TelaInicial.class);
                    abreTelaInicial.putExtra("id_usuario", dados[1]);
                    abreTelaInicial.putExtra("nome_usuario", dados[2]);
                    startActivity(abreTelaInicial);
                    finish();
                } else {
                    relProgress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Usuário e/ou senha incorretos!", Toast.LENGTH_LONG).show();
                }
            } catch (Throwable err) {
                relProgress.setVisibility(View.GONE);
                Toast.makeText(mContext, "Não foi possível entrar, verifique as suas credenciais", Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
