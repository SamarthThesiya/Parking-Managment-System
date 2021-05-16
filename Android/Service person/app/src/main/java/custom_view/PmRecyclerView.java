package custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import interfaces.RecyclerViewListener;
import utils.ViewUtils;

public class PmRecyclerView extends RecyclerView {

    Integer              layout;
    Integer              listSize;
    RecyclerViewListener recyclerViewListener;

    public PmRecyclerView(@NonNull Context context) {
        super(context);
    }

    public PmRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PmRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(int layout, Integer listSize, Context context, RecyclerViewListener recyclerViewListener) {
        this.layout               = layout;
        this.listSize             = listSize;
        this.recyclerViewListener = recyclerViewListener;

        PmRecyclerViewAdaptor pmRecyclerViewAdaptor = new PmRecyclerViewAdaptor();
        setAdapter(pmRecyclerViewAdaptor);
        setLayoutManager(new LinearLayoutManager(context));
        pmRecyclerViewAdaptor.notifyDataSetChanged();
    }

    public class PmRecyclerViewAdaptor extends Adapter<PmRecyclerViewAdaptor.PmViewHolder> {

        @NonNull
        @Override
        public PmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewDataBinding binding = ViewUtils.getDataBinding(layout, parent);
            return new PmViewHolder(binding.getRoot(), binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PmViewHolder holder, int position) {
            recyclerViewListener.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemCount() {
            return listSize;
        }

        public class PmViewHolder extends ViewHolder {

            public ViewDataBinding binding;
            public PmViewHolder(@NonNull View itemView, ViewDataBinding binding) {
                super(itemView);
                this.binding = binding;
            }
        }
    }
}
