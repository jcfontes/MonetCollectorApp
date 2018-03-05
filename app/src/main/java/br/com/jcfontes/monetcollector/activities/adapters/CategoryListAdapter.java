package br.com.jcfontes.monetcollector.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.activities.CategoryDetailActivity;
import br.com.jcfontes.monetcollector.activities.CategoryDetailFragment;
import br.com.jcfontes.monetcollector.models.Category;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    private List<Category> categories;

    private final View.OnClickListener mOnClickListener = view -> {
        Category item = (Category) view.getTag();
        Context context = view.getContext();
        Intent intent = new Intent(context, CategoryDetailActivity.class);
        intent.putExtra(CategoryDetailFragment.ARG_ITEM_ID, item.getId());

        context.startActivity(intent);
    };

    public CategoryListAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int i) {
        Category category = categories.get(i);

        holder.mIdView.setText(category.getName());
        holder.mContentView.setText(category.getDescription());

        holder.itemView.setTag(category);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        final TextView mIdView;
        final TextView mContentView;

        public CategoryViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.etName);
            mContentView = view.findViewById(R.id.etDesc);
        }
    }

    public Category getCategory(int position) {
        return categories.get(position);
    }

    public void setList(List<Category> categories) {
        this.categories = categories;
    }

}
