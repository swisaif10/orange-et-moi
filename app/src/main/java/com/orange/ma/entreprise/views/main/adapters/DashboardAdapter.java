package com.orange.ma.entreprise.views.main.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hhl.gridpagersnaphelper.GridPagerSnapHelper;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.listeners.OnTemplateItemSelectedListener;
import com.orange.ma.entreprise.models.dashboard.Template;
import com.orange.ma.entreprise.utilities.LinePagerIndicatorDecoration;

import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private Context context;
    private List<Template> arrayList;
    private OnTemplateItemSelectedListener onTemplateItemSelectedListener;

    public DashboardAdapter(Context context, List<Template> arrayList, OnTemplateItemSelectedListener onTemplateItemSelectedListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onTemplateItemSelectedListener = onTemplateItemSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Template.TEMPLATE_BILLING:
            case Template.TEMPLATE_PARCK:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_small_simple_item_layout, parent, false),viewType);
            case Template.TEMPLATE_SMALL_LIST:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_small_compound_item_layout, parent, false),viewType);
            case Template.TEMPLATE_LIST_SLIDER:
            case Template.TEMPLATE_LIST:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_large_item_layout, parent, false),viewType);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (arrayList.get(position).getTemplateKey().toLowerCase()) {
            case "template_billing":
                return Template.TEMPLATE_BILLING;
            case "template_parck":
                return Template.TEMPLATE_PARCK;
            case "template_list_slider":
                return Template.TEMPLATE_LIST_SLIDER;
            case "template_small_list":
                return Template.TEMPLATE_SMALL_LIST;
            case "template_list":
                return Template.TEMPLATE_LIST;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Template template = arrayList.get(position);

        if(template.getElementComplex().getCompoundElements()==null || template.getElementComplex().getCompoundElements().size()==0)return;

        holder.color.setBackgroundColor(Color.parseColor(template.getColorIcone()));
        int icon = context.getResources().getIdentifier(template.getIcon(), "drawable", context.getPackageName());
        holder.icon.setImageResource(icon);
        holder.icon.setColorFilter(Color.parseColor(template.getColorIcone()), PorterDuff.Mode.SRC_ATOP);

        holder.title.setText(template.getTitle());
        RecyclerView.LayoutManager layoutManager;

        if (arrayList.get(position).getSize().equalsIgnoreCase("small")) {
            layoutManager = new LinearLayoutManager(context);
            holder.recycler.setLayoutManager(layoutManager);
            holder.recycler.setPadding((int) context.getResources().getDimension(R.dimen._2sdp), 0, 0, 0);
            holder.recycler.setAdapter(new DashboardSubItemAdapter(context, template.getElementComplex().getCompoundElements(), onTemplateItemSelectedListener, holder.getItemViewType()));
        } else {
            holder.recycler.setHasFixedSize(true);
            if(holder.viewType == Template.TEMPLATE_LIST){
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                int h = (int)(context.getResources().getDimension(R.dimen._30sdp)*(template.getElementComplex().getCompoundElements().size()-2)+context.getResources().getDimension(R.dimen._150sdp));
                holder.sliderCard.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,h));
            }
            else
                layoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false);
            holder.recycler.setLayoutManager(layoutManager);
            holder.recycler.setAdapter(new DashboardSubItemAdapter(context, template.getElementComplex().getCompoundElements(), onTemplateItemSelectedListener, holder.getItemViewType()));

            if (holder.viewType== Template.TEMPLATE_LIST_SLIDER && template.getElementComplex().getCompoundElements().size() > 2) {
                GridPagerSnapHelper gridPagerSnapHelper = new GridPagerSnapHelper();
                gridPagerSnapHelper.setRow(2).setColumn(2);
                if(holder.recycler.getOnFlingListener()==null) gridPagerSnapHelper.attachToRecyclerView(holder.recycler);
                holder.recycler.addItemDecoration(new LinePagerIndicatorDecoration(6, 6 + 5, 0, context.getResources().getColor(R.color.orange), context.getResources().getColor(R.color.orange), context));
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
        @BindView(R.id.large_item_card)
        CardView sliderCard;

        int viewType;

        ViewHolder(View itemView,int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.viewType = viewType;
        }
    }
}