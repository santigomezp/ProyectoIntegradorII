package co.com.proyectoii.udea.virtualstore.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.com.proyectoii.udea.virtualstore.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends DialogFragment {


    private View rootView;
    private Button buttonLogin;
    private EditText editTextUsuario;
    private EditText editTextPasword;


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(Bundle argumentos) {
        LoginFragment fragment = new LoginFragment();
        if (argumentos != null) {
            fragment.setArguments(argumentos);
        }
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        buttonLogin = (Button) rootView.findViewById(R.id.buttonLogin);
        editTextUsuario = (EditText) rootView.findViewById(R.id.editTextLoginUsuario);
        editTextPasword = (EditText) rootView.findViewById(R.id.editTextLoginPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

}
