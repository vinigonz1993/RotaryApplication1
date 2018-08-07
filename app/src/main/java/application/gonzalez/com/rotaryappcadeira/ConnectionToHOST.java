package application.gonzalez.com.rotaryappcadeira;


import java.text.DateFormat;
import java.util.Calendar;

public class ConnectionToHOST {

    String HOST_URL = "http://rotarymobi.com.br";

    Calendar calendar1 = Calendar.getInstance();
    String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar1.getTime());
    final String timestampW = currentDate;
    String devolucao1W = currentDate;

}
