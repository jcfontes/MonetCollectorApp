package br.com.jcfontes.monetcollector.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.viewModels.CategoryViewModel;

public class CategoryDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    public CategoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_detail, container, false);
        CollapsingToolbarLayout appBarLayout = this.getActivity().findViewById(R.id.toolbar_layout);

        ViewModelProviders.of(this)
                .get(CategoryViewModel.class)
                .getCategoryById(getArguments().getString(ARG_ITEM_ID))
                .observe(this, category -> {
                    appBarLayout.setTitle(category.getName());
                    ((TextView) view.findViewById(R.id.item_detail)).setText(category.getDescription());
                });

        return view;
    }
}
