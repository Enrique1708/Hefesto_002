package corp.carrizales.hefesto_002;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import corp.carrizales.hefesto_002.modelo.HistorialNotificacion;
import corp.carrizales.hefesto_002.sqlite.InformacionMantenimiento;
import corp.carrizales.hefesto_002.sqlite.OperacionesBaseDatos;

/**
 * Created by Luis on 22/03/2018.
 */

public class ListAdapterHistNotificacion extends ArrayAdapter<HistorialNotificacion>{
    private final List<HistorialNotificacion> list;
    private final Context context;

    OperacionesBaseDatos datos;


    public ListAdapterHistNotificacion(Context context, List<HistorialNotificacion> list){
        super(context, R.layout.list_fila_hist_notificacion, list);
        this.context = context;
        this.list = list;
    }
    static class ViewHolder {

        protected TextView txbTitulo;
        protected TextView txbKilometraje;
        protected TextView txb_fechaCreacion;
        protected TextView txbDescripcion;
        protected CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = null;
        datos = OperacionesBaseDatos.obtenerInstancia(context);
        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(context);
            view = inflator.inflate(R.layout.list_fila_hist_notificacion, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.txbTitulo = (TextView) view.findViewById(R.id.txbTitulo);
            viewHolder.txbKilometraje = (TextView)view.findViewById(R.id.txb_kilometraje);
            viewHolder.txb_fechaCreacion =(TextView)view.findViewById(R.id.txb_fechaRealizacion);
            viewHolder.txbDescripcion = (TextView) view.findViewById(R.id.txbDescripcion);
            viewHolder.checkBox = (CheckBox)view.findViewById(R.id.checkBox_notificacion);

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    HistorialNotificacion notificacion = (HistorialNotificacion) viewHolder.checkBox.getTag();
                    //notificacion.setOportuno(buttonView.isChecked());


                    /////////////////VERIFICAR SI EL MANTENIMIENTO ES OPORTUNO///////////////////////
                    int kilometrajeActual = ListMotocicleta.obtenerMotocicleta(notificacion.getId_IdMotocicleta()).getKilometraje();
                    int kilometrajeComponete = notificacion.getKilometraje();
                    if(kilometrajeActual<=kilometrajeComponete){
                        notificacion.setFechaRealizacion(getDateTime());
                        notificacion.setOportuno(true);
                    }
                    else {
                        notificacion.setFechaRealizacion(getDateTime());
                        notificacion.setOportuno(false);
                    }
                    /////////////////////////////////////////////////////////////////////////////////
                    datos.getDb().beginTransaction();
                    datos.mantenimientoRealizado(notificacion);//ACTUALIZCION DEL ESTADO DEL DATO
                    datos.getDb().setTransactionSuccessful();
                    datos.getDb().endTransaction();
                }
        });
            view.setTag(viewHolder);
            viewHolder.checkBox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkBox.setTag(list.get(position));
            view.getTag();
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.txbTitulo.setText(list.get(position).getNombre());
        holder.txbKilometraje.setText("Km: "+list.get(position).getKilometraje());
        holder.txb_fechaCreacion.setText(""+list.get(position).getFechaCreacion());
        holder.txbDescripcion.setText(list.get(position).getDescripcion());
        holder.checkBox.setChecked(list.get(position).getOportuno());
        return  view;
    }

    @Override

    public int getCount() {
        return super.getCount();
    }

    @Override

    public HistorialNotificacion getItem(int position) {
        return super.getItem(position);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
