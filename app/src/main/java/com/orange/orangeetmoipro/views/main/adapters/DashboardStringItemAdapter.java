package com.orange.orangeetmoipro.views.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.models.dashboard.ItemString;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardStringItemAdapter extends RecyclerView.Adapter<DashboardStringItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ItemString> arrayList;

    public DashboardStringItemAdapter(Context context, ArrayList<ItemString> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_string_subitem_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.value.setText(arrayList.get(position).getText1());
        holder.name.setText(arrayList.get(position).getText2());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.value)
        TextView value;
        @BindView(R.id.name)
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}