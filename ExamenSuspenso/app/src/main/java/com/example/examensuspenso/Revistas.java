package com.example.examensuspenso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Revistas {

    private String urlpdf;
    private String titulo;
    private String portada ;
    private String fechapublicacion;
    private String volumen;
    private String  numero;

    public Revistas(String urlpdf, String titulo, String portada, String fechapublicacion, String volumen, String numero) {
        this.urlpdf = urlpdf;
        this.titulo = titulo;
        this.portada = portada;
        this.fechapublicacion = fechapublicacion;
        this.volumen = volumen;
        this.numero = numero;
    }

    public Revistas() {
    }

    public String getUrlpdf() {
        return urlpdf;
    }

    public void setUrlpdf(String urlpdf) {
        this.urlpdf = urlpdf;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getFechapublicacion() {
        return fechapublicacion;
    }

    public void setFechapublicacion(String fechapublicacion) {
        this.fechapublicacion = fechapublicacion;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }


    public static ArrayList<Revistas> JsonObjectsBuild(JSONArray datos) throws JSONException {//revistas.uteq.edu.ec/webservice/noticia/
        ArrayList<Revistas> articulo = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
         //   articulo.add(new Revistas(datos.getJSONObject(i)));
        }
        return articulo;
    }
}
