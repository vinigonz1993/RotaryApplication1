package application.gonzalez.com.rotaryappcadeira;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Conexao {


    public static String postDados(String urlUsuario, String parametrosUsuarios)   {

        //=========http://square.github.io/okhttp/============

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        builder.url(urlUsuario);

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

        RequestBody body = RequestBody.create(mediaType, parametrosUsuarios);

        builder.post(body);

        Request request = builder.build();

        try {
            Response response = client .newCall(request).execute();

            return response.body().string();

        }   catch (IOException erro) {
            return null;
        }
    }
}
