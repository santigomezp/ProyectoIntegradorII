package co.com.proyectoii.udea.virtualstore.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.com.proyectoii.udea.virtualstore.R;
import co.com.proyectoii.udea.virtualstore.dto.ItemCarrito;


/**
 * Created by Fjqa86 on 01/6/2015.
 */
public class CarritoListAdapter extends ArrayAdapter<ItemCarrito> {

    private ArrayList<ItemCarrito> productos;
    private int layoutResourceId;
    private Context context;

    public CarritoListAdapter(Context context, int layoutResourceId, ArrayList<ItemCarrito> productos) {
            super(context, layoutResourceId, productos);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.productos = productos;
            }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {
            View row = convertView;
            ItemCarritoHolder holder = null;

            if(row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
            }

        holder = new ItemCarritoHolder();
        holder.producto = productos.get(posicion);

        holder.imagen = (ImageView) row.findViewById(R.id.productoIVCarrito);
        holder.nombre = (TextView) row.findViewById(R.id.nombreProdTVCarrito);
        holder.precio = (TextView) row.findViewById(R.id.precioTVCarrito);
        holder.cantidad = (TextView) row.findViewById(R.id.cantidadTVCarrito);
        holder.total = (TextView) row.findViewById(R.id.totalTVCarrito);

            row.setTag(holder);

            setupItem(holder);
            return row;
            }

    private void setupItem(ItemCarritoHolder holder) {
        // Se carga la imagen dinamicamente, se muestra ic_placeholder miestras carga, o ic_error_fallback si hay problemas
        Picasso.with(context).load(new ProductosHTTPClient().getURI(holder.producto.getProducto().getIconoPorDefecto()))
                .placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error_fallback)
                .resize(50,50).into(holder.imagen);
        //holder.imagen.setImageBitmap(((new ItemCarritosHTTPClient()).getImage(holder.producto.getIconoPorDefecto())));

        holder.nombre.setText(holder.producto.getProducto().getNombre()+" "+holder.producto.getColor()+holder.producto.getTalla());
        holder.precio.setText(" Precio :"+String.valueOf(holder.producto.getPrecio()));
        holder.cantidad.setText(" Cantidad :"+String.valueOf(holder.producto.getCantidad()));
        double total = (holder.producto.getPrecio()*holder.producto.getCantidad());
        holder.total.setText(" Total :"+String.valueOf(total));

    }

    public static class ItemCarritoHolder {
        ItemCarrito producto;
        ImageView imagen;
        TextView nombre;
        TextView cantidad;
        TextView precio;
        TextView total;
    }
}
