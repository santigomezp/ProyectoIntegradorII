package co.com.proyectoii.udea.virtualstore.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.proyectoii.udea.virtualstore.dto.Producto;

/**
 * Clase que contiene el metodo para decodificar el archivo JSON retornado por el servicio web de los
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
            int lenArrayVariaciones;
            String metaKey;
            String metaValue;

            for (int i=0;i<lenArray;i++){
                producto = new Producto();

                // Se obtiene la informacion post (principal), meta (secundaria) del producto y sus variaciones de precios
                JSONObject postObj = jsonArray.getJSONObject(i).getJSONObject("post");
                JSONArray metaArray = jsonArray.getJSONObject(i).getJSONArray("meta");
                JSONArray variacionesArray = jsonArray.getJSONObject(i).getJSONArray("variaciones");

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
                            metaValue = metaValue.replace(".", "");
                            metaValue = metaValue.replace(",", "");
                            if(metaValue.length() <3){
                                producto.setPrecio(Double.parseDouble(metaValue)*1000);
                            } else
                                producto.setPrecio(Double.parseDouble(metaValue));
                            break;
                        case "_stock":
                            if (metaValue.isEmpty()){
                                metaValue = "9999";
                            }
                            producto.setStock(Double.parseDouble(metaValue));
                            break;
                        case "_product_image_gallery":
                            JSONArray galleryArray = jsonArray.getJSONObject(i).getJSONArray("secondImages");
                            if(galleryArray.isNull(0))
                                break;
                            ArrayList<String> gallery = new ArrayList<String>();
                            int lenArrayGallery = galleryArray.length();
                            for (int k=0;k<lenArrayGallery;k++){
                                gallery.add(galleryArray.getString(k));
                            }
                            producto.setImagenes(gallery);
                            break;
                        default:
                            break;
                    }
                }

                // Se extrae los precios de cada variacion de cada producto
                JSONObject variacionesObj;
                lenArrayVariaciones = variacionesArray.length();
                ArrayList<String> variaciones = new ArrayList<>();
                String variacion = "";
                for (int l=1;l<=lenArrayVariaciones;l++){
                    variacionesObj = variacionesArray.getJSONObject(l - 1);
                    metaValue = variacionesObj.getString("meta_value");
                    variacion = variacion.concat(metaValue);
                    variacion = variacion.concat(",");
                    if (l%5==0){
                        while (variacion.substring(variacion.length()-1, variacion.length()).equals(","))
                            variacion = variacion.substring(0, variacion.length()-1);
                        System.out.println(variacion);
                        variaciones.add(variacion);
                        variacion = "";
                    }
                    producto.setVariaciones(variaciones);
                }
                productos.add(producto);
            }
        }
        return productos;
    }
}
