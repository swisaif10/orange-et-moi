package com.orange.orangeetmoipro.views.main.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.models.dashboard.CompoundElement;
import com.orange.orangeetmoipro.utilities.LocaleManager;
import com.orange.orangeetmoipro.views.main.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardSubItemAdapter extends RecyclerView.Adapter<DashboardSubItemAdapter.ViewHolder> {


    private Context context;
    private ArrayList<CompoundElement> arrayList;

    public DashboardSubItemAdapter(Context context, ArrayList<CompoundElement> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_string_subitem_layout, parent, false));
            case 1:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_button_subitem_layout, parent, false));
            default:
                return null;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getElements().get(0).getType().equalsIgnoreCase("txt"))
            return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == arrayList.size() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            holder.itemView.setLayoutParams(params);

        }
        if (position % 2 == 0 && holder.separator != null && position != arrayList.size() - 1)
            holder.separator.setVisibility(View.VISIBLE);
        else if (holder.separator != null)
            holder.separator.setVisibility(View.INVISIBLE);


        if (arrayList.get(position).getElements().get(0).getType().equalsIgnoreCase("txt")) {
            if (arrayList.get(position).getElements().get(0).getValue().length() > 3) {
                holder.layout1.setVisibility(View.GONE);
                holder.layout2.setVisibility(View.VISIBLE);
                holder.value2.setText(arrayList.get(position).getElements().get(0).getValue());
                holder.value2.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(0).getColor()));
                holder.name2.setText(arrayList.get(position).getElements().get(1).getValue());
                holder.name2.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getColor()));
            } else {
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.GONE);
                holder.value1.setText(arrayList.get(position).getElements().get(0).getValue());
                holder.value1.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(0).getColor()));
                holder.name1.setText(arrayList.get(position).getElements().get(1).getValue());
                holder.name1.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getColor()));
            }

        } else {
            if (arrayList.size() > 2)
                setLayoutParams(holder);
            int icon = context.getResources().getIdentifier(arrayList.get(position).getElements().get(0).getValue(), "drawable", context.getPackageName());
            holder.icon.setImageResource(icon);
            holder.title.setText(arrayList.get(position).getElements().get(1).getValue());
            if (LocaleManager.getLanguagePref(context).equalsIgnoreCase("ar"))
                holder.arrow.setScaleX(-1);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.value1)
        TextView value1;
        @Nullable
        @BindView(R.id.name1)
        TextView name1;
        @Nullable
        @BindView(R.id.layout_1)
        LinearLayout layout1;
        @Nullable
        @BindView(R.id.value2)
        TextView value2;
        @Nullable
        @BindView(R.id.name2)
        TextView name2;
        @Nullable
        @BindView(R.id.layout_2)
        LinearLayout layout2;
        @Nullable
        @BindView(R.id.icon)
        ImageView icon;
        @Nullable
        @BindView(R.id.title)
        TextView title;
        @Nullable
        @BindView(R.id.separator)
        View separator;
        @Nullable
        @BindView(R.id.arrow)
        ImageView arrow;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setLayoutParams(@NonNull ViewHolder holder) {
        Display display = ((MainActivity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(0, 0);
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        params.width = size.x * 42 / 100;
        params.setMargins(0, 0, px, px);
        holder.itemView.setLayoutParams(params);
    }
}