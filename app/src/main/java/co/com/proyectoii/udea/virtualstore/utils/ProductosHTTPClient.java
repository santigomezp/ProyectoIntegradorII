package co.com.proyectoii.udea.virtualstore.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Clase que contiene los metodos que consumen los servicios web
 * Created by Alan on 12/5/2015.
 */
public class ProductosHTTPClient {

    private static String BASE_URL= "http://servicio.daju.co/servicio/";
    private static String IMG_URL = "http://daju.co/wp-content/uploads/";

    /*
     * Metodo para consumir el servicio web que retorna las categorias de los productos
     * Retorna un String parseable a JSON con el identificador y nombre de las categorias
     */
    public String getCategorias(){
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String serviceName = "producto_categorias";
        URL url = null;

        try{
            url = new URL(BASE_URL + serviceName);
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        try{
            // Se abre la conexión al servidor
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // servicio tipo GET
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            // Se lee la respuesta
            StringBuffer buffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                buffer.append(line + "\r\n");
            inputStream.close();
            connection.disconnect();
            return buffer.toString();
        } catch (Throwable t){
            t.printStackTrace();
        }
        finally {
            try { inputStream.close(); } catch (Throwable t) {}
            try { connection.disconnect(); } catch (Throwable t) {}
        }

        return null;
    }

    /*
     * Metodo para consumir el servicio web que retorna los productos de una categoria
     * Recibe como parametro el identificador de la categoria
     * Retorna un String parseable a JSON con los productos asociados a la categoria
     */
    public String getProdsPorCat(String idCategoria){
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String serviceName = "prods_por_categoria";
        URL url = null;

        try{
            url = new URL(BASE_URL + serviceName+"/?idcat="+ idCategoria);
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        try{
            // Se abre la conexión al servidor
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // servicio tipo GET
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            // Se lee la respuesta
            StringBuffer buffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
                buffer.append(line + "\r\n");
            inputStream.close();
            connection.disconnect();
            return buffer.toString();
        } catch (Throwable t){
            t.printStackTrace();
        }
        finally {
            try { inputStream.close(); } catch (Throwable t) {}
            try { connection.disconnect(); } catch (Throwable t) {}
        }

        return null;
    }

    public Bitmap getImage(String code) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            // Se lee la respuesta
            inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
            return bitmap;
        } catch (Throwable t){
            t.printStackTrace();
        }
        finally {
            try { inputStream.close(); } catch (Throwable t) {}
            try { connection.disconnect(); } catch (Throwable t) {}
        }
        return null;
    }

    public String getURI(String code){
        String uri = IMG_URL + code;
        return  uri;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
