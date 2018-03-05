package br.com.jcfontes.monetcollector.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.activities.adapters.CategoryEditAdapter;
import br.com.jcfontes.monetcollector.activities.listeners.OnItemClickListener;
import br.com.jcfontes.monetcollector.models.Category;
import br.com.jcfontes.monetcollector.repositories.CategoryRepository;
import br.com.jcfontes.monetcollector.repositories.impl.CategoryRepositoryImpl;
import br.com.jcfontes.monetcollector.viewModels.CategoryViewModel;

public class CategoryFragment extends Fragment {

    private RecyclerView rvCategory;
    private CategoryEditAdapter adapter;
    private EditText etFilter;

    private OnItemClickListener editClick;
    private OnItemClickListener deleteClick;

    public CategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);

        etFilter = view.findViewById(R.id.etFilter);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getResultFilter(etFilter.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        this.rvCategory = view.findViewById(R.id.categories);
        assert this.rvCategory != null;

        Context context = view.getContext();

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryAddActivity.class);
            context.startActivity(intent);

            this.rvCategory.getAdapter().notifyDataSetChanged();
        });

        this.editClick = position -> {
            Intent intent = new Intent(context, CategoryAddActivity.class);
            intent.putExtra(CategoryDetailFragment.ARG_ITEM_ID, adapter.getCategory(position).getId());
            context.startActivity(intent);
        };

        this.deleteClick = position -> {
            CategoryRepository repository = new CategoryRepositoryImpl();
            new DeleteAsyncTask(repository).execute(adapter.getCategory(position));
        };

        setupRecyclerView(this.rvCategory);
        return view;
    }

    private void setupRecyclerView(RecyclerView rvCategory) {
        List<Category> categories = new ArrayList<>();
        getCategoryList();
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.adapter = new CategoryEditAdapter(categories, this.editClick, this.deleteClick);
        rvCategory.setAdapter(this.adapter);
    }

    private void getCategoryList() {
        ViewModelProviders.of(this)
                .get(CategoryViewModel.class)
                .getCategories()
                .observe(this, category -> {
                    this.adapter.setList(category);
                    this.rvCategory.getAdapter().notifyDataSetChanged();
                });
    }

    private void getResultFilter(String filter) {

        ViewModelProviders.of(this)
                .get(CategoryViewModel.class)
                .getCategoryContainingName(filter)
                .observe(this, category -> {
                    this.adapter.setList(category);
                    this.rvCategory.getAdapter().notifyDataSetChanged();
                });
    }

    private class DeleteAsyncTask extends AsyncTask<Category, Void, Category> {

        private CategoryRepository repository;

        DeleteAsyncTask(CategoryRepository repository) {
            this.repository = repository;
        }

        @Override
        protected Category doInBackground(final Category... params) {
            this.repository.delete(params[0].getId());
            return params[0];
        }

        @Override
        protected void onPostExecute(Category category) {
            super.onPostExecute(category);
            adapter.removeCategory(category);
            rvCategory.getAdapter().notifyDataSetChanged();
            Toast.makeText(getActivity(), getString(R.string.category_deleted), Toast.LENGTH_SHORT).show();
        }
    }

}
