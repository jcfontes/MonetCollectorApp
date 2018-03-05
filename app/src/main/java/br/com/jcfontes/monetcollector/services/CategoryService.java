package br.com.jcfontes.monetcollector.services;

import android.arch.persistence.room.Update;

import java.util.List;

import br.com.jcfontes.monetcollector.models.Category;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryService {

    @GET("categories")
    Call<List<Category>> findAll();

    @GET("categories/filter")
    Call<List<Category>> findByContainingName(@Query("name") String name);

    @GET("categories/{id}")
    Call<Category> findById(@Path("id") String id);

    @POST("categories")
    Call<Void> save(@Body Category item);

    @PUT("categories/{id}")
    Call<Void> update(@Path("id") String id, @Body Category item);

    @DELETE("categories/{id}")
    Call<Void> delete(@Path("id") String id);

}
