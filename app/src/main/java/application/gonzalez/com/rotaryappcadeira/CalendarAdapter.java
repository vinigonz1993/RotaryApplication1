package application.gonzalez.com.rotaryappcadeira;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {

    private Context ctx;
    private List<Paciente> calendarList;

    public CalendarAdapter(Context ctx2, List<Paciente> calendarList2)   {

        ctx = ctx2;
        calendarList = calendarList2;
    }
    @Override
    public int getCount() {
        return calendarList.size();
    }

    @Override
    public Paciente getItem(int position) {
        return calendarList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            v = inflater.inflate(R.layout.lista_calendario, null);

        }   else    {

            v = convertView;
        }

        Paciente c = getItem(position);

        TextView calNome = v.findViewById(R.id.calendarioNomePaciente);
        TextView calAdded = v.findViewById(R.id.calendarioDataAdded);
        TextView calAdded2 = v.findViewById(R.id.calendarioDataAdded2);
        //TextView calDev = v.findViewById(R.id.calendarioDataEnded);
        //TextView calDev2 = v.findViewById(R.id.calendarioDataEnded2);
        TextView restante1 = v.findViewById(R.id.tempoRestante_1);
        TextView restante2 = v.findViewById(R.id.tempoRestante_2);

        ////
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        //Toast.makeText(context, currentDate, Toast.LENGTH_SHORT).show();
        final String timestamp = currentDate;

        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String atual = currentDate;
        String fimDate = c.getDevolucao_1().toString();
        String fimDate2 = c.getDevolucao_2().toString();
        long diff = 0;
        long diff2 = 0;

        try {
            Date startDate = df.parse(atual);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);

            Date endDate = df.parse(fimDate);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);

            long ms1 = c1.getTimeInMillis();
            long ms2 = c2.getTimeInMillis();

            diff = ms2 - ms1;
            int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));


            if (diffInDays > 0) {
                restante1.setText(String.valueOf(diffInDays + " dias"));
                restante1.setTextColor(Color.GREEN);
            }   else    {
                restante1.setText(String.valueOf("expirado"));
                restante1.setTextColor(Color.YELLOW);
            }
        }   catch (ParseException e)    {
            e.printStackTrace();
        }

        try {
            Date startDate = df.parse(atual);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);

            Date endDate2 = df.parse(fimDate2);
            Calendar c3 = Calendar.getInstance();
            c3.setTime(endDate2);

            long ms1 = c1.getTimeInMillis();
            long ms3 = c3.getTimeInMillis();

            diff2 = ms3 - ms1;
            int diffInDays2 = (int) (diff2 / (24 * 60 * 60 * 1000));


            if (diffInDays2 > 0) {
                restante2.setText(String.valueOf(diffInDays2 + " dias"));
                restante2.setTextColor(Color.GREEN);
            }   else    {
                restante2.setText(String.valueOf("expirado"));
                restante2.setTextColor(Color.YELLOW);
            }
        }   catch (ParseException e)    {
            e.printStackTrace();
        }
        ////

        calNome.setText(c.getNome());
        calAdded.setText(c.getEquip());
        calAdded2.setText(c.getEquip_2());
        //calDev.setText(c.getDevolucao_1());
        //calDev2.setText(c.getDevolucao_2());


        if (calAdded.getText().toString().equals("Nenhum equipamento selecionado"))    {
            calAdded.setVisibility(View.GONE);
            //calDev.setVisibility(View.GONE);
            restante1.setVisibility(View.GONE);
        }   else    {
            calAdded.setVisibility(View.VISIBLE);
            //calDev.setVisibility(View.VISIBLE);
            restante1.setVisibility(View.VISIBLE);
        }

        if (calAdded2.getText().toString().equals("Nenhum equipamento selecionado"))    {
            calAdded2.setVisibility(View.GONE);
            // calDev2.setVisibility(View.GONE);
            restante2.setVisibility(View.GONE);
        }   else    {
            calAdded2.setVisibility(View.VISIBLE);
            // calDev2.setVisibility(View.VISIBLE);
            restante2.setVisibility(View.VISIBLE);
        }

        if (c.getDevolucao_1().toString().isEmpty()
                && c.getDevolucao_2().toString().isEmpty())    {

            calNome.setVisibility(View.GONE);

        }   else    {

            calNome.setVisibility(View.VISIBLE);
        }



        return v;
    }
}
