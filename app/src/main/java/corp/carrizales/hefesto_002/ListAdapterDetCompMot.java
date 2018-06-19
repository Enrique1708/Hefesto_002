package corp.carrizales.hefesto_002;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import corp.carrizales.hefesto_002.modelo.DetalleComponenteMotocicleta;
import corp.carrizales.hefesto_002.modelo.HistorialNotificacion;

/**
 * Created by Luis on 22/02/2018.
 */

public class ListAdapterDetCompMot extends ArrayAdapter<HistorialNotificacion>{

    private final List<HistorialNotificacion> list;
    private final Context context;

    public ListAdapterDetCompMot(Context context, List<HistorialNotificacion> list){
        super(context, R.layout.list_fila_det_comp_mot, list);
        this.context = context;
        this.list = list;
    }
    static class ViewHolder {
        //protected ImageView imageView_DetCompMot;
        protected TextView txbNombre;
        protected TextView txbKilometraje;
        protected TextView txbFechaCreacion;
        protected TextView txbFechaRealizacion;
        protected TextView txbDescripcion;
        //protected CheckBox checkBox;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(context);
            view = inflator.inflate(R.layout.list_fila_det_comp_mot, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.txbNombre = (TextView) view.findViewById(R.id.txbTitulo);
            viewHolder.txbKilometraje = (TextView) view.findViewById(R.id.txbKilometraje);
            viewHolder.txbFechaCreacion = (TextView) view.findViewById(R.id.txbFechaCreacion);
            viewHolder.txbFechaRealizacion = (TextView) view.findViewById(R.id.txbFechaRealizacion);
            viewHolder.txbDescripcion = (TextView) view.findViewById(R.id.txbDescripcion);
            //viewHolder.imageView_DetCompMot = (ImageView) view.findViewById(R.id.ImagView_DetCompMot);

            //viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            //  public void onCheckedChange(CompoundButton buttonView, boolean isChecked){
            //Objeto objeto = (Objeto) viewHolder.checkbox.getTag();
            //element.setSelected(buttonView, isChecked());
            //}
            //});
            view.setTag(viewHolder);
            //viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            //((ViewHolder) view.getTag().checkbox.setTag(list.get(position)));
            view.getTag();
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.txbNombre.setText(list.get(position).getNombre());
        holder.txbKilometraje.setText(""+list.get(position).getKilometraje());
        holder.txbFechaCreacion.setText(""+list.get(position).getFechaCreacion());
        holder.txbFechaRealizacion.setText(""+list.get(position).getFechaRealizacion());
        holder.txbDescripcion.setText(list.get(position).getDescripcion());
        //holder.imageView_DetCompMot.setImageResource(list.get(position).getId_imagen());

        //holder.checkbox.setChecked(list.get(position).isSelected());
        return  view;
    }
}
