package view_holders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import utils.ViewUtils;

public class ViewHolderGenerator {

    public ViewDataBinding binding;

    public ViewHolderGenerator(int layout, ViewGroup parent) {
        binding = ViewUtils.getDataBinding(layout, parent);
    }

    public RecyclerView.ViewHolder generate() {
        return new ExtendedViewHolder(binding.getRoot());
    }

    private static class ExtendedViewHolder extends RecyclerView.ViewHolder {

        public ExtendedViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
