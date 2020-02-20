package com.example.examensuspenso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity implements Asynchtask, AdapterView.OnItemClickListener {

    public Vision vision;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(), new AndroidJsonFactory(), null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer("Llave"));
        vision = visionBuilder.build();

        Map<String, String> datos = new HashMap<String, String>();
        WebService ws = new WebService("http://revistas.uteq.edu.ec/ws/getrevistas.php", datos, this, this);
        ws.execute("");

        ListView lsOpciones = (ListView) findViewById(R.id.texlista);
        lsOpciones.setOnItemClickListener(this);

    }

    public Revistas revista;
    public Revistas[] revistas;

    @Override
    public void processFinish(String result) throws JSONException {

        ArrayList<Revistas> listaRev = new ArrayList<>();
        JSONObject ListaRevistas = new JSONObject(result);
        JSONArray lista = ListaRevistas.getJSONArray("issues");

        for (int i = 0; i < lista.length(); i++) {
            JSONObject revi = lista.getJSONObject(i);
            revista = new Revistas();
            revista.setTitulo(revi.getString("title"));
            revista.setNumero(revi.getString("number"));
            revista.setVolumen(revi.getString("volume"));
            revista.setFechapublicacion(revi.getString("date_published"));
            revista.setUrlpdf(revi.getString("portada"));
            listaRev.add(revista);
        }
        AdaptadorRevistas adaptadornoticias = new AdaptadorRevistas(this, listaRev);
        ListView lsOpciones = (ListView)findViewById(R.id.texlista);
        lsOpciones.setAdapter(adaptadornoticias);
    }
    public void clikObjetos(View view) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<AnnotateImageRequest> requests = new ArrayList<>();

                BitmapDrawable drawable = (BitmapDrawable)imagen.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap = scaleBitmapDown(bitmap, 1200);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] imageInByte = stream.toByteArray();

                Image inputImage = new Image();
                inputImage.encodeContent(imageInByte);

                Feature desiredFeature = new Feature();
                desiredFeature.setType("WEB_DETECTION");
                AnnotateImageRequest request = new AnnotateImageRequest();
                request.setImage(inputImage);
                request.setFeatures(Arrays.asList(desiredFeature));
                BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
                batchRequest.setRequests(Arrays.asList(request));


                request = new AnnotateImageRequest();
                request.setImage(inputImage);
                request.setFeatures(Arrays.asList(desiredFeature));
                batchRequest = new BatchAnnotateImagesRequest();
                batchRequest.setRequests(Arrays.asList(request));

                try {

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse batchResponse = annotateRequest.execute();

                    StringBuilder message = new StringBuilder("I found these things:\n\n");

                    WebDetection annotation = batchResponse.getResponses().get(0).getWebDetection();
                    if (annotation != null) {
                        ArrayList<Articulos> lista = new ArrayList<>();

                        for (WebEntity entity : annotation.getWebEntities()) {
                            if (entity.getScore() > 0.96) {

                                //   message.append(String.format(Locale.ENGLISH, "%.3f: %s", entity.getScore(), entity.getDescription()));
                                //   message.append("\n");
                            } else {

                            }
                        }

                        for (WebImage image : annotation.getPartialMatchingImages()) {
                            message.append(image.getUrl());
                            message.append("\n");
                        }



                    } else {
                        message.append("nothing");
                    }
                    final String result = message.toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //         TextView imageDetail = (TextView) findViewById(R.id.txtResultado);
                            //         imageDetail.setText(result);
                        }
                    });

                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        revista = (Revistas) adapterView.getItemAtPosition(position);

        SharedPreferences datosrevista = getSharedPreferences("datos_posicion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = datosrevista.edit();
        editor.putString("num", revista.getNumero());
        editor.putString("volumen", revista.getVolumen());
        editor.commit();
        startActivity(new Intent(getApplicationContext(), MainArticulos.class));
    }
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
