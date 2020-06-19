package com.orange.orangeetmoipro.views.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Template template = arrayList.get(position);
        switch (template.getType1()) {
            case "small1":
                holder.box1.setVisibility(View.VISIBLE);
                holder.box2.setVisibility(View.GONE);
                holder.box3.setVisibility(View.GONE);
                holder.icon1.setImageDrawable(context.getResources().getDrawable(template.getIcon1()));
                holder.icon2.setImageDrawable(context.getResources().getDrawable(template.getIcon2()));
                holder.title1.setText(template.getTitle1());
                holder.title2.setText(template.getTitle2());
                holder.recycler1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                DashboardStringItemAdapter adapter1 = new DashboardStringItemAdapter(context, template.getItemStrings1());
                holder.recycler1.setAdapter(adapter1);
                holder.recycler2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                DashboardStringItemAdapter adapter2 = new DashboardStringItemAdapter(context, template.getItemStrings2());
                holder.recycler2.setAdapter(adapter2);
                break;
            case "small2":
                holder.box1.setVisibility(View.GONE);
                holder.box2.setVisibility(View.GONE);
                holder.box3.setVisibility(View.VISIBLE);
                holder.icon4.setImageDrawable(context.getResources().getDrawable(template.getIcon1()));
                holder.icon5.setImageDrawable(context.getResources().getDrawable(template.getIcon2()));
                holder.title4.setText(template.getTitle1());
                holder.title5.setText(template.getTitle2());
                holder.recycler4.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                DashboardButtonItemAdapter adapter4 = new DashboardButtonItemAdapter(context, template.getItemButtons1());
                holder.recycler4.setAdapter(adapter4);
                holder.recycler5.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                DashboardButtonItemAdapter adapter5 = new DashboardButtonItemAdapter(context, template.getItemButtons1());
                holder.recycler5.setAdapter(adapter5);
                break;
            default:
                holder.box1.setVisibility(View.GONE);
                holder.box2.setVisibility(View.VISIBLE);
                holder.box3.setVisibility(View.GONE);
                holder.icon3.setImageDrawable(context.getResources().getDrawable(template.getIcon1()));
                holder.title3.setText(template.getTitle1());
                holder.recycler3.setLayoutManager(new GridLayoutManager(context, 2));
                DashboardButtonItemAdapter adapter3 = new DashboardButtonItemAdapter(context, template.getItemButtons1());
                holder.recycler3.setAdapter(adapter3);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon1)
        ImageView icon1;
        @BindView(R.id.title1)
        TextView title1;
        @BindView(R.id.recycler1)
        RecyclerView recycler1;
        @BindView(R.id.icon2)
        ImageView icon2;
        @BindView(R.id.title2)
        TextView title2;
        @BindView(R.id.recycler2)
        RecyclerView recycler2;
        @BindView(R.id.box_1)
        LinearLayout box1;
        @BindView(R.id.icon3)
        ImageView icon3;
        @BindView(R.id.title3)
        TextView title3;
        @BindView(R.id.recycler3)
        RecyclerView recycler3;
        @BindView(R.id.box_2)
        LinearLayout box2;
        @BindView(R.id.icon4)
        ImageView icon4;
        @BindView(R.id.title4)
        TextView title4;
        @BindView(R.id.recycler4)
        RecyclerView recycler4;
        @BindView(R.id.icon5)
        ImageView icon5;
        @BindView(R.id.title5)
        TextView title5;
        @BindView(R.id.recycler5)
        RecyclerView recycler5;
        @BindView(R.id.box_3)
        LinearLayout box3;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}