package com.example.examensuspenso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdaptadorRevistas extends ArrayAdapter<Revistas> {
    public AdaptadorRevistas(Context context, ArrayList<Revistas> datos) {
        super(context, R.layout.ly_revistas, datos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.ly_revistas, null);
        TextView txtTitulo = (TextView)item.findViewById(R.id.LblTitulo);
        txtTitulo.setText(getItem(position).getTitulo());
        TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
        lblSubtitulo.setText(getItem(position).getNumero());
        TextView lblfecha = (TextView)item.findViewById(R.id.LblFechayCategoria);
        lblfecha.setText(getItem(position).getFechapublicacion());
        ImageView imageView = (ImageView) item.findViewById(R.id.imgNoticia);
        Glide.with(this.getContext()).load(getItem(position).getUrlpdf()).error(R.drawable.imgnotfound).into(imageView);
        return (item);
    }
}
