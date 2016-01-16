package co.com.proyectoii.udea.virtualstore.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.StringTokenizer;

import co.com.proyectoii.udea.virtualstore.dto.ItemCarrito;
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
    private Spinner spinnerTalla;
    private Spinner spinnerColor;
    private ImageView imageViewImagen;
    private Button btnAdd;
    private ArrayList<String> imagenes;
    private ArrayList<String> listaColores;
    private ArrayList<String> listaTallas;
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
        listaColores = new ArrayList<>();
        listaTallas = new ArrayList<>();
        rootView = inflater.inflate(R.layout.fragment_detalle_producto, container, false);
        textViewNombreProducto = (TextView) rootView.findViewById(R.id.textViewDetalleProductoNombre);
        textViewDescripcionProducto = (TextView) rootView.findViewById(R.id.textViewDestalleProductoDescripcion);
        spinnerTalla = (Spinner) rootView.findViewById(R.id.spinnerTalla);
        spinnerColor = (Spinner) rootView.findViewById(R.id.spinnerColor);
        imageViewImagen = (ImageView) rootView.findViewById(R.id.imageViewDetalleProducto);
        btnAdd = (Button) rootView.findViewById(R.id.btnAddCar);
                textViewNombreProducto.setText(producto.getNombre());
        btnImgSiguiente = (Button) rootView.findViewById(R.id.btnImgSiguiente);
        btnImgAtras = (Button) rootView.findViewById(R.id.btnImgAtras);
        textViewNombreProducto.setText(producto.getNombre());
        textViewDescripcionProducto.setText(producto.getDescripcion().trim());
        imagenes = producto.getImagenes();
        int posisionIni = producto.getAtributos().indexOf("Tallas");
        int posisionFinal = producto.getAtributos().indexOf(";s:8:");
        String tallas = producto.getAtributos().substring(posisionIni + 26, posisionFinal - 2);
        posisionIni = producto.getAtributos().indexOf("Colores");

        String colores = producto.getAtributos().substring(posisionIni+27,producto.getAtributos().length()-1);
        posisionFinal = colores.indexOf(";s:8:");
        colores = colores.substring(0, posisionFinal - 1);
        StringTokenizer st = new StringTokenizer(tallas,"|");
        while (st.hasMoreTokens()) {
            listaTallas.add(st.nextToken());
        }
        st = new StringTokenizer(colores,"|");
        while (st.hasMoreTokens()) {
            listaColores.add(st.nextToken());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(contextoActivity.getBaseContext(),
                android.R.layout.simple_spinner_item, listaTallas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTalla.setAdapter(dataAdapter);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(contextoActivity.getBaseContext(),
                android.R.layout.simple_spinner_item, listaColores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(dataAdapter2);
        descargarImagen(producto.getIconoPorDefecto());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemCarrito itemCarrito = new ItemCarrito();
                itemCarrito.setProducto(producto);
                itemCarrito.setTalla(spinnerTalla.getSelectedItem().toString());
                itemCarrito.setColor(spinnerColor.getSelectedItem().toString());
                contextoActivity.carrito.add(itemCarrito);
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
