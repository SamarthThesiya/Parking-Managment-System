package interfaces;

import androidx.annotation.NonNull;

import custom_view.PmRecyclerView;


public interface RecyclerViewListener {
    void onBindViewHolder(@NonNull PmRecyclerView.PmRecyclerViewAdaptor.PmViewHolder holder, int position);
}
