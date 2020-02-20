package com.example.examensuspenso;

public class Articulos {
    String titulo, volumen, numero, year, pdf, date_published, section_title;

    public String getSection_title() {
        return section_title;
    }

    public void setSection_title(String section_title) {
        this.section_title = section_title;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getDate_published() {
        return date_published;
    }

    public void setDate_published(String date_published) {
        this.date_published = date_published;
    }

    public Articulos(String titulo, String volumen, String numero, String year, String pdf, String date_published) {
        this.titulo = titulo;
        this.volumen = volumen;
        this.numero = numero;
        this.year = year;
        this.pdf = pdf;
        this.date_published = date_published;
    }

    public Articulos() {
    }
}
