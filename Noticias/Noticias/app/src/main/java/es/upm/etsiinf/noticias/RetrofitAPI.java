package es.upm.etsiinf.noticias;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    Call<ModeloNoticias> getTodasNoticias(@Url String url);

    @GET
    Call<ModeloNoticias>getCategoriaNoticias(@Url String url);

}
