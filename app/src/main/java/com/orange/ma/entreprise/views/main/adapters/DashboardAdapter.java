package com.orange.ma.entreprise.views.main.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.listeners.OnTemplateItemSelectedListener;
import com.orange.ma.entreprise.models.dashboard.CompoundElement;
import com.orange.ma.entreprise.models.dashboard.Template;
import com.orange.ma.entreprise.utilities.LinePagerIndicatorDecoration;
import com.orange.ma.entreprise.utilities.SnapToBlock;
import com.orange.ma.entreprise.utilities.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private Context context;
    private List<Template> arrayList;
    private OnTemplateItemSelectedListener onTemplateItemSelectedListener;
    private int recyclerViewMaxWidth;
    private OnBottomReachedListener listener;

    public DashboardAdapter(Context context, List<Template> arrayList, OnTemplateItemSelectedListener onTemplateItemSelectedListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onTemplateItemSelectedListener = onTemplateItemSelectedListener;
        this.arrayList.add(getBlancTemplate());
    }

    private Template getBlancTemplate() {
        Template t = new Template();
        t.setTemplateKey("blanc");
        return t;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Template.TEMPLATE_BILLING:
            case Template.TEMPLATE_PARCK:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_small_simple_item_layout, parent, false), viewType);
            case Template.TEMPLATE_SMALL_LIST:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_small_compound_item_layout, parent, false), viewType);
            case Template.TEMPLATE_LIST_SLIDER:
            case Template.TEMPLATE_LIST:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_slider_item_layout, parent, false), viewType);

            case Template.BLANC_SPACE:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_blanc, parent, false), viewType);
            default:
                return null;
        }
    }

    public void setListener(OnBottomReachedListener listener) {
        this.listener = listener;
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
            case "blanc":
                return Template.BLANC_SPACE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (holder.viewType == Template.BLANC_SPACE) {

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(holder.itemView.getLayoutParams().width, getHeightByDensity());
            //p.bottomMargin = ;//(int) context.getResources().getDimension(R.dimen._50sdp);
            holder.itemView.setLayoutParams(p);
            holder.itemView.setTag(arrayList.get(position).getTemplateKey().toLowerCase());
            listener.onBottomReached(holder, position);
            return;
        }

        Template template = arrayList.get(position);
        holder.itemView.setTag(arrayList.get(position).getTemplateKey().toLowerCase());
        if (template.getElementComplex().getCompoundElements() == null || template.getElementComplex().getCompoundElements().size() == 0)
            return;

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
            holder.recycler.setAdapter(new DashboardSubItemAdapter(context, template.getElementComplex().getCompoundElements(), onTemplateItemSelectedListener, holder.getItemViewType(), 0));
        } else {
            holder.recycler.setHasFixedSize(true);
            if (holder.viewType == Template.TEMPLATE_LIST) {
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                int h;
                if (template.getElementComplex().getCompoundElements().size() <= 2)
                    h = (int) (context.getResources().getDimension(R.dimen._30sdp) * (template.getElementComplex().getCompoundElements().size() - 2) + context.getResources().getDimension(R.dimen._130sdp));
                else
                    h = (int) (context.getResources().getDimension(R.dimen._30sdp) * (template.getElementComplex().getCompoundElements().size() - 2) + context.getResources().getDimension(R.dimen._158sdp));

                holder.sliderCard.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
                holder.recycler.setLayoutManager(layoutManager);
                holder.recycler.setAdapter(new DashboardSubItemAdapter(context, template.getElementComplex().getCompoundElements(), onTemplateItemSelectedListener, holder.getItemViewType(), 0));
            } else {
                holder.recycler.getViewTreeObserver()
                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                holder.recycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                recyclerViewMaxWidth = holder.recycler.getWidth();
                                createViews(holder, template.getElementComplex().getCompoundElements());
                            }
                        });
            }


            if (holder.viewType == Template.TEMPLATE_LIST_SLIDER && template.getElementComplex().getCompoundElements().size() > 2) {

                holder.recycler.addItemDecoration(new LinePagerIndicatorDecoration(6, 6 + 5, 0, context.getResources().getColor(R.color.orange), context.getResources().getColor(R.color.orange), context));
            }
        }
    }

    private int getHeightByDensity() {
        int h = (int) context.getResources().getDimension(R.dimen._60sdp);
        int densityDpi = (int) (context.getResources().getDisplayMetrics().densityDpi);

        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (Utilities.aspect(context) > 1.8f && (hasMenuKey||hasBackKey))
            return 0;
        if (densityDpi < 500 && ViewConfiguration.get(context).hasPermanentMenuKey())
            h = (int) context.getResources().getDimension(R.dimen._22sdp);
        return h;
    }

    private void createViews(@NonNull ViewHolder holder, List<CompoundElement> compoundElements) {

        holder.recycler.setOnFlingListener(null);
        holder.recycler.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false));

        int recyclerWidth;
        int cellWidth = (recyclerViewMaxWidth) / 2;
        recyclerWidth = (cellWidth) * 2;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.recycler.getLayoutParams();
        lp.width = recyclerWidth;
        holder.recycler.setAdapter(new DashboardSubItemAdapter(context, compoundElements, onTemplateItemSelectedListener, holder.getItemViewType(), cellWidth));
        final SnapToBlock snapToBlock = new SnapToBlock();


        snapToBlock.attachToRecyclerView(holder.recycler);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.color)
        View color;
        @Nullable
        @BindView(R.id.icon)
        ImageView icon;
        @Nullable
        @BindView(R.id.title)
        TextView title;
        @Nullable
        @BindView(R.id.recycler)
        RecyclerView recycler;
        @Nullable
        @BindView(R.id.large_item_card)
        CardView sliderCard;

        int viewType;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.viewType = viewType;
        }
    }
}