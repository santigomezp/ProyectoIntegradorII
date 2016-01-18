package co.com.proyectoii.udea.virtualstore.activity;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.com.proyectoii.udea.virtualstore.R;


public class PagoFragment extends DialogFragment {


    public PagoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pago, container, false);
    }

}
