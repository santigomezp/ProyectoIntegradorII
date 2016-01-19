package co.com.proyectoii.udea.virtualstore.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import co.com.proyectoii.udea.virtualstore.R;
import co.com.proyectoii.udea.virtualstore.dto.Categoria;
import co.com.proyectoii.udea.virtualstore.dto.ItemCarrito;
import co.com.proyectoii.udea.virtualstore.dto.Producto;
import co.com.proyectoii.udea.virtualstore.utils.JSONProductoParser;
import co.com.proyectoii.udea.virtualstore.utils.ProdsListAdapter;
import co.com.proyectoii.udea.virtualstore.utils.ProductosHTTPClient;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    // Lista para almacenar las categorias de productos
    ArrayList<Categoria> listCat = new ArrayList<Categoria>();
    Spinner spinner;
    private ProdsListAdapter listAdpter;
    private Button loginBtn;
    private Button carritoBtn;
    private ListView listViewProductos;
    private Producto productoSeleccionado;
    public String usuario=null;
    ArrayList<ItemCarrito> carrito = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinnerCats);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        carritoBtn = (Button) findViewById(R.id.carritoBtn);
        listViewProductos = (ListView) findViewById(R.id.listProductos);

        // Se crea una tarea asincrona para obtener el listado de categorias desde el servidor DAJU
        CategoriasTask categoriasTask = new CategoriasTask();
        categoriasTask.execute();

        spinner.setOnItemSelectedListener(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogLogin();
            }
        });
        carritoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("carrito", carrito);
                CarritoFragment newFragment = CarritoFragment.newInstance(bundle);
                newFragment.show(getFragmentManager(), "carritoFragment");
            }
        });
        listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productoSeleccionado = (Producto) parent.getItemAtPosition(position);
                abrirDialogDetalleProducto();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (usuario!=null){
            loginBtn.setText("Hola "+usuario);
            loginBtn.setClickable(false);
        }
    }

    /*
         * Cuando se selecciona un elemento del spinner
         */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Se crea tarea asincrona para la carga del listado de productos asociados a la categoria
        // seleccionada. Esta tarea invoca el metodo que consume el servicio web de los productos
        ProductosTask productosTask = new ProductosTask();
        productosTask.execute(listCat.get(position).getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void abrirDialogLogin(){
        LoginFragment newFragment = LoginFragment
                .newInstance(null);
        newFragment.show(getFragmentManager(), "loginFragment");
    }

    private void abrirDialogDetalleProducto(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("producto", productoSeleccionado);
        DetalleProductoFragment newFragment = DetalleProductoFragment.newInstance(bundle);
        newFragment.show(getFragmentManager(), "detalleProductoFragment");
    }

    /*
     * Tarea asincrona para llamar el servicio web que retorna las categorias de productos
     */
    private class CategoriasTask extends AsyncTask<String, Void, Void>{

        private static final String TAG = "CategoriasTask";
        private String Error = null;
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        String data =""; // Aqui se almacena la informacion retornada por el servicio

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Cargando categorias");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                // Se llama al metodo que consume el servicio web
                data = ((new ProductosHTTPClient()).getCategorias());
            } catch (Exception ex) {
                Error = ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            if (Error != null) {
            } else {
                try {
                    // La informacion retornada por el servicio se conviente a un Array JSON
                    JSONArray jsonArray  = new JSONArray(data);

                    // De cada elemento del array, se extrae el identificador y nombre de la categoria
                    if (jsonArray != null) {
                        Categoria categoria;
                        ArrayList<String> nombresCats = new ArrayList<String>();
                        int len = jsonArray.length();
                        for (int i=0;i<len;i++){
                            categoria = new Categoria();
                            categoria.setId(jsonArray.getJSONObject(i).getString("term_id"));
                            categoria.setNombre(jsonArray.getJSONObject(i).getString("name"));
                            nombresCats.add(jsonArray.getJSONObject(i).getString("name"));
                            listCat.add(categoria);
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),
                                android.R.layout.simple_spinner_item, nombresCats);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();
        }
    }

    /*
     * Tarea asincrona para llamar el servicio web que retorna los productos de una categoria
     * Recibe como parametro el identificador de la categoria
     */
    private class ProductosTask extends AsyncTask<String, Void, ArrayList<Producto>>{

        private static final String TAG = "ProductosTask";
        private String Error = null;
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();
        String data ="";

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Cargando productos...");
            dialog.show();
        }

        @Override
        protected ArrayList<Producto> doInBackground(String... params) {
            data = ((new ProductosHTTPClient()).getProdsPorCat(params[0]));

            try{
                listaProductos = JSONProductoParser.getProducto(data);
            } catch (Exception ex) {
                Error = ex.getMessage();
            }
            return listaProductos;
        }

        @Override
        protected void onPostExecute(ArrayList<Producto> productos) {

            // Se crea el adaptador para la lista de los productos y se asigna al ListView
            listAdpter = new ProdsListAdapter(MainActivity.this, R.layout.producto_lista, productos);
            listViewProductos.setAdapter(listAdpter);

            dialog.dismiss();
        }
    }
}
