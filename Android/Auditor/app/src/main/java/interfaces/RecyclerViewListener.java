package interfaces;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import custom_view.PmRecyclerView;


public interface RecyclerViewListener {
    void onBindViewHolder(@NonNull PmRecyclerView.PmRecyclerViewAdaptor.PmViewHolder holder, int position);
}
