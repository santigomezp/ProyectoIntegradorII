package co.com.proyectoii.udea.virtualstore.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.com.proyectoii.udea.virtualstore.dto.Producto;
import co.com.proyectoii.udea.virtualstore.R;

/**
 * Created by artica on 10/12/15.
 */
public class DetalleProductoFragment extends DialogFragment {

    private MainActivity contextoActivity ;
    private View rootView;
    private Producto producto;
    private TextView textViewNombreProducto;
    private TextView textViewDescripcionProducto;

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
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
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
        textViewNombreProducto.setText(producto.getNombre());
        textViewDescripcionProducto.setText(producto.getDescripcion());
        return rootView;
     }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        contextoActivity = (MainActivity)activity;
    }
}
