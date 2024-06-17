package es.upm.etsiinf.noticias;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdaptadorCategoria.InterfazCategoriaClick{

    //8bd10e2e6f4b4bbf9bae58b3bab29ce3

    private RecyclerView noticiasRV, categoriasRV;
    private ProgressBar cargandoPB;
    private ArrayList<Articulos> arrayListArticulos;
    private ArrayList<ModeloCategorias> arrayListModeloCategorias;
    private AdaptadorCategoria adaptadorCategoria;
    private AdaptadorNoticias adaptadorNoticias;

    private Button btnNotificacion;
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noticiasRV = findViewById(R.id.idNoticias);
        categoriasRV = findViewById(R.id.idCategorias);
        cargandoPB = findViewById(R.id.idCargando);
        arrayListArticulos = new ArrayList<>();
        arrayListModeloCategorias = new ArrayList<>();
        adaptadorNoticias = new AdaptadorNoticias(arrayListArticulos, this);
        adaptadorCategoria = new AdaptadorCategoria(arrayListModeloCategorias, this, this::categoriaOnClick);
        noticiasRV.setLayoutManager(new LinearLayoutManager(this));
        noticiasRV.setAdapter(adaptadorNoticias);
        categoriasRV.setAdapter(adaptadorCategoria);
        getCategorias();
        getNoticias("All");
        adaptadorNoticias.notifyDataSetChanged();

        btnNotificacion = findViewById(R.id.btnNotificacion);
        btnNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNotificationChannel();
                creacionNotificacion();
            }
        });
    }

    public void onStop() {
        super.onStop();
        createNotificationChannel();
        creacionNotificacion();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nombre = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, nombre, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void creacionNotificacion() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.noticias);
        builder.setContentTitle("Nueva notificación");
        builder.setContentText("Enterate de las ultimas noticias y ponte al día de lo que esta pasando");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }

    private void getCategorias() {
        arrayListModeloCategorias.add(new ModeloCategorias("All", "https://www.losmolares.es/export/sites/losmolares/.galleries/imagenes-noticias/ultimas_noticias.jpg"));
        arrayListModeloCategorias.add(new ModeloCategorias("Technology", "https://blogs.worldbank.org/sites/default/files/voices/id4d_0.jpg"));
        arrayListModeloCategorias.add(new ModeloCategorias("Science", "https://concepto.de/wp-content/uploads/2020/03/telescopio-hubble-e1585173014940.jpg"));
        arrayListModeloCategorias.add(new ModeloCategorias("Sports", "https://img.freepik.com/foto-gratis/herramientas-deportivas_53876-138077.jpg\n"));
        arrayListModeloCategorias.add(new ModeloCategorias("General", "https://bolamundo.com/wp-content/uploads/2019/03/icono-bola-mundo.jpg\n"));
        arrayListModeloCategorias.add(new ModeloCategorias("Business", "https://imageio.forbes.com/blogs-images/alejandrocremades/files/2018/07/desk-3139127_1920-1200x773.jpg?format=jpg&width=960\n"));
        arrayListModeloCategorias.add(new ModeloCategorias("Entertainment", "https://img.remediosdigitales.com/934bbc/1366_2000/1366_2000.jpeg"));
        arrayListModeloCategorias.add(new ModeloCategorias("Health", "https://media.discordapp.net/attachments/1061317501549551767/1066046141461970954/coronavirus-disease-covid-2019-5060427_1920.png\n"));

    }

    private void getNoticias(String categorias) {
        cargandoPB.setVisibility(View.VISIBLE);
        arrayListArticulos.clear();
        String categoriaURL = "https://newsapi.org/v2/top-headlines?country=us&category=" + categorias + "&apikey=8bd10e2e6f4b4bbf9bae58b3bab29ce3";
        String url = "https://newsapi.org/v2/top-headlines?country=us&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=8bd10e2e6f4b4bbf9bae58b3bab29ce3";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<ModeloNoticias> call;
        if (categorias.equals("All")) {
            call = retrofitAPI.getTodasNoticias(url);
        } else {
            call = retrofitAPI.getCategoriaNoticias(categoriaURL);
            Log.e("call categorias", String.valueOf(call));
        }

        call.enqueue(new Callback<ModeloNoticias>() {
            @Override
            public void onResponse(Call<ModeloNoticias> call, Response<ModeloNoticias> response) {
                ModeloNoticias modeloNoticias = response.body();
                cargandoPB.setVisibility(View.GONE);
                ArrayList<Articulos> articulos = modeloNoticias.getArticles();
                for(int i = 0; i < articulos.size(); i++) {
                    arrayListArticulos.add(new Articulos(articulos.get(i).getTitle(), articulos.get(i).getDescription(), articulos.get(i).getUrlToImage(),
                        articulos.get(i).getUrl(), articulos.get(i).getContent()));
                }
                adaptadorNoticias.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ModeloNoticias> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error. No hay noticias disponibles", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void categoriaOnClick(int position) {
        String categoria = arrayListModeloCategorias.get(position).getCategory();
        getNoticias(categoria);
    }
}