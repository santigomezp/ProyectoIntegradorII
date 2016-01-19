package co.com.proyectoii.udea.virtualstore.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.proyectoii.udea.virtualstore.R;
import co.com.proyectoii.udea.virtualstore.dto.ItemCarrito;
import co.com.proyectoii.udea.virtualstore.utils.CarritoListAdapter;


public class CarritoFragment extends DialogFragment {
    private MainActivity contextoActivity ;
    private View rootView;
    Button btnPagar;
    ArrayList<ItemCarrito> carrito = new ArrayList<>();
    private ListView listViewCarrito;private CarritoListAdapter listAdpter;

    public static CarritoFragment newInstance(Bundle argumentos){
        CarritoFragment fragment = new CarritoFragment();
        if (argumentos != null) {
            fragment.setArguments(argumentos);
        }
        return fragment;

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle argumentos = getArguments();
        if (argumentos != null) {
           carrito = argumentos.getParcelableArrayList("carrito");
        }


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_carrito, container, false);
        listViewCarrito = (ListView)rootView.findViewById(R.id.listViewCarrito);
        TextView textViewTotal= (TextView)rootView.findViewById(R.id.textViewCarritoTotal);
        btnPagar = (Button) rootView.findViewById(R.id.buttonCarritoPagar);
        listAdpter = new CarritoListAdapter(contextoActivity, R.layout.carrito_lista, carrito);
        listViewCarrito.setAdapter(listAdpter);
        listViewCarrito.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                carrito.remove(position);
                contextoActivity.carrito = new ArrayList<ItemCarrito>();
                contextoActivity.carrito.addAll(carrito);
                listAdpter.notifyDataSetChanged();
                return true;
            }
        });
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PagoFragment newFragment = new PagoFragment();
                newFragment.show(getFragmentManager(), "pagoFragment");
                dismiss();
            }
        });
        double totalPagar = 0;
        for (ItemCarrito item : carrito){
            totalPagar= totalPagar+(item.getPrecio()*item.getCantidad());
        }
        textViewTotal.setText(" Total a Pagar : " +String.valueOf(totalPagar));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        contextoActivity = (MainActivity)activity;
    }
}
