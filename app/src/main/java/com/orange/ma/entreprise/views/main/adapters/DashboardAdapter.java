package com.orange.ma.entreprise.views.main.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hhl.gridpagersnaphelper.GridPagerSnapHelper;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.models.dashboard.Template;
import com.orange.ma.entreprise.utilities.LinePagerIndicatorDecoration;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.main.listeners.SubItemClickedListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Template> arrayList;
    SubItemClickedListener subItemClickedListener;
    float aspect = 1;

    public DashboardAdapter(Context context, ArrayList<Template> arrayList, SubItemClickedListener subItemClickedListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.subItemClickedListener = subItemClickedListener;
        this.aspect = (float)context.getResources().getDisplayMetrics().heightPixels/(float) context.getResources().getDisplayMetrics().widthPixels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Template.TEMPLATE_BILLING:
            case Template.TEMPLATE_PARCK:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_small_simple_item_layout, parent, false));
            case Template.TEMPLATE_SMALL_LIST:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_small_compound_item_layout, parent, false));
            case Template.TEMPLATE_LIST_SLIDER:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_large_item_layout, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (arrayList.get(position).getTemplateKey()) {
            case "template_billing":
                return Template.TEMPLATE_BILLING;
            case "template_parck":
                return Template.TEMPLATE_PARCK;
            case "template_list_slider":
                return Template.TEMPLATE_LIST_SLIDER;
            case "template_small_list":
                return Template.TEMPLATE_SMALL_LIST;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        setLayoutParams(holder);
        Template template = arrayList.get(position);
        holder.color.setBackgroundColor(Color.parseColor(template.getColorIcone()));
        int icon = context.getResources().getIdentifier(template.getIcon(), "drawable", context.getPackageName());
        holder.icon.setImageResource(icon);
        holder.title.setText(template.getTitle());
        RecyclerView.LayoutManager layoutManager;
        if (arrayList.get(position).getSize().equalsIgnoreCase("small")) {
            layoutManager = new LinearLayoutManager(context);
            holder.recycler.setLayoutManager(layoutManager);
            holder.recycler.setAdapter(new DashboardSubItemAdapter(context, template.getElementComplex().get(0).getCompoundElements(), subItemClickedListener, holder.getItemViewType()));
        } else {
            holder.recycler.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false);
            holder.recycler.setLayoutManager(layoutManager);
            holder.recycler.setAdapter(new DashboardSubItemAdapter(context, template.getElementComplex().get(0).getCompoundElements(), subItemClickedListener, holder.getItemViewType()));

            if (template.getElementComplex().get(0).getCompoundElements().size() > 2) {
                GridPagerSnapHelper gridPagerSnapHelper = new GridPagerSnapHelper();
                gridPagerSnapHelper.setRow(2).setColumn(2);
                gridPagerSnapHelper.attachToRecyclerView(holder.recycler);
                holder.recycler.addItemDecoration(new LinePagerIndicatorDecoration(6, 6 + 5, 0, context.getResources().getColor(R.color.orange), context.getResources().getColor(R.color.orange), context));
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void setLayoutParams(@NonNull ViewHolder holder) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(holder.itemView.getLayoutParams().width, 0);

        if (aspect>1.78) {
            switch (holder.getItemViewType()) {
                case Template.TEMPLATE_BILLING:
                case Template.TEMPLATE_PARCK:
                    params.height = (int) (height * 0.20);
                    break;
                case Template.TEMPLATE_LIST_SLIDER:
                    params.height = (int) (height * 0.23);
                    break;
                case Template.TEMPLATE_SMALL_LIST:
                    params.height = (int) (height * 0.21);
                    break;
            }
            params.topMargin = 7;
            params.bottomMargin = 7;
            holder.itemView.setLayoutParams(params);
        }else{
            switch (holder.getItemViewType()) {
                case Template.TEMPLATE_BILLING:
                case Template.TEMPLATE_PARCK:
                    params.height = (int) (height * 0.22);
                    break;
                case Template.TEMPLATE_LIST_SLIDER:
                    params.height = (int) (height * 0.26);
                    break;
                case Template.TEMPLATE_SMALL_LIST:
                    params.height = (int) (height * 0.24);
                    break;
            }
            params.topMargin = 5;
            params.bottomMargin = 5;
            holder.itemView.setLayoutParams(params);
        }
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

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}