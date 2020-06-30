package com.orange.orangeetmoipro.views.main.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hhl.gridpagersnaphelper.GridPagerSnapHelper;
import com.hhl.recyclerviewindicator.CirclePageIndicator;
import com.hhl.recyclerviewindicator.OnPageChangeListener;
import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.models.dashboard.Template;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Template> arrayList;

    public DashboardAdapter(Context context, ArrayList<Template> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_small_simple_item_layout, parent, false));
            case 1:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_small_compound_item_layout, parent, false));
            case 2:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_large_item_layout, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getSize().equalsIgnoreCase("small")) {
            if (arrayList.get(position).getTypeTemplate().equalsIgnoreCase("list"))
                return 1;
            else
                return 0;
        } else
            return 2;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Template template = arrayList.get(position);
        holder.color.setBackgroundColor(Color.parseColor(template.getColorIcone()));
        int icon = context.getResources().getIdentifier(template.getIcon(), "drawable", context.getPackageName());
        holder.icon.setImageResource(icon);
        holder.title.setText(template.getTitle());
        RecyclerView.LayoutManager layoutManager;
        if (arrayList.get(position).getSize().equalsIgnoreCase("small")) {
            layoutManager = new LinearLayoutManager(context);
            holder.recycler.setLayoutManager(layoutManager);
            holder.recycler.setAdapter(new DashboardSubItemAdapter(context, template.getElementComplex().get(0).getCompoundElements()));
        } else {
            holder.recycler.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false);
            holder.recycler.setLayoutManager(layoutManager);
            holder.recycler.setAdapter(new DashboardSubItemAdapter(context, template.getElementComplex().get(0).getCompoundElements()));

            if (template.getElementComplex().get(0).getCompoundElements().size() > 2) {
                GridPagerSnapHelper gridPagerSnapHelper = new GridPagerSnapHelper();
                gridPagerSnapHelper.setRow(2).setColumn(2);
                gridPagerSnapHelper.attachToRecyclerView(holder.recycler);

                holder.indicator.setRecyclerView(holder.recycler);
                holder.indicator.setPageColumn(2);

            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.color)
        View color;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.recycler)
        RecyclerView recycler;
        @Nullable
        @BindView(R.id.indicator)
        CirclePageIndicator indicator;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}