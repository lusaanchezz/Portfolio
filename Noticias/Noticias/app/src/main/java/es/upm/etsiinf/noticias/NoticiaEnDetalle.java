package es.upm.etsiinf.noticias;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NoticiaEnDetalle extends AppCompatActivity {

    String titulo, desc, contenido, imageURL, url;
    private TextView tituloTV, subtituloDescTV, contenidoTV;
    private ImageView noticiasIV;
    private Button btnLeerNoticias;
    private Button btnCompartir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_en_detalle);
        titulo = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        contenido = getIntent().getStringExtra("content");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");

        tituloTV = findViewById(R.id.idTVTituloNoticia);
        subtituloDescTV = findViewById(R.id.idTVSubtituloDesc);
        contenidoTV = findViewById(R.id.idTVContenido);
        noticiasIV = findViewById(R.id.idIVNoticias);
        btnLeerNoticias = findViewById(R.id.idBtnLeerNoticias);
        btnCompartir = findViewById(R.id.idbtnCompartir);

        tituloTV.setText(titulo);
        subtituloDescTV.setText(desc);
        contenidoTV.setText(contenido);
        Picasso.get().load(imageURL).into(noticiasIV);
        btnLeerNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
    }
}