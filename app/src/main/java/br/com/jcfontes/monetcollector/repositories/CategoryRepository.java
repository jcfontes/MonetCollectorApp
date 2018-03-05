package br.com.jcfontes.monetcollector.repositories;

import android.arch.lifecycle.LiveData;

import java.util.List;

import br.com.jcfontes.monetcollector.models.Category;

public interface CategoryRepository {

    LiveData<List<Category>> findAll();

    LiveData<List<Category>> findByContainingName(String name);

    LiveData<Category> findById(String id);

    LiveData<Category> create(Category item);

    LiveData<Category> update(String id, Category item);

    LiveData<Category> delete(String id);

}
