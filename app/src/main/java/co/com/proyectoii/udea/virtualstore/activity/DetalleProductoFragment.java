package co.com.proyectoii.udea.virtualstore.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.com.proyectoii.udea.virtualstore.dto.Producto;
import co.com.proyectoii.udea.virtualstore.R;
import co.com.proyectoii.udea.virtualstore.utils.ProductosHTTPClient;

/**
 * Created by artica on 10/12/15.
 */
public class DetalleProductoFragment extends DialogFragment {

    private MainActivity contextoActivity ;
    private View rootView;
    private Producto producto;
    private TextView textViewNombreProducto;
    private TextView textViewDescripcionProducto;
    private ImageView imageViewImagen;
    private Button btnAdd;
    private ArrayList<String> imagenes;
    private Button btnImgSiguiente;
    private Button btnImgAtras;
    private int numeroImagen;

    public static DetalleProductoFragment newInstance(Bundle argumentos){
       DetalleProductoFragment fragment = new DetalleProductoFragment();
        if (argumentos != null) {
            fragment.setArguments(argumentos);
        }
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        int style = DialogFragment.STYLE_NO_TITLE;
        setStyle(style, 0);
        setCancelable(false);
        numeroImagen = 0;
        Bundle argumentos = getArguments();
        if (argumentos != null) {
            producto = argumentos.getParcelable("producto");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detalle_producto, container, false);
        textViewNombreProducto = (TextView) rootView.findViewById(R.id.textViewDetalleProductoNombre);
        textViewDescripcionProducto = (TextView) rootView.findViewById(R.id.textViewDestalleProductoDescripcion);
        imageViewImagen = (ImageView) rootView.findViewById(R.id.imageViewDetalleProducto);
        btnAdd = (Button) rootView.findViewById(R.id.btnAddCar);
                textViewNombreProducto.setText(producto.getNombre());
        btnImgSiguiente = (Button) rootView.findViewById(R.id.btnImgSiguiente);
        btnImgAtras = (Button) rootView.findViewById(R.id.btnImgAtras);
        textViewNombreProducto.setText(producto.getNombre());
        textViewDescripcionProducto.setText(producto.getDescripcion().trim());
        imagenes = producto.getImagenes();
        descargarImagen(producto.getIconoPorDefecto());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnImgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargarImagen(imagenes.get(numeroImagen));
                if (numeroImagen > 0) {
                    numeroImagen--;
                }
            }
        });
        btnImgSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargarImagen(imagenes.get(numeroImagen));
                if (numeroImagen < imagenes.size()) {
                    numeroImagen++;
                }
            }
        });
        return rootView;
     }

    private void descargarImagen(String url){
        Picasso.with(contextoActivity).load(new ProductosHTTPClient().getURI(url)).into(imageViewImagen);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        contextoActivity = (MainActivity)activity;
    }
}
