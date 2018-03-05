package br.com.jcfontes.monetcollector.repositories.impl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import br.com.jcfontes.monetcollector.models.Category;
import br.com.jcfontes.monetcollector.repositories.CategoryRepository;
import br.com.jcfontes.monetcollector.configs.RetrofitClient;
import br.com.jcfontes.monetcollector.services.CategoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepositoryImpl implements CategoryRepository {

    private CategoryService service;

    public CategoryRepositoryImpl() {
        service = RetrofitClient.getCollectorClient().create(CategoryService.class);
    }

    @Override
    public LiveData<List<Category>> findAll() {
        final MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        Call<List<Category>> call = service.findAll();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    @Override
    public LiveData<List<Category>> findByContainingName(String name) {
        final MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        Call<List<Category>> call = service.findByContainingName(name);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    @Override
    public LiveData<Category> findById(String id) {
        final MutableLiveData<Category> liveData = new MutableLiveData<>();
        Call<Category> call = service.findById(id);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Log.e(response.body().toString(), response.body().toString());
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    @Override
    public LiveData<Category> create(Category item) {
        final MutableLiveData<Category> liveData = new MutableLiveData<>();
        service.save(item)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        liveData.setValue(null);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        liveData.setValue(null);
                    }
                });
        return liveData;
    }

    @Override
    public LiveData<Category> update(String id, Category item) {
        final MutableLiveData<Category> liveData = new MutableLiveData<>();
        service.update(id, item)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        liveData.setValue(null);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        liveData.setValue(null);
                    }
                });
        return liveData;
    }

    @Override
    public LiveData<Category> delete(String id) {
        final MutableLiveData<Category> liveData = new MutableLiveData<>();
        service.delete(id)
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        liveData.setValue(null);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        liveData.setValue(null);
                    }
                });
        return liveData;
    }
}