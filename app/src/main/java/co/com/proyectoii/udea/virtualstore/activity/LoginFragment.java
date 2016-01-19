package co.com.proyectoii.udea.virtualstore.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import co.com.proyectoii.udea.virtualstore.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends DialogFragment {

    private static String URL_BASE= "http://servicio.daju.co/servicio/";
    private View rootView;
    private Button buttonLogin;
    private EditText editTextUsuario;
    private EditText editTextPasword;
    private MainActivity contextoActivity;
    JSONObject json;


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
                                               if (editTextUsuario.getText().length() > 0 & editTextPasword.getText().length() > 0) {
                                                   json = new JSONObject();
                                                   try {
                                                       json.put("user", editTextUsuario.getText().toString());
                                                       json.put("password", "$P$BUkLKf5Q4JpWi5sNGxPu0.4758vhD31");
                                                       new LoginTask().execute();
                                                   } catch (JSONException e) {
                                                       e.printStackTrace();
                                                   }
                                               } else{
                                                   Toast.makeText(contextoActivity, "Por favor ingrese usario y contraseña", Toast.LENGTH_LONG).show();
                                               }

                                           }
                                       }

        );

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        contextoActivity = (MainActivity) activity;
    }

    private class LoginTask extends AsyncTask<Void, Void, Void> {
        String resultado;
        private ProgressDialog dialog = new ProgressDialog(contextoActivity);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Logeando...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            resultado = uploadUrl("login",json.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            if(resultado.equals("true")){
                dismiss();
                Toast.makeText(contextoActivity,"Bienvenido",Toast.LENGTH_LONG).show();
                contextoActivity.usuario = editTextUsuario.getText().toString();
            }else{
                Toast.makeText(contextoActivity,"Algo anda mal revisa tu información",Toast.LENGTH_LONG).show();
            }

        }
    }

    private String uploadUrl(String myurl,String json){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(120, TimeUnit.SECONDS);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Request request = new Request.Builder()
                    .url(URL_BASE + myurl)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception E) {
            return "-1";
        }
    }

    private final String md5(final String cadena) {
        final String SHA1 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(SHA1);
            digest.update(cadena.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
