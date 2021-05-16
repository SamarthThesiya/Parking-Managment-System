package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingmanagment.R;

import models.entity.Land;

public class MonthlyEarningsAdaptor extends RecyclerView.Adapter<MonthlyEarningsAdaptor.MonthlyEarningsAdaptorViewHolder> {

    Context context;
    @NonNull
    @Override
    public MonthlyEarningsAdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monthly_earning, parent, false);
        return new MonthlyEarningsAdaptorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyEarningsAdaptorViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MonthlyEarningsAdaptorViewHolder extends RecyclerView.ViewHolder {

        TextView tv_month, tv_earnings;
        public MonthlyEarningsAdaptorViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_month = itemView.findViewById(R.id.tv_month_year);
            tv_earnings = itemView.findViewById(R.id.tv_monthly_earning);
        }
    }

    public void btnClicked(Land land) {

    }
}
