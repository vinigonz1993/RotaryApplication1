package application.gonzalez.com.rotaryappcadeira;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PacienteAdapter extends BaseAdapter {

    private Context ctx;
    private List<Paciente> lista;

    public PacienteAdapter(Context ctx2, List<Paciente> lista2)   {

        ctx = ctx2;
        lista = lista2;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Paciente getItem(int position) {
        return lista.get(position);
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
            v = inflater.inflate(R.layout.item_lista, null);

        }   else    {

            v = convertView;
        }

        final Paciente c = getItem(position);

        TextView itemNome = v.findViewById(R.id.itemNome);
        TextView itemTelefone = v.findViewById(R.id.itemTelefone);
        TextView itemEmail = v.findViewById(R.id.itemEmail);
        TextView itemTimestamp = v.findViewById(R.id.itemTimestamp);
        TextView itemTimestamp_2 = v.findViewById(R.id.itemTimestamp_2);
        final TextView itemEquip = v.findViewById(R.id.itemEquipamento);
        final TextView itemEquip_2 = v.findViewById(R.id.itemEquipamento_2);

        itemNome.setText(c.getNome());
        itemTelefone.setText("Tel.: " + c.getTelefone());
        itemEmail.setText(c.getEmail());

        if (c.getEquip().toString().isEmpty())  {

        }   else    {

            if  (c.getEquip().toString().equals("Nenhum equipamento selecionado"))  {

                itemEquip.setText(c.getEquip());
                itemEquip.setTextColor(android.graphics.Color.parseColor("#8c8c8c"));

            }   else    {
                itemEquip.setText(c.getEquip());
                itemEquip.setTextColor(android.graphics.Color.parseColor("#00035c"));
            }
        }

        if (c.getEquip_2().toString().isEmpty())  {

        }   else    {

            if  (c.getEquip_2().toString().equals("Nenhum equipamento selecionado"))  {

                itemEquip_2.setText(c.getEquip_2());
                itemEquip_2.setTextColor(android.graphics.Color.parseColor("#8c8c8c"));

            }   else    {
                itemEquip_2.setText(c.getEquip_2());
                itemEquip_2.setTextColor(android.graphics.Color.parseColor("#00035c"));
            }
        }

        if (c.getTimestamp().toString().isEmpty())  {
            itemTimestamp.setText("");
        }   else {
            itemTimestamp.setText("Empréstimo em: " + c.getTimestamp());
        }

        if (c.getTimestamp_2().toString().isEmpty())  {
            itemTimestamp_2.setText("");
        }   else {
            itemTimestamp_2.setText("Empréstimo em: " + c.getTimestamp_2());
        }
        
        return v;
    }

}
