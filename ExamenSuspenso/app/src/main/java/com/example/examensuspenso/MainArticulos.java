package com.example.examensuspenso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.google.api.services.vision.v1.model.WebImage;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class MainArticulos extends AppCompatActivity implements Asynchtask, AdapterView.OnItemClickListener ,  View.OnClickListener{
    String numero, volumen;
    public Vision vision;
    static final int REQUEST_IMAGE_CAPTURE = 1;
   public  ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_articulos);



        SharedPreferences prefs = getSharedPreferences("datos_posicion", Context.MODE_PRIVATE);
        numero = prefs.getString("num", "");
        volumen = prefs.getString("volumen", "");


        Map<String, String> datos = new HashMap<String, String>();
        WebService ws = new WebService("http://revistas.uteq.edu.ec/ws/getarticles.php?num='" + numero + "'&volumen='" + volumen + "'", datos, this, this);
        ws.execute("");

        ListView listArticuloss = (ListView) findViewById(R.id.texlistaArticulos);
        listArticuloss.setOnItemClickListener(this);

        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        getPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    Articulos articulo;

    @Override
    public void processFinish(String result) throws JSONException {
        ArrayList<Articulos> listaArt = new ArrayList<>();


        JSONObject jsonObjResultado = new JSONObject(result);
        JSONArray listaArticulos = jsonObjResultado.getJSONArray("articles");

        for (int i = 0; i < listaArticulos.length(); i++) {
            JSONObject revi = listaArticulos.getJSONObject(i);
            articulo = new Articulos();
            articulo.setTitulo(revi.getString("title"));
            articulo.setDate_published(revi.getString("date_published"));
            articulo.setPdf(revi.getString("pdf"));
            articulo.setSection_title(revi.getString("section_title"));
            listaArt.add(articulo);
        }

        AdaptadorArticulos adaptadorarticulos = new AdaptadorArticulos(MainArticulos.this, listaArt);
        ListView lsOpciones = (ListView) findViewById(R.id.texlistaArticulos);
        lsOpciones.setAdapter(adaptadorarticulos);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(((Articulos) adapterView.getItemAtPosition(i)).getPdf()));
        request.setDescription("PDF Paper");
        request.setTitle("Pdf Article");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "filedownload.pdf");
        DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            manager.enqueue(request);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        //Toast.makeText(this.getApplicationContext(), ((Articulo) adapterView.getItemAtPosition(i)).getUrlpdf(), Toast.LENGTH_LONG).show();
    }


    public void getPermission(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Toast.makeText(this.getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {

    }


}
