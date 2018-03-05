package br.com.jcfontes.monetcollector.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.configs.AppConfig;
import br.com.jcfontes.monetcollector.models.Category;
import br.com.jcfontes.monetcollector.repositories.CategoryRepository;
import br.com.jcfontes.monetcollector.repositories.impl.CategoryRepositoryImpl;
import br.com.jcfontes.monetcollector.viewModels.CategoryViewModel;

import static br.com.jcfontes.monetcollector.activities.CategoryDetailFragment.ARG_ITEM_ID;

public class CategoryAddActivity extends AppCompatActivity {

    private EditText txtName;
    private EditText txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_fragment_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        this.txtName = findViewById(R.id.etName);
        this.txtDesc = findViewById(R.id.etDesc);

        String id = getIntent().getExtras() != null && getIntent().getExtras().getString(ARG_ITEM_ID) != null
                ? getIntent().getExtras().getString(ARG_ITEM_ID) : null;

        if (id != null) {
            ViewModelProviders.of(this)
                    .get(CategoryViewModel.class)
                    .getCategoryById(id)
                    .observe(this, category -> {
                        this.txtName.setText(category.getName());
                        this.txtDesc.setText(category.getDescription());
                    });
        }

        fabAdd.setOnClickListener(v -> {
            if (this.txtName.getText().toString().isEmpty()) {
                Toast.makeText(v.getContext(), getString(R.string.category_name_required), Toast.LENGTH_LONG).show();

            } else if (this.txtDesc.getText().toString().isEmpty()) {
                Toast.makeText(v.getContext(), getString(R.string.category_desc_required), Toast.LENGTH_LONG).show();

            } else {
                CategoryRepository repository = new CategoryRepositoryImpl();
                Category category = new Category();
                category.setName(this.txtName.getText().toString());
                category.setDescription(this.txtDesc.getText().toString());

                if (id == null) {
                    new InsertAsyncTask(repository, this).execute(category);

                } else {
                    CategoryTaskParams params = new CategoryTaskParams(id, category);
                    new UpdateAsyncTask(repository, this).execute(params);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class InsertAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryRepository repository;
        private CategoryAddActivity categoryAddActivity;

        InsertAsyncTask(CategoryRepository repository, CategoryAddActivity categoryAddActivity) {
            this.repository = repository;
            this.categoryAddActivity = categoryAddActivity;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            this.repository.create(params[0]);
            Intent intent = new Intent(this.categoryAddActivity, HomeActivity.class);
            intent.putExtra(AppConfig.ARG_FRAGMENT, AppConfig.KEY_FRAGMENT_CATEGORY);
            this.categoryAddActivity.startActivity(intent);

            return null;
        }
    }

    private class UpdateAsyncTask extends AsyncTask<CategoryTaskParams, Void, Void> {

        private CategoryRepository repository;
        private CategoryAddActivity categoryAddActivity;

        UpdateAsyncTask(CategoryRepository repository, CategoryAddActivity categoryAddActivity) {
            this.repository = repository;
            this.categoryAddActivity = categoryAddActivity;
        }

        @Override
        protected Void doInBackground(CategoryTaskParams... params) {
            this.repository.update(params[0].id, params[0].category);
            Intent intent = new Intent(this.categoryAddActivity, HomeActivity.class);
            intent.putExtra(AppConfig.ARG_FRAGMENT, AppConfig.KEY_FRAGMENT_CATEGORY);
            this.categoryAddActivity.startActivity(intent);

            return null;
        }
    }

    private static class CategoryTaskParams {
        String id;
        Category category;

        CategoryTaskParams(String id, Category category) {
            this.id = id;
            this.category = category;
        }
    }
}
