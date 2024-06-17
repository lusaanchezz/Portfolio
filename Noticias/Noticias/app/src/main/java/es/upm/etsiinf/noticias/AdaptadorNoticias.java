package es.upm.etsiinf.noticias;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorNoticias extends RecyclerView.Adapter<AdaptadorNoticias.ViewHolder> {
    private ArrayList<Articulos> arrayListArticulos;
    private Context context;

    public AdaptadorNoticias(ArrayList<Articulos> arrayListArticulos, Context context) {
        this.arrayListArticulos = arrayListArticulos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdaptadorNoticias.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticias,parent, false);
        return new AdaptadorNoticias.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorNoticias.ViewHolder holder, int position) {
        Articulos articulos = arrayListArticulos.get(position);
        holder.subtituloTV.setText(articulos.getDescription());
        holder.tituloTV.setText(articulos.getTitle());
        Picasso.get().load(articulos.getUrlToImage()).into(holder.noticiaIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,NoticiaEnDetalle.class);
                i.putExtra("title", articulos.getTitle());
                i.putExtra("content", articulos.getContent());
                i.putExtra("desc", articulos.getDescription());
                i.putExtra("image", articulos.getUrlToImage());
                i.putExtra("url", articulos.getUrl());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListArticulos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloTV, subtituloTV;
        private ImageView noticiaIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTV = itemView.findViewById(R.id.idTVTituloNoticia);
            subtituloTV = itemView.findViewById(R.id.idTVSubtitulo);
            noticiaIV = itemView.findViewById(R.id.idIVNoticias);
        }
    }
}
