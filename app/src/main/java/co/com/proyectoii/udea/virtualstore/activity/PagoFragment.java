package co.com.proyectoii.udea.virtualstore.activity;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import co.com.proyectoii.udea.virtualstore.R;
import co.com.proyectoii.udea.virtualstore.dto.ItemCarrito;


public class PagoFragment extends DialogFragment {

    private static String URL_BASE= "http://servicio.daju.co/servicio/";
    private MainActivity contextoActivity;
    private View rootView;

    EditText editTextNombres;
    EditText editTextApellidos;
    EditText editTextDireccion1;
    EditText editTextDireccion2;
    EditText editTextTelefono;
    EditText editTextEmail;
    Button btnContraEntrega;
    Button btnPse;
    JSONObject json;

    public PagoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pago, container, false);
        editTextNombres = (EditText) rootView.findViewById(R.id.editTextNombres);
        editTextApellidos = (EditText) rootView.findViewById(R.id.editTextApellidos);
        editTextDireccion1 = (EditText) rootView.findViewById(R.id.editTextDireccion1);
        editTextDireccion2 = (EditText) rootView.findViewById(R.id.editTextDireccion2);
        editTextTelefono = (EditText) rootView.findViewById(R.id.editTextTelefono);
        editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        btnContraEntrega = (Button) rootView.findViewById(R.id.btnContraEntrega);
        btnPse = (Button) rootView.findViewById(R.id.btnPse);

        btnContraEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarFormulario()) {
                    JSONObject jsonaux = new JSONObject();
                    json = new JSONObject();
                    JSONArray arr = new JSONArray();

                    try {
                        json.put("firstName", editTextNombres.getText().toString());
                        json.put("lastName", editTextApellidos.getText().toString());
                        json.put("date", getFechaHora());
                        json.put("company", "UdeA");
                        json.put("address1", editTextDireccion1.getText().toString());
                        json.put("address2", editTextDireccion2.getText().toString());
                        json.put("city", "Medellin");
                        json.put("state", "Antioquia");
                        json.put("email", editTextEmail.getText().toString());
                        json.put("phone", editTextTelefono.getText().toString());
                        json.put("paym", "cod");
                        json.put("discount", "0");
                        json.put("shippingTax", "0");
                        json.put("recorded", "yes");
                        double totalPagar = 0;
                        for (ItemCarrito item : contextoActivity.carrito) {
                            jsonaux.put("name", item.getProducto().getNombre());
                            jsonaux.put("quantity", String.valueOf(item.getCantidad()));
                            jsonaux.put("productID", item.getProducto().getId());
                            jsonaux.put("varID", "0");
                            jsonaux.put("lineTotal", "50");
                            jsonaux.put("talla", item.getTalla());
                            jsonaux.put("color", item.getColor());
                            arr.put(jsonaux);
                            totalPagar = totalPagar + (item.getPrecio() * item.getCantidad());
                        }


                        //Se agrega listado de productos
                        json.put("orderTotal", totalPagar);
                        json.put("item", arr);

                        new CheckoutTask().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            else{
                Toast.makeText(contextoActivity,"Faltan campos por ingresar o los campos son demasiado cortos", Toast.LENGTH_LONG);
            }
        }});

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        contextoActivity = (MainActivity)activity;
    }

    /**
     * Obtiene la hora actual del sitema.
     * @return retorna un String con la fecha y hora formateadas.
     */
    private String getFechaHora(){

        Calendar calendario = new GregorianCalendar();

        Date fecha = calendario.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",  Locale.US);

        String tiempo = df.format(fecha);

        return tiempo;
    }

    private class CheckoutTask extends AsyncTask<Void, Void, Void> {
        String resultado;
        private ProgressDialog dialog = new ProgressDialog(contextoActivity);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Realizando pedido...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            resultado = uploadUrl("comprar_carrito",json.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            if(resultado.equals("1")){
                dismiss();
                Toast.makeText(contextoActivity,"pedido realizado con exito",Toast.LENGTH_LONG);
            }else{
                Toast.makeText(contextoActivity,"No se pudo guardar su pedido intente nuevamente",Toast.LENGTH_LONG);
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
    private boolean validarFormulario(){
        if(editTextNombres.getText().length()<3){
            return false;
        }
        if(editTextApellidos.getText().length()<3){
            return false;
        }
        if(editTextDireccion1.getText().length()<3){
            return false;
        }
        if(editTextDireccion2.getText().length()<3){
            return false;
        }
        if(editTextEmail.getText().length()<3){
            return false;
        }
        if(editTextTelefono.getText().length()<7){
            return false;
        }
        return true;
    }

}
