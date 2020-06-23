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
import com.orange.orangeetmoipro.listeners.OnItemSelectedListener;
import com.orange.orangeetmoipro.models.settings.SettingsItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SettingsItem> arrayList;
    private OnItemSelectedListener onItemSelectedListener;

    public SettingsAdapter(Context context, ArrayList<SettingsItem> arrayList, OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.settings_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //int icon = context.getResources().getIdentifier(arrayList.get(position).getIcon(), "drawable", context.getPackageName());
        //holder.icon.setImageDrawable(context.getResources().getDrawable(icon));
        holder.title.setText(arrayList.get(position).getTitle());
        if (holder.separator != null)
            holder.separator.setVisibility(position == arrayList.size() - 1 ? View.INVISIBLE : View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedListener.onItemSelected(arrayList.get(position).getAction());
            }
        });
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