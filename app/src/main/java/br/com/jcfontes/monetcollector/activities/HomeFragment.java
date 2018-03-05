package br.com.jcfontes.monetcollector.activities;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.activities.adapters.CategoryListAdapter;
import br.com.jcfontes.monetcollector.models.Category;
import br.com.jcfontes.monetcollector.viewModels.CategoryViewModel;

public class HomeFragment extends Fragment {

    private RecyclerView rvCategory;
    private CategoryListAdapter adapter;
    private EditText etFilter;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

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
        setupRecyclerView(this.rvCategory);

        return view;
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

    private void setupRecyclerView(RecyclerView rvCategory) {
        List<Category> categories = new ArrayList<>();
        this.getCategoryList();
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.adapter = new CategoryListAdapter(categories);
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
}
