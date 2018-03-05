package br.com.jcfontes.monetcollector.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import br.com.jcfontes.monetcollector.models.Category;
import br.com.jcfontes.monetcollector.repositories.CategoryRepository;
import br.com.jcfontes.monetcollector.repositories.impl.CategoryRepositoryImpl;

public class CategoryViewModel extends AndroidViewModel {

    private LiveData<List<Category>> listLiveData;
    private LiveData<Category> categoryLiveData;
    private CategoryRepository repository;

    public CategoryViewModel(Application application) {
        super(application);
        this.listLiveData = new MediatorLiveData<>();
        this.categoryLiveData = new MediatorLiveData<>();
        this.repository = new CategoryRepositoryImpl();
        findAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return this.listLiveData;
    }

    public LiveData<List<Category>> getCategoryContainingName(String name) {
        return this.repository.findByContainingName(name);
    }

    public LiveData<Category> getCategoryById(String id) {
        return this.repository.findById(id);
    }

    private void findAllCategories() {
        this.listLiveData = this.repository.findAll();
    }

}
