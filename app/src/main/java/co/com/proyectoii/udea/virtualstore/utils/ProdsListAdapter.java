package co.com.proyectoii.udea.virtualstore.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.com.proyectoii.udea.virtualstore.R;
import co.com.proyectoii.udea.virtualstore.dto.Producto;

/**
 * Created by Alan on 12/6/2015.
 */
public class ProdsListAdapter extends ArrayAdapter<Producto> {

    private ArrayList<Producto> productos;
    private int layoutResourceId;
    private Context context;

    public ProdsListAdapter(Context context, int layoutResourceId, ArrayList<Producto> productos) {
            super(context, layoutResourceId, productos);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.productos = productos;
            }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {
            View row = convertView;
            ProductoHolder holder = null;

            if(row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
            }

            holder = new ProductoHolder();
            holder.producto = productos.get(posicion);

            holder.imagen = (ImageView) row.findViewById(R.id.productoIV);
            holder.nombre = (TextView)row.findViewById(R.id.nombreProdTV);

            row.setTag(holder);

            setupItem(holder);
            return row;
            }

    private void setupItem(ProductoHolder holder) {
        // Se carga la imagen dinamicamente, se muestra ic_placeholder miestras carga, o ic_error_fallback si hay problemas
        Picasso.with(context).load(new ProductosHTTPClient().getURI(holder.producto.getIconoPorDefecto()))
                .placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error_fallback)
                .resize(50,50).into(holder.imagen);
        //holder.imagen.setImageBitmap(((new ProductosHTTPClient()).getImage(holder.producto.getIconoPorDefecto())));

        holder.nombre.setText(holder.producto.getNombre());
    }

    public static class ProductoHolder {
        Producto producto;
        ImageView imagen;
        TextView nombre;
    }
}
