package es.upm.etsiinf.noticias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorCategoria extends RecyclerView.Adapter<AdaptadorCategoria.ViewHolder>{
    private ArrayList<ModeloCategorias> modeloCategoriasRV;
    private Context context;
    private InterfazCategoriaClick interfazCategoriaClick;

    public AdaptadorCategoria(ArrayList<ModeloCategorias> modeloCategoriasRV, Context context, InterfazCategoriaClick interfazCategoriaClick) {
        this.modeloCategoriasRV = modeloCategoriasRV;
        this.context = context;
        this.interfazCategoriaClick = interfazCategoriaClick;
    }

    @NonNull
    @Override
    public AdaptadorCategoria.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_item,parent, false);
        return new AdaptadorCategoria.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorCategoria.ViewHolder holder, int position) { //int position dentro de los parametros que se pasan
        int numero = position;
        ModeloCategorias modeloCategorias = modeloCategoriasRV.get(position);
        holder.categoriaTV.setText(modeloCategorias.getCategory());
        Picasso.get().load(modeloCategorias.getCategoryImageUrl()).into(holder.categoriaIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfazCategoriaClick.categoriaOnClick(numero);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modeloCategoriasRV.size();
    }

    public interface InterfazCategoriaClick {
        void categoriaOnClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView categoriaTV;
        private ImageView categoriaIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriaTV = itemView.findViewById(R.id.idTVCategorias);
            categoriaIV = itemView.findViewById(R.id.idIVCategorias);
        }
    }
}
