package application.gonzalez.com.rotaryappcadeira;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoricoAdapter extends BaseAdapter {

    private Context ctx;
    private List<Devolvido> lista;

    public HistoricoAdapter(Context ctx2, List<Devolvido> lista2)   {

        ctx = ctx2;
        lista = lista2;
    }

    public int getCount() {
        return lista.size();
    }

    @Override
    public Devolvido getItem(int position) {
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
            v = inflater.inflate(R.layout.item_lista_historico, null);

        }   else    {

            v = convertView;
        }

        Devolvido d = getItem(position);

        TextView itemNome = v.findViewById(R.id.itemNome);
        TextView itemEquip = v.findViewById(R.id.itemEquipamento);
        TextView itemTimestamp = v.findViewById(R.id.itemTimestamp);
        TextView itemDevolucao = v.findViewById(R.id.itemDevolucao);


        itemNome.setText(d.getNome());
        itemEquip.setText(d.getEquip());
        itemTimestamp.setText("Data de empréstimo em: " + d.getData_added());
        itemDevolucao.setText("Data de devolução: " + d.getData_devolucao());

        return v;
    }
}
