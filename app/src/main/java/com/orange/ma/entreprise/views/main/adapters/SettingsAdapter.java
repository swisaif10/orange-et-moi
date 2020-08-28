package com.orange.ma.entreprise.views.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.listeners.OnItemSelectedListener;
import com.orange.ma.entreprise.models.settings.SettingsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private Context context;
    private List<SettingsItem> arrayList;
    private OnItemSelectedListener onItemSelectedListener;
    private String lang;

    public SettingsAdapter(Context context, List<SettingsItem> arrayList, OnItemSelectedListener onItemSelectedListener, String lang) {
        this.context = context;
        this.arrayList = arrayList;
        this.onItemSelectedListener = onItemSelectedListener;
        this.lang = lang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.settings_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            int icon = context.getResources().getIdentifier(arrayList.get(position).getIcon(), "drawable", context.getPackageName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(icon));
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.title.setText(arrayList.get(position).getTitle());
        String langTxt = context.getString(R.string.french);
        if (lang.equalsIgnoreCase("ar")) {
            holder.arrow.setScaleX(-1);
            langTxt = context.getString(R.string.arabic);
        }
        holder.language.setText(langTxt);
        if (position == 0)
            holder.language.setVisibility(View.VISIBLE);
        if (holder.separator != null)
            holder.separator.setVisibility(position == arrayList.size() - 1 ? View.INVISIBLE : View.VISIBLE);

        holder.itemView.setOnClickListener(v -> onItemSelectedListener.onItemSelected(arrayList.get(position)));
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
        @BindView(R.id.arrow)
        ImageView arrow;
        @BindView(R.id.language)
        TextView language;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}