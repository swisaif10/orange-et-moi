package com.orange.orangeetmoipro.views.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.models.dashboard.ItemButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardButtonItemAdapter extends RecyclerView.Adapter<DashboardButtonItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ItemButton> arrayList;

    public DashboardButtonItemAdapter(Context context, ArrayList<ItemButton> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_button_subitem_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.icon.setImageDrawable(context.getResources().getDrawable(arrayList.get(position).getIcon()));
        holder.title.setText(arrayList.get(position).getTitle());
        if (holder.separator != null)
            holder.separator.setVisibility(position == arrayList.size() - 1 ? View.INVISIBLE : View.VISIBLE);
       if (arrayList.get(position).getTitle().equalsIgnoreCase("Changement de SIM"))
           holder.separator.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.separator)
        View separator;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}