package co.com.proyectoii.udea.virtualstore.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import co.com.proyectoii.udea.virtualstore.dto.Producto;

/**
 * Clase que contiene el metodo para decodificar el archivo JSON restornado por el servicio web de los
 * productos por categoria
 * Created by Alan on 12/5/2015.
 */
public class JSONProductoParser {

    public static ArrayList<Producto> getProducto(String data) throws JSONException  {
        Producto producto;
        ArrayList<Producto> productos = new ArrayList<Producto>();
        JSONArray jsonArray  = new JSONArray(data);

        // De cada elemento del array, se extrae el identificador y nombre de la categoria
        if (jsonArray != null) {
            int lenArray = jsonArray.length();
            int lenArrayMeta;
            String metaKey;
            String metaValue;

            for (int i=0;i<lenArray;i++){
                producto = new Producto();

                // Se obtiene la informacion post (principal) y meta (secundaria) del producto
                JSONObject postObj = jsonArray.getJSONObject(i).getJSONObject("post");
                JSONArray metaArray = jsonArray.getJSONObject(i).getJSONArray("meta");

                // Se extrae de post la informacion de interes
                producto.setId(postObj.getString("ID"));
                producto.setNombre(postObj.getString("post_title"));
                producto.setCategoria(postObj.getString("term_id"));
                producto.setDescripcion(postObj.getString("post_content"));

                // Se extrae de meta la informacion de interes
                JSONObject metaObj;
                lenArrayMeta = metaArray.length();
                for (int j=0;j<lenArrayMeta;j++){
                    metaObj = metaArray.getJSONObject(j);
                    metaKey = metaObj.getString("meta_key");
                    metaValue = metaObj.getString("meta_value");

                    switch (metaKey){
                        case "_thumbnail_id":
                            producto.setIconoPorDefecto(jsonArray.getJSONObject(i).getString("image"));
                            break;

                        case "_product_attributes":
                            /*ArrayList<String> tallas = new ArrayList<String>();
                            ArrayList<String> colores = new ArrayList<String>();*/
                            producto.setAtributos(metaValue);
                            break;
                        case "_price":
                            producto.setPrecio(Double.parseDouble(metaValue));
                            break;
                        case "_stock":
                            if (metaValue.isEmpty()){
                                metaValue = "9999";
                            }
                            producto.setStock(Double.parseDouble(metaValue));
                            break;
                        case "_product_image_gallery":
                            producto.setImagenes(new ArrayList<String>(Arrays.asList(metaValue.split("\\s*,\\s*"))));
                            break;
                        default:
                            break;
                    }
                }
                productos.add(producto);
            }
        }
        return productos;
    }
}
