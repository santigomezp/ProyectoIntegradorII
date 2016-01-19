package co.com.proyectoii.udea.virtualstore.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button btnCancelar;
    private TextView textViewPrecio;
    private EditText editTextCantidad;

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
        textViewPrecio = (TextView) rootView.findViewById(R.id.textViewDetallerProductoPrecio);
        editTextCantidad = (EditText) rootView.findViewById(R.id.editTextDetalleProductoCantidad);
        textViewDescripcionProducto = (TextView) rootView.findViewById(R.id.textViewDestalleProductoDescripcion);
        spinnerTalla = (Spinner) rootView.findViewById(R.id.spinnerTalla);
        spinnerColor = (Spinner) rootView.findViewById(R.id.spinnerColor);
        imageViewImagen = (ImageView) rootView.findViewById(R.id.imageViewDetalleProducto);
        btnCancelar = (Button) rootView.findViewById(R.id.btnCancelarCar);
        btnAdd = (Button) rootView.findViewById(R.id.btnAddCar);
        textViewNombreProducto.setText(producto.getNombre());
        btnImgSiguiente = (Button) rootView.findViewById(R.id.btnImgSiguiente);
        btnImgAtras = (Button) rootView.findViewById(R.id.btnImgAtras);
        textViewNombreProducto.setText(producto.getNombre());
        textViewDescripcionProducto.setText(producto.getDescripcion().trim());
        imagenes = producto.getImagenes();
        int posisionIni = producto.getAtributos().indexOf("Tallas");
        int posisionFinal = producto.getAtributos().indexOf(";s:8:");
        if (posisionFinal == -1 & posisionIni == -1) {
            spinnerColor.setVisibility(View.GONE);
            spinnerTalla.setVisibility(View.GONE);
        } else {


            String tallas = producto.getAtributos().substring(posisionIni + 26, posisionFinal - 2);
            posisionIni = producto.getAtributos().indexOf("Colores");

            String colores = producto.getAtributos().substring(posisionIni + 27, producto.getAtributos().length() - 1);
            posisionFinal = colores.indexOf(";s:8:");
            colores = colores.substring(0, posisionFinal - 1);
            StringTokenizer st = new StringTokenizer(tallas, "|");
            while (st.hasMoreTokens()) {
                listaTallas.add(st.nextToken());
            }
            st = new StringTokenizer(colores, "|");
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

            spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (spinnerTalla.getSelectedItem() != null) {
                        producto.setPreciosVars(spinnerTalla.getSelectedItem().toString().toLowerCase(), spinnerColor.getSelectedItem().toString().toLowerCase());
                        if (producto.getPrecioDescuento() != 0 && producto.getPrecioDescuento() != -1) {
                            textViewPrecio.setText("" + producto.getPrecioDescuento());
                        } else {
                            if (producto.getPrecioRegular() != 0) {
                                textViewPrecio.setText("" + producto.getPrecioRegular());
                            } else {
                                textViewPrecio.setText("" + producto.getPrecio());
                            }
                        }

                    } else {
                        Toast.makeText(contextoActivity, "Elija primero una talla ", Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemCarrito itemCarrito = new ItemCarrito();
                itemCarrito.setProducto(producto);
                itemCarrito.setTalla(spinnerTalla.getSelectedItem().toString());
                itemCarrito.setColor(spinnerColor.getSelectedItem().toString());
                itemCarrito.setPrecio(Double.parseDouble(textViewPrecio.getText().toString()));
                itemCarrito.setCantidad(Integer.parseInt(editTextCantidad.getText().toString()));
                contextoActivity.carrito.add(itemCarrito);
                dismiss();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
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
