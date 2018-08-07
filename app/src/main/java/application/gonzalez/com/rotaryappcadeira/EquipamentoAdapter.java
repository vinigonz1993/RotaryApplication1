package application.gonzalez.com.rotaryappcadeira;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class EquipamentoAdapter extends BaseAdapter {

    private Context ctx;
    private List<Equipamento> listaEquipamento;

    public EquipamentoAdapter(Context ctx2, List<Equipamento> listaEquipamento2)   {

        ctx = ctx2;
        listaEquipamento = listaEquipamento2;
    }

    @Override
    public int getCount() {
        return listaEquipamento.size();
    }

    @Override
    public Equipamento getItem(int position) {
        return listaEquipamento.get(position);
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
            v = inflater.inflate(R.layout.item_lista_equip, null);

        }   else    {

            v = convertView;
        }


        Equipamento e = getItem(position);

        TextView itemNomeEquip = v.findViewById(R.id.itemNomeEquipamento);
        TextView itemPaciente = v.findViewById(R.id.itemNomePaciente);


        itemNomeEquip.setText(e.getNome_equipamento());
        itemPaciente.setText(e.getNome_paciente());

        if (itemPaciente.getText().toString().equals("Disponível"))  {
            itemPaciente.setTextColor(Color.parseColor("#06800e"));
        }   else    {
            itemPaciente.setTextColor(Color.parseColor("#8c8c8c"));
        }


        return v;
    }

}
